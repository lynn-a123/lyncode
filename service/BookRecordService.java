package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.entity.BookRecord;
import com.example.mapper.BookRecordMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import jakarta.annotation.Resource;

@Service
public class BookRecordService {

    @Resource
    private BookRecordMapper bookRecordMapper;

    /**
     * 新增
     */
    public void add(BookRecord BookRecord) {
    	bookRecordMapper.insert(BookRecord);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
    	bookRecordMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
        	bookRecordMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(BookRecord BookRecord) {
    	bookRecordMapper.updateById(BookRecord);
    }

    /**
     * 根据ID查询
     */
    public BookRecord selectById(Integer id) {
        return bookRecordMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<BookRecord> selectAll(BookRecord BookRecord) {
        return bookRecordMapper.selectAll(BookRecord);
    }

    /**
     * 分页查询
     */
    public PageInfo<BookRecord> selectPage(BookRecord BookRecord, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<BookRecord> list = this.selectAll(BookRecord);

        return PageInfo.of(list);
    }

}