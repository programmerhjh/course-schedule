package com.neusoft.course.schedule.vo;

import com.neusoft.course.schedule.entity.Course;
import com.neusoft.course.schedule.entity.PageResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/3/2.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseApplyListVO implements Serializable {

    private Integer id;    // 课程表主键ID

    private Integer semesterId;    // 开课学年主键ID

    private Integer facultyId;    // 开课院系主键ID

    private String num;    // 课程编号

    private String name;    // 课程名称

    private Integer score;    // 学分

    private Integer time;    // 课程课时

    private String className;    // 教学班名称

    private Integer classCount;    // 上课人数

    private String onlineNum;    // 开课编号

    private Integer startWeek;    // 起始周

    private Integer endWeek;    // 结束周

    private String theoryClassType;    // 理论课教室类型要求

    private Integer theoryTeacherId;    // 理论课任课老师ID

    private String theoryTeacher;    // 理论课任课老师

    private String theoryPlanDesc;    // 理论课排课要求

    private String experimentPlanOnline;    // 实验课上课周次及节次

    private String experimentClass;    // 实验室名称

    private String experimentClassType;    // 实验课班型

    private Integer experimentClassTeacherId;    // 实验课任课老师ID

    private String experimentClassTeacher;    // 实验课任课老师

    private String experimentClassPlanDesc;    // 实验课排课要求

    private String director;    // 课程主任

    private String researchOffice;    // 所属教研室

    private String evaluationTeacher;    // 评建老师

    private Date createTime;    // 创建时间

    private Date modifyTime;    // 修改时间

    private List<Integer> userApplyList;    // 用户已经申请的申请主键

}
