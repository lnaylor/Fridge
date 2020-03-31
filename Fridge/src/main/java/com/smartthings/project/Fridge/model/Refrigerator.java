package com.smartthings.project.Fridge.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Refrigerator {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@ManyToMany
	private List<Item> items;
	
	public Refrigerator() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public List<Item> getItems() {
		if (this.items==null) {
			this.items=new ArrayList<>();
		}
		return this.items;
	}
}