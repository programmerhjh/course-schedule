package com.neusoft.course.schedule.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/28.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyListVO implements Serializable {

    private Integer id;

    private Integer userId;

    private String name; // 用户名

    private Integer courseId;

    private Integer facultyId;    // 申请院系ID

    private Integer termId;    // 申请学期ID

    private String termName;    // 申请学期名称

    private String reason; // 申请理由

    private String state; // 申请状态

    private Date createTime;

    private Date modifyTime;

}
