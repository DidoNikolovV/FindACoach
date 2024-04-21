package com.softuni.fitlaunch.model.dto.program;

import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.user.CoachDTO;
import com.softuni.fitlaunch.model.entity.ClientEntity;
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


