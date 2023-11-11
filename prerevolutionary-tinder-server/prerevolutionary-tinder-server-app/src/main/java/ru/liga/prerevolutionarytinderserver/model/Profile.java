package ru.liga.prerevolutionarytinderserver.model;

import lombok.Data;
import ru.liga.prerevolutionarytinderserver.enums.Seeking;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "profile")
public class Profile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String gender;
    private String name;
    private String descriptionHeader;
    private String description;
    private Seeking seeking;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}