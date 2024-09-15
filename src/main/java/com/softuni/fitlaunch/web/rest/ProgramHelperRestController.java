package com.softuni.fitlaunch.web.rest;


import com.softuni.fitlaunch.model.dto.program.ProgramDTO;
import com.softuni.fitlaunch.model.dto.program.ProgramHelperForm;
import com.softuni.fitlaunch.service.ProgramService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/program-helper")
public class ProgramHelperRestController {

    private final ProgramService programService;

    public ProgramHelperRestController(ProgramService programService) {
        this.programService = programService;
    }

    @PostMapping("/get-recommendation")
    public ResponseEntity<ProgramHelperForm> getRecommendation(@RequestBody ProgramHelperForm form) {
        ProgramHelperForm recommendedProgramDto = programService.getRecommendation(form);
        return ResponseEntity.ok(recommendedProgramDto);
    }
}
