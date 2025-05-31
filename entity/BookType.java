package com.example.entity;

import cn.hutool.core.annotation.Alias;
import java.util.List;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 *书籍分类
 */
@Data
@TableName("book_type")
public class BookType  {

    /** id */
    @TableId(type = IdType.AUTO)
    private Integer id;
	/** 名称 */
	@Alias("名称")
	private String title;
	/** 排序 */
	@Alias("排序")
	private Integer sort;



}
