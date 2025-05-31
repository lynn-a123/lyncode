package com.example.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
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
import com.example.entity.BookLog;
import com.example.entity.BookRecord;
import com.example.entity.BookSet;
import com.example.entity.BookShelf;
import com.example.service.BookChapterService;
import com.example.service.BookInfoService;
import com.example.service.BookLogService;
import com.example.service.BookRecordService;
import com.example.service.BookSetService;
import com.example.service.BookShelfService;
import com.example.service.CoreMathService;
import com.github.pagehelper.PageInfo;

import jakarta.annotation.Resource;

/**
*  描述：书籍信息相关接口
*/
@RestController
@RequestMapping("/bookInfo")
public class BookInfoController {

    @Resource
    BookInfoService bookInfoService;
    @Resource
    BookShelfService bookShelfService;
    @Resource
    BookRecordService bookRecordService;
    @Resource
    BookChapterService bookChapterService;
    @Resource
    CoreMathService coreMathService;
    @Resource
    BookLogService bookLogService;
    @Resource
    BookSetService bookSetService;
    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody BookInfo BookInfo) {
    	BookInfo.setCreateTime(new Date());
    	BookInfo.setUpdateTime(BookInfo.getCreateTime());
    	BookInfo.setReadNums(0);
    	BookInfo.setNums(0);
    	bookInfoService.add(BookInfo);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    @Transactional
    public Result delete(@PathVariable Integer id) {
    	bookInfoService.deleteById(id);
    	
    	//删除 书籍关联得 章节
    	bookChapterService.deleteByBookId(id);
    	//删除书籍关联得 书架
    	bookShelfService.deleteByBookId(id);
    	
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
    	bookInfoService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 更新
     */
    @PutMapping("/update")
    public Result update(@RequestBody BookInfo BookInfo) {
    	BookInfo.setUpdateTime(new Date());
    	bookInfoService.updateById(BookInfo);
        return Result.success();
    }

    /**
     * 查询单个
     */
    @GetMapping("/selectById")
    public Result selectById(Integer id,Integer uid) {
        BookInfo BookInfo = bookInfoService.selectById(id);
        Map<String,Object> data=new HashMap<>();
        data.put("books", BookInfo);
        //查询是否已经加入书架
        BookShelf shelf= bookShelfService.getByBookIdAndUid(id,uid);
        data.put("isShelf", shelf==null?0:1);
        //查询书籍的笔记
        BookRecord record=new BookRecord();
        record.setBookId(id);
        data.put("recordList", bookRecordService.selectAll(record));
        return Result.success(data);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(BookInfo BookInfo) {
        List<BookInfo> list = bookInfoService.selectAll(BookInfo);
        return Result.success(list);
    }
    
    //推荐 协同
    @GetMapping("/getRecommend")
    public Result getRecommend(Integer uid,Integer count) {
        List<BookInfo> list = coreMathService.getRecommend(uid,count);
        return Result.success(list);
    }
    

    /**
     * 查询所有
     */
    @GetMapping("/selectPage")
    public Result selectPage(
            BookInfo BookInfo,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<BookInfo> pageInfo = bookInfoService.selectPage(BookInfo, pageNum, pageSize);
        return Result.success(pageInfo);
    }

	/**
	 * 描述：批量删除
	 */
	@PutMapping("/batchDel")
	public Result deleteBatch(@RequestBody List<Integer> list) {
		for (Integer id : list) {
			bookInfoService.deleteById(id);
		}
		return Result.success();
	}
	
	 /**
     * 读书
     */
    @GetMapping("/readBook")
    @Transactional
    public Result readBook(Integer shelfId,Integer num) {
    	
    	 //查询书架
        BookShelf shelf= bookShelfService.selectById(shelfId);
    	
        BookInfo bookInfo = bookInfoService.selectById(shelf.getBookId());
        Map<String,Object> data=new HashMap<>();
        data.put("books", bookInfo);
        //查询书籍的笔记
        BookRecord record=new BookRecord();
        record.setBookId(shelf.getBookId());
        data.put("recordList", bookRecordService.selectAll(record));
        
       
        BookChapter bookChapter=null;
        if(num!=null) {
        	bookChapter=bookChapterService.selectBySort(shelf.getBookId(),num);
    	}else {
    		if(shelf.getCurNums()==0) {
             	//查询排序1得章节
    			bookChapter=bookChapterService.getFirst(shelf.getBookId());
            }else {
            	bookChapter=bookChapterService.selectById(shelf.getCurChapterId());
            }
    	}
        
        if(bookChapter==null) {
        	return Result.error("章节不存在");
        }
        
        //当前章节
        data.put("curChapter", bookChapter);
        
        Integer nextNum=bookChapter.getSort()+1;
        if(bookInfo.getNums()<nextNum) {
        	nextNum=0;//表示没有下一章
        }
        //下一个章节序号
        data.put("nextNum",nextNum);
        
        
        //插入用户阅读书籍日志  如果没有结束得时候进行插入
        BookLog log= bookLogService.getCurInfo(shelfId);
        if(log==null) {
        	log=new BookLog();
        	log.setBookId(bookInfo.getId());
        	log.setBookName(bookInfo.getName());
        	log.setUid(shelf.getUid());
        	log.setUname(shelf.getUname());
        	log.setCreateTime(new Date());
        	log.setShelfId(shelf.getId());
        	bookLogService.add(log);
        }
        
        //修改书架当前进度
        shelf.setCurNums(bookChapter.getSort());
        shelf.setCurChapterId(bookChapter.getId());
        shelf.setUpdateTime(new Date());
        bookShelfService.updateById(shelf);
        
        //修改阅读数
        bookInfo.setReadNums(bookInfo.getReadNums()+1);
        bookInfoService.updateById(bookInfo);
        
        
        //查询阅读设置
        BookSet bookSet = bookSetService.selectByUId(shelf.getUid());
        data.put("bookSet",bookSet);
        
        
        return Result.success(data);
    }
    
    
    //关闭阅读
    @GetMapping("/closeReadBook")
    @Transactional
    public Result closeReadBook(Integer shelfId) {
    	 //查询书架
        BookShelf shelf= bookShelfService.selectById(shelfId);
        
        BookLog log= bookLogService.getCurInfo(shelfId);
        //修改日志
        log.setEndTime(new Date());
        log.setTimeLen(minsBetween(log.getCreateTime(),log.getEndTime()));
        bookLogService.updateById(log);
        //修改书架
        shelf.setTotalTime(shelf.getTotalTime()+log.getTimeLen());
        bookShelfService.updateById(shelf);
        return Result.success();
    }
    
    //两个时间相差分钟
    public static Integer minsBetween(Date smdate, Date bdate) {
        try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			smdate = sdf.parse(sdf.format(smdate));
			bdate = sdf.parse(sdf.format(bdate));
			Calendar cal = Calendar.getInstance();
			cal.setTime(smdate);
			long time1 = cal.getTimeInMillis();
			cal.setTime(bdate);
			long time2 = cal.getTimeInMillis();
			long between_days = (time2 - time1) / (1000 * 60);
			return Integer.parseInt(String.valueOf(between_days));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        return 0;
    }

}
