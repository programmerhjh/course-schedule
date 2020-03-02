package com.neusoft.course.schedule.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neusoft.course.schedule.dto.SearchDTO;
import com.neusoft.course.schedule.entity.Faculty;
import com.neusoft.course.schedule.entity.PageEntity;
import com.neusoft.course.schedule.entity.PageResult;
import com.neusoft.course.schedule.enums.ResultCode;
import com.neusoft.course.schedule.mapper.FacultyMapper;
import com.neusoft.course.schedule.service.FacultyService;
import com.neusoft.course.schedule.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/16.
 */
@Service
public class FacultyServiceImpl implements FacultyService{

    @Autowired
    private FacultyMapper facultyMapper;

    @Override
    public PageResult<Faculty> getFacultyData(PageEntity pageEntity) {
        if (ObjectUtils.isEmpty(pageEntity.getPage())){
            PageResult<Faculty> facultyPageResult = new PageResult<>();
            List<Faculty> facultyData = facultyMapper.getFacultyData();
            facultyPageResult.setData(facultyData);
            return facultyPageResult;
        }
        return PageUtils.getPageResult(getPageInfo(pageEntity));
    }

    @Override
    public List<Faculty> getFacultyData() {
        return facultyMapper.getFacultyData();
    }

    @Override
    public Integer deleteFaculty(Faculty faculty) {
        Integer row = facultyMapper.deleteFaculty(faculty);
        if (row <= 0){
            throw new RuntimeException(ResultCode.COMMON_FAIL.getMessage());
        }
        return row;
    }

    @Override
    public Integer batchDeleteFaculties(List<Integer> idList) {
        Integer row = facultyMapper.batchDeleteFaculties(idList);
        // mybatis 批量事务操作的 BATCH 执行器无法获取事务操作条数
        return row;
    }

    @Override
    public PageResult<Faculty> searchFaculty(SearchDTO searchDTO) {
        PageResult pageResult = PageUtils.getPageResult(getPageLikeInfo(searchDTO));
        return pageResult;
    }

    @Override
    public Faculty checkFacultyIsRepeat(String name) {
        return facultyMapper.selectFacultyByName(name);
    }

    @Override
    public Integer saveFaculty(Faculty faculty) {
        Integer row = facultyMapper.saveFaculty(faculty);
        if (row <= 0){
            throw new RuntimeException(ResultCode.COMMON_FAIL.getMessage());
        }
        return row;
    }

    @Override
    public Faculty getFacultyDataById(Integer fcId) {
        return facultyMapper.selectFacultyById(fcId);
    }

    /**
     * 调用分页插件完成分页
     * @param pageEntity
     * @return
     */
    private PageInfo<Faculty> getPageInfo(PageEntity pageEntity) {
        int pageNum = pageEntity.getPage();
        int pageSize = pageEntity.getLimit();
        PageHelper.startPage(pageNum, pageSize);
        List<Faculty> facultyList = facultyMapper.getFacultyData();
        return new PageInfo<>(facultyList);
    }

    /**
     * 调用分页插件完成模糊搜索分页
     * @param searchDTO
     * @return
     */
    private PageInfo<Faculty> getPageLikeInfo(SearchDTO searchDTO) {
        int pageNum = searchDTO.getPage();
        int pageSize = searchDTO.getLimit();
        PageHelper.startPage(pageNum, pageSize);
        List<Faculty> faculties = facultyMapper.searchFaculty(searchDTO.getKey());
        return new PageInfo<>(faculties);
    }


}
