package com.neusoft.course.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/28.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchApplyDTO extends SearchDTO implements Serializable {

    private Integer fcId;

    private Integer termId;

}
