package top.wjqian.week09.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
public class HelloController {
    @GetMapping("/hello")
    public ResponseEntity<String> getHello() {
        log.info("进入接口，打印hello");
        return ResponseEntity.ok("hello");
    }
    @GetMapping("/world")
    public ResponseEntity<String> getWorld() {
        log.info("进入接口，打印world");

        return ResponseEntity.ok("world");
    }

}
