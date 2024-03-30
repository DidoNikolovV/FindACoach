package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.MealCreationDTO;
import com.softuni.fitlaunch.model.dto.MealDTO;
import com.softuni.fitlaunch.model.entity.MealEntity;
import com.softuni.fitlaunch.repository.MealRepository;
import com.softuni.fitlaunch.service.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class MealService {

    private final MealRepository mealRepository;

    private final ModelMapper modelMapper;

    public MealService(MealRepository mealRepository, ModelMapper modelMapper) {
        this.mealRepository = mealRepository;
        this.modelMapper = modelMapper;
    }

    public MealDTO createMeal(MealCreationDTO mealCreationDTO, String authorUsername) {
        MealEntity newMeal = modelMapper.map(mealCreationDTO, MealEntity.class);

        newMeal = mealRepository.save(newMeal);

        return modelMapper.map(newMeal, MealDTO.class);

    }

    public MealDTO getMealById(Long mealId) {
        MealEntity meal = mealRepository.findById(mealId).orElseThrow(() -> new ObjectNotFoundException("Meal with id " + mealId + " does not exist"));
        return modelMapper.map(meal, MealDTO.class);
    }
}
