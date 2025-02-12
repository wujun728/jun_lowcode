
package io.github.wujun728.sys.modular.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import io.github.wujun728.core.consts.CommonConstant;
import io.github.wujun728.core.consts.SymbolConstant;
import io.github.wujun728.core.context.constant.ConstantContextHolder;
import io.github.wujun728.core.dbs.CurrentDataSourceContext;
import io.github.wujun728.core.enums.CommonStatusEnum;
import io.github.wujun728.core.exception.AuthException;
import io.github.wujun728.core.exception.ServiceException;
import io.github.wujun728.core.exception.enums.AuthExceptionEnum;
import io.github.wujun728.core.exception.enums.ServerExceptionEnum;
import io.github.wujun728.core.pojo.login.SysLoginUser;
import io.github.wujun728.core.tenant.context.TenantCodeHolder;
import io.github.wujun728.core.tenant.context.TenantDbNameHolder;
import io.github.wujun728.core.tenant.entity.TenantInfo;
import io.github.wujun728.core.tenant.exception.TenantException;
import io.github.wujun728.core.tenant.exception.enums.TenantExceptionEnum;
import io.github.wujun728.core.tenant.service.TenantInfoService;
import io.github.wujun728.core.util.HttpServletUtil;
import io.github.wujun728.core.util.IpAddressUtil;
import io.github.wujun728.sys.core.cache.UserCache;
import io.github.wujun728.sys.core.jwt.JwtPayLoad;
import io.github.wujun728.sys.core.jwt.JwtTokenUtil;
import io.github.wujun728.sys.core.log.LogManager;
import io.github.wujun728.sys.modular.auth.factory.LoginUserFactory;
import io.github.wujun728.sys.modular.auth.service.AuthService;
import io.github.wujun728.sys.modular.log.enums.SysLogSuccessStatusEnum;
import io.github.wujun728.sys.modular.user.entity.SysUser;
import io.github.wujun728.sys.modular.user.service.SysUserService;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 认证相关service实现类
 * @date 2020/3/11 16:58
 */
@Service
public class AuthServiceImpl implements AuthService, UserDetailsService {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private UserCache userCache;

    @Override
    public String login(String account, String password) {

        if (ObjectUtil.hasEmpty(account, password)) {
            LogManager.me().executeLoginLog(account, SysLogSuccessStatusEnum.FAIL.getCode(), AuthExceptionEnum.ACCOUNT_PWD_EMPTY.getMessage());
            throw new AuthException(AuthExceptionEnum.ACCOUNT_PWD_EMPTY);
        }

        SysUser sysUser = sysUserService.getUserByCount(account);

        //用户不存在，账号或密码错误
        if (ObjectUtil.isEmpty(sysUser)) {
            LogManager.me().executeLoginLog(account, SysLogSuccessStatusEnum.FAIL.getCode(), AuthExceptionEnum.ACCOUNT_PWD_ERROR.getMessage());
            throw new AuthException(AuthExceptionEnum.ACCOUNT_PWD_ERROR);
        }

        String passwordBcrypt = sysUser.getPassword();

        //验证账号密码是否正确
        if (ObjectUtil.isEmpty(passwordBcrypt) || !BCrypt.checkpw(password, passwordBcrypt)) {
            LogManager.me().executeLoginLog(sysUser.getAccount(), SysLogSuccessStatusEnum.FAIL.getCode(), AuthExceptionEnum.ACCOUNT_PWD_ERROR.getMessage());
            throw new AuthException(AuthExceptionEnum.ACCOUNT_PWD_ERROR);
        }

        return doLogin(sysUser);
    }

