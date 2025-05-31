package com.example.controller;

import com.example.common.Result;
import com.example.entity.HealthInf;
import com.example.service.HealthInfService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/healthInf")
public class HealthInfController {
    @Resource
    HealthInfService healthInfService;


    /**
     * 查询
     */
    @GetMapping("/selectById/{userId}")
    public Result selectById(@PathVariable Integer userId) {
        HealthInf healthInf = healthInfService.selectById(userId);
        return Result.success(healthInf);
    }

    /**
     * 更新
     */
    @PutMapping("/update")
    public Result update(@RequestBody HealthInf healthInf) {
        healthInfService.updateById(healthInf);
        return Result.success();
    }
}
