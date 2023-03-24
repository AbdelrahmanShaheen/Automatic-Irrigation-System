package com.task.automatic.irrigation.system.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlotConfig {
    private List<IrrigationSlot> slots;
}
