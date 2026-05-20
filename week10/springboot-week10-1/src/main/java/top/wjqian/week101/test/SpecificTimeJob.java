package top.wjqian.week101.test;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
public class SpecificTimeJob {
    @Resource
    private PostMailService postMailService;

    private static final String TARGET_EMAIL = "3352669450@qq.com";

    private static final int TARGET_YEAR = 2026;
    private static final int TARGET_MONTH = 5;
    private static final int TARGET_DAY = 8;

    @Scheduled(cron = "0 17 22 * * ?")
    public void sendMailAtSpecificTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();

        log.info("定时任务触发检查 -> 当前时间: {}", now);
        if (today.getYear() == TARGET_YEAR &&
                today.getMonthValue() == TARGET_MONTH &&
                today.getDayOfMonth() == TARGET_DAY) {

            log.info("✅ 命中目标日期: 2026-05-08 22:17，开始执行邮件发送...");

            String subject = "🎂 生日快乐！祝您拥有一个美好的一年！";
            String htmlContent = buildBirthdayEmail();

            postMailService.sendHtmlMail(TARGET_EMAIL, subject, htmlContent);

        } else {
            log.info("❌ 日期不匹配，今日不发送邮件。");
        }
    }

    private String buildBirthdayEmail() {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>");
        html.append("<html lang='zh-CN'>");
        html.append("<head>");
        html.append("    <meta charset='UTF-8'>");
        html.append("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        html.append("    <title>生日快乐</title>");
        html.append("</head>");
        html.append("<body style='margin: 0; padding: 0; font-family: \"Microsoft YaHei\", Arial, sans-serif; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);'>");
        html.append("    <div style='max-width: 600px; margin: 40px auto; background: white; border-radius: 20px; overflow: hidden; box-shadow: 0 10px 40px rgba(0,0,0,0.2);'>");

        // 头部区域
        html.append("        <div style='background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); padding: 40px 30px; text-align: center;'>");
        html.append("            <h1 style='margin: 0; color: white; font-size: 36px; text-shadow: 2px 2px 4px rgba(0,0,0,0.2);'>🎉 生日快乐 🎉</h1>");
        html.append("            <p style='margin: 10px 0 0 0; color: white; font-size: 18px; opacity: 0.9;'>Happy Birthday!</p>");
        html.append("        </div>");

        // 内容区域
        html.append("        <div style='padding: 40px 30px;'>");
        html.append("            <div style='text-align: center; margin-bottom: 30px;'>");
        html.append("                <div style='font-size: 80px; margin: 20px 0;'>🎂</div>");
        html.append("                <h2 style='color: #333; font-size: 24px; margin: 20px 0;'>亲爱的朋友</h2>");
        html.append("            </div>");

        html.append("            <div style='background: #f8f9fa; padding: 25px; border-radius: 10px; border-left: 4px solid #f5576c;'>");
        html.append("                <p style='color: #555; font-size: 16px; line-height: 1.8; margin: 0 0 15px 0;'>");
        html.append("                    在这个特别的日子里，送上最真挚的祝福：");
        html.append("                </p>");
        html.append("                <ul style='color: #555; font-size: 15px; line-height: 2; margin: 0; padding-left: 20px;'>");
        html.append("                    <li>✨ 愿您的每一天都充满阳光与欢笑</li>");
        html.append("                    <li>💪 愿您身体健康，活力满满</li>");
        html.append("                    <li>🎯 愿您事业有成，梦想成真</li>");
        html.append("                    <li>❤️ 愿您被爱包围，幸福常伴</li>");
        html.append("                    <li>🌟 愿新的一岁更加精彩辉煌</li>");
        html.append("                </ul>");
        html.append("            </div>");

        html.append("            <div style='text-align: center; margin-top: 30px; padding: 20px; background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%); border-radius: 10px;'>");
        html.append("                <p style='color: #333; font-size: 18px; font-weight: bold; margin: 0;'>");
        html.append("                    🎁 祝您生日快乐，年年有今日，岁岁有今朝！🎁");
        html.append("                </p>");
        html.append("            </div>");

        html.append("            <div style='margin-top: 30px; padding-top: 20px; border-top: 1px solid #eee;'>");
        html.append("                <p style='color: #999; font-size: 13px; margin: 5px 0;'>");
        html.append("                    📅 发送时间: ").append(LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss"))).append("</p>");
        html.append("                <p style='color: #999; font-size: 13px; margin: 5px 0;'>");
        html.append("                    👤 来自: 软件2531-40钱文君");
        html.append("                </p>");
        html.append("            </div>");

        html.append("        </div>");

        // 底部区域
        html.append("        <div style='background: #f8f9fa; padding: 20px; text-align: center; border-top: 1px solid #eee;'>");
        html.append("            <p style='color: #999; font-size: 12px; margin: 0;'>");
        html.append("                🎈 这是一封自动发送的生日祝福邮件 🎈");
        html.append("            </p>");
        html.append("        </div>");

        html.append("    </div>");
        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }
}
