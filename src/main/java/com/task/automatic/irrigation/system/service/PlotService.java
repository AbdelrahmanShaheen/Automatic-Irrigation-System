package com.task.automatic.irrigation.system.service;

import com.task.automatic.irrigation.system.errors.InvalidRequestException;
import com.task.automatic.irrigation.system.errors.ResourceNotFoundException;
import com.task.automatic.irrigation.system.model.IrrigationSlot;
import com.task.automatic.irrigation.system.model.Plot;
import com.task.automatic.irrigation.system.model.PlotConfig;
import com.task.automatic.irrigation.system.repository.IrrigationSlotRepository;
import com.task.automatic.irrigation.system.repository.PlotRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlotService {
    @Autowired
    private PlotRepository plotRepository;
    @Autowired
    private IrrigationSlotRepository irrigationSlotRepository;

    public Plot addPlot(Plot plot) {
          //Here i make all the validations
          //All the plot fields cannot be empty.
          if (plot == null) {
              throw new InvalidRequestException("Plot cannot be null");
          }
          if (StringUtils.isBlank(plot.getLocation())
                  || StringUtils.isBlank(plot.getSoilType()) || StringUtils.isBlank(plot.getCropType())
                  || plot.getLocation().length()!=3 || !plot.getLocation().matches("^\\d,\\d$")) {
              throw new InvalidRequestException("Plot name, location, soil type, and crop type cannot be blank");
          }
          //Plot location has to be unique.
          List<Plot>plots = plotRepository.findAll();
          for(Plot existingPlot : plots){
              if(existingPlot.getLocation().equals(plot.getLocation())){
                  throw new InvalidRequestException("Plot location has to be unique ,Choose another cell in the grid");
              }
          }
          List<IrrigationSlot> slots = new ArrayList<>();
          //we can store plots without its configs inside the db as business logic said.
          if(plot.getConfig() != null){
              //Check that all slots data are not blank/empty
              for(IrrigationSlot slot : plot.getConfig().getSlots()) {
                  if (slot.getStartTime() == null || slot.getEndTime() == null || slot.getWaterAmount() == 0.0)
                      throw new InvalidRequestException("slot startTime, endTime and waterAmount cannot be blank");
              }
              //if all of them are valid slot's data, We can store them in the database.
              for(IrrigationSlot slot : plot.getConfig().getSlots()){
                  slots.add(irrigationSlotRepository.save(slot));
              }
              plot.getConfig().setSlots(slots);
          }
          return plotRepository.save(plot);
    }
    //.....................................................................................
    public List<Plot> listPlots() {
        return plotRepository.findAll();
    }
    //.....................................................................................
    public Plot configurePlot(String plotId, PlotConfig plotConfig) {
        if (plotConfig == null) {
            throw new InvalidRequestException("Plot configuration cannot be null");
        }
        Plot plot = plotRepository.findById(plotId).orElseThrow(() -> new ResourceNotFoundException("Plot not found with ID: " + plotId));
        List<IrrigationSlot> slots = new ArrayList<>();
        //Check that all slots data are not blank/empty
        System.out.println(plotConfig.getSlots());
        for(IrrigationSlot slot : plotConfig.getSlots()) {
            if (slot.getStartTime() == null || slot.getEndTime() == null || slot.getWaterAmount() == 0.0)
                throw new InvalidRequestException("slot startTime, endTime and waterAmount cannot be blank");
        }
        //if all of them are valid slot's data, We can store them in the database.
        for(IrrigationSlot slot : plotConfig.getSlots()){
            slots.add(irrigationSlotRepository.save(slot));
        }
        plotConfig.setSlots(slots);
        plot.setConfig(plotConfig);
        return plotRepository.save(plot);
    }
//.....................................................................................
    public Plot editPlot(String plotId, String slotId ,IrrigationSlot slot) {
        if (slot == null) {
            throw new InvalidRequestException("Plot configuration cannot be null");
        }
        Plot plot = plotRepository.findById(plotId).orElseThrow(() -> new ResourceNotFoundException("Plot not found with ID: " + plotId));
        PlotConfig config = plot.getConfig();
        if (config == null) {
            config = new PlotConfig();
            plot.setConfig(config);
        }
        List<IrrigationSlot> slots = config.getSlots();
        if (slots == null) {
            slots = new ArrayList<>();
            config.setSlots(slots);
        }
        IrrigationSlot existingSlot = slots.stream().filter(s -> s.getId().equals(slotId)).findFirst().orElseThrow(() -> new ResourceNotFoundException("Irrigation slot not found"));
        if(slot.getStartTime() != null) {
            existingSlot.setStartTime(slot.getStartTime());
        }
        if(slot.getEndTime() != null) {
            existingSlot.setEndTime(slot.getEndTime());
        }
        if(slot.getWaterAmount() != 0.0) {
            existingSlot.setWaterAmount(slot.getWaterAmount());
        }
        if(slot.getStatus() != null) {
            existingSlot.setStatus(slot.getStatus());
        }
        irrigationSlotRepository.save(existingSlot);
        return plotRepository.save(plot);
    }
}
