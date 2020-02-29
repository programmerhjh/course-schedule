package com.neusoft.course.schedule.utils;

import com.neusoft.course.schedule.constants.ServiceConstants;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/15.
 */
public class AuthorityStringUtils {

    /**
     * 返回适用前台的权限标识
     * @param authorities
     * @return
     */
    public static ArrayList<String> generatorAuthoritiesString(Collection<GrantedAuthority> authorities){
        ArrayList<String> privileges = new ArrayList<>(authorities.size());
        for (Iterator<GrantedAuthority> iterator = authorities.iterator();iterator.hasNext();) {
            GrantedAuthority next = iterator.next();
            if (next.getAuthority().equals(ServiceConstants.PRIVILEGE_TEACHER)){
                privileges.add("0");
            }
            if (next.getAuthority().equals(ServiceConstants.PRIVILEGE_DEPARTMENT_MAN)){
                privileges.add("1");
            }
            if (next.getAuthority().equals(ServiceConstants.PRIVILEGE_ADMIN)){
                privileges.add("2");
            }
        }
        return privileges;
    }

}
