package top.wjqian.week10.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 天气数据实体类
 */
@Data
@TableName("weather_data")
public class WeatherData {
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 城市代码 */
    private String location;

    /** 城市名称 */
    private String locationName;

    /** 温度(°C) */
    private BigDecimal temperature;

    /** 湿度(%) */
    private Integer humidity;

    /** 风速(km/h) */
    private BigDecimal windSpeed;

    /** 风向 */
    private String windDirection;

    /** 天气描述 */
    private String weatherDesc;

    /** 预报时间 */
    private LocalDateTime fxTime;

    /** 查询时间 */
    private LocalDateTime queryTime;

    /** 原始返回数据 */
    private String rawData;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
