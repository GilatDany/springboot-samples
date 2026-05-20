package top.wjqian.week09;

import jakarta.servlet.*;

import java.io.IOException;

public interface Filter {
    default void init(FilterConfig filterConfig) throws ServletException{

    }

    void doFilter(ServletRequest var1, ServletResponse var2, FilterChain var3) throws IOException, ServletException;

    default void destroy(){

    }

}
