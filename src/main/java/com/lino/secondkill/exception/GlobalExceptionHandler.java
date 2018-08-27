package com.lino.secondkill.exception;


import com.lino.secondkill.result.CodeMsg;
import com.lino.secondkill.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


//定义全局异常处理类
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(HttpServletRequest request,Exception e){
        if(e instanceof GlobalException){
            GlobalException globalException = (GlobalException)e;
            CodeMsg codeMsg = globalException.getCodeMsg();
            return Result.error(codeMsg);
        }else if(e instanceof BindException){
            BindException bex = (BindException)e;
            List<ObjectError> allErrors = bex.getAllErrors();
            //因为可能有多个参数检验所以返回多个参数校验结果，现在先取第一个，后期可以优化
            ObjectError objectError = allErrors.get(0);
            String msg = objectError.getDefaultMessage();
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
        }else{
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
