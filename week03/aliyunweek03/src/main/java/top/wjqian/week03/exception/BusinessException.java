package top.wjqian.week03.exception;

import lombok.Getter;

public class BusinessException extends RuntimeException{
    @Getter
    private Integer code;

    private final String message;

    public BusinessException(String message){
        super(message);
        this.message = message;
    }

    public BusinessException(int code, String message){
        this.code=code;
        this.message=message;
    }

    @Override
    public String getMessage(){
        return message;
    }
}
