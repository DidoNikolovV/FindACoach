package com.softuni.fitlaunch.model.dto.meal;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.mapping.Set;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MealDTO {
    private Long id;
    private String name;
    private String description;
    private String type;
    private String imageUrl;

}