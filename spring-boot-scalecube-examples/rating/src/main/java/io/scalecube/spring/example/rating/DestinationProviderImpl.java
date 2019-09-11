package io.scalecube.spring.example.rating;
import io.scalecube.spring.example.domain.Book;
import io.scalecube.services.routing.RandomServiceRouter;
import java.net.URI;
import org.springframework.boot.scalecube.beans.SelectionStrategy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DestinationProviderImpl implements DestinationProvider  {

  private final BookStore bookStore;

  @SelectionStrategy(routerType = RandomServiceRouter.class)
  private final SalesStore salesStore;

  public DestinationProviderImpl(BookStore bookStore,
      SalesStore salesStore) {
    this.bookStore = bookStore;
    this.salesStore = salesStore;
  }

  @Override
  public Mono<URI> provide(String path) {
    return bookStore
        .findByGenre(path)
        .flatMap(salesStore::findForBoook)
        .map(Book::getUri);
  }
}
