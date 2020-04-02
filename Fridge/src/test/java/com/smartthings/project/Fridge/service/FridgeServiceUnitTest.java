package com.smartthings.project.Fridge.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.smartthings.project.Fridge.model.Item;
import com.smartthings.project.Fridge.model.Refrigerator;
import com.smartthings.project.Fridge.repository.FridgeRepository;
import com.smartthings.project.Fridge.repository.ItemRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=FridgeService.class)
public class FridgeServiceUnitTest {
	
	@MockBean
	FridgeRepository fridgeRepository;
	
	@MockBean
	ItemRepository itemRepository;
	
	Refrigerator fridge = new Refrigerator();
	Refrigerator fridge2 = new Refrigerator();
	List<Refrigerator> fridgeList = new ArrayList<>();
	
	@InjectMocks
	FridgeService fridgeService;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(fridgeRepository.findById(1L)).thenReturn(Optional.of(fridge));
		fridge.getItems().clear();
		Item item = new Item("Milk", 1);
		fridge.getItems().add(item);
		fridge2.getItems().clear();
		fridgeService.setSodaError(false);
		fridgeList.add(fridge);
		fridgeList.add(fridge2);
		Mockito.when(fridgeRepository.findAll()).thenReturn(fridgeList);
		Mockito.when(itemRepository.findById(2L)).thenReturn(Optional.of(item));
	}
	
	@Test
	public void testInit() {
		fridgeService.init();
		Mockito.verify(fridgeRepository, Mockito.times(2)).save(Mockito.any());
		Mockito.verify(fridgeRepository, Mockito.times(1)).count();
	}
	
	@Test
	public void testGetAllFridges() {
		Iterable<Refrigerator> fridgeList = new ArrayList<>();
		Mockito.when(fridgeRepository.findAll()).thenReturn(fridgeList);
		assertEquals(fridgeList, fridgeService.getAllFridges());
		
	}
	
	@Test
	public void testGetFridgeById() {
		Refrigerator r = new Refrigerator();
		Optional<Refrigerator> optR = Optional.of(r);
		Mockito.when(fridgeRepository.findById(1L)).thenReturn(optR);
		assertEquals(r, fridgeService.getFridgeById(1L));
		
	}
	
	@Test
	public void testGetItemById() {
		Item i = new Item();
		Optional<Item> optI = Optional.of(i);
		Mockito.when(itemRepository.findById(1L)).thenReturn(optI);
		assertEquals(i, fridgeService.getItemById(1L));
		
	}
	
	@Test
	public void testAddNewItem() {
		fridgeService.addItem(1L, "Eggs", 12);
		Mockito.verify(itemRepository, Mockito.times(1)).save(Mockito.any());
		Mockito.verify(fridgeRepository, Mockito.times(1)).save(fridge);
		boolean foundEggs=false;
		for (Item i : fridge.getItems()) {
			if (i.getDisplayName().equals("Eggs")) {
				assertEquals(12, i.getCount());
				assertEquals("eggs", i.getName());
				foundEggs=true;
			}
		}
		assertTrue(foundEggs);
	}
	
	@Test
	public void testAddExistingItem() {	
		fridgeService.addItem(1L, "Milk", 1);
		Mockito.verify(itemRepository, Mockito.times(1)).save(Mockito.any());
		Mockito.verify(fridgeRepository, Mockito.times(1)).save(fridge);
		boolean foundMilk=false;
		for (Item i : fridge.getItems()) {
			if (i.getDisplayName().equals("Milk")) {
				if (foundMilk) {
					Assert.fail("Should not have duplicate milk");
				}
				assertEquals(2, i.getCount());
				assertEquals("milk", i.getName());
				foundMilk=true;
			}
		}
		assertTrue(foundMilk);
	}
	
	@Test
	public void testAddCanOfSodaNewItemUnderThreshold() {
		fridgeService.addItem(1L, "Can of Soda", 6);
		Mockito.verify(itemRepository, Mockito.times(1)).save(Mockito.any());
		Mockito.verify(fridgeRepository, Mockito.times(1)).save(fridge);
		boolean foundSoda=false;
		for (Item i : fridge.getItems()) {
			if (i.getDisplayName().equals("Can of Soda")) {
				assertEquals(6, i.getCount());
				assertEquals("canofsoda", i.getName());
				foundSoda=true;
			}
		}
		assertFalse(fridgeService.isSodaError());
		assertTrue(foundSoda);
	}
	
	@Test
	public void testAddCanOfSodaNewItemOverThreshold() {
		fridge2.getItems().add(new Item("can of soda", 7));
		fridgeService.addItem(1L, "Can of Soda", 6);
		Mockito.verify(itemRepository, Mockito.times(1)).save(Mockito.any());
		Mockito.verify(fridgeRepository, Mockito.times(1)).save(fridge);
		boolean foundSoda=false;
		for (Item i : fridge.getItems()) {
			if (i.getDisplayName().equals("Can of Soda")) {
				assertEquals(5, i.getCount());
				assertEquals("canofsoda", i.getName());
				foundSoda=true;
			}
		}
		assertTrue(foundSoda);
		assertTrue(fridgeService.isSodaError());
	}
	
	@Test
	public void testAddCanOfSodaExistingItemUnderThreshold() {
		fridge.getItems().add(new Item("Can of Soda", 6));
		fridgeService.addItem(1L, "Can of Soda", 6);
		Mockito.verify(itemRepository, Mockito.times(1)).save(Mockito.any());
		Mockito.verify(fridgeRepository, Mockito.times(1)).save(fridge);
		boolean foundSoda=false;
		for (Item i : fridge.getItems()) {
			if (i.getDisplayName().equals("Can of Soda")) {
				assertEquals(12, i.getCount());
				assertEquals("canofsoda", i.getName());
				foundSoda=true;
			}
		}
		assertFalse(fridgeService.isSodaError());
		assertTrue(foundSoda);
	}
	
	@Test
	public void testAddCanOfSodaExistingItemOverThreshold() {
		fridge.getItems().add(new Item("Can of Soda", 4));
		fridge2.getItems().add(new Item("can of soda", 7));
		fridgeService.addItem(1L, "Can of Soda", 6);
		Mockito.verify(itemRepository, Mockito.times(1)).save(Mockito.any());
		Mockito.verify(fridgeRepository, Mockito.times(1)).save(fridge);
		boolean foundSoda=false;
		for (Item i : fridge.getItems()) {
			if (i.getDisplayName().equals("Can of Soda")) {
				assertEquals(5, i.getCount());
				assertEquals("canofsoda", i.getName());
				foundSoda=true;
			}
		}
		assertTrue(foundSoda);
		assertTrue(fridgeService.isSodaError());
	}
	
	@Test
	public void testDeleteItem() {
		fridgeService.deleteItem(1L, 2L);
		Mockito.verify(itemRepository, Mockito.times(1)).deleteById(2L);
		Mockito.verify(fridgeRepository, Mockito.times(1)).save(fridge);
		assertEquals(0, fridge.getItems().size());
	}
}