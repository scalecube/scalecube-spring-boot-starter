package io.scalecube.spring.example.book;

import io.scalecube.spring.example.domain.BookSet;
import io.scalecube.services.annotations.Service;
import io.scalecube.services.annotations.ServiceMethod;
import reactor.core.publisher.Mono;

@Service("redirect-store")
public interface BookStore {

    @ServiceMethod("read-url")
    Mono<BookSet> findByGenre(String genre);
}
