package com.neusoft.course.schedule.biz;

import com.neusoft.course.schedule.dto.SearchCourseDTO;
import com.neusoft.course.schedule.dto.SearchUserApplyDTO;
import com.neusoft.course.schedule.entity.Apply;
import com.neusoft.course.schedule.entity.Course;
import com.neusoft.course.schedule.entity.PageEntity;
import com.neusoft.course.schedule.entity.PageResult;
import com.neusoft.course.schedule.enums.ResultCode;
import com.neusoft.course.schedule.service.ApplyService;
import com.neusoft.course.schedule.service.CourseService;
import com.neusoft.course.schedule.service.TermService;
import com.neusoft.course.schedule.utils.BeanCopierUtils;
import com.neusoft.course.schedule.utils.JsonResult;
import com.neusoft.course.schedule.utils.ResultGeneratorUtils;
import com.neusoft.course.schedule.vo.CourseApplyListVO;
import com.neusoft.course.schedule.vo.UserCourseTermInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/16.
 */
@RequestMapping(value = "teacher")
@Controller
public class TeacherBiz {

    @Autowired
    ApplyService applyService;

    @Autowired
    CourseService courseService;

    @Autowired
    TermService termService;

    @GetMapping("courseHistoryUI")
    public String courseHistoryUI(){
        return "course/teacherCourseHistory";
    }

    @GetMapping("applyListUI")
    public String applyListUI(){
        return "apply/teacherApplyList";
    }

    @GetMapping("applyCourseUI")
    public ModelAndView applyCourseUI(HttpServletRequest request){
        ModelAndView mv = new ModelAndView("course/teacherApplyCourseList");
        List<Integer> idList = applyService.getApplyIds(Integer.parseInt(request.getSession().getAttribute("userId").toString()));
        mv.addObject("userApplyList", idList);
        return mv;
    }

    @GetMapping("addApplyCourseUI")
    public String addApplyCourseUI(){
        return "course/applyCourseAddUI";
    }

    @PostMapping(value = {"getUserApplyList/{userId}", "getUserApplyList/{userId}/{termId}"})
    @ResponseBody
    public JsonResult getUserApplyList(@PathVariable(value = "userId") Integer userId, @PathVariable(value = "termId", required = false) Integer termId,@RequestBody PageEntity pageEntity){
        if (ObjectUtils.isEmpty(termId)){
            return ResultGeneratorUtils.success(applyService.getUserApplyList(userId, pageEntity));
        }
        return ResultGeneratorUtils.success(applyService.getUserApplyList(userId, termId, pageEntity));
    }

    @PostMapping(value = "searchUserApply")
    @ResponseBody
    public JsonResult searchUserApply(@RequestBody SearchUserApplyDTO searchUserApplyDTO){
        return ResultGeneratorUtils.success(applyService.searchUserApply(searchUserApplyDTO));
    }

    @PostMapping(value = "searchUserHistoryCourse")
    @ResponseBody
    public JsonResult searchUserHistoryCourse(@RequestBody SearchUserApplyDTO searchUserApplyDTO){
        return ResultGeneratorUtils.success(courseService.searchUserHistoryCourse(searchUserApplyDTO));
    }

    @PostMapping(value = {"getCourseHistory/{userId}", "getCourseHistory/{userId}/{termId}"})
    @ResponseBody
    public JsonResult getCourseHistory(@PathVariable(value = "userId") Integer userId, @PathVariable(value = "termId", required = false) Integer termId, @RequestBody PageEntity pageEntity){
        if (ObjectUtils.isEmpty(termId)){
            return ResultGeneratorUtils.success(courseService.getCourseHistory(userId, pageEntity));
        }
        return ResultGeneratorUtils.success(courseService.getCourseHistory(userId, termId, pageEntity));
    }

    @PostMapping(value = {"getApplyCourse/{fcId}/{termId}"})
    @ResponseBody
    public JsonResult getApplyCourse(@PathVariable(value = "fcId") Integer fcId, @PathVariable(value = "termId") Integer termId, @RequestBody PageEntity pageEntity, HttpServletRequest request){
        Integer state = termService.getTermApplyState(termId);
        if (!ObjectUtils.isEmpty(state) && state.equals(0)){
            return ResultGeneratorUtils.fail(ResultCode.TERM_UNOPEN_APPLY);
        }
        List<Integer> idList = applyService.getApplyIds(Integer.parseInt(request.getSession().getAttribute("userId").toString()));
        PageResult<Course> courseData = courseService.getCourseData(fcId, termId, pageEntity);
        List<CourseApplyListVO> courseApplyListVOList = new ArrayList<>(courseData.getData().size());
        PageResult<CourseApplyListVO> courseApplyListVOPageResult = new PageResult<>();
        courseApplyListVOPageResult.setTotalSize(courseData.getTotalSize());
        courseApplyListVOPageResult.setPageNum(courseData.getPageNum());
        courseApplyListVOPageResult.setPageSize(courseData.getPageSize());
        courseApplyListVOPageResult.setTotalPages(courseData.getTotalPages());
        for (Course s: courseData.getData()) {
            CourseApplyListVO courseApplyListVO = new CourseApplyListVO();
            BeanCopierUtils.copy(s, courseApplyListVO);
            courseApplyListVO.setUserApplyList(idList);
            courseApplyListVOList.add(courseApplyListVO);
        }
        courseApplyListVOPageResult.setData(courseApplyListVOList);
        return ResultGeneratorUtils.success(courseApplyListVOPageResult);
    }

    @PostMapping(value = {"getUserCourseTermInfo/{fcId}/{termId}"})
    @ResponseBody
    public JsonResult getUserCourseTermInfo(@PathVariable(value = "fcId") Integer fcId, @PathVariable(value = "termId") Integer termId, HttpServletRequest request){
        UserCourseTermInfoVO userCourseTermInfoVO = courseService.getUserCourseTermInfo(Integer.parseInt(request.getSession().getAttribute("userId").toString()), fcId, termId);
        return ResultGeneratorUtils.success(userCourseTermInfoVO);
    }

    @PostMapping(value = {"deleteApplyCourse/{courseId}/{userId}"})
    @ResponseBody
    public JsonResult deleteApplyCourse(@PathVariable(value = "courseId") Integer courseId, @PathVariable(value = "userId") Integer userId){
        return ResultGeneratorUtils.success(applyService.deleteApplyCourse(courseId, userId));
    }

    @PostMapping(value = {"searchApplyCourse"})
    @ResponseBody
    public JsonResult searchApplyCourse(@RequestBody SearchCourseDTO searchCourseDTO){
        Integer state = termService.getTermApplyState(searchCourseDTO.getTermId());
        if (!ObjectUtils.isEmpty(state) && state.equals(0)){
            return ResultGeneratorUtils.fail(ResultCode.TERM_UNOPEN_APPLY);
        }
        return ResultGeneratorUtils.success(courseService.searchCourse(searchCourseDTO));
    }

    @PostMapping(value = {"saveCourseApply"})
    @ResponseBody
    public JsonResult saveCourseApply(@RequestBody Apply apply){
        return ResultGeneratorUtils.success(applyService.saveApply(apply));
    }

}
