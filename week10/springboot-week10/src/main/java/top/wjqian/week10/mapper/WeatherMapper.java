package top.wjqian.week10.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.wjqian.week10.entity.WeatherData;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface WeatherMapper extends BaseMapper<WeatherData> {
    /**
     * 查询最新的天气数据
     */
    @Select("SELECT * FROM weather_data WHERE location = #{location} ORDER BY query_time DESC LIMIT 1")
    WeatherData findLatestByLocation(String location);

    /**
     * 查询指定时间范围内的天气数据
     */
    @Select("SELECT * FROM weather_data WHERE location = #{location} " +
            "AND query_time BETWEEN #{startTime} AND #{endTime} ORDER BY query_time DESC")
    List<WeatherData> findByTimeRange(String location, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查询最近N条天气记录
     */
    @Select("SELECT * FROM weather_data WHERE location = #{location} ORDER BY query_time DESC LIMIT #{limit}")
    List<WeatherData> findRecentRecords(String location, int limit);
}
