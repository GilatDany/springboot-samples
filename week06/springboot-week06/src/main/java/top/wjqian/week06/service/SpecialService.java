package top.wjqian.week06.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.wjqian.week06.entity.Special;
import top.wjqian.week06.mapper.SpecialMapper;

import java.util.List;

/**
 * Special Service 业务逻辑层
 */

public interface SpecialService {
    Page<Special> selectByTitle(String title, Integer pageNum, Integer pageSize);

    IPage<Special> page(String name, Integer pageNum, Integer pageSize);

    int add(Special special);

    int delete(String id);

    int update(Special special);

    Special getById(String id);

//    java.util.List<Special> listAll();



}