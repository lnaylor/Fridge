package com.smartthings.project.Fridge.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.smartthings.project.Fridge.model.Item;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {

}