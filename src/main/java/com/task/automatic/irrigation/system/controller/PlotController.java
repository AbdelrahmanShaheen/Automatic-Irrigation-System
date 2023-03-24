package com.task.automatic.irrigation.system.controller;

import com.task.automatic.irrigation.system.errors.ErrorDetails;
import com.task.automatic.irrigation.system.errors.InvalidRequestException;
import com.task.automatic.irrigation.system.errors.ResourceNotFoundException;
import com.task.automatic.irrigation.system.model.IrrigationSlot;
import com.task.automatic.irrigation.system.model.Plot;
import com.task.automatic.irrigation.system.model.PlotConfig;
import com.task.automatic.irrigation.system.service.PlotService;
import io.micrometer.common.util.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/plots")
@Validated
public class PlotController {
    @Autowired
    private PlotService plotService;
    //This route add a plot with/without configure it to the DB.
    @PostMapping("")
    public ResponseEntity<Object> addPlot(@RequestBody Plot plot) {
        try {
            Plot newPlot = plotService.addPlot(plot);
            return new ResponseEntity<>(newPlot, HttpStatus.CREATED);
        } catch (InvalidRequestException e) {
            ErrorDetails errorDetails = new ErrorDetails(e.getMessage());
            return new ResponseEntity<>(errorDetails ,HttpStatus.BAD_REQUEST);
        }
    }
    //This route gives all plots in the DB .If there are no plots ,It returns an empty list.
    @GetMapping("")
    public ResponseEntity<List<Plot>> listPlots() {
        try{
            List<Plot> plots = plotService.listPlots();
            return new ResponseEntity<>(plots, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //This route configure a plot (if it is not configured when creating it).
    @PutMapping("/{plotId}")
    public ResponseEntity<Object> configurePlot(@PathVariable String plotId, @RequestBody PlotConfig plotConfig) {
        //Check first if the id valid or not
        ErrorDetails errorDetails;
        if(!ObjectId.isValid(plotId)){
            errorDetails = new ErrorDetails("Enter a valid plotId");
            return new ResponseEntity<>(errorDetails,HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            Plot configuredPlot = plotService.configurePlot(plotId, plotConfig);
            return new ResponseEntity<>(configuredPlot, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            errorDetails = new ErrorDetails(e.getMessage());
            return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
        } catch (InvalidRequestException e){
            errorDetails = new ErrorDetails(e.getMessage());
            return new ResponseEntity<>(errorDetails ,HttpStatus.BAD_REQUEST);
        }
    }
    //Here you can only update the plotConfig of the plot So list of slots.
    //OR : You can simulate the sensor by changing the status of a slot.
    //So you can use this route to update the plot OR to 'Act as a sensor' by changing the status.
    @PatchMapping("/{plotId}/slots/{slotId}")
    public ResponseEntity<Object> editPlot(@PathVariable String plotId, @PathVariable String slotId, @RequestBody IrrigationSlot slot) {
        ErrorDetails errorDetails;
        //Check first if the id valid or not
        if(!ObjectId.isValid(plotId)){
            errorDetails = new ErrorDetails("Enter a valid plotId");
            return new ResponseEntity<>(errorDetails ,HttpStatus.NOT_ACCEPTABLE);
        }
        if(!ObjectId.isValid(slotId)){
            errorDetails = new ErrorDetails("Enter a valid slotId");
            return new ResponseEntity<>(errorDetails ,HttpStatus.NOT_ACCEPTABLE);
        }
        //...............................................
        try {
            Plot updatedPlot = plotService.editPlot(plotId, slotId ,slot);
            return new ResponseEntity<>(updatedPlot, HttpStatus.OK);
        } catch (InvalidRequestException e) {
            errorDetails = new ErrorDetails(e.getMessage());
            return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            errorDetails = new ErrorDetails(e.getMessage());
            return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
        }
    }
}
