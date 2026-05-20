package top.wjqian.week08.sms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ValidateCodeRequest(
        @NotBlank
        @Pattern(regexp = "^1\\d{10}$")
        String phone,
        @NotBlank
        @Size(min = 4, max = 8)
        String code
){}
