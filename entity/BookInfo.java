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
 *书籍信息
 */
@Data
@TableName("book_info")
public class BookInfo  {

    /** id */
    @TableId(type = IdType.AUTO)
    private Integer id;
	@Alias("分类")
	private String typeName;
	@Alias("分类")
	private Integer typeId; 
	@Alias("作者")
	private String author;
	@Alias("书籍名称")
	private String name;
	@Alias("书籍封面")
	private String logo;
	@Alias("电子文件")
	private String fileUrl;
	@Alias("听书文件")
	private String listeningUrl;
	@Alias("书籍版权信息")
	private String version;
	@Alias("简介")
	private String content;
	@Alias("书评")
	private String comment;
	@Alias("章节数")
	private Integer nums;
	@Alias("阅读次数")
	private Integer readNums;
	@Alias("创建时间")
	@JsonFormat(pattern= "yyyy-MM-dd HH:mm",timezone = "GMT+8")
	@DateTimeFormat(pattern= "yyyy-MM-dd HH:mm")
	private Date createTime;
	@Alias("修改时间")
	@JsonFormat(pattern= "yyyy-MM-dd HH:mm",timezone = "GMT+8")
	@DateTimeFormat(pattern= "yyyy-MM-dd HH:mm")
	private Date updateTime;
	//排序
	@TableField(exist = false)
	private Integer sort;
	/** 用户 */
	@Alias("用户")
	private String uname;
	/** 用户 */
	@Alias("用户")
	private Integer uid;
}
