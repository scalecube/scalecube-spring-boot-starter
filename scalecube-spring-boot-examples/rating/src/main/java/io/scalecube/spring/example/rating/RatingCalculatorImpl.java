package io.scalecube.spring.example.rating;

import io.scalecube.services.routing.RandomServiceRouter;
import io.scalecube.spring.example.book.BookStore;
import io.scalecube.spring.example.domain.Book;
import io.scalecube.spring.example.domain.BookSet;
import io.scalecube.spring.example.domain.Sale;
import io.scalecube.spring.example.sales.SalesStore;
import java.util.Map;
import org.springframework.boot.scalecube.beans.SelectionStrategy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Service
public class RatingCalculatorImpl implements RatingCalculator {

  private final BookStore bookStore;

  @SelectionStrategy(routerType = RandomServiceRouter.class)
  private final SalesStore salesStore;

  public RatingCalculatorImpl(BookStore bookStore, SalesStore salesStore) {
    this.bookStore = bookStore;
    this.salesStore = salesStore;
  }

  @Override
  public Mono<Map<String, Long>> calculateRating(String genre) {
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
}
