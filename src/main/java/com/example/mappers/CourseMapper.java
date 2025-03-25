package com.example.mappers;

import com.example.dto.response.CourseResponse;
import com.example.model.Course;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourseMapper {

    public CourseResponse toResponse(Course course){
        CourseResponse courseResponse = new CourseResponse();
        courseResponse.setId(course.getId());
        courseResponse.setTitle(course.getTitle());
        courseResponse.setDescription(course.getDescription());
        courseResponse.setTeacherUsername(course.getTeacher().getUsername());
        courseResponse.setTeacherEmail(course.getTeacherEmail());
        courseResponse.setCreatedAt(course.getCreatedAt());
        courseResponse.setFinishedTime(course.getFinishedTime());
        courseResponse.setStartedTime(course.getStartedTime());
        courseResponse.setTheme(course.getTheme() != null ? course.getTheme().toString() : null);
        courseResponse.setWay(course.getWay() != null ? course.getWay().toString() : null);
        return courseResponse;
    }

    public List<CourseResponse> toResponseList(List <Course> courses){
        return courses.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
