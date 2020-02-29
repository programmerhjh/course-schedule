package com.neusoft.course.schedule.service;

import com.neusoft.course.schedule.dto.ExportExcelByFacultyAndTermDTO;
import com.neusoft.course.schedule.dto.SearchCourseDTO;
import com.neusoft.course.schedule.dto.UploadExcelImportCourseDTO;
import com.neusoft.course.schedule.entity.Course;
import com.neusoft.course.schedule.entity.PageEntity;
import com.neusoft.course.schedule.entity.PageResult;
import com.neusoft.course.schedule.vo.CourseAddPageSelectUserVO;
import com.neusoft.course.schedule.vo.CourseEditPageApplyListVO;
import com.neusoft.course.schedule.vo.CourseHistoryTeacherVO;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.List;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/24.
 */
public interface CourseService {

    /**
     * 分页获取课程信息
     * @param pageEntity 为null则返回全部数据
     * @param fcId
     * @param tId
     * @return
     */
    PageResult<Course> getCourseData(Integer fcId, Integer tId, PageEntity pageEntity);

    /**
     * 删除指定课程
     * @param course
     * @return
     */
    Integer deleteCourse(Course course);

    /**
     * 批量删除指定课程
     * @param idList
     * @return
     */
    Integer batchDeleteCourses(List<Integer> idList);

    /**
     * 搜索指定课程
     * @param searchCourseDTO
     * @return
     */
    PageResult<Course> searchCourse(SearchCourseDTO searchCourseDTO);

    /**
     * 获取添加课程页用户下拉框数据
     * @param fcId
     * @param tId
     * @return
     */
    List<CourseAddPageSelectUserVO> courseAddPageData(Integer fcId, Integer tId);

    /**
     * 通过查找课程ID信息，根据课程编号搜寻之前曾经上过该课的用户
     * @param cId
     * @return
     */
    List<CourseHistoryTeacherVO> courseHistoryTeacherList(Integer cId);

    /**
     * 保存课程
     * @param course
     * @return
     */
    Integer saveCourse(Course course);

    /**
     * 获取一个指定院系学年的工作簿
     * @param exportExcelByFacultyAndTermDTO
     * @return
     */
    SXSSFWorkbook generatorExcelByFacultyAndTerm(ExportExcelByFacultyAndTermDTO exportExcelByFacultyAndTermDTO);

    /**
     * 查询该院系学期下的课程是否均已有 理论课老师ID或者名称
     * @param exportExcelByFacultyAndTermDTO
     * @return
     */
    Boolean checkCourseIsComplete(ExportExcelByFacultyAndTermDTO exportExcelByFacultyAndTermDTO);

    /**
     * 解析当前课程表格并导入到课程表
     * @param uploadExcelImportCourseDTO
     */
    void analyzeExcelImportCourse(UploadExcelImportCourseDTO uploadExcelImportCourseDTO);
}
