package io.scalecube.spring.example.rating;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.scalecube.spring.example.domain.Book;
import io.scalecube.spring.example.domain.BookSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static java.util.Arrays.asList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RatingApplicationTests {

  @Test
  public void contextLoads() throws IOException {
  }

}
