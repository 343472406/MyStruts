package com.cynthia.suit.core.init.bean;

import java.util.Map;

public class InitMapping {

	private String name;
	private String path;
	private String type;
	private String scope;
	private Map<String, InitForward> forward;

	public InitMapping() {
		super();
	}

	public InitMapping(String name, String path, String type, Map<String, InitForward> forward, String scope) {
		super();
		this.name = name;
		this.path = path;
		this.type = type;
		this.forward = forward;
		this.scope = scope;
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

	public Map<String, InitForward> getForward() {
		return forward;
	}

	public void setForward(Map<String, InitForward> forward) {
		this.forward = forward;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	@Override
	public String toString() {
		return "Mapping [name=" + name + ", path=" + path + ", type=" + type + ", forward=" + forward + 
				", scope=" + scope + "]";
	}

}
