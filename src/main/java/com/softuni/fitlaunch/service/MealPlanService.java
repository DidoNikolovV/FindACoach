package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.mealPlan.MealPlanCreationDTO;
import com.softuni.fitlaunch.model.dto.mealPlan.MealPlanDTO;
import com.softuni.fitlaunch.model.entity.CoachEntity;
import com.softuni.fitlaunch.model.entity.MealEntity;
import com.softuni.fitlaunch.model.entity.MealPlanEntity;
import com.softuni.fitlaunch.repository.MealPlanRepository;
import com.softuni.fitlaunch.service.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MealPlanService {

    private final MealPlanRepository mealPlanRepository;

    private final MealService mealService;

    private final CoachService coachService;

    private final ModelMapper modelMapper;

    public MealPlanService(MealPlanRepository mealPlanRepository, MealService mealService, CoachService coachService, ModelMapper modelMapper) {
        this.mealPlanRepository = mealPlanRepository;
        this.mealService = mealService;
        this.coachService = coachService;
        this.modelMapper = modelMapper;
    }

    public MealPlanDTO createMealPlan(MealPlanCreationDTO mealPlanCreationDTO, String username) {
        MealPlanEntity mealPlan = modelMapper.map(mealPlanCreationDTO, MealPlanEntity.class);
        CoachEntity coach = coachService.getCoachEntityByUsername(username);
        List<MealEntity> meals = mealPlanCreationDTO.getSelectedMealIds().stream().map(mealService::getMealEntityById).toList();

        mealPlan.setMeals(meals);
        mealPlan.setCoach(coach);

        mealPlan = mealPlanRepository.save(mealPlan);

        return modelMapper.map(mealPlan, MealPlanDTO.class);
    }

    public MealPlanDTO getMealPlanById(Long id) {
        MealPlanEntity mealPlan = mealPlanRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Meal plan was not found"));
        return modelMapper.map(mealPlan, MealPlanDTO.class);
    }

    public List<MealPlanDTO> getAllMealPlans(String username) {
        CoachEntity coach = coachService.getCoachEntityByUsername(username);
        List<MealPlanEntity> mealPlans = mealPlanRepository.findAllByCoachId(coach.getId());
        return mealPlans.stream().map(mealPlan -> modelMapper.map(mealPlan, MealPlanDTO.class)).toList();
    }
}
