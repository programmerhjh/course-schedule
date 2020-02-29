package com.neusoft.course.schedule.enums;

import lombok.Getter;

/**
 * 导出课程任务的 EXCEL 中可展示字段
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/27.
 */
@Getter
public enum CourseExcelFieldName {

    TC_ID("tc_id", "序号"),
    TC_FACULTY_ID("tc_faculty_id", "院系"),
    TC_NUM("tc_num", "课程编号"),
    TC_NAME("tc_name", "课程名称"),
    TC_SCORE("tc_score", "学分"),
    TC_TIME("tc_time", "课程课时"),
    TC_CLASS_NAME("tc_class_name", "教学班名称"),
    TC_CLASS_COUNT("tc_class_count", "上课人数"),
    TC_ONLINE_NUM("tc_online_num", "开课编号"),
    TC_START_WEEK("tc_start_week", "起始周"),
    TC_END_WEEK("tc_end_week", "结束周"),
    TC_THEORY_CLASS_TYPE("tc_theory_class_type", "理论课教室类型要求"),
    TC_THEORY_TEACHER("tc_theory_teacher", "理论课任课老师"),
    TC_THEORY_PLAN_DESC("tc_theory_plan_desc", "理论课排课要求"),
    TC_EXPERIMENT_PLAN_ONLINE("tc_experiment_plan_online", "实验课上课周次及节次"),
    TC_EXPERIMENT_CLASS("tc_experiment_class", "实验室名称"),
    TC_EXPERIMENT_CLASS_TYPE("tc_experiment_class_type", "实验课班型"),
    TC_EXPERIMENT_CLASS_TEACHER("tc_experiment_class_teacher", "实验课任课老师"),
    TC_EXPERIMENT_CLASS_PLAN_DESC("tc_experiment_class_plan_desc", "实验课排课要求"),
    TC_DIRECTOR("tc_director", "课程主任"),
    TC_RESEARCH_OFFICE("tc_research_office", "所属教研室"),
    TC_EVALUATION_TEACHER("tc_evaluation_teacher", "评建老师")

    ;

    private String fieldName;
    private String detail;

    CourseExcelFieldName(String fieldName, String detail) {
        this.fieldName = fieldName;
        this.detail = detail;
    }

    /**
     * 根据code获取message
     * @param fieldName
     * @return
     */
    public static String getDetailByFieldName(String fieldName) {
        for (CourseExcelFieldName ele : values()) {
            if (ele.getFieldName().equals(fieldName)) {
                return ele.getDetail();
            }
        }
        return null;
    }
}
