package com.task.automatic.irrigation.system.repository;

import com.task.automatic.irrigation.system.model.Plot;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlotRepository extends MongoRepository<Plot,String> {

}
