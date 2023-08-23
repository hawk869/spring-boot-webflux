package com.danielcepeda.webflux.app.controller;

import com.danielcepeda.webflux.app.models.dao.ProductDao;
import com.danielcepeda.webflux.app.models.documents.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Controller
public class ProductController {

    private final ProductDao dao;
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    public ProductController(ProductDao dao) {
        this.dao = dao;
    }

    @GetMapping({"/listar", "/"})
    public String getProducts(Model model) {

        Flux<Product> products = dao.findAll().map(product -> {
                product.setName(product.getName().toUpperCase());
                return product;
        });
        products.subscribe(product -> log.info(product.getName()));

        model.addAttribute("productos", products);
        model.addAttribute("titulo", "Listado de productos");
        return "listar";
    }
    @GetMapping("/listar-datadriver")
    public String getDataDriver(Model model) {

        Flux<Product> products = dao.findAll().map(product -> {
            product.setName(product.getName().toUpperCase());
            return product;
        }).delayElements(Duration.ofSeconds(1));
        products.subscribe(product -> log.info(product.getName()));

        model.addAttribute("productos", new ReactiveDataDriverContextVariable(products,2));
        model.addAttribute("titulo", "Listado de productos");
        return "listar";
    }
    @GetMapping("/listar-full")
    public String getFullProducts(Model model) {

        Flux<Product> products = dao.findAll().map(product -> {
            product.setName(product.getName().toUpperCase());
            return product;
        }).repeat(5000);
        products.subscribe(product -> log.info(product.getName()));

        model.addAttribute("productos", products);
        model.addAttribute("titulo", "Listado de productos");
        return "listar";
    }
}
