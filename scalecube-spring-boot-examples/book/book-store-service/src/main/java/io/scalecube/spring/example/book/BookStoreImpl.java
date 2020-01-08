package io.scalecube.spring.example.book;

import io.scalecube.spring.example.domain.Book;
import io.scalecube.spring.example.domain.BookSet;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BookStoreImpl implements BookStore {

    private final Map<String, List<Book>> store;

    public BookStoreImpl(Map<String, List<Book>> store) {
        this.store = store;
    }

    @Override
    public Mono<BookSet> findByGenre(String genre) {
        BookSet bookSet = BookSet.of(store.get(genre));
        return Mono.justOrEmpty(bookSet);
    }
}
