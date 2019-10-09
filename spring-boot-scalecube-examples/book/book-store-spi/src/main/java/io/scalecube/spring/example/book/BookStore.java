package io.scalecube.spring.example.book;

import io.scalecube.spring.example.domain.BookSet;
import io.scalecube.services.annotations.Service;
import io.scalecube.services.annotations.ServiceMethod;
import reactor.core.publisher.Mono;

@Service("book-store")
public interface BookStore {

    @ServiceMethod("find-book-by-genre")
    Mono<BookSet> findByGenre(String genre);
}
