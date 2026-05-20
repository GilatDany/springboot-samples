package top.wjqian.week03.controller;

import com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.wjqian.week03.common.Result;
import top.wjqian.week03.service.AliyunSmsService;

import java.util.Map;

@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
public class AliyunSmsController {
    private final AliyunSmsService smsService;

    @PostMapping("/send-code")
    public Result<Map<String, String>> sendCode(@RequestBody Map<String,
            String> body) {
        String phone = body.get("phone");
        if (phone == null || phone.isBlank()) {
            return Result.error(400, "⼿机号不能为空 ");
        }
        try {
            SendSmsVerifyCodeResponse response = smsService.sendVerifyCodeAuto(phone);
            if (response.getBody() != null && "OK".equals(response.getBody().getCode())) {
                var model = response.getBody().getModel();
                String bizId = model != null ? model.getBizId() : "";
                String outId = model != null ? model.getOutId() : "";
                return Result.success(Map.of(
                        "message", " 验证码已发送 ",
                        "bizId", bizId != null ? bizId : "",
                        "outId", outId != null ? outId : ""
                ));
            }
            return Result.error(500, response.getBody() != null ?
                    response.getBody().getMessage() : "发送失败");
        } catch (Exception e) {
            return Result.error(500, "发送失败：" + e.getMessage());
        }
    }

    @PostMapping("/verify")
    public Result<Boolean> verify(@RequestBody Map<String, String> body) {
        String phone = body.get("phone");
        String code = body.get("code");
        if (phone == null || phone.isBlank() || code == null || code.isBlank()) {
            return Result.error(400, "⼿机号和验证码不能为空");
        }
        try {
            boolean pass = smsService.checkVerifyCode(phone, code, body.get("outId"));
            return Result.success(pass);
        } catch (Exception e) {
            return Result.error(500, "验证失败：" + e.getMessage());
        }
    }
}

