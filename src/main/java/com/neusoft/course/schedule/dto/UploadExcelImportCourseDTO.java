package com.neusoft.course.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/29.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadExcelImportCourseDTO implements Serializable {

    private Integer fcId;

    private Integer termId;

    private MultipartFile courseExcel;

}
