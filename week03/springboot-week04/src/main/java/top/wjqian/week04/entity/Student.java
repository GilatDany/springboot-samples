package top.wjqian.week04.entity;


public class Student {
    // 私有属性
    private String name;
    private int age;

    // 无参构造（Spring 必须）
    public Student() {}

    // 有参构造
    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // getter & setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "学生姓名：" + name + "，年龄：" + age;
    }
}
