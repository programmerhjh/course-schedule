package com.neusoft.course.schedule.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neusoft.course.schedule.dto.ExportExcelByFacultyAndTermDTO;
import com.neusoft.course.schedule.dto.SearchCourseDTO;
import com.neusoft.course.schedule.dto.UploadExcelImportCourseDTO;
import com.neusoft.course.schedule.entity.Course;
import com.neusoft.course.schedule.entity.PageEntity;
import com.neusoft.course.schedule.entity.PageResult;
import com.neusoft.course.schedule.enums.ResultCode;
import com.neusoft.course.schedule.mapper.CourseMapper;
import com.neusoft.course.schedule.service.CourseService;
import com.neusoft.course.schedule.service.ExcelService;
import com.neusoft.course.schedule.utils.PageUtils;
import com.neusoft.course.schedule.vo.CourseAddPageSelectUserVO;
import com.neusoft.course.schedule.vo.CourseHistoryTeacherVO;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/24.
 */
@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ExcelService excelService;

    @Override
    public PageResult<Course> getCourseData(Integer fcId, Integer tId, PageEntity pageEntity) {
        if (ObjectUtils.isEmpty(pageEntity.getPage())){
            PageResult<Course> coursePageResult = new PageResult<>();
            List<Course> courses = courseMapper.selectCourseByFacultyIdAndTermId(fcId, tId);
            coursePageResult.setData(courses);
            return coursePageResult;
        }
        return PageUtils.getPageResult(getPageInfo(pageEntity, fcId, tId));
    }

    @Override
    public Integer deleteCourse(Course course) {
        Integer row = courseMapper.deleteCourse(course);
        if (row <= 0){
            throw new RuntimeException(ResultCode.COMMON_FAIL.getMessage());
        }
        return row;
    }

    @Override
    public Integer batchDeleteCourses(List<Integer> idList) {
        Integer row = courseMapper.batchDeleteCourses(idList);
        // mybatis 批量事务操作的 BATCH 执行器无法获取事务操作条数
        return row;
    }

    @Override
    public PageResult<Course> searchCourse(SearchCourseDTO searchCourseDTO) {
        PageResult pageResult = PageUtils.getPageResult(getPageLikeInfo(searchCourseDTO));
        return pageResult;
    }

    @Override
    public List<CourseAddPageSelectUserVO> courseAddPageData(Integer fcId, Integer tId) {
        return courseMapper.selectUserCourseTimeByFacultyIdAndTermId(fcId, tId);
    }

    @Override
    public List<CourseHistoryTeacherVO> courseHistoryTeacherList(Integer cId) {
        return courseMapper.courseHistoryTeacherList(cId);
    }

    @Override
    public Integer saveCourse(Course course) {
        if (!ObjectUtils.isEmpty(course.getId())) {
            Course oldCourse = courseMapper.selectCourseById(course.getId());
            if (oldCourse.equals(course)){
                return 0;
            }
        }
        Integer row = courseMapper.saveCourse(course);
        if (row <= 0){
            throw new RuntimeException(ResultCode.COMMON_FAIL.getMessage());
        }
        return row;
    }

    @Override
    public SXSSFWorkbook generatorExcelByFacultyAndTerm(ExportExcelByFacultyAndTermDTO exportExcelByFacultyAndTermDTO) {
        List<Course> courses = courseMapper.selectCoursesByFacultyAndTerm(exportExcelByFacultyAndTermDTO);
        SXSSFWorkbook workbook = excelService.createAndDownloadCoursesExcel(courses);
        return workbook;
    }

    @Override
    public Boolean checkCourseIsComplete(ExportExcelByFacultyAndTermDTO exportExcelByFacultyAndTermDTO) {
        return courseMapper.checkCourseIsComplete(exportExcelByFacultyAndTermDTO) > 0;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void analyzeExcelImportCourse(UploadExcelImportCourseDTO uploadExcelImportCourseDTO) {
        List<Course> courses = excelService.analyzeCourseExcel(uploadExcelImportCourseDTO);
        Integer rows = courseMapper.batchInsertCourses(courses);
        if (rows != courses.size()){
            throw new RuntimeException(ResultCode.COMMON_FAIL.getMessage());
        }
    }

    /**
     * 调用分页插件完成分页
     * @param pageEntity
     * @param fcId
     * @param tId
     * @return
     */
    private PageInfo<Course> getPageInfo(PageEntity pageEntity, Integer fcId, Integer tId) {
        int pageNum = pageEntity.getPage();
        int pageSize = pageEntity.getLimit();
        PageHelper.startPage(pageNum, pageSize);
        List<Course> courseList;
        if (!ObjectUtils.isEmpty(pageEntity.getExtendField()) && (Boolean)pageEntity.getExtendField()){
            courseList = courseMapper.selectUndistributedCourseByFacultyIdAndTermId(fcId, tId);
        } else {
            courseList = courseMapper.selectCourseByFacultyIdAndTermId(fcId, tId);
        }
        return new PageInfo<>(courseList);
    }

    /**
     * 调用分页插件完成模糊搜索分页
     * @param searchDTO
     * @return
     */
    private PageInfo<Course> getPageLikeInfo(SearchCourseDTO searchDTO) {
        int pageNum = searchDTO.getPage();
        int pageSize = searchDTO.getLimit();
        PageHelper.startPage(pageNum, pageSize);
        List<Course> courses = courseMapper.searchCourse(searchDTO.getFcId(), searchDTO.getTermId(), searchDTO.getKey());
        return new PageInfo<>(courses);
    }
}
