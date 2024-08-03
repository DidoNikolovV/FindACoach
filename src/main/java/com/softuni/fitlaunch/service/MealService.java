package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.MealCreationForm;
import com.softuni.fitlaunch.model.dto.meal.MealCreationDTO;
import com.softuni.fitlaunch.model.dto.meal.MealDTO;
import com.softuni.fitlaunch.model.entity.CoachEntity;
import com.softuni.fitlaunch.model.entity.ImageEntity;
import com.softuni.fitlaunch.model.entity.MealEntity;
import com.softuni.fitlaunch.repository.MealRepository;
import com.softuni.fitlaunch.service.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class MealService {

    private final MealRepository mealRepository;

    private final CoachService coachService;

    private final ImageService imageService;

    private final ModelMapper modelMapper;

    public MealService(MealRepository mealRepository, ModelMapper modelMapper, CoachService coachService, ImageService imageService) {
        this.mealRepository = mealRepository;
        this.modelMapper = modelMapper;
        this.coachService = coachService;
        this.imageService = imageService;
    }

    public List<MealDTO> getAllMeals(String username) {
        CoachEntity author = coachService.getCoachEntityByUsername(username);

        List<MealEntity> coachMeals = mealRepository.findAllByAuthorId(author.getId());

        return coachMeals.stream().map(meal -> modelMapper.map(meal, MealDTO.class)).toList();
    }
    public MealDTO createMeal(MealCreationDTO mealCreationDTO, String username, MultipartFile image) {
        MealEntity newMeal = modelMapper.map(mealCreationDTO, MealEntity.class);
        CoachEntity author = coachService.getCoachEntityByUsername(username);

        ImageEntity newImage = imageService.createImage(image);
        newImage.setMeal(newMeal);

        newMeal.setAuthor(author);
        newMeal.setImage(newImage);

        newMeal = mealRepository.save(newMeal);

        return modelMapper.map(newMeal, MealDTO.class);
    }

    public MealDTO getMealById(Long id) {
        MealEntity meal = getMealEntityById(id);
        return modelMapper.map(meal, MealDTO.class);
    }

    public MealEntity getMealEntityById(Long id) {
        return mealRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Meal with id " + id + " does not exist"));
    }

    public List<MealDTO> createMealsForDay(Long dayId, MealCreationForm form) {
        List<MealEntity> dbMeals = mealRepository.findAllById(form.getValues());
        return dbMeals.stream().map(dbMeal -> modelMapper.map(dbMeal, MealDTO.class)).toList();
    }
}
