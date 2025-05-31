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
public class MedicationReminderService {

    @Resource
    private MedicationReminderMapper medicationReminderMapper;

    /**
     * 新增
     */
    public void add(MedicationReminder medicationReminder) {
        medicationReminderMapper.insert(medicationReminder);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        medicationReminderMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            medicationReminderMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(MedicationReminder medicationReminder) {
        medicationReminderMapper.updateById(medicationReminder);
    }

    /**
     * 根据ID查询
     */
    public MedicationReminder selectById(Integer id) {
        return medicationReminderMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<MedicationReminder> selectAll(MedicationReminder medicationReminder) {
        return medicationReminderMapper.selectAll(medicationReminder);
    }

    /**
     * 分页查询
     */
    public PageInfo<MedicationReminder> selectPage(MedicationReminder medicationReminder, Integer pageNum, Integer pageSize) {
		Account currentUser = TokenUtils.getCurrentUser();
		if ("user".equals(currentUser.getRole())) {
			medicationReminder.setUserId(currentUser.getId());
		}

        PageHelper.startPage(pageNum, pageSize);
        List<MedicationReminder> list = this.selectAll(medicationReminder);
		// 处理下多选框
		for (MedicationReminder dbMedicationReminder : list) {
			List<String> noticeList = new ArrayList<>();
			String notice = dbMedicationReminder.getNotice();
			if (ObjectUtil.isNotEmpty(notice)) {
				String[] split = notice.split(",");
				noticeList.addAll(Arrays.asList(split));
			}
			dbMedicationReminder.setNoticeList(noticeList);
		}

        return PageInfo.of(list);
    }

}