package top.wjqian.week04.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Spring 配置类
@Configuration
class BeanConfig {

    // @Bean 创建 Student
    @Bean
    public Student student() {
        return new Student("qwj",23);
    }
}

// 测试运行类
public class TestStudent {
    public static void main(String[] args) {

        ApplicationContext ctx = new AnnotationConfigApplicationContext(BeanConfig.class);

        Student student = ctx.getBean(Student.class);

        System.out.println(student);
    }
}
