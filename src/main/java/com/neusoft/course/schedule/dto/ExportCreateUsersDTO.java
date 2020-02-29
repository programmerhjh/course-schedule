package com.neusoft.course.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/19.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportCreateUsersDTO {

    private String account;

    private String password;

}
