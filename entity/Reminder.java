package com.example.entity;

import cn.hutool.core.annotation.Alias;
import java.util.List;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 任务提醒
 */
@Data
@TableName("reminder")
public class Reminder  {

    /** id */
    @TableId(type = IdType.AUTO)
    private Integer id;
	@TableField(exist = false)
	private String userName;
	@TableField(exist = false)
	private String remName;
	@TableField(exist = false)
	private String remStatus;
	@TableField(exist = false)
	private String remindTime;
	@Alias("任务进度")
	private Integer progress;
	@Alias("提醒内容")
	private String remindText;
	@Alias("提醒方式")
	private String method;
	@TableField(exist = false)
	private List<String> methodList;
	/** 任务记录Id */
	private Integer taskId;
	/** 用户Id */
	private Integer userId;



}
