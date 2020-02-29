package com.neusoft.course.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/24.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCourseDTO extends SearchDTO {

    private Integer fcId;

    private Integer termId;

}
