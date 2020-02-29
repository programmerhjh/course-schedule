package com.neusoft.course.schedule.biz;

import com.neusoft.course.schedule.entity.User;
import com.neusoft.course.schedule.enums.ResultCode;
import com.neusoft.course.schedule.service.UserService;
import com.neusoft.course.schedule.utils.JsonResult;
import com.neusoft.course.schedule.utils.ResultGeneratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/3.
 */
@Controller
@RequestMapping(value = "user")
public class UserBiz {

    @Autowired
    private UserService userService;

    @GetMapping(value = "loginUI")
    public String loginUI() {
        return "login/loginUI";
    }

    @PostMapping(value = "checkAccountWithEmail")
    @ResponseBody
    public JsonResult checkAccountWithEmail(@RequestBody User user) {
        User check = userService.selectUserByAccount(user.getAccount());
        if (ObjectUtils.isEmpty(check)) {
            return ResultGeneratorUtils.fail(ResultCode.USER_ACCOUNT_NOT_EXIST);
        } else if (StringUtils.isEmpty(check.getEmail())){
            return ResultGeneratorUtils.fail(ResultCode.FORGET_EMAIL);
        } else if (!check.getEmail().equals(user.getEmail())){
            return ResultGeneratorUtils.fail(ResultCode.USER_EMAIL_ERROR);
        }
        return ResultGeneratorUtils.success();
    }

    @PostMapping(value = "updateNewPassword")
    @ResponseBody
    public JsonResult updateNewPassword(@RequestBody User user){
        if (userService.updateUserNewPassword(user) < 1){
            return ResultGeneratorUtils.fail(ResultCode.COMMON_FAIL);
        }
        return ResultGeneratorUtils.success(ResultCode.UPDATE_PASSWORD_SUCCESS);
    }

}
