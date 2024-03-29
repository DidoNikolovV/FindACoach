package com.softuni.fitlaunch.model.dto.week;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class WeekCreationDTO {
    private Long id;

    private List<DayCreationDTO> days;
}
