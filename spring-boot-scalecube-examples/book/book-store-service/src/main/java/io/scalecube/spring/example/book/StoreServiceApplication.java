package io.scalecube.spring.example.book;

import io.scalecube.spring.example.domain.Book;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;

@SpringBootApplication
public class StoreServiceApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
            .sources(StoreServiceApplication.class)
            .build(args)
            .run()
        ;
    }

    @Bean
    @ConfigurationProperties(prefix = "store")
    public Map<String, List<Book>> store() {
        return new HashMap<>();
    }

    @Bean
    @ConfigurationPropertiesBinding
    public Converter<String, Book> destinationConverter() {
        return new Converter<String, Book>() {
            @Override
            public Book convert(String source) {
                String[] split = source.split("\\s*\\|\\s*");
                String name = split[0];
                String author = split[1];
                return new Book(name, author);
            }
        };
    }
}
