package io.scalecube.spring.example.sales;

import io.scalecube.spring.example.domain.Sale;
import io.scalecube.services.annotations.Service;
import io.scalecube.services.annotations.ServiceMethod;
import reactor.core.publisher.Mono;

@Service("destination-selector")
public interface SalesStore {

    @ServiceMethod("select")
    Mono<Sale> findForBook(String bookName);

}
