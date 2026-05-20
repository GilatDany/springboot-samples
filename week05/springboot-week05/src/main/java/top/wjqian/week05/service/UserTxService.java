package top.wjqian.week05.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.wjqian.week05.entity.User;
import top.wjqian.week05.mapper.UserMapper;

@Service
@RequiredArgsConstructor
public class UserTxService {
    private final UserMapper userMapper;

    @Transactional
    public void addTwoUsers(User user1, User user2) {
        userMapper.insert(user1);
        if (user2.getUsername() == null || user2.getUsername().isEmpty()){
            throw new RuntimeException("用户名不能为空，事物回滚");
        }
        userMapper.insert(user2);
    }


}
