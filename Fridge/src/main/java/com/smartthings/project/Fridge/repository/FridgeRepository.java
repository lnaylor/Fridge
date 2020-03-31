package com.smartthings.project.Fridge.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.smartthings.project.Fridge.model.Refrigerator;

@Repository
public interface FridgeRepository extends CrudRepository<Refrigerator, Long> {

}