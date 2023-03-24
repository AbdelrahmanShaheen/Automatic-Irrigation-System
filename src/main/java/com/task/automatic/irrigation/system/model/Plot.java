package com.task.automatic.irrigation.system.model;


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

    //note : the farm is a grid of plots/cells so each location can be like this: "2,3"
    private String location;

    private String soilType;

    private String cropType;

    private PlotConfig config;
}
