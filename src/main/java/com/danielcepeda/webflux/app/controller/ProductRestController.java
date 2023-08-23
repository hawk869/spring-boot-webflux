package com.danielcepeda.webflux.app.controller;

import com.danielcepeda.webflux.app.models.dao.ProductDao;
import com.danielcepeda.webflux.app.models.documents.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private final ProductDao dao;
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    public ProductRestController(ProductDao dao) {
        this.dao = dao;
    }

    @GetMapping
    public Flux<Product> index() {
        return dao.findAll().map(product -> {
            product.setName(product.getName().toUpperCase());
            return product;
        }).doOnNext(product -> log.info(product.getName()));
    }
    @GetMapping("/{id}")
    public Mono<Product> getProductById(@PathVariable String id) {
//        Mono<Product> product = dao.findById(id);
        Flux<Product> products = dao.findAll();
        return products.filter(p -> p.getId().equals(id)).next();
    }
}
