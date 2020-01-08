package io.scalecube.spring.example.sales;

import io.scalecube.spring.example.domain.Sale;
import java.util.Map;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SalesStoreImpl implements SalesStore {

  private final Map<String, Sale> salesByBook;

  public SalesStoreImpl(Map<String, Sale> salesByBook) {
    this.salesByBook = salesByBook;
  }

  @Override
  public Mono<Sale> findForBook(String bookName) {
    return Mono.justOrEmpty(salesByBook.get(bookName.replace(" ", "")));
  }
}
