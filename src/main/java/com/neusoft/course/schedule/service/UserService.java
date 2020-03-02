package com.neusoft.course.schedule.service;

import com.neusoft.course.schedule.dto.BatchCreateUserDTO;
import com.neusoft.course.schedule.dto.SearchDTO;
import com.neusoft.course.schedule.entity.PageEntity;
import com.neusoft.course.schedule.entity.PageResult;
import com.neusoft.course.schedule.entity.User;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.List;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/3.
 */
public interface UserService {

    /**
     * 查询用户账号是否存在
     * @param account
     * @return
     */
    User selectUserByAccount(String account);


    /**
     * 查询用户信息（包含密码）
     * @param account
     * @return
     */
    User selectUserPasswordByAccount(String account);

    /**
     * 修改当前用户最后登录时间
     * @param user
     * @return
     */
    Integer updateUserLastLogin(User user);

    /**
     * 修改用户密码
     * @param user
     * @return
     */
    Integer updateUserNewPassword(User user);

    /**
     * 随机生成指定个数的用户
     * @param batchCreateUserDTO 一次最多生成200个用户, 指定初始密码
     * @return
     */
    SXSSFWorkbook randomGeneratorUsers(BatchCreateUserDTO batchCreateUserDTO);

    /**
     * 获取用户列表数据
     * @param pageEntity
     * @return
     */
    PageResult<User> getUserListData(PageEntity pageEntity);

    /**
     * 保存用户信息
     * @param user
     * @return
     */
    Integer saveUser(User user);

    /**
     * 删除指定用户
     * @param user
     * @return
     */
    Integer deleteUser(User user);

    /**
     * 禁用指定用户
     * @param user
     * @return
     */
    Integer forbiddenUser(User user);

    /**
     * 批量删除指定用户
     * @param idList
     * @return
     */
    Integer batchDeleteUsers(List<Integer> idList);

    /**
     * 批量禁用指定用户
     * @param userList
     * @return
     */
    Integer batchForbiddenUsers(List<User> userList);

    /**
     * 查找是否存在该用户名的用户
     * @param param 可为账号或者用户名
     * @return
     */
    User checkUserIsRepeat(String param);

    /**
     * 模糊搜索key相关的用户（包含name，account，email）
     * @param searchDTO
     * @return
     */
    PageResult<User> searchUser(SearchDTO searchDTO);

    /**
     * 模糊搜索院系下key相关的用户（包含name，account，email）
     * @param fcId
     * @param searchDTO
     * @return
     */
    PageResult<User> searchFacultyUser(Integer fcId, SearchDTO searchDTO);

    /**
     * 查询院系下的用户
     * @param fcId
     * @param pageEntity
     * @return
     */
    PageResult<User> getUserListDataByFacultyId(Integer fcId, PageEntity pageEntity);
}
