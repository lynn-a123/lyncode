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
 *文档信息
 */
@Data
@TableName("word_info")
public class WordInfo  {

    /** id */
    @TableId(type = IdType.AUTO)
    private Integer id;
	/** 用户 */
	@Alias("用户")
	private String uname;
	/** 用户 */
	@Alias("用户")
	private Integer uid;
	@Alias("分类")
	private String typeName;
	@Alias("分类")
	private Integer typeId; 
	@Alias("文件名称")
	private String fileName;
	@Alias("文件大小")
	private String fileSize;
	@Alias("文件存储地址")
	private String fileUrl;
	@Alias("是否收藏")
	private Integer isCollect;
	@Alias("是否保密箱")
	private Integer isPwd;
	@Alias("保密箱密码")
	private String pwd;
	@Alias("下载次数")
	private Integer downloadNums;
	@Alias("创建时间")
	@JsonFormat(pattern= "yyyy-MM-dd HH:mm",timezone = "GMT+8")
	@DateTimeFormat(pattern= "yyyy-MM-dd HH:mm")
	private Date createTime;
	@Alias("修改时间")
	@JsonFormat(pattern= "yyyy-MM-dd HH:mm",timezone = "GMT+8")
	@DateTimeFormat(pattern= "yyyy-MM-dd HH:mm")
	private Date updateTime;
	
	//1表示近期数据 近7天
	@TableField(exist = false)
	private Integer flag;
	//排序
	@TableField(exist = false)
	private Integer sort;
}
