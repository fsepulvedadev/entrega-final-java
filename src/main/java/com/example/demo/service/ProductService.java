package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public Product create (Product newProduct) {
        return this.productRepository.save(newProduct);
    }

    public List<Product> findAll () {return this.productRepository.findAll();}


   public Product findById (Integer id) {
        Optional<Product> opProduct = this.productRepository.findById(id);
        if (opProduct.isPresent()) {
            return opProduct.get();
        } else {
            return new Product();
        }

   }





}
