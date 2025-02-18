package com.example.services;

import com.example.dto.request.CourseRequest;
import com.example.dto.response.CourseResponse;
import com.example.mappers.CourseMapper;
import com.example.model.Studing.Course;
import com.example.model.User.User;
import com.example.rabbitMqConfigs.NotificationService;
import com.example.repository.studying.CourseRepository;
import com.example.repository.users.UserRepository;
import com.example.service.studying.CourseService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseMapper courseMapper;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private CourseService courseService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    void testCreateCourse_Success() {

        CourseRequest request = new CourseRequest();
        request.setTitle("Java Programming");
        request.setDescription("Test description");

        User teacher = new User(userId);
        teacher.setUsername("teacher1");
        teacher.setEmail("teacher1@example.com");

        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setTeacher(teacher);

        CourseResponse expectedResponse = new CourseResponse();
        expectedResponse.setTitle("Java Programming");

        when(userRepository.findByUsername("teacher1")).thenReturn(Optional.of(teacher));
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(courseMapper.toResponse(course)).thenReturn(expectedResponse);


        CourseResponse response = courseService.createCourse(request, "teacher1");


        assertNotNull(response);
        assertEquals("Java Programming", response.getTitle());
        verify(notificationService, times(1)).sendAsyncNotification(eq("teacher1@example.com"), anyString(), anyString());
    }

    @Test
    void testCreateCourse_InvalidTeacher() {
        CourseRequest request = new CourseRequest();
        request.setTitle("Java Programming");
        request.setDescription("Test description");

        when(userRepository
                .findByUsername("teacher1"))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> courseService.createCourse(request, "teacher1"));
    }

    @Test
    void testUpdateCourse_Success() {
        // Arrange
        CourseRequest request = new CourseRequest();
        request.setTitle("Updated Java Programming");
        request.setDescription("Updated Test description");

        User teacher = new User(userId);
        teacher.setUsername("teacher1");

        Course course = new Course();
        course.setTitle("Java Programming");
        course.setTeacher(teacher);

        CourseResponse expectedResponse = new CourseResponse();
        expectedResponse.setTitle("Updated Java Programming");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseMapper.toResponse(course)).thenReturn(expectedResponse);

        // Act
        CourseResponse response = courseService.updateCourse(1L, request, "teacher1");

        // Assert
        assertNotNull(response);
        assertEquals("Updated Java Programming", response.getTitle());
    }

    @Test
    void testUpdateCourse_NotAuthorized() {
        User teacher = new User(userId);
        teacher.setUsername("teacher1");

        User anotherTeacher = new User(userId);
        anotherTeacher.setUsername("teacher2");

        Course course = new Course();
        course.setTeacher(anotherTeacher);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));


        AccessDeniedException exception = assertThrows(AccessDeniedException.class,
                () -> courseService.updateCourse(1L, new CourseRequest(), "teacher1"));
        assertEquals("You are not allowed to update this course", exception.getMessage());
    }

    @Test
    void testDeleteCourse_Success() {

        User teacher = new User(userId);
        teacher.setUsername("teacher1");

        Course course = new Course();
        course.setTeacher(teacher);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));


        courseService.deleteCourse(1L, "teacher1");


        verify(courseRepository, times(1)).delete(course);
    }

    @Test
    void testDeleteCourse_NotAuthorized() {

        User teacher = new User(userId);
        teacher.setUsername("teacher1");

        User anotherTeacher = new User(userId);
        anotherTeacher.setUsername("teacher2");

        Course course = new Course();
        course.setTeacher(anotherTeacher);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () ->
                courseService.deleteCourse(1L, "teacher1"));
        assertEquals("You are not allowed to delete this course", exception.getMessage());
    }

    @Test
    void testGetAllCourses() {

        List<Course> courses = List.of(new Course(), new Course());
        when(courseRepository.findAll()).thenReturn(courses);
        when(courseMapper.toResponseList(courses)).thenReturn(List.of(new CourseResponse(), new CourseResponse()));


        List<CourseResponse> responses = courseService.getAllCourses();

        assertEquals(2, responses.size());
    }

    @Test
    void testGetCourseById_Success() {

        Course course = new Course();
        course.setTitle("Java Basics");

        CourseResponse expectedResponse = new CourseResponse();
        expectedResponse.setTitle("Java Basics");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseMapper.toResponse(course)).thenReturn(expectedResponse);


        CourseResponse response = courseService.getCourseById(1L);


        assertNotNull(response);
        assertEquals("Java Basics", response.getTitle());
    }


    @Test
    void testGetCourseById_NotFound() {

        when(courseRepository.findById(1L)).thenReturn(Optional.empty());


        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                courseService.getCourseById(1L));
        assertEquals("Course not found", exception.getMessage());
    }
}
