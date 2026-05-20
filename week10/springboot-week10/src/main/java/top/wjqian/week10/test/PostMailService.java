package top.wjqian.week10.test;


import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PostMailService {
//    @Resource
//    private JavaMailSender mailSender;
//
//    @Value("${spring.mail.username}")
//    private String fromEmail;
//
//    public void sendSimpleMail(String to, String subject, String content) {
//        try {
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setFrom(fromEmail);
//            message.setTo(to);
//            message.setSubject(subject);
//            message.setText(content);
//            mailSender.send(message);
//            log.info("邮件已成功发送至: {}", to);
//        } catch (Exception e) {
//            log.error("邮件发送失败: {}", e.getMessage());
//        }
//    }
}
