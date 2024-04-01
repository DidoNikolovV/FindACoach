package com.softuni.fitlaunch.web;


import com.softuni.fitlaunch.model.dto.meal.MealDTO;
import com.softuni.fitlaunch.model.dto.mealPlan.MealPlanCreationDTO;
import com.softuni.fitlaunch.model.dto.mealPlan.MealPlanDTO;
import com.softuni.fitlaunch.service.MealPlanService;
import com.softuni.fitlaunch.service.MealService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/meal-plans")
public class MealPlanController {


    private final MealService mealService;

    private final MealPlanService mealPlanService;

    public MealPlanController(MealService mealService, MealPlanService mealPlanService) {
        this.mealService = mealService;
        this.mealPlanService = mealPlanService;
    }

    @GetMapping("/create")
    public String createMealPlan(Model model, Principal principal) {

        List<MealDTO> meals = mealService.getAllMeals(principal.getName());

        model.addAttribute("meals", meals);
        model.addAttribute("mealPlanCreationDTO", new MealPlanCreationDTO());

        return "create-meal-plan";

    }

    @PostMapping("/create")
    public String createMealPlan(@ModelAttribute("mealPlanCreationDTO") MealPlanCreationDTO mealPlanCreationDTO, Principal principal) {

        MealPlanDTO mealPlan = mealPlanService.createMealPlan(mealPlanCreationDTO, principal.getName());

        return "redirect:/meal-plans/" + mealPlan.getId();
    }
}
