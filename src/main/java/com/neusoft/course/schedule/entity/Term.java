package com.neusoft.course.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 学年实体
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/3.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Term extends Faculty implements Serializable {

    private Integer id;    // 学年表主键ID

    private String name;    // 学年名称，例：（2016-2017年度），前台在建立学年是会将询问其是否分为几个学期

    private Integer fcId;    // 关联哪个院系

    private Integer state;    // 学年开放申请（0为不能申请，1为可以申请）

    private Date createTime;    // 创建时间

    private Date modifyTime;    // 修改时间

}
