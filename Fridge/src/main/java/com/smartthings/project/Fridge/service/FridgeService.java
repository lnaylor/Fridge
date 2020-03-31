package com.smartthings.project.Fridge.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartthings.project.Fridge.model.Item;
import com.smartthings.project.Fridge.model.Refrigerator;
import com.smartthings.project.Fridge.repository.FridgeRepository;
import com.smartthings.project.Fridge.repository.ItemRepository;

@Service
public class FridgeService {
	
	@Autowired
	FridgeRepository fridgeRepository;
	
	@Autowired
	ItemRepository itemRepository;
	
	private static final String SODA = "canofsoda";
	private static final int MAX_SODA = 12;
	private boolean sodaError = false;
	
	@PostConstruct
	public void init() {
		Refrigerator r = new Refrigerator();
		Item i = new Item("Milk", 1);
		itemRepository.save(i);
		r.getItems().add(i);
		fridgeRepository.save(r);
		fridgeRepository.save(new Refrigerator());
	}
	
	public Iterable<Refrigerator> getAllFridges() {
		return fridgeRepository.findAll();
	}
	
	public Refrigerator getFridgeById(Long id) {
		return fridgeRepository.findById(id).get();
	}
	
	public Item getItemById(Long id) {
		return itemRepository.findById(id).get();
	}
	
	public Long addItem(Long fridgeId, String itemName, int itemCount) {
	    	Refrigerator refrigerator = fridgeRepository.findById(fridgeId).get();
	    	this.setSodaError(false);
	    	boolean itemExists = false;
	    	String itemNameStripped = itemName.toLowerCase().strip().replaceAll("\\s+", "");
	    	for (Item i : refrigerator.getItems()) {
	    		if (itemNameStripped.equals(i.getName())) {

	    			i.setCount(i.getCount()+itemCount);
	    			
	    			if (itemNameStripped.equals(SODA) && i.getCount()>MAX_SODA) {
	    				i.setCount(MAX_SODA);
	    				setSodaError(true);
	    			}
	    			itemRepository.save(i);
	    			itemExists=true;
	    		}
	    	}
	    	if (!itemExists) {
	    		if (itemNameStripped.equals(SODA) && itemCount>MAX_SODA) {
	    			itemCount=MAX_SODA;
	    			setSodaError(true);
	    		}
	    		Item item = new Item(itemName, itemCount);
	    		itemRepository.save(item);
	    		refrigerator.getItems().add(item);
	    	}
	    	
	    	fridgeRepository.save(refrigerator);
	    	return refrigerator.getId();
	    }
	    
	    public Long deleteItem(Long fridgeId, Long itemId) {
	    	Refrigerator refrigerator = fridgeRepository.findById(fridgeId).get();
	    	this.setSodaError(false);
	    	Item item = itemRepository.findById(itemId).get();
	    	refrigerator.getItems().remove(item);
	    	itemRepository.deleteById(itemId);
	    	fridgeRepository.save(refrigerator);
	    	return refrigerator.getId();
	    }

		public boolean isSodaError() {
			return sodaError;
		}

		public void setSodaError(boolean sodaError) {
			this.sodaError = sodaError;
		}
}