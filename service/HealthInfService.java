package com.example.service;

import com.example.entity.HealthInf;
import com.example.mapper.HealthInfMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.example.mapper.*;
import com.example.entity.*;

@Service
public class HealthInfService {

    @Resource
    private HealthInfMapper healthInfMapper;

    /**
     * 修改
     */
    public void updateById(HealthInf healthInf) {
        healthInfMapper.updateById(healthInf);
    }

    /**
     * 根据ID查询
     */
    public HealthInf selectById(Integer userId) {
        return healthInfMapper.selectById(userId);
    }



}