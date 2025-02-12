
package io.github.wujun728.core.pojo.page;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果集
 * @date 2020/3/30 15:44
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 状态码
     */
    private Integer code = 0;

    /**
     * 消息
     */
    private String msg = "请求成功";

    /**
     * 第几页
     */
    private Integer page = 1;

    /**
     * 每页条数
     */
    private Integer limit = 20;

    /**
     * 总记录数
     */
    private Integer count = 0;

    /**
     * 结果集
     */
    private List<T> data;

    public PageResult() {
    }

    /**
     * 将mybatis-plus的page转成自定义的PageResult
     *
     * @author xuyuxiang
     * @date 2020/4/8 19:20
     */
    public PageResult(Page<T> page) {
        this.setData(page.getRecords());
        this.setCount(Convert.toInt(page.getTotal()));
        this.setPage(Convert.toInt(page.getCurrent()));
        this.setLimit(Convert.toInt(page.getSize()));
    }

    /**
     * 将list转为分页方式（无分页）
     *
     * @author xuyuxiang
     * @date 2020/12/4 15:47
     */
    public PageResult(List<T> t) {
        this.setData(t);
    }

    /**
     * 将mybatis-plus的page转成自定义的PageResult
     * 可单独设置rows
     *
     * @author xuyuxiang
     * @date 2020/4/14 20:55
     */
    public PageResult(Page<T> page, List<T> t) {
        this.setData(t);
        this.setCount(Convert.toInt(page.getTotal()));
        this.setPage(Convert.toInt(page.getCurrent()));
        this.setLimit(Convert.toInt(page.getSize()));
    }
}
