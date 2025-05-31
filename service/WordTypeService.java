package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.entity.WordType;
import com.example.mapper.WordTypeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import jakarta.annotation.Resource;

@Service
public class WordTypeService {

    @Resource
    private WordTypeMapper wordTypeMapper;

    /**
     * 新增
     */
    public void add(WordType wordType) {
    	wordTypeMapper.insert(wordType);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
    	wordTypeMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
        	wordTypeMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(WordType wordType) {
    	wordTypeMapper.updateById(wordType);
    }

    /**
     * 根据ID查询
     */
    public WordType selectById(Integer id) {
        return wordTypeMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<WordType> selectAll(WordType wordType) {
        return wordTypeMapper.selectAll(wordType);
    }

    /**
     * 分页查询
     */
    public PageInfo<WordType> selectPage(WordType wordType, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<WordType> list = this.selectAll(wordType);

        return PageInfo.of(list);
    }

}