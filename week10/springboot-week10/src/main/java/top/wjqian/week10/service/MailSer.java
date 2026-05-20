package top.wjqian.week10.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailSer {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${birthday.mail.subject}")
    private String subject;

    @Value("${birthday.mail.template}")
    private String template;

    /**
     * 发送生日祝福邮件
     * @param to 收件人邮箱
     * @param name 收件人姓名
     */
    public void sendBirthdayEmail(String to, String name) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(String.format(template, name));

            mailSender.send(message);
            log.info("生日邮件发送成功 - 收件人: {}, 姓名: {}", to, name);
        } catch (Exception e) {
            log.error("生日邮件发送失败 - 收件人: {}, 错误: {}", to, e.getMessage(), e);
        }
    }
}
