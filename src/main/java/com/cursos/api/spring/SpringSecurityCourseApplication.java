package com.cursos.api.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// @EnableSpringDataWebSupport( pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO )
public class SpringSecurityCourseApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityCourseApplication.class, args);
	}

}