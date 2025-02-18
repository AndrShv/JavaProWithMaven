package com.example.model.Studing;
import com.example.courseDetails.CourseLevel;
import com.example.model.Homework.Homework;
import com.example.model.User.Teacher;
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
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private CourseLevel level;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @OneToMany(mappedBy = "course")
    private List<User> students;

    @OneToMany(mappedBy = "course")
    private List<Homework> homeworks;
}
