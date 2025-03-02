package fimpa.co.fimpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import fimpa.co.fimpa.model.Product;
import fimpa.co.fimpa.service.ProductService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.NumberFormat;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
public ResponseEntity<Object> addProduct(
        @RequestParam("name") String name,
        @RequestParam("description") String description,
        @RequestParam("category") String category,
        @RequestParam(value = "imageUrl", required = false) MultipartFile image,
        @RequestParam("minPrice") Double minPrice,
        @RequestParam("maxPrice") Double maxPrice) {

    try {
        if (minPrice > maxPrice) {
            return buildErrorResponse("Harga minimum tidak boleh lebih besar dari harga maksimum.");
        }

        Product newProduct = productService.addProduct(name, description, category, image, minPrice, maxPrice);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    } catch (IOException e) {
        return buildErrorResponse("Error while uploading image");
    } catch (Exception e) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "") String search) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            Page<Product> productsPage = productService.getAllProducts(pageable, search);
            Map<String, Object> response = new HashMap<>();
            response.put("products", productsPage.getContent());
            response.put("total", productsPage.getTotalElements());
            response.put("totalPages", productsPage.getTotalPages());
            response.put("currentPage", productsPage.getNumber());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable String id) {
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (IllegalArgumentException e) {
            return buildErrorResponse(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(
            @PathVariable String id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam(value = "imageUrl", required = false) MultipartFile image,
            @RequestParam("minPrice") @NumberFormat(pattern = "#,###.##") Double minPrice,
            @RequestParam("maxPrice") @NumberFormat(pattern = "#,###.##") Double maxPrice) {

        try {
            if (minPrice > maxPrice) {
                return buildErrorResponse("Harga minimum tidak boleh lebih besar dari harga maksimum.");
            }

            Product updatedProduct = productService.updateProduct(id, name, description, category, image, minPrice,
                    maxPrice);
            return ResponseEntity.ok(updatedProduct);
        } catch (IOException e) {
            return buildErrorResponse("Error while updating image");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable String id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return buildErrorResponse(e.getMessage());
        } catch (Exception e) {
            logger.error("Error while deleting product", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<Object> buildErrorResponse(String errorMessage) {
        Map<String, String> response = new HashMap<>();
        response.put("error", errorMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}