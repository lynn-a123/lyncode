package com.example.controller;

import java.util.Date;
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
import com.example.entity.BookRecord;
import com.example.service.BookRecordService;
import com.github.pagehelper.PageInfo;

import jakarta.annotation.Resource;

/**
*  描述：用户书籍操作记录相关接口
*/
@RestController
@RequestMapping("/bookRecord")
public class BookRecordController {

    @Resource
    BookRecordService bookRecordService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody BookRecord BookRecord) {
    	BookRecord.setCreateTime(new Date());
    	bookRecordService.add(BookRecord);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
    	bookRecordService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
    	bookRecordService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 更新
     */
    @PutMapping("/update")
    public Result update(@RequestBody BookRecord BookRecord) {

    	bookRecordService.updateById(BookRecord);
        return Result.success();
    }

    /**
     * 查询单个
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        BookRecord BookRecord = bookRecordService.selectById(id);
        return Result.success(BookRecord);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(BookRecord BookRecord) {
        List<BookRecord> list = bookRecordService.selectAll(BookRecord);
        return Result.success(list);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectPage")
    public Result selectPage(
            BookRecord BookRecord,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<BookRecord> pageInfo = bookRecordService.selectPage(BookRecord, pageNum, pageSize);
        return Result.success(pageInfo);
    }

	/**
	 * 描述：批量删除
	 */
	@PutMapping("/batchDel")
	public Result deleteBatch(@RequestBody List<Integer> list) {
		for (Integer id : list) {
			bookRecordService.deleteById(id);
		}
		return Result.success();
	}
}
