package com.neusoft.course.schedule.service;

import com.neusoft.course.schedule.dto.SearchApplyDTO;
import com.neusoft.course.schedule.dto.SearchUserApplyDTO;
import com.neusoft.course.schedule.vo.UserApplyListVO;
import com.neusoft.course.schedule.entity.Apply;
import com.neusoft.course.schedule.entity.PageEntity;
import com.neusoft.course.schedule.entity.PageResult;
import com.neusoft.course.schedule.vo.ApplyListVO;
import com.neusoft.course.schedule.vo.CourseEditPageApplyListVO;

import java.util.List;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/26.
 */
public interface ApplyService {

    /**
     * 根据课程ID获取编辑课程的申请列表
     * @param cId
     * @param pageEntity
     * @return
     */
    PageResult<CourseEditPageApplyListVO> courseApplyList(Integer cId, PageEntity pageEntity);

    /**
     * 批量删除申请
     * @param idList
     * @return
     */
    Integer batchDeleteApplies(List<Integer> idList);

    /**
     * 模糊搜索申请人的申请
     * @param searchApplyDTO
     * @return
     */
    PageResult<ApplyListVO> searchApply(SearchApplyDTO searchApplyDTO);

    /**
     * 删除一个申请
     * @param apply
     * @return
     */
    Integer deleteApply(Apply apply);

    /**
     * 获取申请列表
     * @param fcId
     * @param tId
     * @param pageEntity
     * @return
     */
    PageResult<ApplyListVO> getApplyData(Integer fcId, Integer tId, PageEntity pageEntity);

    /**
     * 查找该申请的课程是否已存在人选
     * @param apply
     * @return
     */
    String checkApplyState(Apply apply);

    /**
     * 保存申请状态
     * @param apply
     * @return
     */
    Integer saveApplyState(Apply apply);

    /**
     * 分页查出用户申请列表
     * @param userId
     * @return
     */
    PageResult<UserApplyListVO> getUserApplyList(Integer userId, PageEntity pageEntity);

    /**
     * 分页查出用户指定学年的申请列表
     * @param userId
     * @param termId
     * @return
     */
    PageResult<UserApplyListVO> getUserApplyList(Integer userId, Integer termId, PageEntity pageEntity);

    /**
     * 模糊搜索用户的申请列表
     * @param searchUserApplyDTO
     * @return
     */
    PageResult<UserApplyListVO> searchUserApply(SearchUserApplyDTO searchUserApplyDTO);

    /**
     * 查询用户申请过的课程主键列表
     * @param userId
     * @return
     */
    List<Integer> getApplyIds(Integer userId);

    /**
     * 删除指定用户申请课程的申请记录
     * @param courseId
     * @param userId
     * @return
     */
    Integer deleteApplyCourse(Integer courseId, Integer userId);

    /**
     * 保存申请
     * @param apply
     * @return
     */
    Integer saveApply(Apply apply);
}
