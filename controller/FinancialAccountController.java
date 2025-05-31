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


@RestController
@RequestMapping("/financialAccount")
public class FinancialAccountController {

    @Resource
    FinancialAccountService financialAccountService;


    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody FinancialAccount financialAccount) {
		Account currentUser = TokenUtils.getCurrentUser();
		if (ObjectUtil.isEmpty(currentUser)) {
			throw new CustomException(ResultCodeEnum.USER_NOT_LOGIN);
		}
		if ("user".equals(currentUser.getRole())) {
			financialAccount.setUserId(currentUser.getId());
		}

        financialAccountService.add(financialAccount);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        financialAccountService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        financialAccountService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 更新
     */
    @PutMapping("/update")
    public Result update(@RequestBody FinancialAccount financialAccount) {

        financialAccountService.updateById(financialAccount);
        return Result.success();
    }

    /**
     * 查询单个
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        FinancialAccount financialAccount = financialAccountService.selectById(id);
        return Result.success(financialAccount);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(FinancialAccount financialAccount) {
        List<FinancialAccount> list = financialAccountService.selectAll(financialAccount);
        return Result.success(list);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectPage")
    public Result selectPage(
            FinancialAccount financialAccount,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<FinancialAccount> pageInfo = financialAccountService.selectPage(financialAccount, pageNum, pageSize);
        return Result.success(pageInfo);
    }

	/**
	 * 批量删除
	 */
	@PutMapping("/batchDel")
	public Result deleteBatch(@RequestBody List<Integer> list) {
		for (Integer id : list) {
			financialAccountService.deleteById(id);
		}
		return Result.success();
	}


    //	批量导出到excel
	@GetMapping("/export")
	public void export(HttpServletResponse response) throws IOException {
		List<FinancialAccount> all = financialAccountService.selectAll(new FinancialAccount());
		List<Map<String, Object>> list = new ArrayList<>(all.size());
		if (CollectionUtil.isEmpty(all)) {
			Map<String, Object> row = new LinkedHashMap<>();
			row.put("用户名称", null);
			row.put("类型", null);
			row.put("收支内容", null);
			row.put("金额", null);
			row.put("备注", null);
			row.put("时间", null);
			list.add(row);
		} else {
			for (FinancialAccount financialAccount : all) {
				Map<String, Object> row = new LinkedHashMap<>();
				row.put("用户名称", financialAccount.getUserName());
				row.put("类型", financialAccount.getType());
				row.put("收支内容", financialAccount.getSort());
				row.put("金额", financialAccount.getBalance());
				row.put("备注", financialAccount.getOther());
				row.put("时间", financialAccount.getTime());
				list.add(row);
			}
		}
		ExcelWriter writer = ExcelUtil.getWriter(true);
		writer.write(list, true);
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
		response.setHeader("Content-Disposition","attachment;filename=financialAccountInfoExcel.xlsx");
		ServletOutputStream out = response.getOutputStream();
		writer.flush(out, true);
		writer.close();
		IoUtil.close(System.out);
	}


}
