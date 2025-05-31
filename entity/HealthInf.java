package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("health_inf")
public class HealthInf {
    @TableField(exist = false)
    private Double weight;
    private Double height;
    private Integer userId;
}
