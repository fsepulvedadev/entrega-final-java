package com.example.demo.controller;


import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(path = "api/product")
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/")
    public ResponseEntity<Product> create(@RequestBody Product product) {
        return new ResponseEntity<>(this.productService.create(product), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<Product>> findAll() {
        return new ResponseEntity<>(this.productService.findAll(),HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Integer id) {
        return new ResponseEntity<>(this.productService.findById(id),HttpStatus.OK);
    }



}
