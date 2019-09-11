package io.scalecube.spring.example.rating;

import io.scalecube.spring.example.domain.Book;
import io.scalecube.spring.example.domain.BookSet;
import io.scalecube.spring.example.domain.Sale;
import io.scalecube.spring.example.book.BookStore;
import io.scalecube.spring.example.sales.SalesStore;
import io.scalecube.services.routing.RandomServiceRouter;
import java.net.URI;
import java.util.Map;
import org.springframework.boot.scalecube.beans.SelectionStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@RestController
public class GreetingRestController {

  // external client
  private final BookStore bookStore;

  // external client with custom router
  @SelectionStrategy(routerType = RandomServiceRouter.class)
  private final SalesStore salesStore;

  // internal service
  private final DestinationProvider destinationProvider;

  public GreetingRestController(
      BookStore bookStore, SalesStore salesStore, DestinationProvider destinationProvider) {
    this.bookStore = bookStore;
    this.salesStore = salesStore;
    this.destinationProvider = destinationProvider;
  }

  @GetMapping("external/{path}")
  public Mono<Map<String, Long>> print1(@PathVariable String genre) {
    return bookStore
        .findByGenre(genre)
        .flatMapIterable(BookSet::books)
        .flatMap(book -> salesStore.findForBook(book.getName()).map(sale -> Tuples.of(book, sale)))
        .map(tuple -> tuple.mapT1(Book::getAuthor))
        .groupBy(Tuple2::getT1)
        .flatMap(
            group ->
                group
                    .map(Tuple2::getT2)
                    .map(Sale::values)
                    .flatMapIterable(i -> i)
                    .reduce(0L, Long::sum)
                    .map(summarySales -> Tuples.of(group.key(), summarySales)))
        .collectMap(Tuple2::getT1, Tuple2::getT2);
  }

  @GetMapping("inner/{path}")
  public Mono<String> print2(@PathVariable String path) {
    return destinationProvider.provide(path).map(URI::toString);
  }
}
