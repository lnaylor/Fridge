package com.smartthings.project.Fridge.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Item {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
	private String name;
	private String displayName;
	private int count;
	
	public Item(String name) {
		super();
		this.displayName=name;
		this.name=name.toLowerCase().trim().replaceAll("\\s+", "");
		this.count=1;
	}
	
	public Item() {
		super();
	}
	
	public Item(String name, int count) {
		super();
		this.displayName=name;
		this.name=name.toLowerCase().trim().replaceAll("\\s+", "");
		this.count=count;
	}
	
	public long getId() {
		return this.id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}