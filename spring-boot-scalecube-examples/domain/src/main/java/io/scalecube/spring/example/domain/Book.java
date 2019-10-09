package io.scalecube.spring.example.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Book {

    private final String name;
    private final String author;

    @JsonCreator
    public Book(@JsonProperty("name") String name, @JsonProperty("author") String author) {
        this.name = name;
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }
}
