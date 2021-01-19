package com.platzimarket.web.controller;

import com.platzimarket.domain.Product;
import com.platzimarket.domain.service.ProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping()
    @ApiOperation("Get all supermarket products")
    @ApiResponse(code = 200, message = "ok")
    public ResponseEntity<List<Product>> getAll() {
        return new ResponseEntity<List<Product>>(productService.getAll(), HttpStatus.OK);
    };

    @GetMapping("/{id}")
    @ApiOperation("Search a product using an id")
    @ApiResponses({
            @ApiResponse(code=200, message="ok"),
            @ApiResponse(code=404, message="Product not found")
    })
    public ResponseEntity<Product> getProduct(
            @ApiParam(value="The product id", required=true, example = "7")
            @PathVariable("id")
                    int productId
    ) {
        return productService.getProduct(productId)
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable("id") int categoryId) {
        return productService.getByCategory(categoryId)
                .map(products -> new ResponseEntity<List<Product>>(products, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping()
    public ResponseEntity<Product> save(@RequestBody Product product) {
        return new ResponseEntity<Product>(productService.save(product), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") int productId) {
        if (productService.delete(productId)) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
