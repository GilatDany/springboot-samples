package top.wjqian.hellosample.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.wjqian.hellosample.vo.ResultVO;

/**
 * 欢迎接口
 * @author wencyqian
 */
@RestController
@RequestMapping("/api")
public class HelloController {

    /**
     * Hello接口，返回统一格式的欢迎信息
     * @return 封装后的响应结果
     */
    @GetMapping("/hello")
    public ResultVO<String> hello() {
        return new ResultVO<>("Hello Spring Boot，Welcome");
    }
}