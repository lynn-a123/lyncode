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
import com.example.entity.BookType;
import com.example.service.BookTypeService;
import com.github.pagehelper.PageInfo;

import jakarta.annotation.Resource;

/**
*  描述：书籍分类相关接口
*/
@RestController
@RequestMapping("/bookType")
public class BookTypeController {

    @Resource
    BookTypeService bookTypeService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody BookType BookType) {

    	bookTypeService.add(BookType);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
    	bookTypeService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
    	bookTypeService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 更新
     */
    @PutMapping("/update")
    public Result update(@RequestBody BookType BookType) {

    	bookTypeService.updateById(BookType);
        return Result.success();
    }

    /**
     * 查询单个
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        BookType BookType = bookTypeService.selectById(id);
        return Result.success(BookType);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(BookType BookType) {
        List<BookType> list = bookTypeService.selectAll(BookType);
        return Result.success(list);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectPage")
    public Result selectPage(
            BookType BookType,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<BookType> pageInfo = bookTypeService.selectPage(BookType, pageNum, pageSize);
        return Result.success(pageInfo);
    }

	/**
	 * 描述：批量删除
	 */
	@PutMapping("/batchDel")
	public Result deleteBatch(@RequestBody List<Integer> list) {
		for (Integer id : list) {
			bookTypeService.deleteById(id);
		}
		return Result.success();
	}
}
