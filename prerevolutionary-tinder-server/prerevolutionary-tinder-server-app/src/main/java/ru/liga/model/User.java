package ru.liga.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "telegram_user_id")
    private Long telegramUserId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "password")
    private String password;
}