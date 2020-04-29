package com.cynthia.struts.bean;

import java.util.Map;

public class MappingAction {

	private String name;
	private String path;
	private String type;
	private Map<String, String> forward;

	public MappingAction() {
		super();
	}

	public MappingAction(String name, String path, String type, Map<String, String> forward) {
		super();
		this.name = name;
		this.path = path;
		this.type = type;
		this.forward = forward;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, String> getForward() {
		return forward;
	}

	public void setForward(Map<String, String> forward) {
		this.forward = forward;
	}

	@Override
	public String toString() {
		return "MappingAction [name=" + name + ", path=" + path + ", type=" + type + ", forward=" + forward + "]";
	}

}
