package top.wjqian.week02.controller;


import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.wjqian.week02.entity.Student;
import top.wjqian.week02.service.StudentService;

@RestController
@RequestMapping("/api/v1/students")
@Slf4j
public class StudentController {
    @Resource()
    private StudentService studentService;
    /**
     * 创建学生
     * @param student 学生对象
     * @return 创建成功
     */
    @PostMapping ()
    public String createStudent(@RequestBody Student student) {
        log.info("接收到学生对象参数:{}",student);
        studentService.createStudent(student);
        return "创建学生成功";
    }
/**
 * 根据id查询学生
 * @param id 学生id
 * @return 学生对象
 */
    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        log.info("接收到学生id参数:{}",id);
        return studentService.getStudentById(id);
    }
    /**
     * 根据姓名查询学生
     * @param name 学生姓名
     * @return 学生对象
     */
    @GetMapping()
    public Student getStudentByName(@RequestParam String name){
        log.info("接收到学生姓名参数:{}",name);
        return studentService.getStudentByName(name);
    }
    /**
     * 查询所有学生
     * @return 所有学生列表
     */
    @GetMapping("/all")
    public Iterable<Student> getAllStudents(){
        return studentService.getAllStudents();
    }
    /**
     * 根据id更新学生
     * @param id 学生id
     * @param student 学生对象
     * @return 更新成功
     */
    @PutMapping("/{id}")
    public String updateStudent(@PathVariable Long id,@RequestBody Student student){
        log.info("接收到学生id参数:{}",id);
        log.info("接收到学生对象参数:{}",student);
        studentService.updateStudentById(id,student);
        return "更新学生成功";
    }

    /**
     * 根据id删除学生
     * @param id 学生id
     * @return 删除成功
     */
    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Long id){
        log.info("接收到学生id参数：{}",id);
        studentService.deleteStudentById(id);
        return "删除学生成功";
    }
}
