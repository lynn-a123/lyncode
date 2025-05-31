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
 *用户书籍操作记录
 */
@Data
@TableName("book_record")
public class BookRecord  {

    /** id */
    @TableId(type = IdType.AUTO)
    private Integer id;
	@Alias("书籍名称")
	private String bookName;
	@Alias("书籍id")
	private Integer bookId; 
	/** 用户 */
	@Alias("用户")
	private String uname;
	/** 用户 */
	@Alias("用户")
	private Integer uid;
	@Alias("内容")
	private String content;
	@Alias("创建时间")
	@JsonFormat(pattern= "yyyy-MM-dd HH:mm",timezone = "GMT+8")
	@DateTimeFormat(pattern= "yyyy-MM-dd HH:mm")
	private Date createTime;
	@Alias("类型 1笔记")
	private Integer type;
}
