package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.model.User;
import com.example.demo.model.UserType;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class MainController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    BookRepository bookRepository;

    @GetMapping("/")
    public String main() {

        return "index";
    }

    @PostMapping("/logout")
    public String logout(){
        return "redirect:/";
    }

    @GetMapping("/regForm")
    public String reg(ModelMap modelMap) {
        List<User> all = userRepository.findAll();
        modelMap.addAttribute("users", all);
        return "register";
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        ModelMap modelMap
    ) {
        User user = userRepository.findUserByEmailAndPassword(email, password);
        if (user != null) {
            if (user.getUserType().equals(UserType.ADMIN)) {
                List<User> users = userRepository.findAll();
                List<Book> books = bookRepository.findAll();
                modelMap.addAttribute("users", user);
                modelMap.addAttribute("user", users);
                modelMap.addAttribute("book", books);
                return "adminPage";
            } else {
                List<User> users = userRepository.findAll();
                List<Book> books = bookRepository.findAll();
                modelMap.addAttribute("user", users);
                modelMap.addAttribute("book", books);
                modelMap.addAttribute("users", user);

                return "userPage";
            }
        } else {
            modelMap.addAttribute("error", "Invalid Email or Password");
            return "index";
        }

    }
}

