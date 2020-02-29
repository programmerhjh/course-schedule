package com.neusoft.course.schedule.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 编辑课程的申请列表
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/26.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseEditPageApplyListVO implements Serializable {

    private String name;

    private String reason;

    private Date createTime;

    private Date modifyTime;

}
