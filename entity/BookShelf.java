package com.example.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

/**
 *用户书架
 */
@Data
@TableName("book_shelf")
public class BookShelf  {

    /** id */
    @TableId(type = IdType.AUTO)
    private Integer id;
	@Alias("书籍名称")
	private String bookName;
	@Alias("书籍id")
	private Integer bookId; 
	@Alias("书籍作者")
	private String bookAuthor;
	@Alias("书籍类型")
	private String bookTypeName;
	@Alias("书籍类型")
	private Integer bookTypeId;
	/** 用户 */
	@Alias("用户")
	private String uname;
	/** 用户 */
	@Alias("用户")
	private Integer uid;
	@Alias("书签")
	private String mark;
	@Alias("创建时间")
	@JsonFormat(pattern= "yyyy-MM-dd HH:mm",timezone = "GMT+8")
	@DateTimeFormat(pattern= "yyyy-MM-dd HH:mm")
	private Date createTime;
	@Alias("最近阅读时间")
	@JsonFormat(pattern= "yyyy-MM-dd HH:mm",timezone = "GMT+8")
	@DateTimeFormat(pattern= "yyyy-MM-dd HH:mm")
	private Date updateTime;
	@Alias("当前进度章节")
	private Integer curNums;
	@Alias("当前章节id")
	private Integer curChapterId;
	@Alias("总时长（分钟）")
	private Integer totalTime;
	//1表示近期数据 近7天
	@TableField(exist = false)
	private Integer flag;
	//排序
	@TableField(exist = false)
	private Integer sort;
}
