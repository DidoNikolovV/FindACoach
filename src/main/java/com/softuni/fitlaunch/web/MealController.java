package com.softuni.fitlaunch.web;


import com.softuni.fitlaunch.model.dto.MealCreationDTO;
import com.softuni.fitlaunch.service.MealService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/meals")
public class MealController {

    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @GetMapping("/create")
    public String createMeal(Model model, Principal principal) {

        model.addAttribute("meal", new MealCreationDTO());

        return "create-meal";
    }
}