    @Override
    public String doLogin(SysUser sysUser) {

        Integer sysUserStatus = sysUser.getStatus();

        //验证账号是否被冻结
        if (CommonStatusEnum.DISABLE.getCode().equals(sysUserStatus)) {
            LogManager.me().executeLoginLog(sysUser.getAccount(), SysLogSuccessStatusEnum.FAIL.getCode(), AuthExceptionEnum.ACCOUNT_FREEZE_ERROR.getMessage());
            throw new AuthException(AuthExceptionEnum.ACCOUNT_FREEZE_ERROR);
        }

        //构造SysLoginUser
        SysLoginUser sysLoginUser = this.genSysLoginUser(sysUser);

        //构造jwtPayLoad
        JwtPayLoad jwtPayLoad = new JwtPayLoad(sysUser.getId(), sysUser.getAccount());

        //生成token
        String token = JwtTokenUtil.generateToken(jwtPayLoad);

        //缓存token与登录用户信息对应, 默认2个小时
        this.cacheLoginUser(jwtPayLoad, sysLoginUser);

        //设置最后登录ip和时间
        sysUser.setLastLoginIp(IpAddressUtil.getIp(HttpServletUtil.getRequest()));
        sysUser.setLastLoginTime(DateTime.now());

        //更新用户登录信息
        sysUserService.updateById(sysUser);

        //登录成功，记录登录日志
        LogManager.me().executeLoginLog(sysUser.getAccount(), SysLogSuccessStatusEnum.SUCCESS.getCode(), null);

        //登录成功，设置SpringSecurityContext上下文，方便获取用户
        this.setSpringSecurityContextAuthentication(sysLoginUser);

        //登录成功，设置Cookie
        this.cacheLoginCookie(token);

        //如果开启限制单用户登陆，则踢掉原来的用户
        Boolean enableSingleLogin = ConstantContextHolder.getEnableSingleLogin();
        if (enableSingleLogin) {

            //获取所有的登陆用户
            Map<String, SysLoginUser> allLoginUsers = userCache.getAllKeyValues();
            for (Map.Entry<String, SysLoginUser> loginedUserEntry : allLoginUsers.entrySet()) {

                String loginedUserKey = loginedUserEntry.getKey();
                SysLoginUser loginedUser = loginedUserEntry.getValue();

                //如果账号名称相同，并且redis缓存key和刚刚生成的用户的uuid不一样，则清除以前登录的
                if (loginedUser.getName().equals(sysUser.getName())
                        && !loginedUserKey.equals(jwtPayLoad.getUuid())) {
                    this.clearUser(loginedUserKey, loginedUser.getAccount());
                }
            }
        }

        //返回token
        return token;
    }

