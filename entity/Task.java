package com.example.entity;

import cn.hutool.core.annotation.Alias;
import java.util.List;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 任务记录
 */
@Data
@TableName("task")
public class Task  {

    @TableId(type = IdType.AUTO)
    private Integer id;
	@TableField(exist = false)
	private String userName;
	@Alias("任务分类")
	private String category;
	@Alias("任务标题")
	private String title;
	@Alias("任务描述")
	private String description;
	@Alias("截止日期")
	private String dueDate;
	@Alias("任务状态")
	private String status;
	@Alias("优先级")
	private Integer priority;
	@Alias("任务进度")
	private Integer progress;
	private Integer userId;



}
