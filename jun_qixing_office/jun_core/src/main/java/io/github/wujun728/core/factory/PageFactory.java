
package io.github.wujun728.core.factory;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.wujun728.core.util.HttpServletUtil;

import javax.servlet.http.HttpServletRequest;


/**
 * 默认分页参数构建
 *
 * @date 2017/11/15 13:52
 */
public class PageFactory {

    /**
     * 每页大小（默认20）
     */
    private static final String PAGE_SIZE_PARAM_NAME = "limit";

    /**
     * 第几页（从1开始）
     */
    private static final String PAGE_NO_PARAM_NAME = "page";

    /**
     * 默认分页，在使用时PageFactory.defaultPage会自动获取limit和page参数
     *
     * @author xuyuxiang
     * @date 2020/3/30 16:42
     */
    public static <T> Page<T> defaultPage() {

        int limit = 20;
        int page = 1;

        HttpServletRequest request = HttpServletUtil.getRequest();

        //每页条数
        String pageSizeString = request.getParameter(PAGE_SIZE_PARAM_NAME);
        if (ObjectUtil.isNotEmpty(pageSizeString)) {
            limit = Integer.parseInt(pageSizeString);
        }

        //第几页
        String pageNoString = request.getParameter(PAGE_NO_PARAM_NAME);
        if (ObjectUtil.isNotEmpty(pageNoString)) {
            page = Integer.parseInt(pageNoString);
        }

        return new Page<>(page, limit);
    }

}
