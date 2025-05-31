package com.example.entity;

import cn.hutool.core.annotation.Alias;
import java.util.List;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 检查报告
 */
@Data
@TableName("medical_report")
public class MedicalReport  {

    @TableId(type = IdType.AUTO)
    private Integer id;
	@TableField(exist = false)
	private String userName;
	@Alias("检查项目")
	private String project;
	@Alias("检查结果")
	private String result;
	@Alias("报告日期")
	private String reportDate;
	private Integer userId;



}
