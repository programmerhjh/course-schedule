package com.neusoft.course.schedule.dto;

import com.neusoft.course.schedule.entity.Faculty;
import com.neusoft.course.schedule.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 用户列表DTO
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchDTO implements Serializable{

    private String key;

    private Integer page;

    private Integer limit;

}
