package fimpa.co.fimpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import fimpa.co.fimpa.model.Product;
import fimpa.co.fimpa.repository.ProductRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Value("${file.upload-dir:D:/uploads/}")
    private String uploadDirectory;

    public Page<Product> getAllProducts(Pageable pageable, String search) {
        if (search.isEmpty()) {
            return productRepository.findAll(pageable);
        }
        return productRepository.findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(search, search, pageable);
    }

    public Product getProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + id));
    }

    public Product addProduct(String name, String description, String category, MultipartFile image, Double price)
            throws IOException {
        String imageUrl = saveImage(image);
        Product product = new Product(name, description, category, imageUrl, price);
        return productRepository.save(product);
    }

    public Product updateProduct(String id, String name, String description, String category, MultipartFile image,
            Double price)
            throws IOException {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + id));

        existingProduct.setName(name);
        existingProduct.setDescription(description);
        existingProduct.setCategory(category);

        if (image != null && !image.isEmpty()) {
            deleteOldImage(existingProduct.getImageUrl());
            String newImageUrl = saveImage(image);
            existingProduct.setImageUrl(newImageUrl);
        }

        return productRepository.save(existingProduct);
    }

    private String saveImage(MultipartFile image) throws IOException {
        Path uploadPath = Paths.get(uploadDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String fileName = System.currentTimeMillis() + "-" + image.getOriginalFilename();
        File file = new File(uploadDirectory + fileName);
        image.transferTo(file);
        return "/uploads/" + fileName;
    }

    private void deleteOldImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            String filePath = uploadDirectory + imageUrl.replace("/uploads/", "");
            File oldImage = new File(filePath);
            if (oldImage.exists()) {
                oldImage.delete();
            }
        }
    }

    public void deleteProduct(String id) {
        productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + id));
        productRepository.deleteById(id);
    }
}