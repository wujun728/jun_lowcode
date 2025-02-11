
package io.github.wujun728.sys.core.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import io.github.wujun728.core.consts.AopSortConstant;
import io.github.wujun728.core.context.login.LoginContextHolder;
import io.github.wujun728.core.pojo.base.param.BaseParam;

import java.util.List;

/**
 * 数据权限切面
 * @date 2020/3/28 17:16
 */
@Aspect
@Order(AopSortConstant.DATA_SCOPE_AOP)
public class DataScopeAop {

    /**
     * 数据范围切入点
     *
     * @author xuyuxiang
     * @date 2020/4/6 13:32
     */
    @Pointcut("@annotation(io.github.wujun728.core.annotion.DataScope)")
    private void getDataScopePointCut() {
    }

    /**
     * 执行数据范围过滤
     *
     * @author xuyuxiang
     * @date 2020/4/6 13:32
     */
    @Before("getDataScopePointCut()")
    public void doDataScope(JoinPoint joinPoint) {

        //不是超级管理员时进行数据权限过滤
        if (!LoginContextHolder.me().isSuperAdmin()) {
            Object[] args = joinPoint.getArgs();

            //数据范围就是组织机构id集合
            List<Long> loginUserDataScopeIdList = LoginContextHolder.me().getLoginUserDataScopeIdList();
            BaseParam baseParam;
            for (Object object : args) {
                if (object instanceof BaseParam) {
                    baseParam = (BaseParam) object;
                    baseParam.setDataScope(loginUserDataScopeIdList);
                }
            }
        }
    }
}
