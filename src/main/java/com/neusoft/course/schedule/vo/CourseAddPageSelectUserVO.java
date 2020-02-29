package com.neusoft.course.schedule.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 添加课程所需要的用户下拉框数据
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/26.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseAddPageSelectUserVO implements Serializable {

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户姓名加上已排课时
     */
    private String usernameWithHasTime;

}
