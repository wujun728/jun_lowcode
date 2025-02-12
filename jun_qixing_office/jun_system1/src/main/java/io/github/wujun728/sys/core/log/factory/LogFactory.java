
package io.github.wujun728.sys.core.log.factory;

import cn.hutool.core.date.DateTime;
import io.github.wujun728.sys.modular.log.entity.SysOpLog;
import io.github.wujun728.sys.modular.log.entity.SysVisLog;
import io.github.wujun728.sys.modular.log.enums.SysLogSuccessStatusEnum;
import io.github.wujun728.sys.modular.log.enums.SysVisLogTypeEnum;
import org.aspectj.lang.JoinPoint;
import io.github.wujun728.core.annotion.BusinessLog;
import io.github.wujun728.core.consts.SymbolConstant;
import io.github.wujun728.core.util.JoinPointUtil;

import java.util.Arrays;

/**
 * 日志对象创建工厂
 * @date 2020/3/12 14:31
 */
public class LogFactory {

    /**
     * 创建登录日志
     *
     * @author xuyuxiang
     * @date 2020/3/12 16:09
     */
    static void createSysLoginLog(SysVisLog sysVisLog, String account, String successCode, String failMessage) {
        sysVisLog.setName(SysVisLogTypeEnum.LOGIN.getMessage());
        sysVisLog.setSuccess(successCode);

        sysVisLog.setVisType(SysVisLogTypeEnum.LOGIN.getCode());
        sysVisLog.setVisTime(DateTime.now());
        sysVisLog.setAccount(account);

        if (SysLogSuccessStatusEnum.SUCCESS.getCode().equals(successCode)) {
            sysVisLog.setMessage(SysVisLogTypeEnum.LOGIN.getMessage() + SysLogSuccessStatusEnum.SUCCESS.getMessage());
        }
        if (SysLogSuccessStatusEnum.FAIL.getCode().equals(successCode)) {
            sysVisLog.setMessage(SysVisLogTypeEnum.LOGIN.getMessage() +
                    SysLogSuccessStatusEnum.FAIL.getMessage() + SymbolConstant.COLON + failMessage);
        }
    }

    /**
     * 创建登出日志
     *
     * @author xuyuxiang
     * @date 2020/3/12 16:09
     */
    static void createSysExitLog(SysVisLog sysVisLog, String account) {
        sysVisLog.setName(SysVisLogTypeEnum.EXIT.getMessage());
        sysVisLog.setSuccess(SysLogSuccessStatusEnum.SUCCESS.getCode());
        sysVisLog.setMessage(SysVisLogTypeEnum.EXIT.getMessage() + SysLogSuccessStatusEnum.SUCCESS.getMessage());
        sysVisLog.setVisType(SysVisLogTypeEnum.EXIT.getCode());
        sysVisLog.setVisTime(DateTime.now());
        sysVisLog.setAccount(account);
    }

    /**
     * 创建操作日志
     *
     * @author xuyuxiang
     * @date 2020/3/12 16:09
     */
    static void createSysOperationLog(SysOpLog sysOpLog, String account, BusinessLog businessLog, JoinPoint joinPoint, String result) {
        fillCommonSysOpLog(sysOpLog, account, businessLog, joinPoint);
        sysOpLog.setSuccess(SysLogSuccessStatusEnum.SUCCESS.getCode());
        sysOpLog.setResult(result);
        sysOpLog.setMessage(SysLogSuccessStatusEnum.SUCCESS.getMessage());
    }

    /**
     * 创建异常日志
     *
     * @author xuyuxiang
     * @date 2020/3/16 17:18
     */
    static void createSysExceptionLog(SysOpLog sysOpLog, String account, BusinessLog businessLog, JoinPoint joinPoint, Exception exception) {
        fillCommonSysOpLog(sysOpLog, account, businessLog, joinPoint);
        sysOpLog.setSuccess(SysLogSuccessStatusEnum.FAIL.getCode());
        sysOpLog.setMessage(Arrays.toString(exception.getStackTrace()));
    }

    /**
     * 生成通用操作日志字段
     *
     * @author xuyuxiang
     * @date 2020/3/16 17:20
     */
    private static void fillCommonSysOpLog(SysOpLog sysOpLog, String account, BusinessLog businessLog, JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getName();

        String methodName = joinPoint.getSignature().getName();

        String param = JoinPointUtil.getArgsJsonString(joinPoint);

        sysOpLog.setName(businessLog.title());
        sysOpLog.setOpType(businessLog.opType().ordinal());
        sysOpLog.setClassName(className);
        sysOpLog.setMethodName(methodName);
        sysOpLog.setParam(param);
        sysOpLog.setOpTime(DateTime.now());
        sysOpLog.setAccount(account);
    }

    /**
     * 构建基础访问日志
     *
     * @author xuyuxiang
     * @date 2020/3/19 14:36
     */
    public static SysVisLog genBaseSysVisLog(String ip, String location, String browser, String os) {
        SysVisLog sysVisLog = new SysVisLog();
        sysVisLog.setIp(ip);
        sysVisLog.setLocation(location);
        sysVisLog.setBrowser(browser);
        sysVisLog.setOs(os);
        return sysVisLog;
    }

    /**
     * 构建基础操作日志
     *
     * @author xuyuxiang
     * @date 2020/3/19 14:36
     */
    public static SysOpLog genBaseSysOpLog(String ip, String location, String browser, String os, String url, String method) {
        SysOpLog sysOpLog = new SysOpLog();
        sysOpLog.setIp(ip);
        sysOpLog.setLocation(location);
        sysOpLog.setBrowser(browser);
        sysOpLog.setOs(os);
        sysOpLog.setUrl(url);
        sysOpLog.setReqMethod(method);
        return sysOpLog;
    }

}
