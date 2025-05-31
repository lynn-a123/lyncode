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
*  描述：健康档案相关接口
*/
@RestController
@RequestMapping("/healthProfile")
public class HealthProfileController {

    @Resource
    HealthProfileService healthProfileService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody HealthProfile healthProfile) {
		Account currentUser = TokenUtils.getCurrentUser();
		if (ObjectUtil.isEmpty(currentUser)) {
			throw new CustomException(ResultCodeEnum.USER_NOT_LOGIN);
		}
		if ("user".equals(currentUser.getRole())) {
			healthProfile.setUserId(currentUser.getId());
		}

        healthProfileService.add(healthProfile);
        return Result.success();
    }


    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        healthProfileService.deleteById(id);
        return Result.success();
    }


    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        healthProfileService.deleteBatch(ids);
        return Result.success();
    }


    @PutMapping("/update")
    public Result update(@RequestBody HealthProfile healthProfile) {

        healthProfileService.updateById(healthProfile);
        return Result.success();
    }


    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        HealthProfile healthProfile = healthProfileService.selectById(id);
        return Result.success(healthProfile);
    }


    @GetMapping("/selectAll")
    public Result selectAll(HealthProfile healthProfile) {
        List<HealthProfile> list = healthProfileService.selectAll(healthProfile);
        return Result.success(list);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectPage")
    public Result selectPage(
            HealthProfile healthProfile,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<HealthProfile> pageInfo = healthProfileService.selectPage(healthProfile, pageNum, pageSize);
        return Result.success(pageInfo);
    }

	/**
	 * 描述：批量删除
	 */
	@PutMapping("/batchDel")
	public Result deleteBatch(@RequestBody List<Integer> list) {
		for (Integer id : list) {
			healthProfileService.deleteById(id);
		}
		return Result.success();
	}

	/**
	 * 描述：批量导出到excel
	 */
	@GetMapping("/export")
	public void export(HttpServletResponse response) throws IOException {
		List<HealthProfile> all = healthProfileService.selectAll(new HealthProfile());
		List<Map<String, Object>> list = new ArrayList<>(all.size());
		if (CollectionUtil.isEmpty(all)) {
			Map<String, Object> row = new LinkedHashMap<>();
			row.put("建档人", null);
			row.put("年龄", null);
			row.put("性别", null);
			row.put("病史", null);
			list.add(row);
		} else {
			for (HealthProfile healthProfile : all) {
				Map<String, Object> row = new LinkedHashMap<>();
				row.put("建档人", healthProfile.getUserName());
				row.put("年龄", healthProfile.getAge());
				row.put("性别", healthProfile.getGender());
				row.put("病史", healthProfile.getHistory());
				list.add(row);
			}
		}
		ExcelWriter writer = ExcelUtil.getWriter(true);
		writer.write(list, true);
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
		response.setHeader("Content-Disposition","attachment;filename=healthProfileInfoExcel.xlsx");
		ServletOutputStream out = response.getOutputStream();
		writer.flush(out, true);
		writer.close();
		IoUtil.close(System.out);
	}


}
