package com.softuni.fitlaunch.web.rest;


import com.softuni.fitlaunch.model.dto.program.ProgramHelperForm;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/program-helper")
public class ProgramHelperRestController {

    @PostMapping("/get-recommendation")
    public ResponseEntity<ProgramHelperForm> getRecommendation(@RequestBody ProgramHelperForm programHelperForm) {
        return ResponseEntity.ok(programHelperForm);
    }
}
