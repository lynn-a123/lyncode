package com.example.mapper;

import com.example.entity.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    /**
      * 查询所有
    */
    List<User> selectAll(User user);

    /**
      * 根据ID查询
    */
    User selectById(Integer id);

    /**
      * 删除
    */
    int deleteById(Integer id);

	@Select("select * from user where `username` = #{name}")
	User selectByUsername(@Param("name") String userName);



}