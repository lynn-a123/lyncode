package com.example.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.Result;
import com.example.entity.WordType;
import com.example.service.WordTypeService;
import com.github.pagehelper.PageInfo;

import jakarta.annotation.Resource;

/**
*  描述：文档分类相关接口
*/
@RestController
@RequestMapping("/wordType")
public class WordTypeController {

    @Resource
    WordTypeService wordTypeService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody WordType WordType) {

    	wordTypeService.add(WordType);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
    	wordTypeService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
    	wordTypeService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 更新
     */
    @PutMapping("/update")
    public Result update(@RequestBody WordType WordType) {

    	wordTypeService.updateById(WordType);
        return Result.success();
    }

    /**
     * 查询单个
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        WordType WordType = wordTypeService.selectById(id);
        return Result.success(WordType);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(WordType WordType) {
        List<WordType> list = wordTypeService.selectAll(WordType);
        return Result.success(list);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectPage")
    public Result selectPage(
            WordType WordType,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<WordType> pageInfo = wordTypeService.selectPage(WordType, pageNum, pageSize);
        return Result.success(pageInfo);
    }

	/**
	 * 描述：批量删除
	 */
	@PutMapping("/batchDel")
	public Result deleteBatch(@RequestBody List<Integer> list) {
		for (Integer id : list) {
			wordTypeService.deleteById(id);
		}
		return Result.success();
	}
}
