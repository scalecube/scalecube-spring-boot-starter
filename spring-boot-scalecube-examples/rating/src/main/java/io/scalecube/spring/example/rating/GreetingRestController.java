package io.scalecube.spring.example.rating;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class GreetingRestController {

  // internal service
  private final RatingCalculator ratingCalculator;

  public GreetingRestController(RatingCalculator ratingCalculator) {
    this.ratingCalculator = ratingCalculator;
  }

  @GetMapping("rating/{genre}")
  public Mono<Map<String, Long>> print1(@PathVariable String genre) {
    return ratingCalculator.calculateRating(genre);
  }

}
