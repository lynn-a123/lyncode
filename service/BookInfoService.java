package com.example.service;

import java.util.List;

import com.example.entity.BookInfo;
import com.example.mapper.BookInfoMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;

@Service
public class BookInfoService {

    @Resource
    private BookInfoMapper bookInfoMapper;

    /**
     * 新增
     */
    public void add(BookInfo BookInfo) {
    	bookInfoMapper.insert(BookInfo);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
    	bookInfoMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
        	bookInfoMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(BookInfo BookInfo) {
    	bookInfoMapper.updateById(BookInfo);
    }

    /**
     * 根据ID查询
     */
    public BookInfo selectById(Integer id) {
        return bookInfoMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<BookInfo> selectAll(BookInfo BookInfo) {
        return bookInfoMapper.selectAll(BookInfo);
    }

    /**
     * 分页查询
     */
    public PageInfo<BookInfo> selectPage(BookInfo BookInfo, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<BookInfo> list = this.selectAll(BookInfo);

        return PageInfo.of(list);
    }

}