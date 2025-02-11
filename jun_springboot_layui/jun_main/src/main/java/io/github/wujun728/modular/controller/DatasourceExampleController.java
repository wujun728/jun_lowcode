
package io.github.wujun728.modular.controller;

import io.github.wujun728.core.pojo.response.ResponseData;
import io.github.wujun728.core.pojo.response.SuccessResponseData;
import io.github.wujun728.modular.service.DatasourceExampleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 一个示例接口
 *
 * @date 2020/4/9 18:09
 */
@Controller
@RequestMapping("/example")
public class DatasourceExampleController {

    @Resource
    private DatasourceExampleService datasourceService;

    @ResponseBody
    @GetMapping("/niceDay")
    public ResponseData niceDay() {
        return new SuccessResponseData("nice day");
    }

    @ResponseBody
    @GetMapping("/masterDatasource")
    public ResponseData masterDatasource() {
        return new SuccessResponseData(datasourceService.masterDatasource());
    }

    @ResponseBody
    @GetMapping("/backupDatasource")
    public ResponseData backupDatasource() {
        return new SuccessResponseData(datasourceService.backupDatasource());
    }

    @ResponseBody
    @GetMapping("/datasourceTransactionNone")
    public ResponseData datasourceTransactionNone() {
        datasourceService.datasourceTransactionNone();
        return new SuccessResponseData();
    }

    @ResponseBody
    @GetMapping("/datasourceTransaction")
    public ResponseData datasourceTransaction() {
        datasourceService.datasourceTransaction();
        return new SuccessResponseData();
    }

}
