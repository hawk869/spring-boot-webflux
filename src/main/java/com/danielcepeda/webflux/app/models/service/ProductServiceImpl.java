package com.danielcepeda.webflux.app.models.service;

import com.danielcepeda.webflux.app.models.dao.ProductDao;
import com.danielcepeda.webflux.app.models.documents.Product;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductDao dao;

    public ProductServiceImpl(ProductDao dao) {
        this.dao = dao;
    }

    @Override
    public Flux<Product> findAll() {
        return dao.findAll();
    }

    @Override
    public Flux<Product> findAllByNameUpperCase() {
        return dao.findAll().map(product -> {
            product.setName(product.getName().toUpperCase());
            return product;
        });
    }

    @Override
    public Flux<Product> findAllByNameUpperCaseRepeat() {
        return findAllByNameUpperCase().repeat(5000);
    }

    @Override
    public Mono<Product> findById(String id) {
        return dao.findById(id);
    }

    @Override
    public Mono<Product> save(Product product) {
        return dao.save(product);
    }

    @Override
    public Mono<Void> delete(Product product) {
        return dao.delete(product);
    }
}
