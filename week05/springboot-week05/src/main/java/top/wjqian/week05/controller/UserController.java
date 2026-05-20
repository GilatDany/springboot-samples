package top.wjqian.week05.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.wjqian.week05.common.Result;
import top.wjqian.week05.entity.User;
import top.wjqian.week05.service.UserService;


import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/search")
    public List<User> search(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer minAge
    ){
        return userService.search(username, minAge);
    }
    @PostMapping
    public Result<String> add(@RequestBody User user){
        int row = userService.add(user);
        if(row!=1){
            return Result.error("添加失败");
        }
        return Result.success("添加成功");
    }

    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id){
        return Result.success(userService.getById(id));
    }

    @GetMapping("/list")
    public Result<List<User>> list(){
        return Result.success(userService.list());
    }
    @PutMapping
    public Result<String> update(@RequestBody User user){
        int row = userService.update(user);
        if(row!=1){
            return Result.error("更新失败");
        }
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id){
        int row = userService.delete(id);
        if(row!=1){
            return Result.error("删除失败");
        }
        return Result.success("删除成功");
    }
}