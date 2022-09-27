package com.xz.seckill.validator;

import com.xz.seckill.util.ValidatorUtil;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author xz
 * 结合自定义注解一起使用
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    private boolean required = false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (required) {
            return ValidatorUtil.isMobile(s);
        } else {
            if (!StringUtils.hasText(s)) {
                return true;
            } else {
                return ValidatorUtil.isMobile(s);
            }
        }
    }
}
