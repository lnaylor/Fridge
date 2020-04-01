package com.smartthings.project.Fridge.service;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartthings.project.Fridge.model.Item;
import com.smartthings.project.Fridge.model.Refrigerator;
import com.smartthings.project.Fridge.repository.FridgeRepository;
import com.smartthings.project.Fridge.repository.ItemRepository;

@Service
public class FridgeService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FridgeService.class);
	
	@Autowired
	FridgeRepository fridgeRepository;
	
	@Autowired
	ItemRepository itemRepository;
	
	private static final String SODA = "canofsoda";
	private static final int MAX_SODA = 12;
	
	//will be set to true if someone tries to overfill the fridge with soda
	//this value is used to determine if the soda error message should be displayed on the 
	//fridgeContents.html page
	private boolean sodaError = false;
	
	@PostConstruct
	public void init() {
		Refrigerator r = new Refrigerator();
		Item i = new Item("Milk", 1);
		itemRepository.save(i);
		r.getItems().add(i);
		fridgeRepository.save(r);
		fridgeRepository.save(new Refrigerator());
		LOGGER.info("Initialized {} refrigerators", fridgeRepository.count());
	}
	
	public Iterable<Refrigerator> getAllFridges() {
		LOGGER.info("Retrieving all fridges from database");
		return fridgeRepository.findAll();
	}
	
	public Refrigerator getFridgeById(Long id) {
		LOGGER.info("Retrieving fridge with ID {} from database", id);
		return fridgeRepository.findById(id).get();
	}
	
	public Item getItemById(Long id) {
		LOGGER.info("Retrieving item with ID {} from database", id);
		return itemRepository.findById(id).get();
	}
	
	public Long addItem(Long fridgeId, String itemName, int itemCount) {
		LOGGER.info("Adding item with name {} and count {} to fridge {}", itemName, itemCount, fridgeId);
	    	Refrigerator refrigerator = fridgeRepository.findById(fridgeId).get();
	    	this.setSodaError(false);
	    	boolean itemExists = false;
	    	String itemNameStripped = itemName.toLowerCase().strip().replaceAll("\\s+", "");
	    	
	    	for (Item i : refrigerator.getItems()) {
	    		if (itemNameStripped.equals(i.getName())) {
	    			LOGGER.info("Item {} already exists in fridge, adding {} to the existing amount of {}", itemName, itemCount, i.getCount());
	    			
	    			//if the item is a can of soda, and this addition will put it over its limit,
	    			//add only the number of cans that will put it at the limit
	    			//Set soda error to true so that the error message will be displayed on the fridgeContents page
	    			if (itemNameStripped.equals(SODA)) {
	    				int numSodaCans=getTotalSodaCans();
	    				if (numSodaCans+itemCount>MAX_SODA) {
	    					LOGGER.info("Adding {} cans of soda will violate the soda limit. Only adding {} cans.", itemCount, MAX_SODA-numSodaCans);
	    					itemCount=(MAX_SODA-numSodaCans);
		    				setSodaError(true);
	    				}
	    				
	    			}
	    			
	    			//if item already exists, increment its count instead of adding a new item
	    			i.setCount(i.getCount()+itemCount);
	    			itemRepository.save(i);
	    			itemExists=true;
	    		}
	    	}
	    	//Same logic as above, but in the case that the item did not already exist in the fridge
	    	if (!itemExists) {
	    		if (itemNameStripped.equals(SODA)) {
	    			int numSodaCans=getTotalSodaCans();
	    			if (numSodaCans+itemCount>MAX_SODA) {
	    				LOGGER.info("Adding {} cans of soda will violate the soda limit. Only adding {} cans.", itemCount, MAX_SODA-numSodaCans);
	    				itemCount=MAX_SODA-numSodaCans;
		    			setSodaError(true);
	    			}
	    			
	    		}
	    		Item item = new Item(itemName, itemCount);
	    		itemRepository.save(item);
	    		refrigerator.getItems().add(item);
	    	}
	    	
	    	fridgeRepository.save(refrigerator);
	    	return refrigerator.getId();
	    }
	    
	    public Long deleteItem(Long fridgeId, Long itemId) {
	    	LOGGER.info("Deleting item ID {} from fridge {}", itemId, fridgeId);
	    	Refrigerator refrigerator = fridgeRepository.findById(fridgeId).get();
	    	this.setSodaError(false);
	    	Item item = itemRepository.findById(itemId).get();
	    	refrigerator.getItems().remove(item);
	    	itemRepository.deleteById(itemId);
	    	fridgeRepository.save(refrigerator);
	    	return refrigerator.getId();
	    }
	    
	    private int getTotalSodaCans() {
	    	int numSodaCans=0;
	    	for (Refrigerator r : fridgeRepository.findAll()) {
	    		for (Item i : r.getItems()) {
	    			if (i.getName().equals(SODA)) {
	    				numSodaCans+=i.getCount();
	    			}
	    		}
	    	}
	    	LOGGER.info("Current soda can total is {}", numSodaCans);
	    	return numSodaCans;
	    }

		public boolean isSodaError() {
			return sodaError;
		}

		public void setSodaError(boolean sodaError) {
			this.sodaError = sodaError;
		}
}