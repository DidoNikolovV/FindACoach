package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.mealPlan.MealPlanCreationDTO;
import com.softuni.fitlaunch.model.dto.mealPlan.MealPlanDTO;
import com.softuni.fitlaunch.model.entity.MealEntity;
import com.softuni.fitlaunch.model.entity.MealPlanEntity;
import com.softuni.fitlaunch.repository.MealPlanRepository;
import com.softuni.fitlaunch.service.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MealPlanService {

    private final MealPlanRepository mealPlanRepository;

    private final MealService mealService;

    private final ModelMapper modelMapper;

    public MealPlanService(MealPlanRepository mealPlanRepository, MealService mealService, ModelMapper modelMapper) {
        this.mealPlanRepository = mealPlanRepository;
        this.mealService = mealService;
        this.modelMapper = modelMapper;
    }

    public MealPlanDTO createMealPlan(MealPlanCreationDTO mealPlanCreationDTO, String username) {
        MealPlanEntity mealPlan = modelMapper.map(mealPlanCreationDTO, MealPlanEntity.class);
        List<MealEntity> meals = mealPlanCreationDTO.getSelectedMealIds().stream().map(mealService::getMealEntityById).toList();
        mealPlan.setMeals(meals);

        mealPlan = mealPlanRepository.save(mealPlan);

        return modelMapper.map(mealPlan, MealPlanDTO.class);
    }

    public MealPlanDTO getMealPlanById(Long id) {
        MealPlanEntity mealPlan = mealPlanRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Meal plan was not found"));
        return modelMapper.map(mealPlan, MealPlanDTO.class);
    }
}
