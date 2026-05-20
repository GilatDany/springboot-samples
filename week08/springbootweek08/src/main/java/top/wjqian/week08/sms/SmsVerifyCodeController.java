package top.wjqian.week08.sms;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import top.wjqian.week08.sms.dto.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sms")
@RequiredArgsConstructor
public class SmsVerifyCodeController {
    private final SmsVerifyCodeService smsVerifyCodeService;

    @PostMapping("/verify-codes")
    public ApiResult<SendCodeResponse> send(
            @RequestBody @Valid SendCodeRequest request) {
        return ApiResult.success(smsVerifyCodeService.sendCode(request.phone()));
    }

    @PostMapping("/verify-codes/validate")
    public ResponseEntity<ApiResult<ValidateCodeView>> validate(
            @RequestBody @Valid ValidateCodeRequest request) {
        if (!smsVerifyCodeService.validateCode(request.phone(), request.code())) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(ApiResult.error(422, "验证码错误或已过期"));
        }
        return ResponseEntity.ok(ApiResult.success(new ValidateCodeView(true)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<Void> onValid(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));
        return ApiResult.error(400, msg.isEmpty() ? "参数不合法" : msg);
    }
}
