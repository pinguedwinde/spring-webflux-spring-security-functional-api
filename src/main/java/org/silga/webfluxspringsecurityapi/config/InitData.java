package org.silga.webfluxspringsecurityapi.config;

import org.silga.webfluxspringsecurityapi.model.Product;
import org.silga.webfluxspringsecurityapi.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import reactor.core.publisher.Flux;

@Configuration
public class InitData {
    @Bean
    CommandLineRunner init(ReactiveMongoOperations operations, ProductRepository repository) {
        return args -> {
            Flux<Product> productFlux = Flux.just(
                    new Product(null, "Big Latte", 2.99),
                    new Product(null, "Big Decaf", 2.49),
                    new Product(null, "Green Tea", 1.99),
                    new Product(null, "Green Tea Lip", 5.99),
                    new Product(null, "Green Tea Gingger", 3.99),
                    new Product(null, "Green Tea Berry", 3.99))
                    .flatMap(repository::save);

            productFlux
                    .thenMany(repository.findAll())
                    .subscribe(System.out::println);

            /*operations.collectionExists(Product.class)
                    .flatMap(exists -> exists ? operations.dropCollection(Product.class) : Mono.just(exists))
					.thenMany(v -> operations.createCollection(Product.class))
					.thenMany(productFlux)
					.thenMany(repository.findAll())
					.subscribe(System.out::println);*/
        };
    }
}
