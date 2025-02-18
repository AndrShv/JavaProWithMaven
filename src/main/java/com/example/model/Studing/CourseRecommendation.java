package com.example.model.Studing;

import com.example.model.User.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "recommendedCourses")
public class CourseRecommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "course_recommendation_courses",
            joinColumns = @JoinColumn(name = "course_recommendation_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> recommendedCourses;

    @Column(length = 500)
    private String reason;
}
