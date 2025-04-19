package com.excel.excel_handling.model;

import com.excel.excel_handling.dto.ExerciseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Entity -> Model -> DTO

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseModel {
    private Long id;
    private String name;
    private String image_url;
    private String affected_muscle_groups;
    private String steps;
    private String action;

    //Convert Entity to Model
    public ExerciseModel(Exercise exercise) {
        this.id = exercise.getId();
        this.name = exercise.getName();
        this.image_url = exercise.getImage_url();
        this.affected_muscle_groups = exercise.getAffected_muscle_groups();
        this.action = exercise.getAction();
        this.steps = exercise.getSteps();
    }

    //Covert Model to DTO
    public ExerciseDTO toDto() {
        return new ExerciseDTO(
                this.id,
                this.name,
                this.image_url,
                this.affected_muscle_groups,
                this.action,
                this.steps);
    }
}
