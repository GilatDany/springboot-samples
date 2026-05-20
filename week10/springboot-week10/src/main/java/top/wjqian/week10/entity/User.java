package top.wjqian.week10.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {
    @TableId(type= IdType.AUTO)
    private Long id;
    /** 姓名 */
    private String name;

    /** 邮箱 */
    private String email;

    /** 生日 */
    private LocalDate birthday;

    /** 出生年份 */
    private Integer birthdayYear;

    /** 最后一次发送邮件的年份 */
    private String lastSentYear;

    /** 是否启用 */
    private Integer enabled;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
