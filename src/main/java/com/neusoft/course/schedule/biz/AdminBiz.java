package com.neusoft.course.schedule.biz;

import com.alibaba.fastjson.JSON;
import com.neusoft.course.schedule.constants.ServiceConstants;
import com.neusoft.course.schedule.dto.*;
import com.neusoft.course.schedule.entity.*;
import com.neusoft.course.schedule.enums.ResultCode;
import com.neusoft.course.schedule.service.*;
import com.neusoft.course.schedule.utils.ExcelUtil;
import com.neusoft.course.schedule.utils.JsonResult;
import com.neusoft.course.schedule.utils.ObjectUtils;
import com.neusoft.course.schedule.utils.ResultGeneratorUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/16.
 */
@CrossOrigin
@RequestMapping(value = "admin")
@Controller
public class AdminBiz {

    @Autowired
    private UserService userService;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private TermService termService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ApplyService applyService;

    @Autowired
    private ExcelService excelService;

    @GetMapping(value = "userListUI")
    public String userListUI(){
        return "user/userList";
    }

    @PostMapping(value = {"userList","userList/{fcId}"})
    @ResponseBody
    public JsonResult getUserListData(@PathVariable(value = "fcId", required = false) Integer fcId, @RequestBody(required = false) PageEntity pageEntity){
        if (!org.springframework.util.ObjectUtils.isEmpty(fcId)){
            return ResultGeneratorUtils.success(userService.getUserListDataByFacultyId(fcId, pageEntity));
        }
        return ResultGeneratorUtils.success(userService.getUserListData(pageEntity));
    }

    @GetMapping(value = "userAddUI")
    public ModelAndView userAddUI(){
        ModelAndView mv = new ModelAndView("user/userAddUI");
        List<Faculty> facultyData = facultyService.getFacultyData();
        mv.addObject("facultyList", JSON.toJSONString(facultyData));
        mv.addObject("roleList", JSON.toJSONString(ServiceConstants.ROLE_LIST));
        return mv;
    }

    @PostMapping(value = "saveUser")
    @ResponseBody
    public JsonResult saveUser(@RequestBody User user){
        String account = user.getAccount(), name = user.getName();
        User userByAccount = userService.checkUserIsRepeat(account);
        if (account.equals(name)){
            return ResultGeneratorUtils.fail(ResultCode.USER_NAME_ACCOUNT_REPEAT);
        }
        // 用户名和账号均不重复, 即可保存用户
        if (userByAccount != null){
            // 如果传来的账号还是之前自己的账号就跳过，反之则账号重复
            if (!userByAccount.getId().equals(user.getId())){
                return ResultGeneratorUtils.fail(ResultCode.USER_ACCOUNT_ALREADY_EXIST);
            }
            // 如果传过来的数据没有发生改变，则直接响应成功
            if (userByAccount.equals(user)) {
                return ResultGeneratorUtils.success();
            }
        }
        User userByName = userService.checkUserIsRepeat(name);
        if (userByName != null){
            // 如果传来的用户名还是之前自己的用户名就跳过，反之则用户名重复
            if (!userByName.getId().equals(user.getId())){
                return ResultGeneratorUtils.fail(ResultCode.USER_NAME_ALREADY_EXIST);
            }
            // 如果传过来的数据没有发生改变，则直接响应成功
            if (userByName.equals(user)) {
                return ResultGeneratorUtils.success();
            }
        }
        userService.saveUser(user);
        return ResultGeneratorUtils.success();
    }

    @PostMapping(value = "deleteUser")
    @ResponseBody
    public JsonResult deleteUser(@RequestBody User user){
        userService.deleteUser(user);
        return ResultGeneratorUtils.success();
    }

    @PostMapping(value = "forbiddenUser")
    @ResponseBody
    public JsonResult forbiddenUser(@RequestBody User user){
        userService.forbiddenUser(user);
        return ResultGeneratorUtils.success();
    }

    @PostMapping(value = "batchDeleteUsers")
    @ResponseBody
    public JsonResult batchDeleteUsers(@RequestBody List<Integer> idList){
        userService.batchDeleteUsers(idList);
        return ResultGeneratorUtils.success();
    }

