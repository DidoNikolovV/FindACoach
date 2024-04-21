package com.softuni.fitlaunch.web;


import com.softuni.fitlaunch.model.dto.meal.MealDTO;
import com.softuni.fitlaunch.model.dto.mealPlan.MealPlanCreationDTO;
import com.softuni.fitlaunch.model.dto.mealPlan.MealPlanCreationDetails;
import com.softuni.fitlaunch.model.dto.mealPlan.MealPlanDTO;
import com.softuni.fitlaunch.service.MealPlanService;
import com.softuni.fitlaunch.service.MealService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

        MealPlanCreationDetails mealPlan = mealPlanService.createMealPlan(mealPlanCreationDTO, principal.getName());

        return String.format("redirect:/meal-plans/create/%d/details", mealPlan.getId());
    }

    @GetMapping("/create/{mealPlanId}/details")
    public String createMealPlan(@PathVariable("mealPlanId") Long mealPlanId, Model model, Principal principal) {

        List<MealDTO> meals = mealService.getAllMeals(principal.getName());
        MealPlanCreationDetails mealPlan = mealPlanService.getMealPlanCreationById(mealPlanId);

        model.addAttribute("meals", meals);
        model.addAttribute("mealPlan", mealPlan);

        return "meal-plan-creation-details";

    }

    @GetMapping("/{mealPlanId}")
    public String loadMealPlan(@PathVariable("mealPlanId") Long mealPlanId, Model model) {

        MealPlanDTO mealPlan = mealPlanService.getMealPlanById(mealPlanId);

        model.addAttribute("mealPlan", mealPlan);

        return "meal-plan-details";
    }

    @GetMapping("/all")
    public String loadAllMealPlans(Model model, Principal principal) {
        List<MealPlanDTO> mealPlans = mealPlanService.getAllMealPlans(principal.getName());

        model.addAttribute("mealPlans", mealPlans);

        return "all-meal-plans";
    }

    @GetMapping("/{mealPlanId}/edit")
    public String loadAllMealPlans(@PathVariable("mealPlanId") Long mealPlanId, Model model, Principal principal) {
        MealPlanDTO mealPlan = mealPlanService.getMealPlanById(mealPlanId);
        List<MealDTO> meals = mealService.getAllMeals(principal.getName());

        model.addAttribute("mealPlan", mealPlan);
        model.addAttribute("meals", meals);

        return "edit-meal-plan";
    }

}
