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
 *书籍章节信息
 */
@Data
@TableName("book_chapter")
public class BookChapter  {

    /** id */
    @TableId(type = IdType.AUTO)
    private Integer id;
	@Alias("书籍名称")
	private String bookName;
	@Alias("书籍id")
	private Integer bookId; 
	@Alias("章节标题")
	private String title;
	@Alias("章节内容")
	private String content;
	@Alias("创建时间")
	@JsonFormat(pattern= "yyyy-MM-dd HH:mm",timezone = "GMT+8")
	@DateTimeFormat(pattern= "yyyy-MM-dd HH:mm")
	private Date createTime;
	//排序
	@Alias("排序")
	private Integer sort;
	@Alias("听书文件")
	private String listeningUrl;
}
