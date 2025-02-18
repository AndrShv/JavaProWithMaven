package com.example.model.User;

import com.example.model.Studing.Level;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "userLevel")
public class UserLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "level_id", nullable = false)
    private Level level;
    @Column(nullable = false)
    private int levelNumber;

    @Column(nullable = false)
    private int currentExperience;

    @Column(nullable = false)
    private int nextLevelExperience;
}
