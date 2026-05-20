package top.wjqian.week08.sms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SendCodeResponse(
        String phone,
        int ttlSeconds,
        String codePlain
) {
}