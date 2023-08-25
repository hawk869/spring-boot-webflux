package com.danielcepeda.webflux.app.models.service;

import com.danielcepeda.webflux.app.models.documents.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {

    Flux<Product> findAll();
    Flux<Product> findAllByNameUpperCase();
    Flux<Product> findAllByNameUpperCaseRepeat();
    Mono<Product> findById(String id);
    Mono<Product> save(Product product);
    Mono<Void> delete(Product product);
}
