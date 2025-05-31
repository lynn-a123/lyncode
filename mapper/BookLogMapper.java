package com.example.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.BookLog;

public interface BookLogMapper extends BaseMapper<BookLog> {

    /**
      * 查询所有
    */
    List<BookLog> selectAll(BookLog BookLog);

    /**
      * 根据ID查询
    */
    BookLog selectById(Integer id);

    /**
      * 删除
    */
    int deleteById(Integer id);



}