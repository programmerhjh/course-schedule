package com.neusoft.course.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分页响应
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/15.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> implements Serializable{

    private Integer pageNum;    // 当前页码

    private Integer pageSize;   // 每页数量

    private Long totalSize; // 记录总数

    private Integer totalPages; // 页码总数

    private List<T> data;   // 数据

    private Object extendField;   // 补充数据

}
