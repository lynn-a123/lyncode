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
public class TaskService {

    @Resource
    private TaskMapper taskMapper;

    @Resource
    private ReminderMapper reminderMapper;

    /**
     * 新增
     */
    public void add(Task task) {
        taskMapper.insert(task);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        taskMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            taskMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Task task) {
        taskMapper.updateById(task);
        // 同步更新相关提醒的进度
        updateRelatedReminders(task);
    }

    /**
     * 更新相关提醒的进度
     */
    private void updateRelatedReminders(Task task) {
        if (task.getId() != null && task.getProgress() != null) {
            // 只更新与当前任务相关的提醒
            Reminder reminder = new Reminder();
            reminder.setTaskId(task.getId());
            List<Reminder> reminders = reminderMapper.selectAll(reminder);
            for (Reminder r : reminders) {
                if (r.getTaskId().equals(task.getId())) {  // 确保只更新当前任务的提醒
                    r.setProgress(task.getProgress());
                    reminderMapper.updateById(r);
                }
            }
        }
    }

    /**
     * 根据ID查询
     */
    public Task selectById(Integer id) {
        return taskMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Task> selectAll(Task task) {
        List<Task> tasks = taskMapper.selectAll(task);
        // 计算每个任务的优先级
        for (Task t : tasks) {
            calculatePriority(t);
        }
        // 按优先级排序
        tasks.sort((t1, t2) -> t2.getPriority() - t1.getPriority());
        return tasks;
    }

    /**
     * 计算任务优先级
     * 优先级计算规则：
     * 1. 截止日期越近，优先级越高
     * 2. 未开始的任务优先级高于进行中的任务
     * 3. 工作类任务优先级高于学习和生活类任务
     * 4. 进度越慢的任务优先级越高
     */
    private void calculatePriority(Task task) {
        int priority = 0;
        
        // 1. 截止日期评分 (0-40分)
        if (task.getDueDate() != null) {
            long daysUntilDue = java.time.LocalDate.parse(task.getDueDate())
                .until(java.time.LocalDate.now()).getDays();
            if (daysUntilDue <= 0) {
                priority += 40; // 已过期
            } else if (daysUntilDue <= 3) {
                priority += 35; // 3天内
            } else if (daysUntilDue <= 7) {
                priority += 30; // 一周内
            } else if (daysUntilDue <= 14) {
                priority += 20; // 两周内
            } else if (daysUntilDue <= 30) {
                priority += 15; // 一个月内
            } else {
                priority += 5; // 一个月以上
            }
        }

        // 2. 任务状态评分 (0-30分)
        if ("未开始".equals(task.getStatus())) {
            priority += 30;
        } else if ("进行中".equals(task.getStatus())) {
            priority += 20;
        } else {
            priority += 0; // 已完成
        }

        // 3. 任务分类评分 (0-20分)
        if ("工作".equals(task.getCategory())) {
            priority += 20;
        } else if ("学习".equals(task.getCategory())) {
            priority += 15;
        } else {
            priority += 10; // 生活
        }

        // 4. 任务进度评分 (0-10分)
        if (task.getProgress() != null) {
            if (task.getProgress() >= 100) {
                priority += 0; // 已完成的任务优先级最低
            } else {
                priority += (100 - task.getProgress()) / 10; // 进度越慢，分数越高
            }
        }

        // 5. 已完成的任务优先级降低
        if ("已完成".equals(task.getStatus()) || (task.getProgress() != null && task.getProgress() >= 100)) {
            priority = Math.min(priority, 40); // 已完成的任务最高优先级不超过40
        }

        task.setPriority(priority);
    }

    /**
     * 分页查询
     */
    public PageInfo<Task> selectPage(Task task, Integer pageNum, Integer pageSize) {
		Account currentUser = TokenUtils.getCurrentUser();
		if ("user".equals(currentUser.getRole())) {
			task.setUserId(currentUser.getId());
		}

        PageHelper.startPage(pageNum, pageSize);
        List<Task> list = this.selectAll(task);

        return PageInfo.of(list);
    }

}