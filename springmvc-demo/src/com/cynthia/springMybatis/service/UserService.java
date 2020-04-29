package com.cynthia.springMybatis.service;

import java.util.List;

import com.cynthia.springMybatis.pojo.User;

public interface UserService {
	/* DML：增删改 */
	Integer insert(User user);
	Integer updateByPrimaryKey(User user);
	Integer deleteByPrimaryKey(Integer id);
	
	/* DQL:查询 */
	User selectByPrimaryKey(Integer id);
	List<User> selectList();
	User selectNameAndPassword(String name, String password);
	
}
