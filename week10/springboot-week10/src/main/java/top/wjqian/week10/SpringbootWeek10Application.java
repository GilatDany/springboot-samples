package top.wjqian.week10;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("top.wjqian.week10.mapper")
public class SpringbootWeek10Application {

    public static void main(String[] args) {

        SpringApplication.run(SpringbootWeek10Application.class, args);

        System.out.println("\n" + "=".repeat(50));
        System.out.println("✅ 定时任务应用启动成功！");
        System.out.println("=".repeat(50));
        System.out.println("📧 生日邮件任务：每天 09:00 检查并发送");
        System.out.println("🌤️ 天气更新任务：每 5 分钟获取并存储到数据库");
        System.out.println("🗑️ 数据清理任务：每天 02:00 清理30天前的旧数据");
        System.out.println("=".repeat(50) + "\n");
    }

}
