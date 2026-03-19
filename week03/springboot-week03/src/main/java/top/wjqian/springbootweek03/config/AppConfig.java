package top.wjqian.springbootweek03.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wencyqian
 * spring创建对象只有一个
 */
@Component
@ConfigurationProperties(prefix="app")//批量读取以app开头的属性
@Data
public class AppConfig {//data注解，生成测试alt+enter
    private String appName;
    private String version;
    private String description;
    private List<String> envs;
    private  Author author;
    private  Integer tokens;
    private Boolean enabled=true;

    @Data
    public static class Author{
        private String name;
        private String website;
        private String email;
    }
}
