package com.smartthings.project.Fridge.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartthings.project.Fridge.model.Item;
import com.smartthings.project.Fridge.model.Refrigerator;

@Controller
public class FridgeController {
	
	
	@Value("${spring.application.name}")
    String appName;
	
	private List<Refrigerator> fridgeList = new ArrayList<>();
	
	@PostConstruct
	public void init() {
		fridgeList.add(new Refrigerator());
		fridgeList.add(new Refrigerator());
	}
 
    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("fridges", fridgeList);
        return "fridgeHome";
    }
    
    @GetMapping("/refrigerator/{id}")
    public String getFridge(@PathVariable Long id, Model model) {
    	Refrigerator refrigerator = new Refrigerator();
    	refrigerator.getItems().add(new Item("Milk"));
    	refrigerator.getItems().add(new Item("Eggs", 12));
    	model.addAttribute("refrigerator", refrigerator);
    	List<Item> itemList = refrigerator.getItems();
    	model.addAttribute("items", itemList);
        return "fridgeContents";
    }
    
    @GetMapping("/refrigerator/update/{id}/{itemid}")
    public String updateFridgeItem(@PathVariable Long id, @PathVariable Long itemid, Model model) {
    	Refrigerator refrigerator = new Refrigerator();
    	refrigerator.getItems().add(new Item("Milk"));
    	refrigerator.getItems().add(new Item("Eggs", 12));
    	Item item = new Item("Milk");
    	model.addAttribute("refrigerator", refrigerator);
    	model.addAttribute("item", item);
        return "itemUpdate";
    }
    
    @GetMapping("/refrigerator/add/{id}")
    public String addFridgeItem(@PathVariable Long id, Model model) {
    	Refrigerator refrigerator = new Refrigerator();
    	refrigerator.getItems().add(new Item("Milk"));
    	refrigerator.getItems().add(new Item("Eggs", 12));
    	model.addAttribute("refrigerator", refrigerator);
        return "itemAdd";
    }
    
    @GetMapping("/refrigerator/delete/{id}/{itemid}")
    public String deleteFridgeItem(@PathVariable Long id, @PathVariable Long itemid, Model model) {
    	Refrigerator refrigerator = new Refrigerator();
    	refrigerator.getItems().add(new Item("Milk"));
    	refrigerator.getItems().add(new Item("Eggs", 12));
    	model.addAttribute("refrigerator", refrigerator);
        return "redirect:/refrigerator/"+refrigerator.getId();
    }
    
    @PostMapping(value="/update")
	public String updateItem(@RequestParam String itemName, 
						@RequestParam String count, Model model) {
    	Refrigerator refrigerator = new Refrigerator();
    	refrigerator.getItems().add(new Item("Milk"));
    	refrigerator.getItems().add(new Item("Eggs", 12));
    	model.addAttribute("refrigerator", refrigerator);
    	List<Item> itemList = refrigerator.getItems();
    	model.addAttribute("items", itemList);
    	return "redirect:/refrigerator/"+refrigerator.getId();
    }
    
    @PostMapping(value="/add")
	public String addItem(@RequestParam String itemName, 
						@RequestParam String count, Model model) {
    	Refrigerator refrigerator = new Refrigerator();
    	refrigerator.getItems().add(new Item("Milk"));
    	refrigerator.getItems().add(new Item("Eggs", 12));
    	model.addAttribute("refrigerator", refrigerator);
    	List<Item> itemList = refrigerator.getItems();
    	model.addAttribute("items", itemList);
    	return "redirect:/refrigerator/"+refrigerator.getId();
    }
    
    
}