package com.example.bitsbids.Controller;

import com.example.bitsbids.Repository.UserRepository;
import com.example.bitsbids.entity.User;
import com.example.bitsbids.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/save-user")
    public ResponseEntity<String> saveUser(@RequestBody User user) {
        userService.saveUser(user);
        return ResponseEntity.ok("User saved successfully!");
    }
    @RequestMapping("/user-profile")
    public ModelAndView hello() {
        DefaultOidcUser user = (DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Check if the user already exists in the database
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            // User already exists, you may update the existing user or handle it as needed
            // For now, let's just return the existing user details
            ModelAndView mav = new ModelAndView("user-profile");
            mav.addObject("user", existingUser.get());
            return mav;
        } else {
            // User doesn't exist, save the user to the database
            User newUser = new User();
            newUser.setName(capitalize(user.getFullName()));
            newUser.setEmail(user.getEmail());
            userRepository.save(newUser);

            // Fetch all users (including the newly added one)
            List<User> userList = userRepository.findAll();

            // Return the user list to the view
            ModelAndView mav = new ModelAndView("user-profile");
            mav.addObject("user", userList);
            return mav;
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

}

