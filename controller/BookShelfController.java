package com.example.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.example.entity.BookInfo;
import com.example.entity.BookRecord;
import com.example.entity.BookShelf;
import com.example.service.BookInfoService;
import com.example.service.BookRecordService;
import com.example.service.BookShelfService;
import com.github.pagehelper.PageInfo;

import jakarta.annotation.Resource;

/**
*  描述：用户书架相关接口
*/
@RestController
@RequestMapping("/bookShelf")
public class BookShelfController {

    @Resource
    BookShelfService bookShelfService;
    @Resource
    BookInfoService bookInfoService;
    @Resource
    BookRecordService bookRecordService;
    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody BookShelf BookShelf) {
    	BookShelf.setCreateTime(new Date());  
    	BookShelf.setCurNums(0);
    	BookShelf.setTotalTime(0);
    	bookShelfService.add(BookShelf);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
    	bookShelfService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
    	bookShelfService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 更新
     */
    @PutMapping("/update")
    public Result update(@RequestBody BookShelf BookShelf) {

    	bookShelfService.updateById(BookShelf);
        return Result.success();
    }

    /**
     * 查询单个
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        BookShelf BookShelf = bookShelfService.selectById(id);
        return Result.success(BookShelf);
    }
    
    @GetMapping("/selectById")
    public Result selectById(Integer id,Integer uid) {
        Map<String,Object> data=new HashMap<>();
        BookShelf BookShelf = bookShelfService.selectById(id);
        data.put("bookShelf", BookShelf);
        
        BookInfo BookInfo = bookInfoService.selectById(BookShelf.getBookId());
        data.put("books", BookInfo);
        //查询书籍的笔记
        BookRecord record=new BookRecord();
        record.setBookId(BookShelf.getBookId());
        data.put("recordList", bookRecordService.selectAll(record));
        
        
        
        return Result.success(data);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(BookShelf BookShelf) {
        List<BookShelf> list = bookShelfService.selectAll(BookShelf);
        return Result.success(list);
    }
    
    @GetMapping("/selectPage")
    public Result selectPage(
    		BookShelf BookShelf,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<BookShelf> pageInfo = bookShelfService.selectPage(BookShelf, pageNum, pageSize);
        return Result.success(pageInfo);
    }
}
