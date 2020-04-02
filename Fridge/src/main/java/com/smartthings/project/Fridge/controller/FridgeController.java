package com.smartthings.project.Fridge.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartthings.project.Fridge.model.Refrigerator;
import com.smartthings.project.Fridge.service.FridgeService;

@Controller
public class FridgeController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FridgeController.class);
	
	@Autowired
	FridgeService fridgeService;
	
	@GetMapping("/login")
    public String login(Model model) {
        return "loginPage";
    }
	
	@GetMapping("/accessDenied")
    public String accessDenied(Model model) {
        return "redirect:/fridgeHome";
    }
	
	@GetMapping("/")
    public String homePageRedirect(Model model) {
        return "redirect:/fridgeHome";
    }
	
    @GetMapping("/fridgeHome")
    public String fridgeHome(Model model) {
    	fridgeService.setSodaError(false);
        model.addAttribute("fridges", fridgeService.getAllFridges());
        return "fridgeHomePage";
    }
    
    @GetMapping("/refrigerator/{id}")
    public String getFridge(@PathVariable Long id, Model model) {
    	Refrigerator refrigerator = fridgeService.getFridgeById(id);
    	LOGGER.info("Displaying information for fridge ID {}", id);
    	
    	model.addAttribute("refrigerator", refrigerator);
    	model.addAttribute("items", refrigerator.getItems());
    	model.addAttribute("sodaError", fridgeService.isSodaError());
        return "fridgeContentsPage";
    }
    
    @GetMapping("/refrigerator/update/{id}/{itemid}")
    public String updateItemView(@PathVariable Long id, @PathVariable Long itemid, Model model) {
    	LOGGER.info("Update Item view for Fridge ID {} and Item ID {}", id, itemid);
    	
    	model.addAttribute("refrigerator", fridgeService.getFridgeById(id));
    	model.addAttribute("item", fridgeService.getItemById(itemid));
        return "itemUpdatePage";
    }
    
    @PostMapping(value="/update")
	public String updateItem(@RequestParam String itemName, @RequestParam String count, @RequestParam Long fridgeId, 
			@RequestParam Long itemId, Model model) {
    	int amount = count.isEmpty() ? 0 : Integer.valueOf(count);
    	LOGGER.info("Updating item ID {} to have name {} and count {}", itemId, itemName, count);
    	fridgeService.deleteItem(fridgeId, itemId);
    	if (amount>0) {
    		fridgeService.addItem(fridgeId, itemName, amount);
    	}
    	
    	return "redirect:/refrigerator/"+fridgeId;
    }
    
    @GetMapping("/refrigerator/add/{id}")
    public String addItemView(@PathVariable Long id, Model model) {
    	LOGGER.info("Add Item view for fridge ID {}", id);
    	model.addAttribute("refrigerator", fridgeService.getFridgeById(id));
        return "itemAddPage";
    }
    
    @PostMapping(value="/add")
	public String addItem(@RequestParam String itemName, 
						@RequestParam String count, @RequestParam Long fridgeId, Model model) {
    	int amount = count.isEmpty() ? 0 : Integer.valueOf(count);
    	if (amount>0) {
    		fridgeService.addItem(fridgeId, itemName, amount);
    	}
    	return "redirect:/refrigerator/"+fridgeId;
    }
    
    @GetMapping("/refrigerator/delete/{id}/{itemid}")
    public String deleteFridgeItem(@PathVariable Long id, @PathVariable Long itemid, Model model) {
    	fridgeService.deleteItem(id, itemid);
        return "redirect:/refrigerator/"+id;
    }
    
    @GetMapping("/admin")
    public String admin(Model model) {
        return "adminPage";
    }
    
}