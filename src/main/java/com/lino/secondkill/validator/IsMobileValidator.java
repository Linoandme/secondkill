package com.lino.secondkill.validator;

import com.alibaba.druid.util.StringUtils;
import com.lino.secondkill.util.ValidatorUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {
    private boolean required = false;
    //初始化方法
    @Override
    public void initialize(IsMobile constraintAnnotation) {
        //看看传进来的参数是否是必须的
        required = constraintAnnotation.required();
    }
    //做参数校验
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(required){
            return ValidatorUtil.isMobile(s);
        }else{
            if(StringUtils.isEmpty(s)){
                return true;
            }else {
                return ValidatorUtil.isMobile(s);
            }
        }
    }

}
