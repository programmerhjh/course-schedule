package com.neusoft.course.schedule.biz;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/14.
 */
@RequestMapping(value = "course/schedule")
@Controller
public class CourseScheduleBiz {

    @GetMapping(value = "csUI")
    public ModelAndView csUI(){
        ModelAndView mv = new ModelAndView("course-schedule/csUI");
        return mv;
    }

}
