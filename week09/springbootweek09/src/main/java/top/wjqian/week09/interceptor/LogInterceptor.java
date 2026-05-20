package top.wjqian.week09.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Component
@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    public static final String ATTR_START_MS = "interceptor.log.startMs";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute(ATTR_START_MS, System.currentTimeMillis());
        log.info("[日志拦截器]请求进入path={},method={},ip={},time={}",request.getRequestURI(),request.getMethod(),request.getRemoteAddr(),LocalDateTime.now());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex){
       Long startMs = (Long) request.getAttribute(ATTR_START_MS);
       long cost=startMs!=null?System.currentTimeMillis()-startMs:-1L;
       log.info("[日志拦截器]请求结束path={},status={},耗时={}ms,time={},ex={}",request.getRequestURI(),request.getMethod(),cost, LocalDateTime.now(),ex!=null?ex.getMessage():null);
       if(ex!=null){
           log.warn("[日志拦截器]请求处理异常",ex);
       }
    }


}