    @PostMapping(value = "batchForbiddenUsers")
    @ResponseBody
    public JsonResult batchForbiddenUsers(@RequestBody List<User> userList){
        userService.batchForbiddenUsers(userList);
        return ResultGeneratorUtils.success();
    }

    @PostMapping(value = {"searchUser", "searchUser/{fcId}"})
    @ResponseBody
    public JsonResult searchUser(@PathVariable(value = "fcId", required = false) Integer fcId, @RequestBody SearchDTO searchDTO){
        if (!org.springframework.util.ObjectUtils.isEmpty(fcId)){
            return ResultGeneratorUtils.success(userService.searchFacultyUser(fcId, searchDTO));
        }
        return ResultGeneratorUtils.success(userService.searchUser(searchDTO));
    }







    @GetMapping(value = "facultyListUI")
    public String facultyListUI(){
        return "faculty/facultyList";
    }

    @PostMapping(value = "facultyList")
    @ResponseBody
    public JsonResult getFacultyListData(@RequestBody PageEntity pageEntity){
        return ResultGeneratorUtils.success(facultyService.getFacultyData(pageEntity));
    }

    @GetMapping(value = "facultyAddUI")
    public ModelAndView facultyAddUI(){
        ModelAndView mv = new ModelAndView("faculty/facultyAddUI");
        return mv;
    }

    @PostMapping(value = "getFacultyData")
    @ResponseBody
    public JsonResult getFacultyData(){
        return ResultGeneratorUtils.success(facultyService.getFacultyData());
    }

    @PostMapping(value = "saveFaculty")
    @ResponseBody
    public JsonResult saveFaculty(@RequestBody Faculty faculty){
        String name = faculty.getName();
        Faculty facultyByName = facultyService.checkFacultyIsRepeat(name);
        if (facultyByName != null){
            return ResultGeneratorUtils.fail(ResultCode.FACULTY_NAME_ALREADY_EXIST);
        }
        facultyService.saveFaculty(faculty);
        return ResultGeneratorUtils.success();
    }

    @PostMapping(value = "deleteFaculty")
    @ResponseBody
    public JsonResult deleteFaculty(@RequestBody Faculty faculty){
        facultyService.deleteFaculty(faculty);
        return ResultGeneratorUtils.success();
    }

    @PostMapping(value = "batchDeleteFaculties")
    @ResponseBody
    public JsonResult batchDeleteFaculties(@RequestBody List<Integer> idList){
        facultyService.batchDeleteFaculties(idList);
        return ResultGeneratorUtils.success();
    }

    @PostMapping(value = "searchFaculty")
    @ResponseBody
    public JsonResult searchFaculty(@RequestBody SearchDTO searchDTO){
        return ResultGeneratorUtils.success(facultyService.searchFaculty(searchDTO));
    }






    @GetMapping(value = "facultyTermListUI")
    public String facultyTermListUI(){
        return "term/facultyTermListUI";
    }

    @GetMapping(value = "termAddUI")
    public ModelAndView termAddUI(){
        ModelAndView mv = new ModelAndView("term/termAddUI");
        return mv;
    }

    @GetMapping(value = "termListUI")
    public String termListUI(){
        return "term/termList";
    }

    @PostMapping(value = "termList/{fcId}")
    @ResponseBody
    public JsonResult termList(@PathVariable("fcId") Integer fcId, @RequestBody(required = false) PageEntity pageEntity){
        return ResultGeneratorUtils.success(termService.getTermData(pageEntity, fcId));
    }

    @GetMapping(value = "termList/{fcId}")
    @ResponseBody
    public JsonResult allTermList(@PathVariable("fcId") Integer fcId){
        return ResultGeneratorUtils.success(termService.getTermData(new PageEntity(), fcId));
    }

    @PostMapping(value = "searchTerm")
    @ResponseBody
    public JsonResult searchTerm(@RequestBody SearchTermDTO searchTermDTO){
        return ResultGeneratorUtils.success(termService.searchTerm(searchTermDTO));
    }

    @PostMapping(value = "saveTerm/{fcId}")
    @ResponseBody
    public JsonResult saveTerm(@PathVariable("fcId") Integer fcId, @RequestBody Term term){
        String name = term.getName();
        Term termByName = termService.checkTermNameIsRepeat(fcId, name);
        if (termByName != null){
            return ResultGeneratorUtils.fail(ResultCode.TERM_NAME_ALREADY_EXIST_THIS_FACULTY);
        }
        term.setFcId(fcId);
        termService.saveTerm(term);
        return ResultGeneratorUtils.success();
    }

