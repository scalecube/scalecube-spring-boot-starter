package io.scalecube.spring.example.rating;

import io.scalecube.spring.example.book.BookStore;
import io.scalecube.spring.example.sales.SalesStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.scalecube.beans.EnableScalecube;

@SpringBootApplication
@EnableScalecube(remoteServices = {BookStore.class, SalesStore.class})
public class RedirectApplication {

  public static void main(String[] args) {
    SpringApplication.run(RedirectApplication.class, args);
  }

}
