
package io.github.wujun728.core.util;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import io.github.wujun728.core.consts.CommonConstant;
import io.github.wujun728.core.exception.ServiceException;
import io.github.wujun728.core.exception.enums.ServerExceptionEnum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

/**
 * HttpServlet工具类，获取当前request和response
 * @date 2020/3/30 15:09
 */
public class HttpServletUtil {
    /** 编码 **/
    private static final String ENCODED = "UTF-8";

    /** 引用 **/
    private static final String REFERER = "referer";

    /**
     * 获取当前请求的request对象
     *
     * @author xuyuxiang
     * @date 2020/3/30 15:10
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new ServiceException(ServerExceptionEnum.REQUEST_EMPTY);
        } else {
            return requestAttributes.getRequest();
        }
    }

    /**
     * 获取当前请求的response对象
     *
     * @author xuyuxiang
     * @date 2020/3/30 15:10
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new ServiceException(ServerExceptionEnum.REQUEST_EMPTY);
        } else {
            return requestAttributes.getResponse();
        }
    }

    /**
     * @description 从header.referer获取token信息
     * @author dongxiayu
     * @date 2022/7/2 0:46
     * @return
     **/
    public static String getTokenFromReferer(){
        String refererContent = getRequest().getHeader(REFERER);
        Map<String,String> paramMap = StrUtil.isNotBlank(refererContent)? HttpUtil.decodeParamMap(refererContent, CharsetUtil.charset(ENCODED)):null;
        String token = Objects.nonNull(paramMap)?paramMap.get(CommonConstant.TOKEN_NAME):null;
        return token;
    }
}
