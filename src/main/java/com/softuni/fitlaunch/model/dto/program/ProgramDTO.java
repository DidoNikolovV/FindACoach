package com.softuni.fitlaunch.model.dto.program;

import com.softuni.fitlaunch.model.dto.week.WeekCreationDTO;
import com.softuni.fitlaunch.model.entity.WeekEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProgramDTO {

    private Long id;
    private String imgUrl;
    private String name;
    private List<WeekCreationDTO> weeks;
}


