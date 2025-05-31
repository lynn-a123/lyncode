package com.example.controller;

import com.example.common.config.TokenUtils;
import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageInfo;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.*;
import com.example.service.*;
import com.example.exception.CustomException;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.*;

import java.util.List;

/**
*  描述：任务提醒相关接口
*/
@RestController
@RequestMapping("/reminder")
public class ReminderController {

    @Resource
    ReminderService reminderService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Reminder reminder) {
		List<String> methodList = reminder.getMethodList();
		if (CollectionUtil.isNotEmpty(methodList)) {
			StringBuilder builder = new StringBuilder();
			for (String s : methodList) {
				builder.append(s).append(",");
			}
			reminder.setMethod(builder.substring(0, builder.length() - 1));
		}
		Account currentUser = TokenUtils.getCurrentUser();
		if (ObjectUtil.isEmpty(currentUser)) {
			throw new CustomException(ResultCodeEnum.USER_NOT_LOGIN);
		}
		if ("user".equals(currentUser.getRole())) {
			reminder.setUserId(currentUser.getId());
		}

        reminderService.add(reminder);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        reminderService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        reminderService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 更新
     */
    @PutMapping("/update")
    public Result update(@RequestBody Reminder reminder) {
		List<String> methodList = reminder.getMethodList();
		if (CollectionUtil.isNotEmpty(methodList)) {
			StringBuilder builder = new StringBuilder();
			for (String s : methodList) {
				builder.append(s).append(",");
			}
			reminder.setMethod(builder.substring(0, builder.length() - 1));
		}

        reminderService.updateById(reminder);
        return Result.success();
    }

    /**
     * 查询单个
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Reminder reminder = reminderService.selectById(id);
        return Result.success(reminder);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Reminder reminder) {
        List<Reminder> list = reminderService.selectAll(reminder);
        return Result.success(list);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectPage")
    public Result selectPage(
            Reminder reminder,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Reminder> pageInfo = reminderService.selectPage(reminder, pageNum, pageSize);
        return Result.success(pageInfo);
    }

	/**
	 * 描述：批量删除
	 */
	@PutMapping("/batchDel")
	public Result deleteBatch(@RequestBody List<Integer> list) {
		for (Integer id : list) {
			reminderService.deleteById(id);
		}
		return Result.success();
	}

	/**
	 * 描述：批量导出到excel
	 */
	@GetMapping("/export")
	public void export(HttpServletResponse response) throws IOException {
		List<Reminder> all = reminderService.selectAll(new Reminder());
		List<Map<String, Object>> list = new ArrayList<>(all.size());
		if (CollectionUtil.isEmpty(all)) {
			Map<String, Object> row = new LinkedHashMap<>();
			row.put("用户名称", null);
			row.put("任务名称", null);
			row.put("任务状态", null);
			row.put("提醒时间", null);
			row.put("任务进度", null);
			row.put("提醒内容", null);
			row.put("提醒方式", null);
			list.add(row);
		} else {
			for (Reminder reminder : all) {
				Map<String, Object> row = new LinkedHashMap<>();
				row.put("用户名称", reminder.getUserName());
				row.put("任务名称", reminder.getRemName());
				row.put("任务状态", reminder.getRemStatus());
				row.put("提醒时间", reminder.getRemindTime());
				row.put("任务进度", reminder.getProgress());
				row.put("提醒内容", reminder.getRemindText());
				row.put("提醒方式", reminder.getMethod());
				list.add(row);
			}
		}
		ExcelWriter writer = ExcelUtil.getWriter(true);
		writer.write(list, true);
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
		response.setHeader("Content-Disposition","attachment;filename=reminderInfoExcel.xlsx");
		ServletOutputStream out = response.getOutputStream();
		writer.flush(out, true);
		writer.close();
		IoUtil.close(System.out);
	}

	/**
	 * 描述：通过excel批量导入
	 */
	@PostMapping("/upload")
	public Result upload(MultipartFile file) throws IOException {
		List<Reminder> infoList = ExcelUtil.getReader(file.getInputStream()).readAll(Reminder.class);
		if (!CollectionUtil.isEmpty(infoList)) {
			for (Reminder info : infoList) {
				try {
					reminderService.add(info);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return Result.success();
	}

}
