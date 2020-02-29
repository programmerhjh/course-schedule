package com.neusoft.course.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 分页实体
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/15.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageEntity implements Serializable{

    private Integer page;   // 页数

    private Integer limit;  // 每页条数

    private Object extendField;  // 额外附带参数

}
