package com.danielcepeda.webflux.app.models.documents;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data @NoArgsConstructor
@Document(collection = "products")
public class Product {

    @Id
    private String id;
    private String name;
    private Double price;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createAt;

    public Product(String name, Double price) {
        this.name = name;
        this.price = price;
    }
}
