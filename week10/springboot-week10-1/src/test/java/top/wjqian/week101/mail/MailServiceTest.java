package top.wjqian.week101.mail;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MailServiceTest {

    @Resource
    MailService mailService;

    @Test
    public void sendSimpleMail() {
        mailService.sendSimpleMail("1272065161@qq.com", "普通文本邮件", "普通文本邮件内容测试");
    }

    @Test
    public void sendHtmlMail() throws MessagingException {
        String activationLink = "https://example.com/activate?token=YOUR_ACTIVATION_TOKEN";

        mailService.sendHtmlMail("1272065161@qq.com", "恭喜您注册成功！请激活您的账号", """
                    <div style="max-width:600px;margin:0 auto;font-family:Arial;background:#f5f5f5;padding:20px;">
                        <div style="background:linear-gradient(135deg,#667eea,#764ba2);padding:30px;text-align:center;color:white;">
                            <h2>🎉 欢迎加入我们！</h2>
                        </div>
                        <div style="background:white;padding:30px;">
                            <h3>尊敬的用户，您好！</h3>
                            <p>恭喜您成功注册我们的平台。为了保障您的账号安全，请点击下方按钮激活您的账号：</p>
                            <div style="text-align:center;margin:30px 0;">
                                <a href="%s" style="background:linear-gradient(135deg,#667eea,#764ba2);color:white;padding:12px 30px;text-decoration:none;border-radius:25px;display:inline-block;">立即激活账号</a>
                            </div>
                            <div style="background:#f8f9fa;padding:15px;font-size:12px;color:#999;">
                                <strong>温馨提示：</strong><br>
                                如果按钮无法点击，请复制以下链接到浏览器：<br>
                                <a href="%s" style="color:#667eea;">%s</a><br><br>
                                <span style="color:#ff6b6b;">激活链接有效期为24小时，请尽快完成激活。</span>
                            </div>
                        </div>
                        <div style="background:#f5f5f5;padding:20px;text-align:center;font-size:12px;color:#999;">
                            <p>此邮件由系统自动发送，请勿直接回复</p>
                        </div>
                    </div>
                """.formatted(activationLink, activationLink, activationLink));
    }

    @Test
    void sendAttachmentsMail() throws MessagingException {
        String filePaths = "D:\\AAAA_qianwenjun\\背景\\紫4.PNG";
        String filePaths2 = "D:\\AAAA_qianwenjun\\背景\\紫3.PNG";
        mailService.sendAttachmentsMail("1272065161@qq.com", "这是一封带附件的邮件--来自wjqian", "邮件中带附件，请注意查收", filePaths, filePaths2);
    }
}