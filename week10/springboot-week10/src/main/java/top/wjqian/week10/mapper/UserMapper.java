package top.wjqian.week10.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.wjqian.week10.entity.User;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT * FROM user WHERE enabled = 1 " +
            "AND DATE_FORMAT(birthday, '%m-%d') = DATE_FORMAT(#{today}, '%m-%d')")
    List<User> findTodayBirthdayUsers(LocalDate today);

    @Update("UPDATE user SET last_sent_year = #{year} WHERE id = #{userId}")
    void updateLastSentYear(Long userId, String year);
}
