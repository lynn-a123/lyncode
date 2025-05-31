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
*  描述：服药提醒相关接口
*/
@RestController
@RequestMapping("/medicationReminder")
public class MedicationReminderController {

    @Resource
    MedicationReminderService medicationReminderService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody MedicationReminder medicationReminder) {
		List<String> noticeList = medicationReminder.getNoticeList();
		if (CollectionUtil.isNotEmpty(noticeList)) {
			StringBuilder builder = new StringBuilder();
			for (String s : noticeList) {
				builder.append(s).append(",");
			}
			medicationReminder.setNotice(builder.substring(0, builder.length() - 1));
		}
		Account currentUser = TokenUtils.getCurrentUser();
		if (ObjectUtil.isEmpty(currentUser)) {
			throw new CustomException(ResultCodeEnum.USER_NOT_LOGIN);
		}
		if ("user".equals(currentUser.getRole())) {
			medicationReminder.setUserId(currentUser.getId());
		}

        medicationReminderService.add(medicationReminder);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        medicationReminderService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        medicationReminderService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 更新
     */
    @PutMapping("/update")
    public Result update(@RequestBody MedicationReminder medicationReminder) {
		List<String> noticeList = medicationReminder.getNoticeList();
		if (CollectionUtil.isNotEmpty(noticeList)) {
			StringBuilder builder = new StringBuilder();
			for (String s : noticeList) {
				builder.append(s).append(",");
			}
			medicationReminder.setNotice(builder.substring(0, builder.length() - 1));
		}

        medicationReminderService.updateById(medicationReminder);
        return Result.success();
    }

    /**
     * 查询单个
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        MedicationReminder medicationReminder = medicationReminderService.selectById(id);
        return Result.success(medicationReminder);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(MedicationReminder medicationReminder) {
        List<MedicationReminder> list = medicationReminderService.selectAll(medicationReminder);
        return Result.success(list);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectPage")
    public Result selectPage(
            MedicationReminder medicationReminder,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<MedicationReminder> pageInfo = medicationReminderService.selectPage(medicationReminder, pageNum, pageSize);
        return Result.success(pageInfo);
    }

	/**
	 * 描述：批量删除
	 */
	@PutMapping("/batchDel")
	public Result deleteBatch(@RequestBody List<Integer> list) {
		for (Integer id : list) {
			medicationReminderService.deleteById(id);
		}
		return Result.success();
	}

	/**
	 * 描述：批量导出到excel
	 */
	@GetMapping("/export")
	public void export(HttpServletResponse response) throws IOException {
		List<MedicationReminder> all = medicationReminderService.selectAll(new MedicationReminder());
		List<Map<String, Object>> list = new ArrayList<>(all.size());
		if (CollectionUtil.isEmpty(all)) {
			Map<String, Object> row = new LinkedHashMap<>();
			row.put("患者", null);
			row.put("药物名称", null);
			row.put("剂量", null);
			row.put("服药频次", null);
			row.put("注意事项", null);
			list.add(row);
		} else {
			for (MedicationReminder medicationReminder : all) {
				Map<String, Object> row = new LinkedHashMap<>();
				row.put("患者", medicationReminder.getMedName());
				row.put("药物名称", medicationReminder.getMedicationName());
				row.put("剂量", medicationReminder.getDosage());
				row.put("服药频次", medicationReminder.getFrequency());
				row.put("注意事项", medicationReminder.getNotice());
				list.add(row);
			}
		}
		ExcelWriter writer = ExcelUtil.getWriter(true);
		writer.write(list, true);
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
		response.setHeader("Content-Disposition","attachment;filename=medicationReminderInfoExcel.xlsx");
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
		List<MedicationReminder> infoList = ExcelUtil.getReader(file.getInputStream()).readAll(MedicationReminder.class);
		if (!CollectionUtil.isEmpty(infoList)) {
			for (MedicationReminder info : infoList) {
				try {
					medicationReminderService.add(info);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return Result.success();
	}

}
