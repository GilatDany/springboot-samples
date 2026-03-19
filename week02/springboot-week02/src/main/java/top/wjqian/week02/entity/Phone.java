package top.wjqian.week02.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wencyqian
 */
@Data
@AllArgsConstructor
@NoArgsConstructor//无参构造
@Builder//参数任意
public class Phone {
    private String band;
    private Double price;
    private String color;
}
