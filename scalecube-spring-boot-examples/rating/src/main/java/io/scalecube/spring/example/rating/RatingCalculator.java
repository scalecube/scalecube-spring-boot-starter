package io.scalecube.spring.example.rating;

import io.scalecube.services.annotations.Service;
import io.scalecube.services.annotations.ServiceMethod;
import java.util.Map;
import reactor.core.publisher.Mono;

@Service
public interface RatingCalculator {

  @ServiceMethod
  Mono<Map<String, Long>> calculateRating(String genre);

}
