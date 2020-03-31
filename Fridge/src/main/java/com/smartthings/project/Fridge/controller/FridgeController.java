package com.smartthings.project.Fridge.controller;

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
	
	@Autowired
	FridgeService fridgeService;
	
	@GetMapping("/login")
    public String login(Model model) {
        return "login";
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
        model.addAttribute("fridges", fridgeService.getAllFridges());
        return "fridgeHome";
    }
    
    @GetMapping("/refrigerator/{id}")
    public String getFridge(@PathVariable Long id, Model model) {
    	Refrigerator refrigerator = fridgeService.getFridgeById(id);
    	
    	model.addAttribute("refrigerator", refrigerator);
    	model.addAttribute("items", refrigerator.getItems());
    	model.addAttribute("sodaError", fridgeService.isSodaError());
        return "fridgeContents";
    }
    
    @GetMapping("/refrigerator/update/{id}/{itemid}")
    public String updateItemView(@PathVariable Long id, @PathVariable Long itemid, Model model) {
    	model.addAttribute("refrigerator", fridgeService.getFridgeById(id));
    	model.addAttribute("item", fridgeService.getItemById(itemid));
        return "itemUpdate";
    }
    
    @PostMapping(value="/update")
	public String updateItem(@RequestParam String itemName, @RequestParam int count, @RequestParam Long fridgeId, 
			@RequestParam Long itemId, Model model) {
    	fridgeService.deleteItem(fridgeId, itemId);
    	Long id = fridgeService.addItem(fridgeId, itemName, count);
    	
    	return "redirect:/refrigerator/"+id;
    }
    
    @GetMapping("/refrigerator/add/{id}")
    public String addItemView(@PathVariable Long id, Model model) {
    	model.addAttribute("refrigerator", fridgeService.getFridgeById(id));
        return "itemAdd";
    }
    
    @PostMapping(value="/add")
	public String addItem(@RequestParam String itemName, 
						@RequestParam int count, @RequestParam Long fridgeId, Model model) {
    	Long id = fridgeService.addItem(fridgeId, itemName, count);
    	return "redirect:/refrigerator/"+id;
    }
    
    @GetMapping("/refrigerator/delete/{id}/{itemid}")
    public String deleteFridgeItem(@PathVariable Long id, @PathVariable Long itemid, Model model) {
    	Long fridgeId = fridgeService.deleteItem(id, itemid);
        return "redirect:/refrigerator/"+fridgeId;
    }
    
}