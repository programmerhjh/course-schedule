package com.neusoft.course.schedule.mapper;

import com.neusoft.course.schedule.dto.ExportExcelByFacultyAndTermDTO;
import com.neusoft.course.schedule.entity.Course;
import com.neusoft.course.schedule.entity.Faculty;
import com.neusoft.course.schedule.vo.CourseAddPageSelectUserVO;
import com.neusoft.course.schedule.vo.CourseHistoryTeacherVO;
import com.neusoft.course.schedule.vo.UserCourseTermInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/3.
 */
public interface CourseMapper {

    /**
     * 分页获取课程信息
     * @param fcId 课程ID
     * @param tId 学年ID
     * @return
     */
    List<Course> selectCourseByFacultyIdAndTermId(@Param("fcId") Integer fcId, @Param("tId") Integer tId);

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
    Integer batchDeleteCourses(@Param("idList") List<Integer> idList);

    /**
     * 模糊搜索课程
     * @param fcId 院系ID
     * @param tId 学年ID
     * @param key 关键字
     * @return
     */
    List<Course> searchCourse(@Param("fcId") Integer fcId, @Param("tId") Integer tId, @Param("key") String key);

    /**
     * 查出该院系学年下的老师已排课时
     * @param fcId
     * @param tId
     * @return
     */
    List<CourseAddPageSelectUserVO> selectUserCourseTimeByFacultyIdAndTermId(@Param("fcId") Integer fcId, @Param("tId") Integer tId);

    /**
     * 通过查找课程ID信息，根据课程编号搜寻之前曾经上过该课的用户
     * @param cId
     * @return
     */
    List<CourseHistoryTeacherVO> courseHistoryTeacherList(@Param("cId") Integer cId);

    /**
     * 根据ID查找课程
     * @param id
     * @return
     */
    Course selectCourseById(@Param("id") Integer id);

    /**
     * 保存课程
     * @param course
     * @return
     */
    Integer saveCourse(Course course);

    /**
     * 查询该院系该学期下未分配老师的课程
     * @param fcId
     * @param tId
     * @return
     */
    List<Course> selectUndistributedCourseByFacultyIdAndTermId(@Param("fcId") Integer fcId, @Param("tId") Integer tId);

    /**
     * 获取指定院系学年的课程
     * @param exportExcelByFacultyAndTermDTO
     * @return
     */
    List<Course> selectCoursesByFacultyAndTerm(ExportExcelByFacultyAndTermDTO exportExcelByFacultyAndTermDTO);

    /**
     * 获取课程表字段名列表
     * @return
     */
    List<String> getExcelConfig();

    /**
     * 根据主键ID修改任课老师名及ID
     * @param course
     * @return
     */
    Integer updateCourseTheoryTeacher(Course course);

    /**
     * 查询指定院系学期课程为添加理论课老师ID或名字条数
     * @param exportExcelByFacultyAndTermDTO
     * @return
     */
    Integer checkCourseIsComplete(ExportExcelByFacultyAndTermDTO exportExcelByFacultyAndTermDTO);

    /**
     * 批量插入课程
     * @param courses
     * @return
     */
    Integer batchInsertCourses(@Param("courses") List<Course> courses);

    /**
     * 查询用户课程列表
     * @param userId
     * @return
     */
    List<Course> selectCoursesByUserId(@Param("userId") Integer userId);

    /**
     * 查询指定学年用户课程列表
     * @param userId
     * @param termId
     * @return
     */
    List<Course> selectCoursesByUserIdAndTermId(@Param("userId") Integer userId, @Param("termId") Integer termId);

    /**
     * 模糊搜索用户历史课程
     * @param userId
     * @param key 课程名
     * @return
     */
    List<Course> selectCoursesByUserIdAndKey(@Param("userId") Integer userId, @Param("key") String key);

    /**
     * 计算用户当前学期的课程信息 (当前学期课时情况)
     * @param userId
     * @param fcId
     * @param termId
     * @return
     */
    UserCourseTermInfoVO getUserCourseTermInfo(@Param("userId") Integer userId, @Param("fcId") Integer fcId, @Param("termId") Integer termId);

}
