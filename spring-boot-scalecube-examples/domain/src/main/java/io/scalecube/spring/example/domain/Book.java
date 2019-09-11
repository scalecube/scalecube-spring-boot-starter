package io.scalecube.spring.example.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public class Book {

    private final String name;
    private final String author;

    @JsonCreator
    public Book(String name, String author) {
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
