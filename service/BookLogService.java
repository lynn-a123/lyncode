package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.BookLog;
import com.example.entity.BookShelf;
import com.example.mapper.BookLogMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import jakarta.annotation.Resource;

@Service
public class BookLogService {

    @Resource
    private BookLogMapper bookLogMapper;

    /**
     * 新增
     */
    public void add(BookLog BookLog) {
    	bookLogMapper.insert(BookLog);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
    	bookLogMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
        	bookLogMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(BookLog BookLog) {
    	bookLogMapper.updateById(BookLog);
    }

    /**
     * 根据ID查询
     */
    public BookLog selectById(Integer id) {
        return bookLogMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<BookLog> selectAll(BookLog BookLog) {
        return bookLogMapper.selectAll(BookLog);
    }

    /**
     * 分页查询
     */
    public PageInfo<BookLog> selectPage(BookLog BookLog, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<BookLog> list = this.selectAll(BookLog);

        return PageInfo.of(list);
    }

	public BookLog getCurInfo(Integer shelfId) {
		QueryWrapper<BookLog> wrapper = new QueryWrapper<>();
		wrapper.eq("shelf_id", shelfId);
		wrapper.orderByDesc("create_time");
		wrapper.isNull("end_time");
		wrapper.last(" limit 1");
		return bookLogMapper.selectOne(wrapper);
	}

}