package com.example.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.BookShelf;

public interface BookShelfMapper extends BaseMapper<BookShelf> {

    /**
      * 查询所有
    */
    List<BookShelf> selectAll(BookShelf BookShelf);

    /**
      * 根据ID查询
    */
    BookShelf selectById(Integer id);

    /**
      * 删除
    */
    int deleteById(Integer id);



}