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
public class MedicalReportService {

    @Resource
    private MedicalReportMapper medicalReportMapper;

    /**
     * 新增
     */
    public void add(MedicalReport medicalReport) {
        medicalReportMapper.insert(medicalReport);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        medicalReportMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            medicalReportMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(MedicalReport medicalReport) {
        medicalReportMapper.updateById(medicalReport);
    }

    /**
     * 根据ID查询
     */
    public MedicalReport selectById(Integer id) {
        return medicalReportMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<MedicalReport> selectAll(MedicalReport medicalReport) {
        return medicalReportMapper.selectAll(medicalReport);
    }

    /**
     * 分页查询
     */
    public PageInfo<MedicalReport> selectPage(MedicalReport medicalReport, Integer pageNum, Integer pageSize) {
		Account currentUser = TokenUtils.getCurrentUser();
		if ("user".equals(currentUser.getRole())) {
			medicalReport.setUserId(currentUser.getId());
		}

        PageHelper.startPage(pageNum, pageSize);
        List<MedicalReport> list = this.selectAll(medicalReport);

        return PageInfo.of(list);
    }

}