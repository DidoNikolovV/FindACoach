package com.softuni.fitlaunch.web;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/contact-us")
    public String loadContactUs(Model model) {
        model.addAttribute("activePage", "contactUs");
        return "contact-us";
    }
}
