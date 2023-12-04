package ru.liga.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "page_info")
public class PageInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "search_profile_current_page")
    private int searchProfileCurrentPage;

    @Column(name = "favorite_current_page")
    private int favoriteCurrentPage;

    public PageInfo() {
    }

    public PageInfo(int searchProfileCurrentPage, int favoriteCurrentPage) {
        this.searchProfileCurrentPage = searchProfileCurrentPage;
        this.favoriteCurrentPage = favoriteCurrentPage;
    }
}

