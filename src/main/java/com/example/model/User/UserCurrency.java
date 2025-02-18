package com.example.model.User;
import com.example.model.VirtualCurrency;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_currency")
public class UserCurrency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "currency_id", nullable = false)
    private VirtualCurrency virtualCurrency;

    @Column(nullable = false)
    private Long balance;

    //@Column
   // private List<Transaction> transactions;
}
