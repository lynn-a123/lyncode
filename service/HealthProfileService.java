package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.config.TokenUtils;
import com.example.mapper.*;
import com.example.entity.*;
import com.example.exception.CustomException;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class HealthProfileService {

    @Resource
    private HealthProfileMapper healthProfileMapper;

    /**
     * 新增
     */
    public void add(HealthProfile healthProfile) {
        healthProfileMapper.insert(healthProfile);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        healthProfileMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            healthProfileMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(HealthProfile healthProfile) {
        healthProfileMapper.updateById(healthProfile);
    }

    /**
     * 根据ID查询
     */
    public HealthProfile selectById(Integer id) {
        return healthProfileMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<HealthProfile> selectAll(HealthProfile healthProfile) {
        return healthProfileMapper.selectAll(healthProfile);
    }

    /**
     * 分页查询
     */
    public PageInfo<HealthProfile> selectPage(HealthProfile healthProfile, Integer pageNum, Integer pageSize) {
		Account currentUser = TokenUtils.getCurrentUser();
		if ("user".equals(currentUser.getRole())) {
			healthProfile.setUserId(currentUser.getId());
		}

        PageHelper.startPage(pageNum, pageSize);
        List<HealthProfile> list = this.selectAll(healthProfile);

        return PageInfo.of(list);
    }

}