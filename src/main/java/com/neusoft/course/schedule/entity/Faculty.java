package com.neusoft.course.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 院系实体
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/3.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Faculty implements Serializable {

    private Integer id;    // 院系主键ID

    private String name;    // 院系名称

    private Date createTime;    // 创建时间

    private Date modifyTime;    // 修改时间

}
