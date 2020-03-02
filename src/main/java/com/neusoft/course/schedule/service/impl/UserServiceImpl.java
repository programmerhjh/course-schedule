package com.neusoft.course.schedule.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neusoft.course.schedule.constants.ServiceConstants;
import com.neusoft.course.schedule.dto.BatchCreateUserDTO;
import com.neusoft.course.schedule.dto.SearchDTO;
import com.neusoft.course.schedule.entity.Faculty;
import com.neusoft.course.schedule.entity.PageEntity;
import com.neusoft.course.schedule.entity.PageResult;
import com.neusoft.course.schedule.entity.User;
import com.neusoft.course.schedule.enums.ResultCode;
import com.neusoft.course.schedule.mapper.UserMapper;
import com.neusoft.course.schedule.service.ExcelService;
import com.neusoft.course.schedule.service.FacultyService;
import com.neusoft.course.schedule.service.UserService;
import com.neusoft.course.schedule.utils.BeanCopierUtils;
import com.neusoft.course.schedule.utils.PageUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/3.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ExcelService excelService;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User selectUserByAccount(String account) {
        return userMapper.selectUserByAccount(account);
    }

    @Override
    public User selectUserPasswordByAccount(String account) {
        return userMapper.selectUserPasswordByAccount(account);
    }

    @Override
    public Integer updateUserLastLogin(User user) {
        return userMapper.updateUserLastLogin(user);
    }

    @Override
    public Integer updateUserNewPassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.updateUserPassword(user);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    @Override
    public SXSSFWorkbook randomGeneratorUsers(BatchCreateUserDTO batchCreateUserDTO) {
        Integer num = batchCreateUserDTO.getNum();
        String password = batchCreateUserDTO.getPassword();
        String privilege = batchCreateUserDTO.getPrivilege();
        if (num <= 0 && num >= Byte.MAX_VALUE) {
            throw new RuntimeException(ResultCode.PARAM_NOT_VALID.getMessage());
        }
        List<User> users = new ArrayList<>(num);
        List<User> forExcelUsers = new ArrayList<>(num);
        StringBuilder builder = new StringBuilder();
        HashSet<String> accountSet = new HashSet<>(num);
        Random random = new Random();
        String encode = passwordEncoder.encode(password);
        for (int i=0;i<num;i++){
            builder.append(String.valueOf(System.currentTimeMillis()));
            builder.append(String.valueOf(random.nextInt(Short.MAX_VALUE)));
            String account = builder.toString();
            builder.setLength(0);
            if (accountSet.contains(account)) {
                i --;
                continue;
            }
            accountSet.add(account);
            User user = new User();
            user.setAccount(account);
            user.setPassword(encode);
            user.setPrivilege(privilege);
            user.setDelete(0);
            user.setComplete(0);
            users.add(user);
            // 构造导出用户Excel返参
            User forExcelUser = new User();
            BeanCopierUtils.copy(user, forExcelUser);
            forExcelUser.setPassword(password);
            forExcelUsers.add(forExcelUser);
        }
        int rows = userMapper.batchInsertUser(users);
        if (rows != num) {
            throw new RuntimeException(ResultCode.COMMON_FAIL.getMessage());
        }
        SXSSFWorkbook workbook = excelService.createAndDownloadUsersExcel(forExcelUsers);
        return workbook;
    }

    @Override
    public PageResult<User> getUserListData(PageEntity pageEntity) {
        if (ObjectUtils.isEmpty(pageEntity.getPage())){
            PageResult<User> pageResult = new PageResult<>();
            List<User> users = userMapper.selectUserList();
            pageResult.setData(users);
            return pageResult;
        }
        PageResult pageResult = PageUtils.getPageResult(getPageInfo(pageEntity));
        setFacultyData(pageResult);
        return pageResult;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    @Override
    public Integer saveUser(User user) {
        // complete 为1代表正在完善资料
        if (!ObjectUtils.isEmpty(user.getComplete()) && user.getComplete().equals(1)){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            Integer row = userMapper.saveUser(user);
            if (row <= 0){
                throw new RuntimeException(ResultCode.COMMON_FAIL.getMessage());
            }
            return row;
        }
        // 设置剩余的sql参数，密码在新增用户的时候会插入，更改的时候不会修改用户密码
        user.setPassword(passwordEncoder.encode(ServiceConstants.DEFAULT_PASSWORD));
        user.setComplete(0);
        Integer row = userMapper.saveUser(user);
        if (row <= 0){
            throw new RuntimeException(ResultCode.COMMON_FAIL.getMessage());
        }
        return row;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    @Override
    public Integer deleteUser(User user) {
        Integer row = userMapper.deleteUser(user);
        if (row <= 0){
            throw new RuntimeException(ResultCode.COMMON_FAIL.getMessage());
        }
        return row;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    @Override
    public Integer forbiddenUser(User user) {
        Integer row = userMapper.forbiddenUser(user);
        if (row <= 0){
            throw new RuntimeException(ResultCode.COMMON_FAIL.getMessage());
        }
        return row;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    @Override
    public Integer batchDeleteUsers(List<Integer> idList) {
        Integer row = userMapper.batchDeleteUsers(idList);
        // mybatis 批量事务操作的 BATCH 执行器无法获取事务操作条数
        return row;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    @Override
    public Integer batchForbiddenUsers(List<User> userList) {
        Integer row = userMapper.batchForbiddenUsers(userList);
        return row;
    }

    @Override
    public User checkUserIsRepeat(String param) {
        return userMapper.selectUserByNameOrAccount(param);
    }

    @Override
    public PageResult<User> searchUser(SearchDTO searchDTO) {
        PageResult pageResult = PageUtils.getPageResult(getPageLikeInfo(searchDTO));
        setFacultyData(pageResult);
        return pageResult;
    }

    @Override
    public PageResult<User> searchFacultyUser(Integer fcId, SearchDTO searchDTO) {
        PageResult pageResult = PageUtils.getPageResult(getPageLikeInfoWithFcId(fcId, searchDTO));
        setFacultyData(pageResult);
        return pageResult;
    }

    @Override
    public PageResult<User> getUserListDataByFacultyId(Integer fcId, PageEntity pageEntity) {
        if (ObjectUtils.isEmpty(pageEntity.getPage())){
            PageResult<User> pageResult = new PageResult<>();
            List<User> users = userMapper.selectUserListByFacultyId(fcId);
            pageResult.setData(users);
            return pageResult;
        }
        PageResult pageResult = PageUtils.getPageResult(getPageInfoWithFcId(fcId, pageEntity));
        setFacultyData(pageResult);
        return pageResult;
    }

    /**
     * 调用分页插件完成分页
     * @param pageEntity
     * @return
     */
    private PageInfo<User> getPageInfo(PageEntity pageEntity) {
        int pageNum = pageEntity.getPage();
        int pageSize = pageEntity.getLimit();
        PageHelper.startPage(pageNum, pageSize);
        List<User> users = userMapper.selectUserList();
        return new PageInfo<>(users);
    }

    /**
     * 调用分页插件完成分页
     * @param fcId
     * @param pageEntity
     * @return
     */
    private PageInfo<User> getPageInfoWithFcId(Integer fcId, PageEntity pageEntity) {
        int pageNum = pageEntity.getPage();
        int pageSize = pageEntity.getLimit();
        PageHelper.startPage(pageNum, pageSize);
        List<User> users = userMapper.selectUserListByFacultyId(fcId);
        return new PageInfo<>(users);
    }

    /**
     * 调用分页插件完成模糊搜索分页
     * @param searchDTO
     * @return
     */
    private PageInfo<User> getPageLikeInfo(SearchDTO searchDTO) {
        int pageNum = searchDTO.getPage();
        int pageSize = searchDTO.getLimit();
        PageHelper.startPage(pageNum, pageSize);
        List<User> users = userMapper.searchUser(searchDTO.getKey());
        return new PageInfo<>(users);
    }

    /**
     * 调用分页插件完成模糊搜索分页
     * @param fcId
     * @param searchDTO
     * @return
     */
    private PageInfo<User> getPageLikeInfoWithFcId(Integer fcId, SearchDTO searchDTO) {
        int pageNum = searchDTO.getPage();
        int pageSize = searchDTO.getLimit();
        PageHelper.startPage(pageNum, pageSize);
        List<User> users = userMapper.searchFacultyUser(fcId, searchDTO.getKey());
        return new PageInfo<>(users);
    }

    /**
     * 设置用户列表额外需要的院系数据
     * @param pageResult
     */
    private void setFacultyData(PageResult pageResult){
        List<Faculty> facultyData = facultyService.getFacultyData();
        HashMap<Integer, String> facultyMap = new HashMap<>(facultyData.size());
        for (Faculty f: facultyData) {
            facultyMap.put(f.getId(), f.getName());
        }
        pageResult.setExtendField(facultyMap);
    }

}
