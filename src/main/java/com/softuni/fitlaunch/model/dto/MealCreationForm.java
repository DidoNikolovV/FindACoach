package com.softuni.fitlaunch.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealCreationForm {
    private Long breakfast;
    private Long lunch;
    private Long snack;
    private Long dinner;

    public List<Long> getValues() {
        return List.of(breakfast, lunch, snack, dinner);
    }
}

