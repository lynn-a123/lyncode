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
import com.example.entity.WordInfo;
import com.example.service.WordInfoService;
import com.github.pagehelper.PageInfo;

import jakarta.annotation.Resource;

/**
*  描述：文档信息相关接口
*/
@RestController
@RequestMapping("/wordInfo")
public class WordInfoController {

    @Resource
    WordInfoService wordInfoService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody WordInfo WordInfo) {
    	WordInfo.setCreateTime(new Date());
    	WordInfo.setUpdateTime(WordInfo.getCreateTime());
    	wordInfoService.add(WordInfo);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
    	wordInfoService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
    	wordInfoService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 更新
     */
    @PutMapping("/update")
    public Result update(@RequestBody WordInfo WordInfo) {
    	WordInfo.setUpdateTime(new Date());
    	wordInfoService.updateById(WordInfo);
        return Result.success();
    }

    /**
     * 查询单个
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        WordInfo WordInfo = wordInfoService.selectById(id);
        return Result.success(WordInfo);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(WordInfo WordInfo) {
        List<WordInfo> list = wordInfoService.selectAll(WordInfo);
        return Result.success(list);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectPage")
    public Result selectPage(
            WordInfo WordInfo,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<WordInfo> pageInfo = wordInfoService.selectPage(WordInfo, pageNum, pageSize);
        return Result.success(pageInfo);
    }

	/**
	 * 描述：批量删除
	 */
	@PutMapping("/batchDel")
	public Result deleteBatch(@RequestBody List<Integer> list) {
		for (Integer id : list) {
			wordInfoService.deleteById(id);
		}
		return Result.success();
	}
	
	/**
	 * 收藏
	 * @param id
	 * @return
	 */
    @GetMapping("/collectById")
    public Result collectById(Integer id) {
        WordInfo info = wordInfoService.selectById(id);
        info.setIsCollect(info.getIsCollect()==1?0:1);
        wordInfoService.updateById(info);
        return Result.success();
    }
    
    //保险柜
    @GetMapping("/pwdById")
    public Result pwdById(Integer id,String pwd) {
        WordInfo info = wordInfoService.selectById(id);
        if(info.getIsPwd()==1) {
        	if(!pwd.equals(info.getPwd())) {
        		return Result.error("密码错误");
        	 }
        	 info.setIsPwd(0);
        	info.setPwd("");
        }else {//加入保险柜
        	info.setIsPwd(1);
        	info.setPwd(pwd);
        }
        wordInfoService.updateById(info);
        return Result.success();
    }
    
    
    //保险柜查看保险柜
    @GetMapping("/boxById")
    public Result boxById(Integer id,String pwd) {
        WordInfo info = wordInfoService.selectById(id);
    	if(!pwd.equals(info.getPwd())) {
    		return Result.error("密码错误");
    	}
        return Result.success();
    }
    
    
    
    
    @GetMapping("/updateNums")
    public Result updateNums(Integer id) {
        WordInfo info = wordInfoService.selectById(id);
        info.setDownloadNums(info.getDownloadNums()+1);
        wordInfoService.updateById(info);
        return Result.success();
    }
}
