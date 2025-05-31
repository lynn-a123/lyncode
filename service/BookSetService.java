package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.BookSet;
import com.example.entity.BookShelf;
import com.example.mapper.BookSetMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import jakarta.annotation.Resource;

@Service
public class BookSetService {

    @Resource
    private BookSetMapper bookSetMapper;

    /**
     * 新增
     */
    public void add(BookSet BookSet) {
    	bookSetMapper.insert(BookSet);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
    	bookSetMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
        	bookSetMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(BookSet BookSet) {
    	bookSetMapper.updateById(BookSet);
    }

    /**
     * 根据ID查询
     */
    public BookSet selectById(Integer id) {
        return bookSetMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<BookSet> selectAll(BookSet BookSet) {
        return bookSetMapper.selectAll(BookSet);
    }

    /**
     * 分页查询
     */
    public PageInfo<BookSet> selectPage(BookSet BookSet, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<BookSet> list = this.selectAll(BookSet);

        return PageInfo.of(list);
    }

	public BookSet selectByUId(Integer uid) {
		QueryWrapper<BookSet> wrapper = new QueryWrapper<>();
		wrapper.eq("uid", uid);
		wrapper.last(" limit 1");
		return bookSetMapper.selectOne(wrapper);
	}

}