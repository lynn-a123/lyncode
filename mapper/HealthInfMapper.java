package com.example.mapper;

import com.example.entity.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface HealthInfMapper extends BaseMapper<HealthInf> {


    /**
     * 根据ID查询
     */
    HealthInf selectById(Integer userId);

    /**
     * 更新
     */
    int updateById(HealthInf healthInf);



}