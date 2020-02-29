package com.neusoft.course.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/20.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchTermDTO extends SearchDTO implements Serializable{

    private Integer fcId;    // 院系ID

}
