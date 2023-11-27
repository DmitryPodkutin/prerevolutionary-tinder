package ru.liga.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "\"user\"")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "telegram_user_id")
    private Long telegramId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "password")
    private String password;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserState userState;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private PageInfo pageInfo;

    public User() {
    }

    public User(Long telegramId, String userName, String password, UserState userState, PageInfo pageInfo) {
        this.telegramId = telegramId;
        this.userName = userName;
        this.password = password;
        this.userState = userState;
        userState.setUser(this);
        this.pageInfo = pageInfo;
        pageInfo.setUser(this);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", telegramId=" + telegramId +
                ", userName='" + userName + '\'' +
                '}';
    }
}
