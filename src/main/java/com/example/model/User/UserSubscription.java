package com.example.model.User;

import com.example.model.Tournament.TournamentChallenge;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "subscription")
public class UserSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private User user;
    @Column(nullable = false)
    private TournamentChallenge challenge;
    @Column(nullable = false)
    private String dateSubscribed;
}
