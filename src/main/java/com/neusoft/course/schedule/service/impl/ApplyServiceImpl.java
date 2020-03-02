package com.neusoft.course.schedule.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neusoft.course.schedule.constants.ServiceConstants;
import com.neusoft.course.schedule.dto.SearchApplyDTO;
import com.neusoft.course.schedule.dto.SearchUserApplyDTO;
import com.neusoft.course.schedule.entity.*;
import com.neusoft.course.schedule.enums.ResultCode;
import com.neusoft.course.schedule.mapper.ApplyMapper;
import com.neusoft.course.schedule.mapper.CourseMapper;
import com.neusoft.course.schedule.mapper.UserMapper;
import com.neusoft.course.schedule.service.ApplyService;
import com.neusoft.course.schedule.utils.PageUtils;
import com.neusoft.course.schedule.vo.ApplyListVO;
import com.neusoft.course.schedule.vo.CourseEditPageApplyListVO;
import com.neusoft.course.schedule.vo.UserApplyListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/26.
 */
@Service
public class ApplyServiceImpl implements ApplyService {

    @Autowired
    private ApplyMapper applyMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public PageResult<CourseEditPageApplyListVO> courseApplyList(Integer cId, PageEntity pageEntity) {
        if (ObjectUtils.isEmpty(pageEntity.getPage())){
            PageResult<CourseEditPageApplyListVO> applyListVOPageResult = new PageResult<>();
            List<CourseEditPageApplyListVO> applyListVOList = applyMapper.selectApplyByCourseId(cId);
            applyListVOPageResult.setData(applyListVOList);
            return applyListVOPageResult;
        }
        return PageUtils.getPageResult(getPageInfoByCourseId(pageEntity, cId));
    }

    @Override
    public Integer batchDeleteApplies(List<Integer> idList) {
        Integer row = applyMapper.batchDeleteApplies(idList);
        // mybatis 批量事务操作的 BATCH 执行器无法获取事务操作条数
        return row;
    }

    @Override
    public Integer deleteApply(Apply apply) {
        Integer row = applyMapper.deleteApply(apply);
        if (row <= 0){
            throw new RuntimeException(ResultCode.COMMON_FAIL.getMessage());
        }
        return row;
    }

    @Override
    public PageResult<ApplyListVO> searchApply(SearchApplyDTO searchApplyDTO) {
        PageResult pageResult;
        if (ObjectUtils.isEmpty(searchApplyDTO.getFcId())) {
            throw new RuntimeException(ResultCode.PARAM_NOT_COMPLETE.getMessage());
        }
        if (ObjectUtils.isEmpty(searchApplyDTO.getTermId())){
            pageResult = PageUtils.getPageResult(getPageLikeInfoByFacultyId(searchApplyDTO));
        } else {
            pageResult = PageUtils.getPageResult(getPageLikeInfoByFacultyIdAndTermId(searchApplyDTO));
        }
        return pageResult;
    }

    @Override
    public PageResult<ApplyListVO> getApplyData(Integer fcId, Integer tId, PageEntity pageEntity) {
        PageResult<ApplyListVO> applyPageResult = new PageResult<>();
        // pageEntity 为空直接查出全部数据
        if (ObjectUtils.isEmpty(pageEntity.getPage())){
            List<ApplyListVO> applies = applyMapper.selectApplyData();
            applyPageResult.setData(applies);
            return applyPageResult;
        }
        if (!ObjectUtils.isEmpty(fcId) && ObjectUtils.isEmpty(tId)) {
            return PageUtils.getPageResult(getPageInfoByFacultyId(pageEntity, fcId));
        }
        return PageUtils.getPageResult(getPageInfoByFacultyIdAndTermId(pageEntity, fcId, tId));
    }

    @Override
    public String checkApplyState(Apply apply) {
        return applyMapper.selectUserNameByApplyCourseId(apply);
    }

    @Override
    @Transactional
    public Integer saveApplyState(Apply apply) {
        if (apply.getState().equals(ServiceConstants.APPLY_STATE_FAIL)){
            // 申请改为未通过
            Integer row = applyMapper.updateApplyStateById(apply);
            return row;
        }
        // 先将课程任教老师改为当前申请人
        User user = userMapper.selectUserByUserId(apply.getUserId());
        Course course = new Course();
        course.setTheoryTeacherId(user.getId());
        course.setId(apply.getTaskId());
        course.setTheoryTeacher(user.getName() != null ? user.getName() : user.getAccount());
        courseMapper.updateCourseTheoryTeacher(course);
        // 再把申请改为已通过
        Integer row = applyMapper.updateApplyStateById(apply);
        return row;
    }

    @Override
    public PageResult<UserApplyListVO> getUserApplyList(Integer userId, PageEntity pageEntity) {
        return PageUtils.getPageResult(getPageInfoByUserId(pageEntity, userId));
    }

    @Override
    public PageResult<UserApplyListVO> getUserApplyList(Integer userId, Integer termId, PageEntity pageEntity) {
        return PageUtils.getPageResult(getPageInfoByUserIdAndTermId(pageEntity, userId, termId));
    }

    @Override
    public PageResult<UserApplyListVO> searchUserApply(SearchUserApplyDTO searchUserApplyDTO) {
        return PageUtils.getPageResult(getPageLikeInfoByUserId(searchUserApplyDTO));
    }

