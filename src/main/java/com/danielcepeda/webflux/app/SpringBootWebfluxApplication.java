package com.danielcepeda.webflux.app;

import com.danielcepeda.webflux.app.models.dao.ProductDao;
import com.danielcepeda.webflux.app.models.documents.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

import java.util.Date;

@SpringBootApplication
public class SpringBootWebfluxApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SpringBootWebfluxApplication.class);
    private final ProductDao productDao;
    private final ReactiveMongoTemplate mongoTemplate;

    public SpringBootWebfluxApplication(ProductDao productDao, ReactiveMongoTemplate mongoTemplate) {
        this.productDao = productDao;
        this.mongoTemplate = mongoTemplate;
    }


    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebfluxApplication.class, args);
    }

    @Override
    public void run(String... args) {

        mongoTemplate.dropCollection("products").subscribe();

        Flux.just(new Product("TV Panasonic Pantalla LCD", 456.89),
                new Product("Sony Camara", 177.89),
                new Product("Apple iPod", 46.89),
                new Product("Sony Notebook", 846.89),
                new Product("HP Multifuncional", 200.89),
                new Product("Bicicleta TREK", 70.89),
                new Product("TV Sony Bravia Pantalla LCD", 2456.89),
                new Product("MacBook Air M2", 1256.89),
                new Product("iPhone 14 Pro", 1446.89)
        )
//                .flatMap(productDao::save)
                .flatMap(product -> {
                    product.setCreateAt(new Date());
                    return productDao.save(product);
                })
                .subscribe(product -> log.info("Insert: " + product.getId() + product.getName()));
    }
}
