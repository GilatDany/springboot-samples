package top.wjqian.week10.test;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
public class SpecificTimeJob {
//    @Resource
//    private PostMailService postMailService;
//
////    private static final String TARGET_EMAIL = "16422802@qq.com";
////private static final String TARGET_EMAIL = "3352669450@qq.com";
//
//    private static final int TARGET_YEAR = 2026;
//    private static final int TARGET_MONTH = 5;
//    private static final int TARGET_DAY = 7;
//
//    @Scheduled(cron = "0 10 17 * * ?")
//    public void sendMailAtSpecificTime() {
//        LocalDateTime now = LocalDateTime.now();
//        LocalDate today = now.toLocalDate();
//
//        log.info("定时任务触发检查 -> 当前时间: {}", now);
//        if (today.getYear() == TARGET_YEAR &&
//                today.getMonthValue() == TARGET_MONTH &&
//                today.getDayOfMonth() == TARGET_DAY) {
//
//            log.info("✅ 命中目标日期: 2026-05-07 17:10，开始执行邮件发送...");
//
//            String subject = "定点提醒：2026年5月7日任务";
//            String content = "这是一封由软件2531-40钱文君 在 2026年5月7日 17:10 准时发送的测试邮件。";
//
//            postMailService.sendSimpleMail(TARGET_EMAIL, subject, content);
//
//        } else {
//            log.info("❌ 日期不匹配，今日不发送邮件。");
//        }
//    }
}