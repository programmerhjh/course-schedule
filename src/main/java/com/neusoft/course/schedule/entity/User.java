package com.neusoft.course.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/3.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private Integer id;    // 用户表主键ID

    private String name;    // 用户名称

    private String privilege;    // 用户权限标识（0代表普通老师，1代表系部负责人，2代表管理员），默认为0

    private Integer faculty;    // 所属院系ID（管理员没有院系）

    private String account;    // 用户账号

    private String password;    // 用户密码

    private Integer delete;    // 账户是否被禁用

    private Integer complete;    // 账户信息是否完善

    private Date lastlogin;    // 最后登录时间

    private String email;    // 用户邮箱

    private Date createTime;    // 创建时间

    private Date modifyTime;    // 修改时间

    @Override
    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }
        if (!(obj instanceof User)) {
            return false;
        }
        User user = (User)obj;
        initEqual(this, user);

        return ((this.id == null && user.id == null) ? true : user.id.equals(this.id)) && ((this.name == null && user.name == null) ? true : user.name.equals(this.name)) && ((this.account == null && user.account == null) ? true : user.account.equals(this.account)) && ((this.faculty == null && user.faculty == null) ? true : user.faculty.equals(this.faculty)) && ((this.privilege == null && user.privilege == null) ? true : user.privilege.equals(this.privilege)) && ((this.delete == null && user.delete == null) ? true : user.delete.equals(this.delete)) && ((this.email == null && user.email == null) ? true : user.email.equals(this.email)) && ((this.complete == null && user.complete == null) ? true : user.complete.equals(this.complete));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((account == null) ? 0 : account.hashCode());
        result = prime * result + faculty;
        result = prime * result + ((privilege == null) ? 0 : privilege.hashCode());
        result = prime * result + delete;
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        return result;
    }

    private void initEqual(User u1, User u2){
        if (u1.id != null && u2.id == null){
            u2.id = u1.id;
        }
        if (u1.name != null && u2.name == null){
            u2.name = u1.name;
        }
        if (u1.password != null && u2.password == null){
            u2.password = u1.password;
        }
        if (u1.privilege != null && u2.privilege == null){
            u2.privilege = u1.privilege;
        }
        if (u1.faculty != null && u2.faculty == null){
            u2.faculty = u1.faculty;
        }
        if (u1.account != null && u2.account == null){
            u2.account = u1.account;
        }
        if (u1.delete != null && u2.delete == null){
            u2.delete = u1.delete;
        }
        if (u1.complete != null && u2.complete == null){
            u2.complete = u1.complete;
        }
        if (u1.email != null && u2.email == null){
            u2.email = u1.email;
        }
    }

}
