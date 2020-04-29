package com.cynthia.springMybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cynthia.springMybatis.pojo.User;

/* 配置应由MyBatis的SQLSession创建，这里不写@Repository */
public interface UserMapper {
	
	/* DML：增删改 */
	@Insert("INSERT INTO USER  (NAME,PASSWORD,AGE) VALUES(#{name},#{password},#{age}")
	Integer insert(User user);
	
	@Update("UPDATE USER SET NAME=#{name},PASSWORD=#{password},AGE=#{age} WHERE ID=#{id}")
	Integer updateByPrimaryKey(User user);
	
	@Delete("DELETE FROM USER WHERE ID=#{id}")
	Integer deleteByPrimaryKey(Integer id);
	
	/* DQL:查询 */
	@Select("SELECT * FROM USER WHERE ID=#{id}")
	User selectByPrimaryKey(Integer id);
	
	@Select("SELECT * FROM USER")
	List<User> selectList();

	@Select("SELECT * FROM USER WHERE NAME=#{name} AND PASSWORD=#{password}")
	User selectNameAndPassword(@Param("name")String name, @Param("password")String password);
	
}
