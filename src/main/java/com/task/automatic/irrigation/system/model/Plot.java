package com.task.automatic.irrigation.system.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "plots")
public class Plot {
    @Id
    private String id;

    //note : the farm is a grid of plots so each location can be like this: "2,3"
    @NotBlank
    @Size(min = 3, max = 3)
    @NotNull
    private String location;

    @NotBlank
    private String soilType;

    @NotBlank
    private String cropType;

    private PlotConfig config;
}
