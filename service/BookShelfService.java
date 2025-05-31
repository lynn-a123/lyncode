package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.BookShelf;
import com.example.mapper.BookShelfMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import jakarta.annotation.Resource;

@Service
public class BookShelfService {

    @Resource
    private BookShelfMapper bookShelfMapper;

    /**
     * 新增
     */
    public void add(BookShelf BookShelf) {
    	bookShelfMapper.insert(BookShelf);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
    	bookShelfMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
        	bookShelfMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(BookShelf BookShelf) {
    	bookShelfMapper.updateById(BookShelf);
    }

    /**
     * 根据ID查询
     */
    public BookShelf selectById(Integer id) {
        return bookShelfMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<BookShelf> selectAll(BookShelf BookShelf) {
        return bookShelfMapper.selectAll(BookShelf);
    }

    /**
     * 分页查询
     */
    public PageInfo<BookShelf> selectPage(BookShelf BookShelf, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<BookShelf> list = this.selectAll(BookShelf);

        return PageInfo.of(list);
    }

	public BookShelf getByBookIdAndUid(Integer bid, Integer uid) {
		QueryWrapper<BookShelf> wrapper = new QueryWrapper<>();
		wrapper.eq("book_id", bid);
		wrapper.eq("uid", uid);
		wrapper.last(" limit 1");
		return bookShelfMapper.selectOne(wrapper);
	}

	public void deleteByBookId(Integer bid) {
		QueryWrapper<BookShelf> wrapper = new QueryWrapper<>();
		wrapper.eq("book_id", bid);
		bookShelfMapper.delete(wrapper);
	}

}