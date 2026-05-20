package top.wjqian.week03.service;


import com.aliyun.dypnsapi20170525.Client;
import com.aliyun.dypnsapi20170525.models.CheckSmsVerifyCodeRequest;
import com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeRequest;
import com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeResponse;
import com.aliyun.teaopenapi.models.Config;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.wjqian.week03.config.AliyunSmsProperties;
import top.wjqian.week03.exception.BusinessException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AliyunSmsService {
    private final AliyunSmsProperties smsProperties;

    private Client createClient() throws Exception {
        Config config = new Config()
                .setAccessKeyId(smsProperties.getAccessKeyId())
                .setAccessKeySecret(smsProperties.getAccessKeySecret());
        config.endpoint = smsProperties.getEndpoint();
        config.regionId = smsProperties.getRegionId();
        return new Client(config);
    }

    public SendSmsVerifyCodeResponse sendVerifyCodeAuto(String phone) throws Exception {
        String templateParam = "{\"code\":\"##code##\",\"min\":\"5\"}";
        return sendVerifyCode(phone, templateParam, true);
    }

    public SendSmsVerifyCodeResponse sendVerifyCode(String phone, String templateParam, boolean returnVerifyCode)
            throws Exception {
        if (!smsProperties.isEnabled()) {
            throw new BusinessException(500, "短信服务未启用");
        }

        if (smsProperties.getAccessKeyId() == null || smsProperties.getAccessKeyId().isBlank()) {
            throw new BusinessException(500, "短信服务未配置AccessKey，请设置环境变量 ALIYUN_SMS_ACCESS_KEY_ID");
        }

        SendSmsVerifyCodeRequest request = new SendSmsVerifyCodeRequest()
                .setPhoneNumber(phone)
                .setSignName(smsProperties.getSignName())
                .setTemplateCode(smsProperties.getTemplateCode())
                .setTemplateParam(templateParam)
                .setCodeLength(6L)
                .setValidTime(300L)
                .setDuplicatePolicy(1L)
                .setInterval(60L)
                .setCodeType(1L)
                .setReturnVerifyCode(returnVerifyCode);
        //设置方案名称（可选）
        if (smsProperties.getSchemeName() != null && !smsProperties.getSchemeName().isBlank()) {
            request.setSchemeName(smsProperties.getSchemeName());
        }

        //发送短信
        Client client = createClient();
        SendSmsVerifyCodeResponse response = client.sendSmsVerifyCode(request);

        if (response.getBody() != null) {
            String bizCode = response.getBody().getCode();
            if ("OK".equals(bizCode)) {
                log.info("短信验证码发送成功，手机号={},bizId={}",
                        maskPhone(phone),
                        response.getBody().getModel() != null ?
                                response.getBody().getModel().getBizId() : "");
            } else {
                log.warn("短信验证码发送失败，手机号={}，code={},message={}",
                        maskPhone(phone), bizCode, response.getBody().getMessage());

            }
        }
        return response;
    }

    public boolean checkVerifyCode(String phone,String verifyCode,String outId)
        throws Exception{
        if(!smsProperties.isEnabled()){
            return false;
        }
        CheckSmsVerifyCodeRequest req=new CheckSmsVerifyCodeRequest()
                .setPhoneNumber(phone)
                .setVerifyCode(verifyCode);

        if(outId!=null&& !outId.isBlank()){
            req.setOutId(outId);

        }
        if (smsProperties.getSchemeName()!= null && !smsProperties.getSchemeName().isBlank()) {
            req.setSchemeName(smsProperties.getSchemeName());
        }
        Client client = createClient();
        var resp = client.checkSmsVerifyCode(req);
        if (resp.getBody() != null && resp.getBody().getModel() != null)
        {
            return "PASS".equals(resp.getBody().getModel().getVerifyResult());
        }
        return false;
    }
    private String maskPhone(String phone) {
        if (phone == null || phone.length() < 7) {
            return "***";
        }
        return phone.substring(0, 3) + "***" + phone.substring(phone.length() - 4);
    }
}
