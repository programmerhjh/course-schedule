package com.neusoft.course.schedule.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/3/1.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserApplyListVO implements Serializable {

    private Integer id;

    private Integer termId;

    private Integer courseId;

    private String facultyName;

    private String termName;

    private String courseName;

    private String name;

    private String reason;

    private Integer state;

    private Date createTime;

    private Date modifyTime;

}
