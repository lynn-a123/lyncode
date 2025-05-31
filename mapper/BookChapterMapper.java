package com.example.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.BookChapter;

public interface BookChapterMapper extends BaseMapper<BookChapter> {

    /**
      * 查询所有
    */
    List<BookChapter> selectAll(BookChapter BookChapter);

    /**
      * 根据ID查询
    */
    BookChapter selectById(Integer id);

    /**
      * 删除
    */
    int deleteById(Integer id);



}