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
import com.example.entity.BookSet;
import com.example.service.BookSetService;
import com.github.pagehelper.PageInfo;

import jakarta.annotation.Resource;

/**
*  描述：用户阅读设置相关接口
*/
@RestController
@RequestMapping("/bookSet")
public class BookSetController {

    @Resource
    BookSetService bookSetService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody BookSet BookSet) {

    	bookSetService.add(BookSet);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
    	bookSetService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
    	bookSetService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 更新
     */
    @PutMapping("/update")
    public Result update(@RequestBody BookSet BookSet) {

    	bookSetService.updateById(BookSet);
        return Result.success();
    }

    /**
     * 查询单个 根据用户获取
     */
    @GetMapping("/selectById/{uid}")
    public Result selectById(@PathVariable Integer uid) {
    	
    	
        BookSet BookSet = bookSetService.selectByUId(uid);
        return Result.success(BookSet);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(BookSet BookSet) {
        List<BookSet> list = bookSetService.selectAll(BookSet);
        return Result.success(list);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectPage")
    public Result selectPage(
            BookSet BookSet,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<BookSet> pageInfo = bookSetService.selectPage(BookSet, pageNum, pageSize);
        return Result.success(pageInfo);
    }

	/**
	 * 描述：批量删除
	 */
	@PutMapping("/batchDel")
	public Result deleteBatch(@RequestBody List<Integer> list) {
		for (Integer id : list) {
			bookSetService.deleteById(id);
		}
		return Result.success();
	}
}
