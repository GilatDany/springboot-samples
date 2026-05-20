package top.wjqian.week10.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {
    @JsonProperty("code")
    private String code;

    @JsonProperty("updateTime")
    private String updateTime;

    @JsonProperty("fxLink")
    private String fxLink;

    @JsonProperty("hourly")
    private List<HourlyWeather> hourly;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HourlyWeather {
        @JsonProperty("fxTime")
        private String fxTime;

        @JsonProperty("temp")
        private String temp;

        @JsonProperty("humidity")
        private String humidity;

        @JsonProperty("windSpeed")
        private String windSpeed;

        @JsonProperty("windDir")
        private String windDir;

        @JsonProperty("text")
        private String text;
    }
}
