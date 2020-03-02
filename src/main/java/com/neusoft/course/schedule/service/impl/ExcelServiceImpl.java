package com.neusoft.course.schedule.service.impl;

import com.neusoft.course.schedule.constants.ServiceConstants;
import com.neusoft.course.schedule.dto.UploadExcelImportCourseDTO;
import com.neusoft.course.schedule.entity.Course;
import com.neusoft.course.schedule.entity.User;
import com.neusoft.course.schedule.enums.CourseExcelFieldName;
import com.neusoft.course.schedule.enums.CreateUsersExcelFieldName;
import com.neusoft.course.schedule.enums.ResultCode;
import com.neusoft.course.schedule.mapper.CourseMapper;
import com.neusoft.course.schedule.mapper.FacultyMapper;
import com.neusoft.course.schedule.mapper.UserMapper;
import com.neusoft.course.schedule.service.ExcelService;
import com.neusoft.course.schedule.utils.ExcelUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/19.
 */
@Service
public class ExcelServiceImpl implements ExcelService{

    @Autowired
    UserMapper userMapper;

    @Autowired
    FacultyMapper facultyMapper;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    ExcelUtil excelUtil;

   @Override
    public SXSSFWorkbook createAndDownloadUsersExcel(List<User> users) {
        // 字段名Key
        List<String> fieldNames = userMapper.getExcelConfig();
        for (Iterator<String> iterator = fieldNames.iterator(); iterator.hasNext();) {
            if (CreateUsersExcelFieldName.getDetailByFieldName(iterator.next()) == null){
                iterator.remove();
            }
        }
        ArrayList<String> sheetNames = new ArrayList<String>(1){{
           add(ServiceConstants.CREATE_USER_FORM);
        }};
        SXSSFWorkbook workbook = excelUtil.export(sheetNames, users, fieldNames, CreateUsersExcelFieldName.class);
        return workbook;
    }

    @Override
    public SXSSFWorkbook createAndDownloadCoursesExcel(List<Course> courses) {
        List<String> fieldNames = courseMapper.getExcelConfig();
        for (Iterator<String> iterator = fieldNames.iterator(); iterator.hasNext();) {
            if (CourseExcelFieldName.getDetailByFieldName(iterator.next()) == null){
                iterator.remove();
            }
        }
        String facultyName = facultyMapper.selectFacultyById(courses.get(0).getFacultyId()).getName();
        ArrayList<String> sheetNames = new ArrayList<String>(1){{
            add(facultyName);
        }};
        SXSSFWorkbook workbook = excelUtil.export(sheetNames, courses, fieldNames, CourseExcelFieldName.class);
        return workbook;
    }

    @Override
    public void getImportCourseExcelTemplate(HttpServletRequest req, HttpServletResponse resp) {
        InputStream in = null;
        ServletOutputStream out = null;
        try {
            ClassPathResource cpr = new ClassPathResource(ServiceConstants.TEMPLATE_FILE_ADDRESS);
            String fileName = URLEncoder.encode("课程导入模板Excel.xls","UTF-8");
            resp.setCharacterEncoding("UTF-8");
            resp.setHeader("Content-disposition", "attachment; filename=" + fileName);// 设定输出文件头
            resp.setContentType("application/msexcel");// 定义输出类型
            in = cpr.getInputStream();
            out = resp.getOutputStream();
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
        } catch(Exception e){
            e.printStackTrace();
        }finally {
            if(null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<Course> analyzeCourseExcel(UploadExcelImportCourseDTO uploadExcelImportCourseDTO) {
        File file = new File(ServiceConstants.TEMP_SAVE_FILE_ADDRESS);
        List<Course> courses;
        try {
            if (!file.exists()){
                file.createNewFile();
            }
            uploadExcelImportCourseDTO.getCourseExcel().transferTo(file);
            courses = excelUtil.parseObjectFromExcel(file, Course.class, uploadExcelImportCourseDTO);
            if (CollectionUtils.isEmpty(courses)){
                throw new RuntimeException(ServiceConstants.EXCEL_HAVE_NOTHING_DATA);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(ResultCode.COMMON_FAIL.getMessage());
        }
        return courses;
    }

}
