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
        return this.id.equals(user.id) && (this.name == null ? user.name == null : this.name.equals(user.name)) && this.account.equals(user.account) && this.faculty.equals(user.faculty) && this.privilege.equals(user.privilege) && this.delete == user.delete && (this.email == null ? user.email == null : this.email.equals(user.email));
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

}
