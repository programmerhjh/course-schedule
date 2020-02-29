package com.neusoft.course.schedule.mapper;

import com.neusoft.course.schedule.entity.Faculty;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/3.
 */

public interface FacultyMapper {

    /**
     * 获取院系数据
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
    Integer batchDeleteFaculties(@Param("idList") List<Integer> idList);

    /**
     * 模糊搜索院系
     * @param key
     * @return
     */
    List<Faculty> searchFaculty(String key);

    /**
     * 通过院系名称搜索对应院系
     * @param name
     * @return
     */
    Faculty selectFacultyByName(String name);

    /**
     * 保存一个院系
     * @param faculty
     * @return
     */
    Integer saveFaculty(Faculty faculty);

    /**
     * 根据ID查找院系
     * @param id
     * @return
     */
    Faculty selectFacultyById(@Param("id") Integer id);
}
