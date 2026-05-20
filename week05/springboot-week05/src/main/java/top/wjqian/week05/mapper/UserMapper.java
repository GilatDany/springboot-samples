package top.wjqian.week05.mapper;

import org.apache.ibatis.annotations.*;
import top.wjqian.week05.entity.User;

import java.util.List;

@Mapper
public interface UserMapper {

//    @Select("<script>" +
//            "SELECT * FROM t_user " +
//            "<where>" +
//            "<if test='username != null and username != \"\">'>" +
//            "AND username LIKE CONCAT('%', #{username}, '%')" +
//            "</if>" +
//            "<if test='minAge != null'>\">" +
//            "AND age >= #{minAge}" +
//            "</if>" +
//            "</where>" +
//            "</script>")
//    List<User> selectByCondition(String username,Integer minAge);

//    List<User> selectByCondition(String username, Integer minAge);
    @Select("<script>" +
            "SELECT * FROM t_user " +
            "<where>" +
            "<if test='username != null and username != \"\"'>" +
            "AND username LIKE CONCAT('%', #{username}, '%')" +
            "</if>" +
            "<if test='minAge != null'>" +
            "AND age >= #{minAge}" +
            "</if>" +
            "</where>" +
            "</script>")
    List<User> selectByCondition(String username, Integer minAge);



    @Insert("INSERT INTO t_user(username,password,age,email) VALUES (#{username},#{password},#{age},#{email})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User record);

    @Select("SELECT * FROM t_user WHERE id=#{id}")
    User selectByPrimaryKey(Long id);

    @Select("SELECT * FROM t_user")
    List<User> selectList();

    @Update("UPDATE t_user SET username=#{username}, age=#{age}, email=#{email} WHERE id=#{id}")
    int updateByPrimaryKey(User record);

    @Delete("DELETE FROM t_user WHERE id=#{id}")
    int deleteByPrimaryKey(Long id);
}