    @Override
    public String getTokenFromRequest(HttpServletRequest request) {
        String authToken = request.getHeader(CommonConstant.AUTHORIZATION);
        if (ObjectUtil.isEmpty(authToken)) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (CommonConstant.AUTHORIZATION.equals(cookie.getName())) {
                        authToken = cookie.getValue();
                        break;
                    }
                }
            }
            return authToken;
        } else {
            //token不是以Bearer打头，则响应回格式不正确
            if (!authToken.startsWith(CommonConstant.TOKEN_TYPE_BEARER)) {
                throw new AuthException(AuthExceptionEnum.NOT_VALID_TOKEN_TYPE);
            }
            try {
                authToken = authToken.substring(CommonConstant.TOKEN_TYPE_BEARER.length() + 1);
            } catch (StringIndexOutOfBoundsException e) {
                throw new AuthException(AuthExceptionEnum.NOT_VALID_TOKEN_TYPE);
            }
        }

        return authToken;
    }

    @Override
    public SysLoginUser getLoginUserByToken(String token) {

        //校验token，错误则抛异常
        this.checkToken(token);

        //根据token获取JwtPayLoad部分
        JwtPayLoad jwtPayLoad = JwtTokenUtil.getJwtPayLoad(token);

        //从redis缓存中获取登录用户
        Object cacheObject = userCache.get(jwtPayLoad.getUuid());

        //用户不存在则表示登录已过期
        if (ObjectUtil.isEmpty(cacheObject)) {
            // 如果通过缓存获取不到，直接根据数据库来查
            Long loginUserId = jwtPayLoad.getUserId();
            SysUser sysUser = sysUserService.getById(loginUserId);
            if(ObjectUtil.isNotEmpty(sysUser)) {
                // 构造SysLoginUser
                return this.genSysLoginUser(sysUser);
            } else {
                // 数据库也获取不到，就实在没办法了，抛异常
                throw new AuthException(AuthExceptionEnum.LOGIN_EXPIRED);
            }
        }

        //转换成登录用户
        SysLoginUser sysLoginUser = (SysLoginUser) cacheObject;

        //用户存在, 无痛刷新缓存，在登录过期前活动的用户自动刷新缓存时间
        this.cacheLoginUser(jwtPayLoad, sysLoginUser);

        //返回用户
        return sysLoginUser;
    }

    @Override
    public void logout() {

        HttpServletRequest request = HttpServletUtil.getRequest();

        if (ObjectUtil.isNotNull(request)) {

            //获取token
            String token = this.getTokenFromRequest(request);

            //如果token为空直接返回
            if (ObjectUtil.isEmpty(token)) {
                return;
            }

            //校验token，错误则抛异常，待确定
            this.checkToken(token);

            //根据token获取JwtPayLoad部分
            JwtPayLoad jwtPayLoad = JwtTokenUtil.getJwtPayLoad(token);

            //获取缓存的key
            String loginUserCacheKey = jwtPayLoad.getUuid();

            //清除缓存的用户信息
            this.clearUser(loginUserCacheKey, jwtPayLoad.getAccount());

            //清除cookie
            this.deleteLoginCookie();
        } else {
            throw new ServiceException(ServerExceptionEnum.REQUEST_EMPTY);
        }
    }

    @Override
    public void setSpringSecurityContextAuthentication(SysLoginUser sysLoginUser) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        sysLoginUser,
                        null,
                        sysLoginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public void checkToken(String token) {
        //校验token是否正确
        Boolean tokenCorrect = JwtTokenUtil.checkToken(token);
        if (!tokenCorrect) {
            throw new AuthException(AuthExceptionEnum.REQUEST_TOKEN_ERROR);
        }

        //校验token是否失效
        Boolean tokenExpired = JwtTokenUtil.isTokenExpired(token);
        if (tokenExpired) {
            throw new AuthException(AuthExceptionEnum.LOGIN_EXPIRED);
        }
    }

    @Override
    public void cacheTenantInfo(String tenantCode) {
        if (StrUtil.isBlank(tenantCode)) {
            return;
        }

        // 从spring容器中获取service，如果没开多租户功能，没引入相关包，这里会报错
        TenantInfoService tenantInfoService = null;
        try {
            tenantInfoService = SpringUtil.getBean(TenantInfoService.class);
        } catch (Exception e) {
            throw new TenantException(TenantExceptionEnum.TENANT_MODULE_NOT_ENABLE_ERROR);
        }

        // 获取租户信息
        TenantInfo tenantInfo = tenantInfoService.getByCode(tenantCode);
        if (tenantInfo != null) {
            String dbName = tenantInfo.getDbName();

            // 租户编码的临时存放
            TenantCodeHolder.put(tenantCode);

            // 租户的数据库名称临时缓存
            TenantDbNameHolder.put(dbName);

            // 数据源信息临时缓存
            CurrentDataSourceContext.setDataSourceType(dbName);
        } else {
            throw new TenantException(TenantExceptionEnum.CNAT_FIND_TENANT_ERROR);
        }
    }

    @Override
    public SysLoginUser loadUserByUsername(String account) throws UsernameNotFoundException {
        SysLoginUser sysLoginUser = new SysLoginUser();
        SysUser user = sysUserService.getUserByCount(account);
        BeanUtil.copyProperties(user, sysLoginUser);
        return sysLoginUser;
    }

    /**
     * 根据key清空登陆信息
     *
     * @author xuyuxiang
     * @date 2020/6/19 12:28
     */
    private void clearUser(String loginUserKey, String account) {
        //获取缓存的用户
        Object cacheObject = userCache.get(loginUserKey);

        //如果缓存的用户存在，清除会话，否则表示该会话信息已失效，不执行任何操作
        if (ObjectUtil.isNotEmpty(cacheObject)) {
            //清除登录会话
            userCache.remove(loginUserKey);
            //创建退出登录日志
            LogManager.me().executeExitLog(account);
        }
    }

    /**
     * 构造登录用户信息
     *
     * @author xuyuxiang
     * @date 2020/3/12 17:32
     */
    @Override
    public SysLoginUser genSysLoginUser(SysUser sysUser) {
        SysLoginUser sysLoginUser = new SysLoginUser();
        BeanUtil.copyProperties(sysUser, sysLoginUser);
        LoginUserFactory.fillLoginUserInfo(sysLoginUser);
        return sysLoginUser;
    }

    @Override
    public void deleteLoginCookie() {
        Cookie[] cookies =HttpServletUtil.getRequest().getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String tokenHeader = CommonConstant.AUTHORIZATION;
                if (tokenHeader.equalsIgnoreCase(cookie.getName())) {
                    Cookie temp = new Cookie(cookie.getName(), "");
                    temp.setMaxAge(0);
                    temp.setPath(SymbolConstant.LEFT_DIVIDE);
                    HttpServletUtil.getResponse().addCookie(temp);
                }
            }
        }
    }

    /**
     * 缓存token与登录用户信息对应, 默认2个小时
     *
     * @author xuyuxiang
     * @date 2020/3/13 14:51
     */
    private void cacheLoginUser(JwtPayLoad jwtPayLoad, SysLoginUser sysLoginUser) {
        String redisLoginUserKey = jwtPayLoad.getUuid();
        userCache.put(redisLoginUserKey, sysLoginUser, Convert.toLong(ConstantContextHolder.getSessionTokenExpireSec()));
    }

    /**
     * 缓存token到cookie
     *
     * @author xuyuxiang
     * @date 2020/3/13 14:51
     */
    private void cacheLoginCookie(String token) {
        //创建cookie
        Cookie authorization = new Cookie(CommonConstant.AUTHORIZATION, token);
        authorization.setMaxAge(Convert.toInt(ConstantContextHolder.getSessionTokenExpireSec()));
        authorization.setHttpOnly(true);
        authorization.setPath(SymbolConstant.LEFT_DIVIDE);
        HttpServletUtil.getResponse().addCookie(authorization);
    }

    @Override
    public void refreshUserDataScope(Long orgId) {
        // request获取到token
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = this.getTokenFromRequest(request);
        SysLoginUser sysLoginUser = this.getLoginUserByToken(token);
        sysLoginUser.getDataScopes().add(orgId);
        this.cacheLoginUser(JwtTokenUtil.getJwtPayLoad(token), sysLoginUser);
    }

}
