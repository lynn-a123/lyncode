package com.example.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

/**
 *用户阅读设置
 */
@Data
@TableName("book_set")
public class BookSet  {

    /** id */
    @TableId(type = IdType.AUTO)
    private Integer id;
	/** 用户 */
	@Alias("用户")
	private String uname;
	/** 用户 */
	@Alias("用户")
	private Integer uid;
	@Alias("字体颜色")
	private String color;
	@Alias("背景颜色")
	private String bgcolor;
	@Alias("字体大小")
	private String fontSize;
	@Alias("字体加重")
	private String fontBold;
}
