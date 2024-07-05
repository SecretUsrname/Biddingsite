package com.example.bitsbids.Controller;

import com.example.bitsbids.Repository.ProductRepository;
import com.example.bitsbids.Repository.UserRepository;
import com.example.bitsbids.entity.Product;
import com.example.bitsbids.entity.User;
import com.example.bitsbids.service.ProductService;
import com.example.bitsbids.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository pRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/")
    public ModelAndView showProduct()
    {
        ModelAndView mav =new ModelAndView("products-list");
        List<Product> list =pRepo.findAll();
        mav.addObject("Product",list);
        DefaultOidcUser user = (DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if(!existingUser.isPresent()) {
            User newUser = new User();
            newUser.setName(capitalize(user.getFullName()));
            newUser.setEmail(user.getEmail());
            userRepository.save(newUser);
            List<User> userList = userRepository.findAll();
        }
        return mav;
    }

    @GetMapping("/addProductForm")
    public ModelAndView addProductForm()
    {
        ModelAndView mav = new ModelAndView("start-bid");
        Product newproduct = new Product();
        mav.addObject("Product",newproduct);
        return mav;
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product, @RequestParam("image") MultipartFile imageFile) {


        if (!imageFile.isEmpty()) {
            productService.saveImage(imageFile, product.getId());
            String imageName = saveImage(imageFile);
            product.setImageName(imageName);
        }
        product.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        pRepo.save(product);
        return "redirect:/";
    }

    private String saveImage(MultipartFile imageFile) {
        // Generate a unique filename for the image (you can use a UUID or any other method)
        String imageName = UUID.randomUUID().toString() + "-" + imageFile.getOriginalFilename();

        // Define the directory where you want to save the images
        String uploadDir = "C:/Users/User/IdeaProjects/bitsbids/src/main/resources/Images";

        try {
            // Save the image to the specified directory
            Path filePath = Paths.get(uploadDir, imageName);
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }

        return imageName;
    }

    @GetMapping("/get-image/{imageName}")
    @ResponseBody
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        // Load the image from the directory
        Path filePath = Paths.get("C:/Users/User/IdeaProjects/bitsbids/src/main/resources/Images", imageName);
        Resource resource = new FileSystemResource(filePath.toFile());

        // Return the image as a ResponseEntity
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)  // Change to the appropriate content type (e.g., IMAGE_PNG)
                .body(resource);
    }

    @GetMapping("/product-view/{productId}")
    public String showProductView(@PathVariable Long productId, Model model) {
        Optional<Product> optionalProduct = productService.getProductById(productId);

        if (optionalProduct.isPresent()) {
            Product selectedProduct = optionalProduct.get();
            model.addAttribute("selectedProduct", selectedProduct);

            // Calculate time left and add it to the model
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime bidEndTime = selectedProduct.getBidEndTime();
            Duration timeLeft = Duration.between(now, bidEndTime);
            model.addAttribute("timeLeft", timeLeft);

            model.addAttribute("selectedProduct", selectedProduct);
            model.addAttribute("timeLeft", timeLeft);
            return "product-view";
        } else {
            // Handle product not found error
            return "redirect:/";
        }
    }
    public static String capitalize(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }
    @GetMapping("/search")
    public ModelAndView searchProducts(@RequestParam(name = "query", required = false) String query) {
        ModelAndView mav = new ModelAndView("products-list");

        if (query != null && !query.isEmpty()) {
            List<Product> searchResults = productService.searchProducts(query);
            mav.addObject("Product", searchResults);
        } else {
            List<Product> allProducts = pRepo.findAll();
            mav.addObject("Product", allProducts);
        }

        return mav;
    }
}
