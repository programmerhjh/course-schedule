package com.neusoft.course.schedule.biz;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/16.
 */
@RequestMapping(value = "leadership")
@Controller
public class LeadershipBiz {

    @GetMapping("termListUI")
    public String termListUI(){
        return "term/leadershipTermList";
    }

    @GetMapping("applyListUI")
    public String applyListUI(){
        return "apply/leadershipApplyList";
    }

    @GetMapping("courseListUI")
    public String courseListUI(){
        return "course/leadershipCourseList";
    }

    @GetMapping("userListUI")
    public String userListUI(){
        return "user/leadershipUserList";
    }

    @GetMapping(value = "excelExportUI")
    public String excelExportUI(){
        return "excel/dm-excel-export-popup";
    }

    @GetMapping(value = "excelImportUI")
    public String excelImportUI(){
        return "excel/dm-excel-import-popup";
    }

}