    @PostMapping(value = "deleteTerm")
    @ResponseBody
    public JsonResult deleteTerm(@RequestBody Term term){
        termService.deleteTerm(term);
        return ResultGeneratorUtils.success();
    }

    @PostMapping(value = "batchDeleteTerms")
    @ResponseBody
    public JsonResult batchDeleteTerms(@RequestBody List<Integer> idList){
        termService.batchDeleteTerms(idList);
        return ResultGeneratorUtils.success();
    }




    @GetMapping(value = "applyListUI")
    public String applyListUI(){
        return "apply/applyList";
    }

    @GetMapping(value = "applyAddUI")
    public ModelAndView applyAddUI(){
        ModelAndView mv = new ModelAndView("apply/applyAddUI");
        return mv;
    }





    @PostMapping(value = "openTermApply")
    @ResponseBody
    public JsonResult openTermApply(@RequestBody OpenTermApplyDTO openTermApplyDTO){
        termService.openTermApply(openTermApplyDTO);
        return ResultGeneratorUtils.success();
    }

    @GetMapping(value = "getTermApplyState/{id}")
    @ResponseBody
    public JsonResult getTermApplyState(@PathVariable("id") Integer id){
        return ResultGeneratorUtils.success(termService.getTermApplyState(id));
    }


    @PostMapping(value = {"applyList/{fcId}/{termId}","applyList/{fcId}"})
    @ResponseBody
    public JsonResult applyList(@PathVariable(value = "fcId") Integer fcId, @PathVariable(value = "termId", required = false) Integer tId, @RequestBody PageEntity pageEntity){
        return ResultGeneratorUtils.success(applyService.getApplyData(fcId, tId, pageEntity));
    }

    @PostMapping(value = "deleteApply")
    @ResponseBody
    public JsonResult deleteApply(@RequestBody Apply apply){
        applyService.deleteApply(apply);
        return ResultGeneratorUtils.success();
    }

    @PostMapping(value = "checkApplyState")
    @ResponseBody
    public JsonResult checkApplyState(@RequestBody Apply apply){
        return ResultGeneratorUtils.success(applyService.checkApplyState(apply));
    }

    @PostMapping(value = "saveApplyState")
    @ResponseBody
    public JsonResult saveApplyState(@RequestBody Apply apply){
        applyService.saveApplyState(apply);
        return ResultGeneratorUtils.success();
    }

    @PostMapping(value = "searchApply")
    @ResponseBody
    public JsonResult searchApply(@RequestBody SearchApplyDTO searchApplyDTO){
        return ResultGeneratorUtils.success(applyService.searchApply(searchApplyDTO));
    }

    @PostMapping(value = "batchDeleteApplies")
    @ResponseBody
    public JsonResult batchDeleteApplies(@RequestBody List<Integer> idList){
        applyService.batchDeleteApplies(idList);
        return ResultGeneratorUtils.success();
    }





    @GetMapping(value = "courseAddUI")
    public ModelAndView courseAddUI(){
        ModelAndView mv = new ModelAndView("course/courseAddUI");
        return mv;
    }

    @GetMapping(value = "facultyCourseListUI")
    public String facultyCourseListUI(){
        return "course/facultyCourseListUI";
    }

    @GetMapping(value = "courseListUI")
    public String courseListUI(){
        return "course/courseList";
    }

    @PostMapping(value = "deleteCourse")
    @ResponseBody
    public JsonResult deleteCourse(@RequestBody Course course){
        courseService.deleteCourse(course);
        return ResultGeneratorUtils.success();
    }

    @PostMapping(value = "searchCourse")
    @ResponseBody
    public JsonResult searchCourse(@RequestBody SearchCourseDTO searchCourseDTO){
        return ResultGeneratorUtils.success(courseService.searchCourse(searchCourseDTO));
    }

    @PostMapping(value = "batchDeleteCourses")
    @ResponseBody
    public JsonResult batchDeleteCourses(@RequestBody List<Integer> idList){
        courseService.batchDeleteCourses(idList);
        return ResultGeneratorUtils.success();
    }

