package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.entity.BookType;
import com.example.mapper.BookTypeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import jakarta.annotation.Resource;

@Service
public class BookTypeService {

    @Resource
    private BookTypeMapper bookTypeMapper;

    /**
     * 新增
     */
    public void add(BookType BookType) {
    	bookTypeMapper.insert(BookType);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
    	bookTypeMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
        	bookTypeMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(BookType BookType) {
    	bookTypeMapper.updateById(BookType);
    }

    /**
     * 根据ID查询
     */
    public BookType selectById(Integer id) {
        return bookTypeMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<BookType> selectAll(BookType BookType) {
        return bookTypeMapper.selectAll(BookType);
    }

    /**
     * 分页查询
     */
    public PageInfo<BookType> selectPage(BookType BookType, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<BookType> list = this.selectAll(BookType);

        return PageInfo.of(list);
    }

}