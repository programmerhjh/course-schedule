package com.neusoft.course.schedule.mapper;

import com.neusoft.course.schedule.entity.Apply;
import com.neusoft.course.schedule.vo.ApplyListVO;
import com.neusoft.course.schedule.vo.CourseEditPageApplyListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/3.
 */
public interface ApplyMapper {

    /**
     * 根据课程ID获取申请列表
     * @param courseId
     * @return
     */
    List<CourseEditPageApplyListVO> selectApplyByCourseId(@Param("cId") Integer courseId);

    /**
     * 批量删除申请
     * @param idList
     * @return
     */
    Integer batchDeleteApplies(@Param("idList") List<Integer> idList);

    /**
     * 删除一个申请
     * @param apply
     * @return
     */
    Integer deleteApply(Apply apply);

    /**
     * 获取所有申请数据
     * @return
     */
    List<ApplyListVO> selectApplyData();

    /**
     * 查找该申请课程的任课老师名称
     * @param apply
     * @return
     */
    String selectUserNameByApplyCourseId(Apply apply);

    /**
     * 根据主键修改申请状态
     * @param apply
     * @return
     */
    Integer updateApplyStateById(Apply apply);

    /**
     * 根据院系查询申请
     * @param fcId
     * @return
     */
    List<ApplyListVO> selectApplyByFacultyId(@Param("fcId") Integer fcId);

    /**
     * 根据院系学年查询申请
     * @param fcId
     * @param tId
     * @return
     */
    List<ApplyListVO> selectApplyByFacultyIdAndTermId(@Param("fcId") Integer fcId, @Param("tId") Integer tId);

    /**
     * 模糊查询院系学年申请
     * @param fcId
     * @param termId
     * @param key
     * @return
     */
    List<ApplyListVO> searchApplyByFacultyIdAndTermId(@Param("fcId") Integer fcId, @Param("tId") Integer termId, @Param("key") String key);

    /**
     * 模糊查询院系申请
     * @param fcId
     * @param key
     * @return
     */
    List<ApplyListVO> searchApplyByFacultyId(@Param("fcId") Integer fcId, @Param("key") String key);
}
