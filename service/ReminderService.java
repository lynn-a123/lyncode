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
public class ReminderService {

    @Resource
    private ReminderMapper reminderMapper;

    /**
     * 新增
     */
    public void add(Reminder reminder) {
        reminderMapper.insert(reminder);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        reminderMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            reminderMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Reminder reminder) {
        reminderMapper.updateById(reminder);
    }

    /**
     * 根据ID查询
     */
    public Reminder selectById(Integer id) {
        return reminderMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Reminder> selectAll(Reminder reminder) {
        return reminderMapper.selectAll(reminder);
    }

    /**
     * 分页查询
     */
    public PageInfo<Reminder> selectPage(Reminder reminder, Integer pageNum, Integer pageSize) {
		Account currentUser = TokenUtils.getCurrentUser();
		if ("user".equals(currentUser.getRole())) {
			reminder.setUserId(currentUser.getId());
		}

        PageHelper.startPage(pageNum, pageSize);
        List<Reminder> list = this.selectAll(reminder);
		// 处理下多选框
		for (Reminder dbReminder : list) {
			List<String> methodList = new ArrayList<>();
			String method = dbReminder.getMethod();
			if (ObjectUtil.isNotEmpty(method)) {
				String[] split = method.split(",");
				methodList.addAll(Arrays.asList(split));
			}
			dbReminder.setMethodList(methodList);
		}

        return PageInfo.of(list);
    }

}