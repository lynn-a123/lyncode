package com.example.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.BookInfo;

public interface BookInfoMapper extends BaseMapper<BookInfo> {

    /**
      * 查询所有
    */
    List<BookInfo> selectAll(BookInfo info);

    /**
      * 根据ID查询
    */
    BookInfo selectById(Integer id);

    /**
      * 删除
    */
    int deleteById(Integer id);



}