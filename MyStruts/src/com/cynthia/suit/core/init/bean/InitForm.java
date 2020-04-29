package com.cynthia.suit.core.init.bean;

public class InitForm {

	private String name;
	private String type;

	public InitForm() {
		super();
	}

	public InitForm(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Form [name=" + name + ", type=" + type + "]";
	}

}
