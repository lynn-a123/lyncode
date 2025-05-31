package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.BookChapter;
import com.example.mapper.BookChapterMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import jakarta.annotation.Resource;

@Service
public class BookChapterService {

    @Resource
    private BookChapterMapper bookChapterMapper;

    /**
     * 新增
     */
    public void add(BookChapter BookChapter) {
    	bookChapterMapper.insert(BookChapter);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
    	bookChapterMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
        	bookChapterMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(BookChapter BookChapter) {
    	bookChapterMapper.updateById(BookChapter);
    }

    /**
     * 根据ID查询
     */
    public BookChapter selectById(Integer id) {
        return bookChapterMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<BookChapter> selectAll(BookChapter BookChapter) {
        return bookChapterMapper.selectAll(BookChapter);
    }

    /**
     * 分页查询
     */
    public PageInfo<BookChapter> selectPage(BookChapter BookChapter, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<BookChapter> list = this.selectAll(BookChapter);

        return PageInfo.of(list);
    }

	public void deleteByBookId(Integer bid) {
		QueryWrapper<BookChapter> wrapper = new QueryWrapper<>();
		wrapper.eq("book_id", bid);
		bookChapterMapper.delete(wrapper);
	}

	public BookChapter getFirst(Integer bookId) {
		QueryWrapper<BookChapter> wrapper = new QueryWrapper<>();
		wrapper.eq("book_id", bookId);
		wrapper.orderByAsc("sort");
		wrapper.last(" limit 1");
		return bookChapterMapper.selectOne(wrapper);
	}

	public BookChapter selectBySort(Integer bookId, Integer num) {
		QueryWrapper<BookChapter> wrapper = new QueryWrapper<>();
		wrapper.eq("book_id", bookId);
		wrapper.eq("sort", num);
		wrapper.last(" limit 1");
		return bookChapterMapper.selectOne(wrapper);
	}

}