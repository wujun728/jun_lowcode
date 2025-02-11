
package io.github.wujun728.sys.config;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.github.wujun728.core.context.constant.ConstantContextHolder;
import io.github.wujun728.core.file.FileOperator;
import io.github.wujun728.core.file.modular.local.LocalFileOperator;
import io.github.wujun728.core.file.modular.local.prop.LocalFileProperties;

/**
 * 文件存储的配置
 * <p>
 * 默认激活本地文件存储
 *
 * @date 2020/6/6 22:27
 */
@Configuration
public class FileConfig {

    /**
     * 默认文件存储的位置
     */
    public static final String DEFAULT_BUCKET = "defaultBucket";

    /**
     * 本地文件操作客户端
     *
     *
     * @date 2020/6/9 21:39
     */
    @Bean
    public FileOperator fileOperator() {
        LocalFileProperties localFileProperties = new LocalFileProperties();
        String fileUploadPathForWindows = ConstantContextHolder.getDefaultFileUploadPathForWindows();
        if (ObjectUtil.isNotEmpty(fileUploadPathForWindows)) {
            localFileProperties.setLocalFileSavePathWin(fileUploadPathForWindows);
        }

        String fileUploadPathForLinux = ConstantContextHolder.getDefaultFileUploadPathForLinux();
        if (ObjectUtil.isNotEmpty(fileUploadPathForLinux)) {
            localFileProperties.setLocalFileSavePathLinux(fileUploadPathForLinux);
        }
        return new LocalFileOperator(localFileProperties);
    }

}
