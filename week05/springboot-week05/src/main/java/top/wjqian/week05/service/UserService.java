package top.wjqian.week05.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.wjqian.week05.entity.User;
import top.wjqian.week05.mapper.UserMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserMapper userMapper;
    public int add(User user){
        int result = userMapper.insert(user);
        user.setCreateTime(LocalDateTime.now());
        log.info("添加⽤户:{}", user);
        return result;
    }

    public User getById(Long id){
        return userMapper.selectByPrimaryKey(id);
    }

    public List<User> list(){

        return userMapper.selectList();
    }

    public List<User> search(String username, Integer minAge) {
        return userMapper.selectByCondition(username, minAge);
    }

    public int update(User user){
        return userMapper.updateByPrimaryKey(user);
    }

    public int delete(Long id){
        return userMapper.deleteByPrimaryKey(id);
    }
}
