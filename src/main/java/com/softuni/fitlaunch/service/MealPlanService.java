package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.mealPlan.MealPlanCreationDTO;
import com.softuni.fitlaunch.model.dto.mealPlan.MealPlanCreationDetails;
import com.softuni.fitlaunch.model.dto.mealPlan.MealPlanDTO;
import com.softuni.fitlaunch.model.entity.CoachEntity;
import com.softuni.fitlaunch.model.entity.MealPlanDayEntity;
import com.softuni.fitlaunch.model.entity.MealPlanEntity;
import com.softuni.fitlaunch.model.entity.MealPlanWeekEntity;
import com.softuni.fitlaunch.model.enums.DaysEnum;
import com.softuni.fitlaunch.repository.MealPlanRepository;
import com.softuni.fitlaunch.service.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public MealPlanCreationDetails createMealPlan(MealPlanCreationDTO mealPlanCreationDTO, String username) {
        MealPlanEntity mealPlan = modelMapper.map(mealPlanCreationDTO, MealPlanEntity.class);
        CoachEntity coach = coachService.getCoachEntityByUsername(username);
        Set<MealPlanWeekEntity> weeks = createWeeks(mealPlanCreationDTO.getWeeks(), mealPlan);
        mealPlan.setWeeks(weeks);
//        List<MealEntity> meals = mealPlanCreationDTO.getSelectedMealIds().stream().map(mealService::getMealEntityById).toList();

//        mealPlan.setMeals(meals);
        mealPlan.setCoach(coach);

        mealPlan = mealPlanRepository.save(mealPlan);

        return modelMapper.map(mealPlan, MealPlanCreationDetails.class);
    }

    private Set<MealPlanWeekEntity> createWeeks(int numberOfWeeks, MealPlanEntity mealPlan) {
        Set<MealPlanWeekEntity> mealPlanWeeks = new HashSet<>();
        for (int i = 0; i < numberOfWeeks; i++) {
            MealPlanWeekEntity week = createWeek();
            week.setMealPlan(mealPlan);
            for (int j = 1; j <= 7; j++) {
                MealPlanDayEntity day = createDay();
                String dayName = DaysEnum.values()[j - 1].name();
                day.setName(dayName);
                day.setWeek(week);
                week.getWeekDays().add(day);
            }
            mealPlanWeeks.add(week);
        }

        return mealPlanWeeks;
    }

    public MealPlanDTO getMealPlanById(Long id) {
        MealPlanEntity mealPlan = mealPlanRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Meal plan was not found"));
        return modelMapper.map(mealPlan, MealPlanDTO.class);
    }

    public MealPlanCreationDetails getMealPlanCreationById(Long id) {
        MealPlanEntity mealPlan = mealPlanRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Meal plan was not found"));
        return modelMapper.map(mealPlan, MealPlanCreationDetails.class);
    }

    public List<MealPlanDTO> getAllMealPlans(String username) {
        CoachEntity coach = coachService.getCoachEntityByUsername(username);
        List<MealPlanEntity> mealPlans = mealPlanRepository.findAllByCoachId(coach.getId());
        return mealPlans.stream().map(mealPlan -> modelMapper.map(mealPlan, MealPlanDTO.class)).toList();
    }

    public MealPlanWeekEntity createWeek() {
        return new MealPlanWeekEntity();
    }

    public MealPlanDayEntity createDay() {
        return new MealPlanDayEntity();
    }
}
