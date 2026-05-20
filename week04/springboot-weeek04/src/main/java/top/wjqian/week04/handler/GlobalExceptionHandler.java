package top.wjqian.week04.handler;


import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.wjqian.week04.common.Result;
import top.wjqian.week04.exception.BusinessException;

import java.util.StringJoiner;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidException(MethodArgumentNotValidException e) {
       StringJoiner sj= new StringJoiner(";");
       for(FieldError fieldError: e.getBindingResult().getFieldErrors()){
           sj.add(fieldError.getDefaultMessage());
       }
       return Result.error(400,sj.toString());
    }

    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        return Result.error(e.getCode(),e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        return Result.error(500,"服务器异常，请稍后重试");
    }
}






















