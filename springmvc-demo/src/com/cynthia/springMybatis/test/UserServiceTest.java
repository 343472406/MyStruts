package com.cynthia.springMybatis.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cynthia.springMybatis.pojo.User;
import com.cynthia.springMybatis.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/springdao.xml")
public class UserServiceTest {

	/* 是配置service的测试类，不是impl */
	@Autowired
	private UserService service;

	@Test
	public void testSelectList() {
		List<User> list = service.selectList();
		for (User user : list) {
			System.out.println(user);
		}
	}

	@Test
	public void testInsert() {
	}

	@Test
	public void testUpdateByPrimaryKey() {
	}

	@Test
	public void testDeleteByPrimaryKey() {
	}

	@Test
	public void testSelectByPrimaryKey() {
	}

}
