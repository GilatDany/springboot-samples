package top.wjqian.week08.sms.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SendCodeRequest(
        @NotBlank
        @Pattern(regexp = "^1\\d{10}$", message = "请输入 11 位国内手机号")
        String phone
) {
}