    @PostMapping(value = "courseList/{fcId}/{tId}")
    @ResponseBody
    public JsonResult getCourseList(@PathVariable("fcId") Integer fcId, @PathVariable("tId") Integer tId, @RequestBody PageEntity pageEntity){
        return ResultGeneratorUtils.success(courseService.getCourseData(fcId, tId, pageEntity));
    }

    @PostMapping(value = "courseAddData/{fcId}/{tId}")
    @ResponseBody
    public JsonResult courseAddPageData(@PathVariable("fcId") Integer fcId, @PathVariable("tId") Integer tId){
        return ResultGeneratorUtils.success(courseService.courseAddPageData(fcId, tId));
    }

    @PostMapping(value = "courseApplyList/{cId}")
    @ResponseBody
    public JsonResult courseApplyList(@PathVariable("cId") Integer cId, @RequestBody PageEntity pageEntity){
        return ResultGeneratorUtils.success(applyService.courseApplyList(cId, pageEntity));
    }

    @PostMapping(value = "courseHistoryTeacherList/{cId}")
    @ResponseBody
    public JsonResult courseHistoryTeacherList(@PathVariable("cId") Integer cId){
        return ResultGeneratorUtils.success(courseService.courseHistoryTeacherList(cId));
    }

    @PostMapping(value = "saveCourse")
    @ResponseBody
    public JsonResult saveCourse(@RequestBody Course course){
        courseService.saveCourse(course);
        return ResultGeneratorUtils.success();
    }






    @PostMapping(value = "createAndDownloadUsersExcel")
    @ResponseBody
    public void createAndDownloadUsersExcel(@RequestBody BatchCreateUserDTO batchCreateUserDTO){
        SXSSFWorkbook workbook = userService.randomGeneratorUsers(batchCreateUserDTO);
        responseExcel(workbook);
    }

    @PostMapping(value = "exportExcelByFacultyAndTerm")
    @ResponseBody
    public void exportExcelByFacultyAndTerm(@RequestBody ExportExcelByFacultyAndTermDTO exportExcelByFacultyAndTermDTO){
        SXSSFWorkbook workbook = courseService.generatorExcelByFacultyAndTerm(exportExcelByFacultyAndTermDTO);
        responseExcel(workbook);
    }

    @PostMapping(value = "checkCourseIsComplete")
    @ResponseBody
    public JsonResult checkCourseIsComplete(@RequestBody ExportExcelByFacultyAndTermDTO exportExcelByFacultyAndTermDTO){
        if (ObjectUtils.hasFieldIsNULL(exportExcelByFacultyAndTermDTO)) {
            return ResultGeneratorUtils.fail(ResultCode.PARAM_NOT_COMPLETE);
        }
        return ResultGeneratorUtils.success(courseService.checkCourseIsComplete(exportExcelByFacultyAndTermDTO));
    }

    @PostMapping(value = "uploadExcelImportCourse")
    @ResponseBody
    public JsonResult uploadExcelImportCourse(@RequestParam(value="courseExcel") MultipartFile courseExcel, @RequestParam(value="fcId") Integer fcId, @RequestParam(value="termId") Integer termId){
        UploadExcelImportCourseDTO uploadExcelImportCourseDTO = new UploadExcelImportCourseDTO(fcId, termId, courseExcel);
        if (ObjectUtils.hasFieldIsNULL(uploadExcelImportCourseDTO)) {
            return ResultGeneratorUtils.fail(ResultCode.PARAM_NOT_COMPLETE);
        }
        courseService.analyzeExcelImportCourse(uploadExcelImportCourseDTO);
        return ResultGeneratorUtils.success();
    }

    @GetMapping(value = "getImportCourseExcelTemplate")
    public void getImportCourseExcelTemplate(HttpServletRequest req, HttpServletResponse resp){
        excelService.getImportCourseExcelTemplate(req, resp);
    }

    @GetMapping(value = "excelExportUI")
    public String excelExportUI(){
        return "excel/excel-export-popup";
    }

    @GetMapping(value = "excelImportUI")
    public String excelImportUI(){
        return "excel/excel-import-popup";
    }

    /**
     * 响应 Excel 格式文件
     * @param workbook 工作簿
     */
    private void responseExcel(SXSSFWorkbook workbook){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        //以流输出到浏览器
        ExcelUtil.writeToResponse(workbook, request, response);
    }

}
