package com.neusoft.course.schedule.mapper;

import com.neusoft.course.schedule.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/3.
 */
public interface UserMapper {

    /**
     * 查询用户列表
     * @return
     */
    List<User> selectUserList();

    /**
     * 查询用户账号信息
     * @param account
     * @return
     */
    User selectUserByAccount(@Param("account") String account);

    /**
     * 查询用户账号信息 (包含密码)
     * @param account
     * @return
     */
    User selectUserPasswordByAccount(@Param("account") String account);

    /**
     * 修改用户信息上次登录信息
     * @param user
     * @return
     */
    Integer updateUserLastLogin(User user);

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    Integer updateUserPassword(User user);

    /**
     * 批量插入用户
     * @param users
     * @return
     */
    Integer batchInsertUser(@Param("users") List<User> users);

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
    Integer batchDeleteUsers(@Param("idList") List<Integer> idList);

    /**
     * 批量禁用指定用户
     * @param userList
     * @return
     */
    Integer batchForbiddenUsers(@Param("userList") List<User> userList);

    /**
     * 查找是否存在该用户
     * @param param 参数可为用户名或者账号
     * @return
     */
    User selectUserByNameOrAccount(@Param(value = "param") String param);

    /**
     * 模糊搜索用户，包含用户名，账户，邮箱
     * @param key
     * @return
     */
    List<User> searchUser(String key);

    /**
     * 查询用户表中的字段列表
     * @return
     */
    List<String> getExcelConfig();

    /**
     * 根据主键查询用户
     * @param userId
     * @return
     */
    User selectUserByUserId(@Param("userId") Integer userId);
}
