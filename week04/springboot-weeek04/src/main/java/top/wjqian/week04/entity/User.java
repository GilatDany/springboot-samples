package top.wjqian.week04.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Long id;

    private String username;

    private LocalDateTime createTime;
}
