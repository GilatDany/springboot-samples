package top.wjqian.week05.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.wjqian.week05.entity.User;
import top.wjqian.week05.mapper.UserMPMapper;

@Service
@RequiredArgsConstructor
public class UserMPService {
    private final UserMPMapper userMPMapper;

    public IPage<User> page(String username, Integer pageNum, Integer pageSize) {
        Page<User> page = Page.of(pageNum, pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(username != null && !username.isEmpty(), User::getUsername, username);
        return userMPMapper.selectPage(page, wrapper);
    }

    public int add(User user) {
        return userMPMapper.insert(user);
    }
    public int delete(Long id) {
        return userMPMapper.deleteById(id);
    }
}
