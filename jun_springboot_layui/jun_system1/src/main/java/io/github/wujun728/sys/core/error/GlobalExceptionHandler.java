
package io.github.wujun728.sys.core.error;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.log.Log;
import io.github.wujun728.core.exception.enums.*;
import org.apache.ibatis.exceptions.PersistenceException;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;
import io.github.wujun728.core.consts.AopSortConstant;
import io.github.wujun728.core.context.requestno.RequestNoContext;
import io.github.wujun728.core.exception.AuthException;
import io.github.wujun728.core.exception.DemoException;
import io.github.wujun728.core.exception.PermissionException;
import io.github.wujun728.core.exception.ServiceException;
import io.github.wujun728.core.sms.modular.aliyun.exp.AliyunSmsException;
import io.github.wujun728.core.sms.modular.tencent.exp.TencentSmsException;
import io.github.wujun728.core.util.HttpServletUtil;
import io.github.wujun728.core.util.ResponseUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 * @date 2020/3/18 19:03
 */
@Order(AopSortConstant.GLOBAL_EXP_HANDLER_AOP)
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Log log = Log.get();

    /**
     * 腾讯云短信发送异常
     *
     *
     * @date 2020/6/7 18:03
     */
    @ResponseBody
    @ExceptionHandler(TencentSmsException.class)
    public Object aliyunSmsException(TencentSmsException e) {
        log.error(">>> 发送短信异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), e.getErrorMessage());
        return ResponseUtil.handleError(ServerExceptionEnum.SERVER_ERROR.getCode(), e.getErrorMessage());
    }

    /**
     * 阿里云短信发送异常
     *
     *
     * @date 2020/6/7 18:03
     */
    @ResponseBody
    @ExceptionHandler(AliyunSmsException.class)
    public Object aliyunSmsException(AliyunSmsException e) {
        log.error(">>> 发送短信异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), e.getErrorMessage());
        return ResponseUtil.handleError(ServerExceptionEnum.SERVER_ERROR.getCode(), e.getErrorMessage());
    }

    /**
     * 请求参数缺失异常
     *
     *
     * @date 2020/6/7 18:03
     */
    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Object missParamException(MissingServletRequestParameterException e) {
        log.error(">>> 请求参数异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), e.getMessage());
        String parameterType = e.getParameterType();
        String parameterName = e.getParameterName();
        String message = StrUtil.format(">>> 缺少请求的参数{}，类型为{}", parameterName, parameterType);
        return ResponseUtil.handleError(ServerExceptionEnum.SERVER_ERROR.getCode(), message);
    }

    /**
     * 拦截参数格式传递异常
     *
     * @author xuyuxiang
     * @date 2020/4/2 15:32
     */
    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Object httpMessageNotReadable(HttpMessageNotReadableException e) {
        log.error(">>> 参数格式传递异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), e.getMessage());
        return ResponseUtil.handleError(RequestTypeExceptionEnum.REQUEST_JSON_ERROR);
    }

    /**
     * 拦截不支持媒体类型异常
     *
     * @author xuyuxiang
     * @date 2020/4/2 15:38
     */
    @ResponseBody
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Object httpMediaTypeNotSupport(HttpMediaTypeNotSupportedException e) {
        log.error(">>> 参数格式传递异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), e.getMessage());
        return ResponseUtil.handleError(RequestTypeExceptionEnum.REQUEST_TYPE_IS_JSON);
    }

    /**
     * 拦截请求方法的异常
     *
     * @author xuyuxiang
     * @date 2020/3/18 19:14
     */
    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Object methodNotSupport(HttpServletRequest request) {
        if (ServletUtil.isPostMethod(request)) {
            log.error(">>> 请求方法异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), RequestMethodExceptionEnum.REQUEST_METHOD_IS_GET.getMessage());
            return ResponseUtil.handleError(RequestMethodExceptionEnum.REQUEST_METHOD_IS_GET);
        }
        if (ServletUtil.isGetMethod(request)) {
            log.error(">>> 请求方法异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), RequestMethodExceptionEnum.REQUEST_METHOD_IS_POST.getMessage());
            return ResponseUtil.handleError(RequestMethodExceptionEnum.REQUEST_METHOD_IS_POST);
        }
        return null;
    }

    /**
     * 拦截资源找不到的运行时异常
     *
     * @author xuyuxiang
     * @date 2020/4/2 15:38
     */
    @ResponseBody
    @ExceptionHandler(NoHandlerFoundException.class)
    public Object notFound(NoHandlerFoundException e) {
        log.error(">>> 资源不存在异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), e.getMessage() +"，请求地址为:" + HttpServletUtil.getRequest().getRequestURI());
        return ResponseUtil.handleError(PermissionExceptionEnum.URL_NOT_EXIST.getCode(), PermissionExceptionEnum.URL_NOT_EXIST.getMessage() +"，请求地址为:" + HttpServletUtil.getRequest().getRequestURI());
    }

    /**
     * 拦截参数校验错误异常,JSON传参
     *
     * @author xuyuxiang
     * @date 2020/4/2 15:38
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object methodArgumentNotValidException(MethodArgumentNotValidException e) {
        String argNotValidMessage = ResponseUtil.getArgNotValidMessage(e.getBindingResult());
        log.error(">>> 参数校验错误异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), argNotValidMessage);
        return ResponseUtil.handleError(ParamExceptionEnum.PARAM_ERROR.getCode(), argNotValidMessage);
    }

    /**
     * 拦截参数校验错误异常
     *
     * @author xuyuxiang
     * @date 2020/3/18 19:41
     */
    @ResponseBody
    @ExceptionHandler(BindException.class)
    public Object paramError(BindException e) {
        String argNotValidMessage = ResponseUtil.getArgNotValidMessage(e.getBindingResult());
        log.error(">>> 参数校验错误异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), argNotValidMessage);
        return ResponseUtil.handleError(ParamExceptionEnum.PARAM_ERROR.getCode(), argNotValidMessage);
    }

    /**
     * 拦截认证失败异常
     *
     * @author xuyuxiang
     * @date 2020/3/18 19:41
     */
    @ResponseBody
    @ExceptionHandler(AuthException.class)
    public Object authFail(AuthException e) {
        log.error(">>> 认证异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), e.getMessage());
        return ResponseUtil.handleError(e.getCode(), e.getErrorMessage());
    }

    /**
     * 拦截权限异常
     *
     * @author xuyuxiang
     * @date 2020/3/18 19:41
     */
    @ResponseBody
    @ExceptionHandler(PermissionException.class)
    public Object noPermission(PermissionException e) {
        log.error(">>> 权限异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), e.getMessage() +"，请求地址为:" + HttpServletUtil.getRequest().getRequestURI());
        return ResponseUtil.handleError(e.getCode(), e.getErrorMessage() + "，请求地址为:" + HttpServletUtil.getRequest().getRequestURI());
    }

    /**
     * 拦截业务异常
     *
     * @author xuyuxiang
     * @date 2020/3/18 19:41
     */
    @ResponseBody
    @ExceptionHandler(ServiceException.class)
    public Object businessError(ServiceException e) {
        log.error(">>> 业务异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), e.getMessage());
        return ResponseUtil.handleError(e.getCode(), e.getErrorMessage(), e);
    }

    /**
     * 拦截mybatis数据库操作的异常
     * <p>
     * 用在demo模式，拦截DemoException
     *
     *
     * @date 2020/5/5 15:19
     */
    @ResponseBody
    @ExceptionHandler(MyBatisSystemException.class)
    public Object persistenceException(MyBatisSystemException e) {
        log.error(">>> mybatis操作出现异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), e.getMessage());
        Throwable cause = e.getCause();
        if (cause instanceof PersistenceException) {
            Throwable secondCause = cause.getCause();
            if (secondCause instanceof DemoException) {
                DemoException demoException = (DemoException) secondCause;
                return ResponseUtil.handleErrorWithoutWrite(demoException.getCode(), demoException.getErrorMessage(), e.getStackTrace()[0].toString());
            }
        }
        return ResponseUtil.handleErrorWithoutWrite(ServerExceptionEnum.SERVER_ERROR.getCode(), ServerExceptionEnum.SERVER_ERROR.getMessage(), e.getStackTrace()[0].toString());
    }

    /**
     * 拦截未知的运行时异常
     *
     * @author xuyuxiang
     * @date 2020/3/18 19:41
     */
    @ResponseBody
    @ExceptionHandler(Throwable.class)
    public Object serverError(Throwable e) {
        log.error(">>> 服务器运行异常，请求号为：{}", RequestNoContext.get());
        e.printStackTrace();
        return ResponseUtil.handleError(e);
    }
}
