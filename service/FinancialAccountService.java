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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


@Service
public class FinancialAccountService {

    @Resource
    private FinancialAccountMapper financialAccountMapper;

    public List<FinancialAccount> getByUserId(Integer userId) {
        QueryWrapper<FinancialAccount> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return financialAccountMapper.selectList(queryWrapper);
    }
    /**
     * 新增
     */
    public void add(FinancialAccount financialAccount) {
        financialAccountMapper.insert(financialAccount);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        financialAccountMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            financialAccountMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(FinancialAccount financialAccount) {
        financialAccountMapper.updateById(financialAccount);
    }

    /**
     * 根据ID查询
     */
    public FinancialAccount selectById(Integer id) {
        return financialAccountMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<FinancialAccount> selectAll(FinancialAccount financialAccount) {
        return financialAccountMapper.selectAll(financialAccount);
    }

    /**
     * 分页查询
     */
    public PageInfo<FinancialAccount> selectPage(FinancialAccount financialAccount, Integer pageNum, Integer pageSize) {
		Account currentUser = TokenUtils.getCurrentUser();
		if ("user".equals(currentUser.getRole())) {
			financialAccount.setUserId(currentUser.getId());
		}

        PageHelper.startPage(pageNum, pageSize);
        List<FinancialAccount> list = this.selectAll(financialAccount);

        return PageInfo.of(list);
    }

}