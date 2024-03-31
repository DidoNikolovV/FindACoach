package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.meal.MealCreationDTO;
import com.softuni.fitlaunch.model.dto.meal.MealDTO;
import com.softuni.fitlaunch.model.entity.CoachEntity;
import com.softuni.fitlaunch.model.entity.MealEntity;
import com.softuni.fitlaunch.repository.MealRepository;
import com.softuni.fitlaunch.service.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MealService {

    private final MealRepository mealRepository;

    private final ModelMapper modelMapper;

    private final CoachService coachService;

    private  final FileUpload fileUpload;

    public MealService(MealRepository mealRepository, ModelMapper modelMapper, CoachService coachService, FileUpload fileUpload) {
        this.mealRepository = mealRepository;
        this.modelMapper = modelMapper;
        this.coachService = coachService;
        this.fileUpload = fileUpload;
    }

    public List<MealDTO> getAllMeals(String username) {
        CoachEntity author = coachService.getCoachEntityByUsername(username);

        List<MealEntity> coachMeals = mealRepository.findAllByAuthorId(author.getId());

        return coachMeals.stream().map(meal -> modelMapper.map(meal, MealDTO.class)).toList();
    }

    public MealDTO createMeal(MealCreationDTO mealCreationDTO, String username) {
        MealEntity newMeal = modelMapper.map(mealCreationDTO, MealEntity.class);
        CoachEntity author = coachService.getCoachEntityByUsername(username);

//        String image = fileUpload.uploadFile(mealCreationDTO.getImage());

        newMeal.setAuthor(author);
//        newMeal.setImage(image);
        newMeal = mealRepository.save(newMeal);

        return modelMapper.map(newMeal, MealDTO.class);
    }

    public MealDTO getMealById(Long mealId) {
        MealEntity meal = mealRepository.findById(mealId).orElseThrow(() -> new ObjectNotFoundException("Meal with id " + mealId + " does not exist"));
        return modelMapper.map(meal, MealDTO.class);
    }
}
