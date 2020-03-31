package com.smartthings.project.Fridge.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.smartthings.project.Fridge.model.Refrigerator;

@Controller
public class FridgeController {
	
	
	@Value("${spring.application.name}")
    String appName;
 
    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        List<Refrigerator> fridgeList = new ArrayList<>();
        fridgeList.add(new Refrigerator());
        model.addAttribute("fridges", fridgeList);
        return "fridgeHome";
    }
    
    @GetMapping("/refrigerator/{id}")
    public String getFridge(@PathVariable Long id, Model model) {
    	model.addAttribute("refrigerator", new Refrigerator());
        return "fridgeContents";
    }
}