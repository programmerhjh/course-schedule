package com.neusoft.course.schedule.utils;

import java.lang.reflect.Field;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/29.
 */
public class ObjectUtils {

    /**
     * 判断类中每个属性是否都为空
     *
     * @param o
     * @return
     */
    public static Boolean allFieldIsNULL(Object o){
        try {
            for (Field field : o.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object object = field.get(o);
                if (!org.springframework.util.ObjectUtils.isEmpty(object)) {
                    return false;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 判断类中是否有属性为空
     *
     * @param o
     * @return
     */
    public static Boolean hasFieldIsNULL(Object o){
        try {
            for (Field field : o.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object object = field.get(o);
                if (org.springframework.util.ObjectUtils.isEmpty(object)) {
                    return true;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
}
