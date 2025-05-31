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
public class InvestmentService {

    @Resource
    private InvestmentMapper investmentMapper;

    /**
     * 新增
     */
    public void add(Investment investment) {
        investmentMapper.insert(investment);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        investmentMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            investmentMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Investment investment) {
        investmentMapper.updateById(investment);
    }

    /**
     * 根据ID查询
     */
    public Investment selectById(Integer id) {
        return investmentMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Investment> selectAll(Investment investment) {
        return investmentMapper.selectAll(investment);
    }

    /**
     * 分页查询
     */
    public PageInfo<Investment> selectPage(Investment investment, Integer pageNum, Integer pageSize) {
		Account currentUser = TokenUtils.getCurrentUser();
		if ("user".equals(currentUser.getRole())) {
			investment.setUserId(currentUser.getId());
		}

        PageHelper.startPage(pageNum, pageSize);
        List<Investment> list = this.selectAll(investment);

        return PageInfo.of(list);
    }

}