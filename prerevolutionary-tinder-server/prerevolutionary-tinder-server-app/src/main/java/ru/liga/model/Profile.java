package ru.liga.model;

import lombok.Data;
import lombok.ToString;
import ru.liga.enums.Gender;
import ru.liga.enums.SeekingFor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@ToString
@Table(name = "profile")
public class Profile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String name;
    private String descriptionHeader;
    private String description;
    @Enumerated(EnumType.STRING)
    private SeekingFor seeking;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public String descriptionToString() {
        return this.descriptionHeader + "\n\r" + this.description;
    }
}
