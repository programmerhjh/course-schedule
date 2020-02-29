package com.neusoft.course.schedule.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/26.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseHistoryTeacherVO implements Serializable {

    private String termName;

    private String teacherName;

}
