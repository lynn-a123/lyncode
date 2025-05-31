package com.example.entity;

import cn.hutool.core.annotation.Alias;
import java.util.List;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 用户
 */
@Data
@TableName("user")
public class User extends Account {

    /** id */
    @TableId(type = IdType.AUTO)
    private Integer id;
	private String username;
	private String password;
	private String name;
	private String avatar;
	private String phone;
	private String email;



}
