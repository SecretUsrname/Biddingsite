package com.example.bitsbids.service;

import com.example.bitsbids.Repository.ProductRepository;
import com.example.bitsbids.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private static final String UPLOAD_DIR = "C:/Users/User/IdeaProjects/OOPfinal/src/main/resources/Images";

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }

    public void saveImage(MultipartFile imageFile, Long productId) {
        try {
            // Ensure the upload directory exists
            Files.createDirectories(Paths.get(UPLOAD_DIR));

            // Save the image to the directory
            Path filePath = Paths.get(UPLOAD_DIR, productId + "_" + StringUtils.cleanPath(imageFile.getOriginalFilename()));
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            // Handle exception
            e.printStackTrace();
        }
    }

    public Instant getBidEndTime(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            // Assuming there's a bidEndTime field in your Product entity
            LocalDateTime bidEndTime = optionalProduct.get().getBidEndTime();

            // Convert LocalDateTime to Instant
            return ((LocalDateTime) bidEndTime).atZone(ZoneId.systemDefault()).toInstant();
        } else {
            // Handle the case where the product is not found
            //throw new ProductNotFoundException("Product not found with id: " + productId);
        }
        return null;
    }

    public List<Product> searchProducts(String query) {
        // Implement the search logic based on your needs
        // For example, you can search by product name or description

        // Here, we are searching by product name or description containing the query string
        return productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);
    }
}