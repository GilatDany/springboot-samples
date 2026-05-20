package top.wjqian.week10.task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.wjqian.week10.entity.User;
import top.wjqian.week10.entity.WeatherData;
import top.wjqian.week10.mapper.UserMapper;
import top.wjqian.week10.service.MailSer;
import top.wjqian.week10.service.WeatherService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 定时任务类
 */
@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {

    private final UserMapper userMapper;
    private final MailSer mailSer;
    private final WeatherService weatherService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // ==================== 定时任务1：生日邮件 ====================

    /**
     * 任务1：定点发送生日祝福邮件
     * 每天上午9:00执行，检查今天是否有用户过生日
     */
    @Scheduled(cron = "0 6 22 * * ?")
    public void sendBirthdayEmailJob() {
        log.info("========== 开始执行生日邮件定时任务 ==========");
        log.info("执行时间: {}", LocalDateTime.now().format(DATE_FORMATTER));

        long startTime = System.currentTimeMillis();

        try {
            LocalDate today = LocalDate.now();
            String currentYear = String.valueOf(today.getYear());

            // 查询今天过生日的用户
            List<User> birthdayUsers = userMapper.findTodayBirthdayUsers(today);

            log.info("今日过生日的用户数量: {}", birthdayUsers.size());

            int successCount = 0;
            int skipCount = 0;

            for (User user : birthdayUsers) {
                // 检查今年是否已经发送过
                if (currentYear.equals(user.getLastSentYear())) {
                    log.info("用户 {} 今年已发送过生日邮件，跳过", user.getName());
                    skipCount++;
                    continue;
                }

                // 发送邮件
                mailSer.sendBirthdayEmail(user.getEmail(), user.getName());

                // 更新发送记录
                userMapper.updateLastSentYear(user.getId(), currentYear);

                log.info("✅ 已为 {} ({}) 发送生日祝福邮件", user.getName(), user.getEmail());
                successCount++;
            }

            long duration = System.currentTimeMillis() - startTime;
            log.info("生日邮件任务完成 - 成功: {}, 跳过: {}, 总耗时: {} ms",
                    successCount, skipCount, duration);

        } catch (Exception e) {
            log.error("生日邮件任务执行失败: {}", e.getMessage(), e);
        }

        log.info("========== 生日邮件定时任务执行完成 ==========\n");
    }

    // ==================== 定时任务2：天气数据更新 ====================

    /**
     * 任务2：每5分钟获取一次天气数据并更新到数据库
     * fixedRate = 300000 毫秒 = 5分钟
     *
     * 执行内容：
     * 1. 调用天气 API 获取实时数据
     * 2. 将数据存储到 weather_data 表
     * 3. 记录日志便于追踪
     */
    @Scheduled(fixedRate = 300000)  // 5分钟
    public void updateWeatherDataJob() {
        log.info("========== 开始执行天气数据更新定时任务 ==========");
        log.info("执行时间: {}", LocalDateTime.now().format(DATE_FORMATTER));

        long startTime = System.currentTimeMillis();

        try {
            // 获取天气数据并存储到数据库
            WeatherData weatherData = weatherService.fetchAndSaveWeather();

            long duration = System.currentTimeMillis() - startTime;

            // 输出详细的天气信息
            if (weatherData.getId() != null) {
                log.info("✅ 天气数据更新成功!");
                log.info("   ├─ 数据ID: {}", weatherData.getId());
                log.info("   ├─ 城市: {} ({})", weatherData.getLocationName(), weatherData.getLocation());
                log.info("   ├─ 温度: {}°C", weatherData.getTemperature());
                log.info("   ├─ 湿度: {}%", weatherData.getHumidity());
                log.info("   ├─ 风速: {} km/h", weatherData.getWindSpeed());
                log.info("   ├─ 风向: {}", weatherData.getWindDirection());
                log.info("   ├─ 天气: {}", weatherData.getWeatherDesc());
                log.info("   ├─ 预报时间: {}", weatherData.getFxTime());
                log.info("   └─ 查询时间: {}", weatherData.getQueryTime().format(DATE_FORMATTER));
                log.info("⏱️ 总耗时: {} ms", duration);
            } else {
                log.warn("⚠️ 天气数据获取失败，已记录错误");
            }

            // 可选：查询最新5条记录用于验证
            if (log.isDebugEnabled()) {
                var recentRecords = weatherService.getLast24HourWeather();
                log.debug("最近24小时共获取 {} 条天气记录", recentRecords.size());
            }

        } catch (Exception e) {
            log.error("❌ 天气数据更新任务执行失败: {}", e.getMessage(), e);
        }

        log.info("========== 天气数据更新定时任务执行完成 ==========\n");
    }

    // ==================== 新增：天气数据清理任务（可选） ====================

    /**
     * 任务3：每天凌晨2点清理30天前的旧天气数据（可选）
     * 避免数据表无限增长
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanOldWeatherData() {
        log.info("========== 开始执行天气数据清理任务 ==========");

        try {
            // 计算30天前的日期
            LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);

            // 使用 MyBatis-Plus 的条件构造器删除
            int deletedCount = weatherService.deleteOldRecords(thirtyDaysAgo);

            log.info("✅ 清理完成，删除了 {} 条30天前的天气数据", deletedCount);

        } catch (Exception e) {
            log.error("清理天气数据失败: {}", e.getMessage(), e);
        }

        log.info("========== 天气数据清理任务执行完成 ==========\n");
    }
}