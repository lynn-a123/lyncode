package com.example.service;

import java.util.List;

import com.example.entity.WordInfo;
import com.example.mapper.WordInfoMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;

@Service
public class WordInfoService {

    @Resource
    private WordInfoMapper wordInfoMapper;

    /**
     * 新增
     */
    public void add(WordInfo wordInfo) {
    	wordInfoMapper.insert(wordInfo);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
    	wordInfoMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
        	wordInfoMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(WordInfo wordInfo) {
    	wordInfoMapper.updateById(wordInfo);
    }

    /**
     * 根据ID查询
     */
    public WordInfo selectById(Integer id) {
        return wordInfoMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<WordInfo> selectAll(WordInfo wordInfo) {
        return wordInfoMapper.selectAll(wordInfo);
    }

    /**
     * 分页查询
     */
    public PageInfo<WordInfo> selectPage(WordInfo wordInfo, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<WordInfo> list = this.selectAll(wordInfo);

        return PageInfo.of(list);
    }

}