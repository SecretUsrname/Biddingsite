package com.example.bitsbids;


import com.example.bitsbids.entity.User;
import com.example.bitsbids.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    public CustomAuthenticationSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException, IOException {
        // Retrieve user details from authentication
        String name = authentication.getName();
        String email = authentication.getPrincipal().toString(); // Adjust this based on your authentication details

        // Create a new User entity and save it to the database
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        userService.saveUser(user);

        // You can redirect the user to a specific page after successful login if needed
        response.sendRedirect("/dashboard");
    }
}

