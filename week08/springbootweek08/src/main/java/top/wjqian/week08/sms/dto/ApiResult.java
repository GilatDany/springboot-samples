package top.wjqian.week08.sms.dto;

public record ApiResult<T>(int code,String message,T data) {
    public static<T> ApiResult<T> success(T data){
        return new ApiResult<>(0,"success",data);
    }
    public static<T> ApiResult<T> error(int code,String message){
        return new ApiResult<>(code,message,null);
    }
}
