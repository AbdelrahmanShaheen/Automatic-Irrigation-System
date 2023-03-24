package com.task.automatic.irrigation.system.repository;

import com.task.automatic.irrigation.system.model.IrrigationSlot;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IrrigationSlotRepository extends MongoRepository<IrrigationSlot, String> {
}
