package ru.liga.dto.custompage;

import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CustomPage<T> implements Serializable {

    private final List<T> content = new ArrayList<>();
    private int totalElements;
    private int page;
    private int totalPages;

    public CustomPage() {

    }

    public CustomPage(List<T> content) {
        this.content.addAll(content);
    }

    public CustomPage(Page page, List<T> content) {
        this(page);
        this.content.addAll(content);
    }

    public CustomPage(Page page) {
        this.totalElements = (int) page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.page = page.getNumber();
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<? extends T> content) {
        this.content.addAll(content);
    }

    public int getSize() {
        return content.size();
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int total) {
        this.totalElements = total;
    }

    public int getOffset() {
        return page * content.size();
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public boolean isLast() {
        return page == totalPages - 1;
    }

    public boolean isFirst() {
        return page == 0;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
