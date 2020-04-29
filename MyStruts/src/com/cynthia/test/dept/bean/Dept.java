package com.cynthia.test.dept.bean;

import com.cynthia.suit.core.basic.ActionForm;

public class Dept extends ActionForm {

	private Integer id;
	private String name;
	private String deptNo;

	public Dept() {
		super();
	}

	public Dept(String name, String deptNo) {
		super();
		this.name = name;
		this.deptNo = deptNo;
	}

	public Dept(Integer id, String name, String deptNo) {
		super();
		this.id = id;
		this.name = name;
		this.deptNo = deptNo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeptNo() {
		return deptNo;
	}

	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}

	@Override
	public String toString() {
		return "Dept [id=" + id + ", name=" + name + ", deptNo=" + deptNo + "]";
	}

}
