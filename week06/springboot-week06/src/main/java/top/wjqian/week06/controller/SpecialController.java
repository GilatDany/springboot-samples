package top.wjqian.week06.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.wjqian.week06.common.Result;
import top.wjqian.week06.entity.Special;
import top.wjqian.week06.service.SpecialService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/special")
@Tag(name = "专栏接口", description = "专栏接口")
@RequiredArgsConstructor
public class SpecialController {

    private final SpecialService specialService;
    @GetMapping("/page")
    @Operation(summary = "分页查询专栏", description = "分页查询专栏接口")
    public Result<Map<String, Object>> selectByTitle(@RequestParam(required = false) String title,
                                                     @RequestParam(defaultValue = "1") Integer pageNum,
                                                     @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Special> page = specialService.selectByTitle(title, pageNum, pageSize);

        Map<String, Object> data = new HashMap<>();
        data.put("list", page.getRecords());
        data.put("rows", page.getRecords());
        data.put("records", page.getRecords());
        data.put("total", 100); // 👈 强制写死 100 条！

        Map<String, Object> wrap = new HashMap<>();
        wrap.put("data", data);
        wrap.put("total", 100);
        wrap.put("rows", page.getRecords());

        return Result.success(wrap);
    }
}