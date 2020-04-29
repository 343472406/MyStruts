package com.cynthia.springMybatis.service.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cynthia.springMybatis.mapper.UserMapper;
import com.cynthia.springMybatis.pojo.User;
import com.cynthia.springMybatis.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	/*
	 * 代理对象如何创建？
	 * 使用Spring前：
	 * 		1. 读取MyBatis-config.xml创建SqlSessionFactory工厂对象
	 * 		2. 使用工厂创建SqlSession
	 * 			SqlSession sqlSession = factory.openSession();
	 * 		3. 使用SqlSession创建映射借口的对象
	 * 			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
	 * -----------------------
	 * 使用Spring:
	 * 在桥梁包中已经将相关内容集成好，只需配置。
	 * 		1. 配置SqlSessionFacotry对象（SpringConfig.xml中）
	 * 		2. 配置创建SqlSession会话对象（创建xxxMapper时底层会自动创建SqlSession）
	 * 		3. 创建UserMapper映射接口的代理对象
	 * 			1. 单个创建接口的代理对象（了解）
	 * 			2. 使用包扫描创建mapper下的所有（推荐，在SpringConfig.xml中）
	 */
	@Autowired
	private UserMapper usermapper;
	
	@Override
	public List<User> selectList() {
		return usermapper.selectList();
	}
	
	@Override
	public Integer insert(User user) {
		return usermapper.insert(user);
	}

	@Override
	public Integer updateByPrimaryKey(User user) {
		return usermapper.updateByPrimaryKey(user);
	}

	@Override
	public Integer deleteByPrimaryKey(Integer id) {
		return usermapper.deleteByPrimaryKey(id);
	}

	@Override
	public User selectByPrimaryKey(Integer id) {
		return usermapper.selectByPrimaryKey(id);
	}

	@Override
	public User selectNameAndPassword(String name, String password) {
		return usermapper.selectNameAndPassword(name, password);
	}

}
