package com.smartthings.project.Fridge.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.smartthings.project.Fridge.model.Refrigerator;
import com.smartthings.project.Fridge.service.FridgeService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=FridgeController.class)
public class FridgeControllerUnitTest {
	
	private MockMvc mockMvc;
	
	@MockBean
	FridgeService fridgeService;
	
	@InjectMocks
	FridgeController fridgeController;
	
	@Mock
	Refrigerator fridge = new Refrigerator();
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(fridgeController).build();
	}
	
	@Test
	public void testLogin() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/login")).andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void testAccessDenied() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/accessDenied")).andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}
	
	@Test
	public void testHomePageRedirect() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/")).andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}
	
	@Test
	public void testFridgeHome() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/fridgeHome")).andExpect(MockMvcResultMatchers.status().isOk());
		Mockito.verify(fridgeService, Mockito.times(1)).getAllFridges();
	}
	
	@Test
	public void testGetFridge() throws Exception {
		Mockito.when(fridgeService.getFridgeById(1L)).thenReturn(fridge);
		mockMvc.perform(MockMvcRequestBuilders.get("/refrigerator/1")).andExpect(MockMvcResultMatchers.status().isOk());
		Mockito.verify(fridgeService, Mockito.times(1)).getFridgeById(1L);
		Mockito.verify(fridge, Mockito.times(1)).getItems();
		Mockito.verify(fridgeService, Mockito.times(1)).isSodaError();
	}
	
	@Test
	public void testUpdateItemView() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/refrigerator/update/1/2")).andExpect(MockMvcResultMatchers.status().isOk());
		Mockito.verify(fridgeService, Mockito.times(1)).getFridgeById(1L);
		Mockito.verify(fridgeService, Mockito.times(1)).getItemById(2L);
	}
	
	@Test
	public void testUpdateAmountNotEmpty() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/update")
				.param("itemName", "Eggs")
				.param("count", "12")
				.param("fridgeId", "1")
				.param("itemId", "2")).andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	
		Mockito.verify(fridgeService, Mockito.times(1)).deleteItem(1L, 2L);
		Mockito.verify(fridgeService, Mockito.times(1)).addItem(1L, "Eggs", 12);
	}
	
	@Test
	public void testUpdateAmountEmpty() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/update")
				.param("itemName", "Eggs")
				.param("count", "")
				.param("fridgeId", "1")
				.param("itemId", "2")).andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	
		Mockito.verify(fridgeService, Mockito.times(1)).deleteItem(1L, 2L);
		Mockito.verify(fridgeService, Mockito.times(0)).addItem(1L, "Eggs", 0);
	}
	
	@Test
	public void testAddItemView() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/refrigerator/add/1")).andExpect(MockMvcResultMatchers.status().isOk());
		Mockito.verify(fridgeService, Mockito.times(1)).getFridgeById(1L);
	}
	
	@Test
	public void testAddItemNotEmpty() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/add")
				.param("itemName", "Eggs")
				.param("count", "12")
				.param("fridgeId", "1")).andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	
		Mockito.verify(fridgeService, Mockito.times(1)).addItem(1L, "Eggs", 12);
	}
	
	@Test
	public void testAddItemEmpty() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/add")
				.param("itemName", "Eggs")
				.param("count", "")
				.param("fridgeId", "1")).andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	
		Mockito.verify(fridgeService, Mockito.times(0)).addItem(1L, "Eggs", 0);
	}
	
	@Test
	public void testDeleteItem() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/refrigerator/delete/1/2")).andExpect(MockMvcResultMatchers.status().is3xxRedirection());
		Mockito.verify(fridgeService, Mockito.times(1)).deleteItem(1L, 2L);
	}
	
	@Test
	public void testAdmin() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/admin")).andExpect(MockMvcResultMatchers.status().isOk());
	}
	
}