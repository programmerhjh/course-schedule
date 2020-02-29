package com.neusoft.course.schedule;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * @author 洪家豪
 * @date 2020年2月1日16:48:18
 */
@SpringBootApplication
@MapperScan(value = {"com.neusoft.course.schedule.mapper"})
@EnableTransactionManagement
public class CourseScheduleApplication {

	public static void main(String[] args) {
		SpringApplication.run(CourseScheduleApplication.class, args);
	}

}
