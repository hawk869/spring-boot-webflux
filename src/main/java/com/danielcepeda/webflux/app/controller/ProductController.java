package com.danielcepeda.webflux.app.controller;

import com.danielcepeda.webflux.app.models.dao.ProductDao;
import com.danielcepeda.webflux.app.models.documents.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;

@Controller
public class ProductController {

    private final ProductDao dao;

    public ProductController(ProductDao dao) {
        this.dao = dao;
    }
    @GetMapping({"/listar", "/"})
    public String getProducts(Model model) {

        Flux<Product> products = dao.findAll();
        model.addAttribute("productos", products);
        model.addAttribute("titulo", "Listado de productos");
        return "listar";
    }
}
