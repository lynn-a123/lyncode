package com.example.entity;

import cn.hutool.core.annotation.Alias;
import java.util.List;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 管理员
 */
@Data
@TableName("admin")
public class Admin extends Account {

    @TableId(type = IdType.AUTO)
    private Integer id;
	private String username;
	private String password;
	private String name;
	private String avatar;



}
