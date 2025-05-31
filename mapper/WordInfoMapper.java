package com.example.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.WordInfo;

public interface WordInfoMapper extends BaseMapper<WordInfo> {

    /**
      * 查询所有
    */
    List<WordInfo> selectAll(WordInfo info);

    /**
      * 根据ID查询
    */
    WordInfo selectById(Integer id);

    /**
      * 删除
    */
    int deleteById(Integer id);



}