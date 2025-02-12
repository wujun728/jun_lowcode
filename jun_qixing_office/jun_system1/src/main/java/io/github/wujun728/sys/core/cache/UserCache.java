
package io.github.wujun728.sys.core.cache;

import cn.hutool.cache.impl.TimedCache;
import io.github.wujun728.core.pojo.login.SysLoginUser;
import io.github.wujun728.core.cache.base.AbstractMemoryCacheOperator;

/**
 * 登录用户的缓存，存储了当前登录的用户
 * <p>
 * 一般用于在线用户的查看和过滤器检测用户是否登录
 * <p>
 * key为用户的唯一id，value为LoginUser对象
 *
 * @date 2020/7/9 11:02
 */
public class UserCache extends AbstractMemoryCacheOperator<SysLoginUser> {

    /**
     * 登录用户缓存前缀
     */
    public static final String LOGIN_USER_CACHE_PREFIX = "LOGIN_USER_";

    public UserCache(TimedCache<String, SysLoginUser> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return LOGIN_USER_CACHE_PREFIX;
    }
}
