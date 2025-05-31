package com.example.entity;

import cn.hutool.core.annotation.Alias;
import java.util.List;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 健康档案
 */
@Data
@TableName("health_profile")
public class HealthProfile  {

    @TableId(type = IdType.AUTO)
    private Integer id;
	@TableField(exist = false)
	private String userName;
	@Alias("年龄")
	private Integer age;
	@Alias("性别")
	private String gender;
	private String healthReport;
	@Alias("病史")
	private String history;
	private Integer userId;



}
