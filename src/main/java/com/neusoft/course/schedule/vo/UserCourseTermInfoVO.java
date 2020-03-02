package com.neusoft.course.schedule.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/3/2.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCourseTermInfoVO implements Serializable {

    private Integer time; // 课程合计课时

    private Integer num; // 课程合计数目

}
