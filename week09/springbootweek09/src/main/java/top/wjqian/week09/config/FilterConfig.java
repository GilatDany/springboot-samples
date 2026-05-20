package top.wjqian.week09.config;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import top.wjqian.week09.filter.AuthFilter;
import top.wjqian.week09.filter.CORSFilter;
import top.wjqian.week09.filter.CustomFilter;
import top.wjqian.week09.filter.LogFilter;

//@Configuration
public class FilterConfig {
//    @Bean
//    public FilterRegistrationBean<CORSFilter> corsFilter() {
//        FilterRegistrationBean<CORSFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new CORSFilter());
//        registrationBean.addUrlPatterns("/api/hello");
//        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//        return registrationBean;
//    }
//    @Bean
//    public FilterRegistrationBean<AuthFilter> authFilter() {
//        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new AuthFilter());
//        registrationBean.addUrlPatterns("/api/hello");
//        registrationBean.setOrder(3);
//        return registrationBean;
//    }
//    @Bean
//    public FilterRegistrationBean<LogFilter> logFilterRegistration() {
//        FilterRegistrationBean<LogFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new LogFilter());
//        registrationBean.addUrlPatterns("/api/hello");
//        registrationBean.setOrder(2);
//        return registrationBean;
//    }

//    @Bean
//    public FilterRegistrationBean<CustomFilter> customFilterRegistration(){
//        FilterRegistrationBean<CustomFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new CustomFilter());
//        registrationBean.addUrlPatterns("/api/*");
//        registrationBean.setOrder(1);
//        return registrationBean;
//    }

}
