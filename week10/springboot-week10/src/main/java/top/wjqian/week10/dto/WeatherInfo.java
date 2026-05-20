package top.wjqian.week10.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class WeatherInfo {
    private boolean success;

    /** 错误信息（失败时） */
    private String errorMsg;

    /** 温度 (°C) */
    private String temperature;

    /** 湿度 (%) */
    private String humidity;

    /** 风速 (km/h) */
    private String windSpeed;

    /** 天气描述 */
    private String weatherDesc;

    /** 预报时间 */
    private String fxTime;

    /** 查询时间 */
    private LocalDateTime queryTime;

    @Override
    public String toString() {
        if (!success) {
            return "天气获取失败: " + errorMsg;
        }
        return String.format("当前天气 - 温度: %s°C, 湿度: %s%%, 风速: %skm/h, 天气: %s, 预报时间: %s",
                temperature, humidity, windSpeed, weatherDesc, fxTime);
    }
}
