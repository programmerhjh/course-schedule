package com.neusoft.course.schedule.service;

import com.neusoft.course.schedule.dto.SearchDTO;
import com.neusoft.course.schedule.entity.Faculty;
import com.neusoft.course.schedule.entity.PageEntity;
import com.neusoft.course.schedule.entity.PageResult;

import java.util.List;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/16.
 */
public interface FacultyService {

    /**
     * 分页获取系部信息
     * @param pageEntity 为null则返回全部数据
     * @return
     */
    PageResult<Faculty> getFacultyData(PageEntity pageEntity);

    /**
     * 获取全部院系数据
     * @return
     */
    List<Faculty> getFacultyData();

    /**
     * 删除指定院系
     * @param faculty
     * @return
     */
    Integer deleteFaculty(Faculty faculty);

    /**
     * 批量删除指定院系
     * @param idList
     * @return
     */
    Integer batchDeleteFaculties(List<Integer> idList);

    /**
     * 模糊搜索key相关的院系
     * @param searchDTO
     * @return
     */
    PageResult<Faculty> searchFaculty(SearchDTO searchDTO);

    /**
     * 验证院系名称是否重复
     * @param name
     * @return
     */
    Faculty checkFacultyIsRepeat(String name);

    /**
     * 保存院系
     * @param faculty
     * @return
     */
    Integer saveFaculty(Faculty faculty);

}
