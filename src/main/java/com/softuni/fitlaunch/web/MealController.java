package com.softuni.fitlaunch.web;


import com.softuni.fitlaunch.model.dto.meal.MealCreationDTO;
import com.softuni.fitlaunch.model.dto.meal.MealDTO;
import com.softuni.fitlaunch.service.MealService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/meals")
public class MealController {

    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @GetMapping("/create")
    public String createMeal(Model model) {

        model.addAttribute("meal", new MealCreationDTO());

        return "create-meal";
    }

    @PostMapping("/create")
    public String createMeal(@ModelAttribute("mealCreationDTO") MealCreationDTO mealCreationDTO, Principal principal) {
        MealDTO meal = mealService.createMeal(mealCreationDTO, principal.getName(), mealCreationDTO.getImage());

        return "redirect:/meals/" + meal.getId();
    }

    @GetMapping("/{mealId}")
    public String mealDetails(@PathVariable("mealId") Long mealId, Model model) {
        MealDTO meal = mealService.getMealById(mealId);

        model.addAttribute("meal", meal);

        return "meal-details";
    }

    @GetMapping("/all")
    public String loadAllMeals(Model model, Principal principal) {

        List<MealDTO> allMeals = mealService.getAllMeals(principal.getName());

        model.addAttribute("allMeals", allMeals);

        return "all-meals";
    }
}
