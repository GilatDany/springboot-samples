package top.wjqian.week10.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.wjqian.week10.dto.WeatherInfo;
import top.wjqian.week10.dto.WeatherResponse;
import top.wjqian.week10.entity.WeatherData;
import top.wjqian.week10.mapper.WeatherMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 天气服务 - 获取实时天气并存储到数据库
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherService {

    private final WeatherMapper weatherMapper;

    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.location}")
    private String location;

    @Value("${weather.api.location-name:未知城市}")
    private String locationName;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter API_DATE_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    /**
     * 获取天气数据并存储到数据库
     * @return 存储后的天气数据实体
     */
    @Transactional(rollbackFor = Exception.class)
    public WeatherData fetchAndSaveWeather() {
        try {
            String url = buildApiUrl();
            log.info("调用天气API: {}", url);

            String responseBody = callWeatherApi(url);

            // 解析并存储天气数据
            WeatherData weatherData = parseAndBuildWeatherData(responseBody);
            weatherMapper.insert(weatherData);

            log.info("天气数据存储成功 - ID: {}, 温度: {}°C, 湿度: {}%, 查询时间: {}",
                    weatherData.getId(),
                    weatherData.getTemperature(),
                    weatherData.getHumidity(),
                    weatherData.getQueryTime().format(FORMATTER));

            return weatherData;

        } catch (Exception e) {
            log.error("获取天气数据失败: {}", e.getMessage(), e);

            // 存储失败记录到数据库
            WeatherData errorData = createErrorRecord(e.getMessage());
            weatherMapper.insert(errorData);

            return errorData;
        }
    }

    /**
     * 仅获取天气数据（不存储），用于实时查询
     */
    public WeatherInfo getWeatherInfo() {
        try {
            String url = buildApiUrl();
            String responseBody = callWeatherApi(url);
            return parseWeatherObject(responseBody);
        } catch (Exception e) {
            log.error("获取天气信息失败", e);
            return WeatherInfo.builder()
                    .success(false)
                    .errorMsg(e.getMessage())
                    .queryTime(LocalDateTime.now())
                    .build();
        }
    }

    /**
     * 查询最新的天气数据（从数据库）
     */
    public WeatherData getLatestWeather() {
        return weatherMapper.findLatestByLocation(location);
    }

    /**
     * 查询最近24小时的天气数据
     */
    public List<WeatherData> getLast24HourWeather() {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusHours(24);
        return weatherMapper.findByTimeRange(location, startTime, endTime);
    }

    /**
     * 构建 API URL
     */
    private String buildApiUrl() {
        return String.format("%s?location=%s&key=%s", apiUrl, location, apiKey);
    }

    /**
     * 调用天气 API
     */
    private String callWeatherApi(String url) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Accept", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                int statusCode = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity());

                if (statusCode != 200) {
                    throw new RuntimeException(String.format("API返回错误码: %d, 响应: %s",
                            statusCode, responseBody));
                }
                return responseBody;
            }
        }
    }

    /**
     * 解析并构建 WeatherData 实体
     */
    private WeatherData parseAndBuildWeatherData(String rawData) throws Exception {
        WeatherResponse response = objectMapper.readValue(rawData, WeatherResponse.class);

        WeatherData weatherData = new WeatherData();
        weatherData.setLocation(location);
        weatherData.setLocationName(locationName);
        weatherData.setRawData(rawData);
        weatherData.setQueryTime(LocalDateTime.now());

        if (response.getHourly() != null && !response.getHourly().isEmpty()) {
            WeatherResponse.HourlyWeather current = response.getHourly().get(0);

            // 设置温度
            if (current.getTemp() != null) {
                weatherData.setTemperature(new BigDecimal(current.getTemp()));
            }

            // 设置湿度
            if (current.getHumidity() != null) {
                weatherData.setHumidity(Integer.parseInt(current.getHumidity()));
            }

            // 设置风速
            if (current.getWindSpeed() != null) {
                weatherData.setWindSpeed(new BigDecimal(current.getWindSpeed()));
            }

            // 设置风向
            weatherData.setWindDirection(current.getWindDir());

            // 设置天气描述
            weatherData.setWeatherDesc(current.getText());

            // 设置预报时间
            try {
                weatherData.setFxTime(LocalDateTime.parse(current.getFxTime(), API_DATE_FORMATTER));
            } catch (Exception e) {
                log.warn("解析预报时间失败: {}", current.getFxTime());
            }
        }

        return weatherData;
    }

    /**
     * 创建错误记录
     */
    private WeatherData createErrorRecord(String errorMsg) {
        WeatherData weatherData = new WeatherData();
        weatherData.setLocation(location);
        weatherData.setLocationName(locationName);
        weatherData.setQueryTime(LocalDateTime.now());
        weatherData.setRawData("ERROR: " + errorMsg);
        return weatherData;
    }

    /**
     * 解析为结构化对象（用于日志输出）
     */
    private WeatherInfo parseWeatherObject(String json) throws Exception {
        WeatherResponse response = objectMapper.readValue(json, WeatherResponse.class);

        WeatherInfo.WeatherInfoBuilder builder = WeatherInfo.builder()
                .success(true)
                .queryTime(LocalDateTime.now());

        if (response.getHourly() != null && !response.getHourly().isEmpty()) {
            WeatherResponse.HourlyWeather current = response.getHourly().get(0);
            builder.temperature(current.getTemp())
                    .humidity(current.getHumidity())
                    .windSpeed(current.getWindSpeed())
                    .weatherDesc(current.getText())
                    .fxTime(current.getFxTime());
        } else {
            builder.success(false).errorMsg("未获取到天气数据");
        }

        return builder.build();
    }

    /**
     * 删除指定时间之前的旧数据
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteOldRecords(LocalDateTime beforeTime) {
        // 使用 MyBatis-Plus 的 LambdaQueryWrapper
        LambdaQueryWrapper<WeatherData> wrapper = new LambdaQueryWrapper<>();
        wrapper.lt(WeatherData::getQueryTime, beforeTime);

        int deletedCount = weatherMapper.delete(wrapper);
        log.info("删除了 {} 条 {} 之前的天气数据", deletedCount, beforeTime.format(FORMATTER));

        return deletedCount;
    }
}
