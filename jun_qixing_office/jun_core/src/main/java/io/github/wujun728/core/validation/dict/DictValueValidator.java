
package io.github.wujun728.core.validation.dict;

import io.github.wujun728.core.context.system.SystemContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * 字典值校验
 *
 * @date 2020/4/14 23:49
 */
public class DictValueValidator implements ConstraintValidator<DictValue, String> {

    private String[] dictType;

    @Override
    public void initialize(DictValue constraintAnnotation) {
        this.dictType = constraintAnnotation.dictType();
    }

    @Override
    public boolean isValid(String dictValue, ConstraintValidatorContext context) {
        List<String> dictCodes = SystemContextHolder.me().getDictCodesByDictTypeCode(dictType);
        if (dictCodes != null && dictCodes.size() > 0) {
            return dictCodes.contains(dictValue);
        } else {
            return false;
        }
    }
}
