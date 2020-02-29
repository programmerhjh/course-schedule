package com.neusoft.course.schedule.service;

import com.neusoft.course.schedule.dto.UploadExcelImportCourseDTO;
import com.neusoft.course.schedule.entity.Course;
import com.neusoft.course.schedule.entity.User;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/19.
 */
public interface ExcelService {

    /**
     * 创建导出批量用户数据Excel
     * @param users
     * @return
     */
    SXSSFWorkbook createAndDownloadUsersExcel(List<User> users);

    /**
     * 创建导出课程任务表
     * @param courses
     * @return
     */
    SXSSFWorkbook createAndDownloadCoursesExcel(List<Course> courses);

    /**
     * 获取课程模板文件
     * @param req
     * @param resp
     */
    void getImportCourseExcelTemplate(HttpServletRequest req, HttpServletResponse resp);

    /**
     * 分析课程Excel并构造出Course对象集合
     * @param uploadExcelImportCourseDTO
     * @return
     */
    List<Course> analyzeCourseExcel(UploadExcelImportCourseDTO uploadExcelImportCourseDTO);
}
