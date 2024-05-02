package com.danielcepeda.webflux.app.controller;

import com.danielcepeda.webflux.app.models.documents.Product;
import com.danielcepeda.webflux.app.models.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@SessionAttributes("producto")
@Controller
public class ProductController {

    private final ProductService service;
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping({"/listar", "/"})
    public Mono<String> getProducts(Model model) {

        Flux<Product> products = service.findAllByNameUpperCase();
        products.subscribe(product -> log.info(product.getName()));

        model.addAttribute("productos", products);
        model.addAttribute("titulo", "Listado de productos");
        return Mono.just("listar");
    }
    @GetMapping("/form")
    public Mono<String> craer(Model model) {
        model.addAttribute("producto", new Product());
        model.addAttribute("titulo", "Listado de productos");
        model.addAttribute("boton", "Crear");
        return Mono.just("form");
    }
    @GetMapping("/form-v2/{id}")
    public Mono<String> editarV2(@PathVariable String id, Model model) {
        return service.findById(id).doOnNext(p -> {
            log.info("Producto: {}", p.getName());
            model.addAttribute("titulo", "Editar Producto");
            model.addAttribute("boton", "Editar");
            model.addAttribute("producto", p);
        }).defaultIfEmpty(new Product())
                .flatMap(p -> {
                    if (p.getId() == null)
                        return Mono.error(new InterruptedException("No existe el producto"));
                    return Mono.just(p);
                }).then(Mono.just("form"))
                .onErrorResume(ex -> Mono.just("redirect:/listar?error=no+existe+el+producto"));
    }
    @GetMapping("/form/{id}")
    public Mono<String> editar(@PathVariable String id, Model model) {
        Mono<Product> productMono = service.findById(id).doOnNext(p -> {
            log.info("Producto: {}", p.getName());
        }).defaultIfEmpty(new Product());
        model.addAttribute("titulo", "Editar Producto");
        model.addAttribute("boton", "Editar");
        model.addAttribute("producto", productMono);
        return Mono.just("form");
    }
    @PostMapping("/form")
    public Mono<String> guardar(Product product, SessionStatus status) {
        status.setComplete();
        return service.save(product).doOnNext(p -> {
            log.info("Producto guardado: {} Id: {}", p.getName(), p.getId());
        }).thenReturn("redirect:/listar");
    }
    @GetMapping("/listar-datadriver")
    public String getDataDriver(Model model) {

        Flux<Product> products = service.findAllByNameUpperCase().delayElements(Duration.ofSeconds(1));
        products.subscribe(product -> log.info(product.getName()));

        model.addAttribute("productos", new ReactiveDataDriverContextVariable(products,2));
        model.addAttribute("titulo", "Listado de productos");
        return "listar";
    }
    @GetMapping("/listar-full")
    public String getFullProducts(Model model) {

        Flux<Product> products = service.findAllByNameUpperCaseRepeat();
        products.subscribe(product -> log.info(product.getName()));

        model.addAttribute("productos", products);
        model.addAttribute("titulo", "Listado de productos");
        return "listar";
    }
    @GetMapping("/listar-chunked")
    public String getChunkedProducts(Model model) {

        Flux<Product> products = service.findAllByNameUpperCaseRepeat();
        products.subscribe(product -> log.info(product.getName()));

        model.addAttribute("productos", products);
        model.addAttribute("titulo", "Listado de productos");
        return "listar-chunked";
    }
}
