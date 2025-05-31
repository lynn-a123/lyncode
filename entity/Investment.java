package com.example.entity;

import cn.hutool.core.annotation.Alias;
import java.util.List;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 理财产品
 */
@Data
@TableName("investment")
public class Investment  {

    @TableId(type = IdType.AUTO)
    private Integer id;
	@TableField(exist = false)
	private String investName;
	@Alias("产品名称")
	private String productName;
	@Alias("投资金额")
	private Double amount;
	@Alias("到期日")
	private String maturityDate;
	@Alias("预期收益率（%）")
	private Integer expectedReturn;
	private Integer userId;



}
