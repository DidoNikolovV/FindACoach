package com.softuni.fitlaunch.model.dto.program;

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
    private List<ProgramWeekDTO> weeks;
}


