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
import com.example.entity.BookChapter;
import com.example.entity.BookInfo;
import com.example.service.BookChapterService;
import com.example.service.BookInfoService;
import com.github.pagehelper.PageInfo;

import jakarta.annotation.Resource;

/**
*  描述：书籍章节相关接口
*/
@RestController
@RequestMapping("/bookChapter")
public class BookChapterController {

    @Resource
    BookChapterService bookChapterService;
    @Resource
    BookInfoService bookInfoService;
    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody BookChapter BookChapter) {
    	
    	BookInfo bookInfo =bookInfoService.selectById(BookChapter.getBookId());
	   	BookChapter.setCreateTime(new Date());
	   	BookChapter.setSort(bookInfo.getNums()+1);
    	bookChapterService.add(BookChapter);
    	
    	 bookInfo.setNums(bookInfo.getNums()+1);
		 bookInfoService.updateById(bookInfo);
    	
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
    	BookChapter bookChapter=bookChapterService.selectById(id);
    	bookChapterService.deleteById(id);
    	 BookInfo bookInfo =bookInfoService.selectById(bookChapter.getBookId());
    	 if(bookInfo!=null) {
    		 bookInfo.setNums(bookInfo.getNums()-1);
    		 bookInfoService.updateById(bookInfo);
    	 }
        return Result.success();
    }

    /**
     * 更新
     */
    @PutMapping("/update")
    public Result update(@RequestBody BookChapter BookChapter) {

    	bookChapterService.updateById(BookChapter);
        return Result.success();
    }

    /**
     * 查询单个
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        BookChapter BookChapter = bookChapterService.selectById(id);
        return Result.success(BookChapter);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(BookChapter BookChapter) {
        List<BookChapter> list = bookChapterService.selectAll(BookChapter);
        return Result.success(list);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectPage")
    public Result selectPage(
            BookChapter BookChapter,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<BookChapter> pageInfo = bookChapterService.selectPage(BookChapter, pageNum, pageSize);
        return Result.success(pageInfo);
    }

	/**
	 * 描述：批量删除
	 */
	@PutMapping("/batchDel")
	public Result deleteBatch(@RequestBody List<Integer> list) {
		for (Integer id : list) {
			bookChapterService.deleteById(id);
		}
		return Result.success();
	}
}
