package com.neusoft.course.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 申请实体
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/3.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Apply implements Serializable{

    private Integer id;    // 申请表主键ID

    private Integer userId;    // 申请人ID

    private Integer taskId;    // 申请课程ID

    private Integer facultyId;    // 申请院系ID

    private Integer termId;    // 申请学期ID

    private String desc;    // 申请理由或额外添加备注

    private Integer state;    // 申请状态（0为待审批，1为审批通过，2为审批不通过）

    private Date createTime;    // 创建时间

    private Date modifyTime;    // 修改时间

}
