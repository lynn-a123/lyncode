package com.example.mapper;

import com.example.entity.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;

public interface MedicationReminderMapper extends BaseMapper<MedicationReminder> {

    /**
      * 查询所有
    */
    List<MedicationReminder> selectAll(MedicationReminder medicationReminder);

    /**
      * 根据ID查询
    */
    MedicationReminder selectById(Integer id);

    /**
      * 删除
    */
    int deleteById(Integer id);



}