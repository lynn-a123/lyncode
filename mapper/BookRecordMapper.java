package com.example.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.BookRecord;

public interface BookRecordMapper extends BaseMapper<BookRecord> {

    /**
      * 查询所有
    */
    List<BookRecord> selectAll(BookRecord BookRecord);

    /**
      * 根据ID查询
    */
    BookRecord selectById(Integer id);

    /**
      * 删除
    */
    int deleteById(Integer id);



}