    @Override
    public List<Integer> getApplyIds(Integer userId) {
        return applyMapper.getApplyIdsByUserId(userId);
    }

    @Override
    public Integer deleteApplyCourse(Integer courseId, Integer userId) {
        Integer row = applyMapper.deleteApplyByUserIdAndCourseId(userId, courseId);
        if (row <= 0){
            throw new RuntimeException(ResultCode.COMMON_FAIL.getMessage());
        }
        return row;
    }

    @Override
    public Integer saveApply(Apply apply) {
        Integer row = applyMapper.saveApply(apply);
        if (row <= 0){
            throw new RuntimeException(ResultCode.COMMON_FAIL.getMessage());
        }
        return row;
    }

    /**
     * 调用分页插件完成分页
     * @param pageEntity
     * @param cId 课程ID
     * @return
     */
    private PageInfo<CourseEditPageApplyListVO> getPageInfoByCourseId(PageEntity pageEntity, Integer cId) {
        int pageNum = pageEntity.getPage();
        int pageSize = pageEntity.getLimit();
        PageHelper.startPage(pageNum, pageSize);
        List<CourseEditPageApplyListVO> applyListVOList = applyMapper.selectApplyByCourseId(cId);
        return new PageInfo<>(applyListVOList);
    }

    /**
     * 调用分页插件完成分页
     * @param pageEntity
     * @param fcId 院系ID
     * @return
     */
    private PageInfo<ApplyListVO> getPageInfoByFacultyId(PageEntity pageEntity, Integer fcId) {
        int pageNum = pageEntity.getPage();
        int pageSize = pageEntity.getLimit();
        PageHelper.startPage(pageNum, pageSize);
        List<ApplyListVO> applyListVOList = applyMapper.selectApplyByFacultyId(fcId);
        return new PageInfo<>(applyListVOList);
    }

    /**
     * 调用分页插件完成分页
     * @param pageEntity
     * @param fcId 院系ID
     * @param tId 学年ID
     * @return
     */
    private PageInfo<ApplyListVO> getPageInfoByFacultyIdAndTermId(PageEntity pageEntity, Integer fcId, Integer tId) {
        int pageNum = pageEntity.getPage();
        int pageSize = pageEntity.getLimit();
        PageHelper.startPage(pageNum, pageSize);
        List<ApplyListVO> applyListVOList = applyMapper.selectApplyByFacultyIdAndTermId(fcId, tId);
        return new PageInfo<>(applyListVOList);
    }

    /**
     * 调用分页插件完成模糊搜索分页
     * @param searchDTO
     * @return
     */
    private PageInfo<ApplyListVO> getPageLikeInfoByFacultyIdAndTermId(SearchApplyDTO searchDTO) {
        int pageNum = searchDTO.getPage();
        int pageSize = searchDTO.getLimit();
        PageHelper.startPage(pageNum, pageSize);
        List<ApplyListVO> applyListVOList = applyMapper.searchApplyByFacultyIdAndTermId(searchDTO.getFcId(), searchDTO.getTermId(), searchDTO.getKey());
        return new PageInfo<>(applyListVOList);
    }

    /**
     * 调用分页插件完成模糊搜索分页
     * @param searchDTO
     * @return
     */
    private PageInfo<ApplyListVO> getPageLikeInfoByFacultyId(SearchApplyDTO searchDTO) {
        int pageNum = searchDTO.getPage();
        int pageSize = searchDTO.getLimit();
        PageHelper.startPage(pageNum, pageSize);
        List<ApplyListVO> applyListVOList = applyMapper.searchApplyByFacultyId(searchDTO.getFcId(), searchDTO.getKey());
        return new PageInfo<>(applyListVOList);
    }

    private PageInfo<UserApplyListVO> getPageInfoByUserId(PageEntity pageEntity, Integer userId) {
        int pageNum = pageEntity.getPage();
        int pageSize = pageEntity.getLimit();
        PageHelper.startPage(pageNum, pageSize);
        List<UserApplyListVO> userApplyListVOList = applyMapper.selectApplyByUserId(userId);
        return new PageInfo<>(userApplyListVOList);
    }

    private PageInfo<UserApplyListVO> getPageInfoByUserIdAndTermId(PageEntity pageEntity, Integer userId, Integer termId) {
        int pageNum = pageEntity.getPage();
        int pageSize = pageEntity.getLimit();
        PageHelper.startPage(pageNum, pageSize);
        List<UserApplyListVO> userApplyListVOList = applyMapper.selectApplyByUserIdAndTermId(userId, termId);
        return new PageInfo<>(userApplyListVOList);
    }

    private PageInfo<UserApplyListVO> getPageLikeInfoByUserId(SearchUserApplyDTO searchUserApplyDTO) {
        int pageNum = searchUserApplyDTO.getPage();
        int pageSize = searchUserApplyDTO.getLimit();
        PageHelper.startPage(pageNum, pageSize);
        List<UserApplyListVO> userApplyListVOList = applyMapper.selectApplyByUserIdAndKey(searchUserApplyDTO.getUserId(), searchUserApplyDTO.getKey());
        return new PageInfo<>(userApplyListVOList);
    }

}
