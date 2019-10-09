package io.scalecube.spring.example.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class BookSet {

  private final List<Book> books;

  private BookSet(List<Book> books) {
    this.books = books == null || books.isEmpty() ? Collections.emptyList()
        : new ArrayList<>(books);
  }

  @JsonCreator
  public static BookSet of(List<Book> books) {
    return books == null ? null : new BookSet(books);
  }

  @JsonValue
  public List<Book> books() {
    return books;
  }

  public Stream<Book> stream() {
    return books.stream();
  }

  @JsonIgnore
  public boolean isEmpty() {
    return books.isEmpty();
  }
}
