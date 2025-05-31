package com.example.entity;

import cn.hutool.core.annotation.Alias;
import java.util.List;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 服药提醒
 */
@Data
@TableName("medication_reminder")
public class MedicationReminder  {

    /** id */
    @TableId(type = IdType.AUTO)
    private Integer id;
	@TableField(exist = false)
	private String medName;
	@Alias("药物名称")
	private String medicationName;
	@Alias("剂量")
	private String dosage;
	@Alias("服药频次")
	private String frequency;
	@Alias("注意事项")
	private String notice;
	@TableField(exist = false)
	private List<String> noticeList;
	private Integer userId;



}
