package com.example.controllers;

import com.example.controller.course.CourseController;
import com.example.dto.request.CourseRequest;
import com.example.dto.response.CourseResponse;
import com.example.service.studying.CourseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;

    private CourseRequest courseRequest;

    @BeforeEach
    void setUp() {
        courseRequest = new CourseRequest();
        courseRequest.setTitle("Java Basics");
        courseRequest.setDescription("Introduction to Java");
    }

    @Test
    @WithMockUser(username = "teacher1", roles = {"TEACHER"})
    void testCreateCourse() throws Exception {

        CourseResponse courseResponse = new CourseResponse();

        when(courseService.createCourse(any(), eq("teacher1"))).thenReturn(courseResponse);

        mockMvc.perform(post("/api/courses/create-course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseRequest)))
                .andExpect(status().isCreated()) // Ожидаем код ответа 201
                .andExpect(jsonPath("$.title").value("Java Basics"))
                .andExpect(jsonPath("$.description").value("Introduction to Java"))
                .andExpect(jsonPath("$.teacherUsername").value("teacher1"));

        verify(courseService, times(1)).createCourse(any(), eq("teacher1"));
    }

    @Test
    @WithMockUser(username = "teacher1", roles = {"TEACHER"})
    void testUpdateCourse() throws Exception {
        // Подготовка данных
        CourseResponse courseResponse = new CourseResponse();

        when(courseService.updateCourse(eq(1L), any(), eq("teacher1"))).thenReturn(courseResponse);

        mockMvc.perform(put("/api/courses/{courseId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseRequest)))
                .andExpect(status().isOk()) // Ожидаем код ответа 200
                .andExpect(jsonPath("$.title").value("Java Basics"))
                .andExpect(jsonPath("$.description").value("Updated Description"));

        verify(courseService, times(1)).updateCourse(eq(1L), any(), eq("teacher1"));
    }

    @Test
    @WithMockUser(username = "teacher1", roles = {"TEACHER"})
    void testDeleteCourse() throws Exception {
        mockMvc.perform(delete("/api/courses/{courseId}", 1L))
                .andExpect(status().isNoContent()); // Ожидаем код ответа 204

        verify(courseService, times(1)).deleteCourse(eq(1L), eq("teacher1"));
    }

    @Test
    @WithMockUser(username = "student1", roles = {"STUDENT"})
    void testGetAllCourses() throws Exception {

        CourseResponse courseResponse = new CourseResponse();

        when(courseService.getAllCourses()).thenReturn(List.of(courseResponse));

        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk()) // Ожидаем код ответа 200
                .andExpect(jsonPath("$[0].title").value("Java Basics"))
                .andExpect(jsonPath("$[0].description").value("Introduction to Java"));

        verify(courseService, times(1)).getAllCourses();
    }

    @Test
    @WithMockUser(username = "student1", roles = {"STUDENT"})
    void testGetCourseById() throws Exception {

        CourseResponse courseResponse = new CourseResponse();

        when(courseService.getCourseById(1L)).thenReturn(courseResponse);

        mockMvc.perform(get("/api/courses/{courseId}", 1L))
                .andExpect(status().isOk()) // Ожидаем код ответа 200
                .andExpect(jsonPath("$.title").value("Java Basics"))
                .andExpect(jsonPath("$.description").value("Introduction to Java"));

        verify(courseService, times(1)).getCourseById(1L);
    }

    @Test
    void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isUnauthorized());
    }
}
