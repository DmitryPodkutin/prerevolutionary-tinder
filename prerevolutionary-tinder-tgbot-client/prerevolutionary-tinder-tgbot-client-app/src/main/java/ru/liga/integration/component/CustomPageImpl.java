package ru.liga.integration.component;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom implementation of Spring Data's PageImpl with JSON properties support.
 *
 * @param <T> Type of content in the page
 */
public class CustomPageImpl<T> extends PageImpl<T> {

    private static final long serialVersionUID = 3248189030448292002L;

    /**
     * Constructor for deserialization with JSON properties.
     *
     * @param content          Content of the page
     * @param number           Page number
     * @param size             Page size
     * @param totalElements    Total elements
     * @param pageable         Pageable information as JSON
     * @param last             Flag indicating whether it's the last page
     * @param totalPages       Total number of pages
     * @param sort             Sort information as JSON
     * @param first            Flag indicating whether it's the first page
     * @param numberOfElements Number of elements in this page
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CustomPageImpl(@JsonProperty("content") List<T> content, @JsonProperty("number") int number,
                          @JsonProperty("size") int size,
                          @JsonProperty("totalElements") Long totalElements,
                          @JsonProperty("pageable") JsonNode pageable, @JsonProperty("last") boolean last,
                          @JsonProperty("totalPages") int totalPages, @JsonProperty("sort") JsonNode sort,
                          @JsonProperty("first") boolean first,
                          @JsonProperty("numberOfElements") int numberOfElements) {
        super(content, PageRequest.of(number, size), totalElements);
    }

    /**
     * Constructor for creating a CustomPageImpl with the provided content and pageable information.
     *
     * @param content  Content of the page
     * @param pageable Pageable information
     * @param total    Total number of elements
     */
    public CustomPageImpl(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    /**
     * Constructor for creating a CustomPageImpl with the provided content.
     *
     * @param content Content of the page
     */
    public CustomPageImpl(List<T> content) {
        super(content);
    }


    /**
     * Default constructor creating an empty CustomPageImpl.
     */
    public CustomPageImpl() {
        super(new ArrayList<T>());
    }

}

