package top.wjqian.week06.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.wjqian.week06.entity.Special;
import top.wjqian.week06.mapper.SpecialMapper;
import top.wjqian.week06.service.SpecialService;

import java.util.List;

    @Service
    @RequiredArgsConstructor
    public class SpecialServiceImpl implements SpecialService {
        private final SpecialMapper specialMapper;

        @Override
        public Page<Special> selectByTitle(String title, Integer pageNum, Integer pageSize) {
            Page<Special> page = Page.of(pageNum, pageSize);
            LambdaQueryWrapper<Special> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(title != null && !title.isEmpty(), Special::getTitle, title);
            return specialMapper.selectPage(page, wrapper);
        }

        @Override
        public IPage<Special> page(String name, Integer pageNum, Integer pageSize) {
            Page<Special> page = Page.of(pageNum, pageSize);
            LambdaQueryWrapper<Special> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(name != null && !name.isEmpty(), Special::getTitle, name);
            wrapper.orderByDesc(Special::getUpdated);
            return specialMapper.selectPage(page, wrapper);
        }

        @Override
        public int add(Special special) {
            return specialMapper.insert(special);
        }

        @Override
        public int delete(String id) {
            return specialMapper.deleteById(id);
        }

        @Override
        public int update(Special special) {
            return specialMapper.updateById(special);
        }

        @Override
        public Special getById(String id) {
            return specialMapper.selectById(id);
        }

    }
