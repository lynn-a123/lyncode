package com.example.entity;

import cn.hutool.core.annotation.Alias;
import java.util.List;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 记账日记
 */
@Data
@TableName("financial_account")
public class FinancialAccount  {

    @TableId(type = IdType.AUTO)
    private Integer id;
	@TableField(exist = false)
	private String userName;
	@Alias("类型")
	private String type;
	@Alias("收支内容")
	private String sort;
	@Alias("金额")
	private Double balance;
	@Alias("备注")
	private String other;
	@Alias("时间")
	private String time;
	private Integer userId;



}
