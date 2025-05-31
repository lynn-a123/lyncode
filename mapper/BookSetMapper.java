package com.example.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.BookSet;

public interface BookSetMapper extends BaseMapper<BookSet> {

    /**
      * 查询所有
    */
    List<BookSet> selectAll(BookSet BookSet);

    /**
      * 根据ID查询
    */
    BookSet selectById(Integer id);

    /**
      * 删除
    */
    int deleteById(Integer id);



}