package com.cynthia.suit.core.init.bean;

public class InitForward {

	private String name;
	private String path;
	private boolean redirect;

	public InitForward() {
		super();
	}

	public InitForward(String name, String path, boolean redirect) {
		super();
		this.name = name;
		this.path = path;
		this.redirect = redirect;
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

	public boolean getRedirect() {
		return redirect;
	}

	public void setRedirect(boolean redirect) {
		this.redirect = redirect;
	}

	@Override
	public String toString() {
		return "InitForward [name=" + name + ", path=" + path + ", redirect=" + redirect + "]";
	}

}
