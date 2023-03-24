package com.task.automatic.irrigation.system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class IrrigationSlot {
    @Id
    private String id;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private double waterAmount;

    private SlotStatus status = SlotStatus.NOT_STARTED;

}