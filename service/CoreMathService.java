package com.example.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.CoreMath;
import com.example.common.CosVO;
import com.example.entity.BookInfo;
import com.example.entity.BookShelf;
import com.example.mapper.BookInfoMapper;
import com.example.mapper.BookShelfMapper;

import jakarta.annotation.Resource;

@Service
public class CoreMathService {

    @Resource
    BookShelfMapper bookShelfMapper;
    @Resource
    BookInfoMapper bookInfoMapper;
    
    
	public List<BookInfo> getRecommend(Integer userId,Integer count) {
		 List<BookInfo> recommendBook = new ArrayList<>();
		//查询所有用户书架数据
		 QueryWrapper<BookShelf> wrapper = new QueryWrapper<>();
		List<BookShelf> list=bookShelfMapper.selectList(wrapper);
		
		 if(null == list || list.isEmpty()){//如果没有则随机取
			 QueryWrapper<BookInfo> bookWrapper = new QueryWrapper<>();
			 bookWrapper.last(" ORDER BY RAND() LIMIT  "+count);
	         return bookInfoMapper.selectList(bookWrapper);
	     }
		 
	    List<CosVO> cosList = new ArrayList<>();
        for (BookShelf info : list) {
        	CosVO vo = new CosVO();
        	vo.setUserId(info.getUid());
        	vo.setBookId(info.getBookId());
        	vo.setIndex(5);
        	cosList.add(vo);

        }
        
       
        //协同过滤算法
        CoreMath coreMath = new CoreMath();
        //执行算法，返回推荐书籍id
        List<Integer> recommendIdLists = coreMath.recommend(userId, cosList);
        if(null == recommendIdLists || recommendIdLists.isEmpty()){//如果没有则随机取
        	 QueryWrapper<BookInfo> bookWrapper = new QueryWrapper<>();
			 bookWrapper.last(" ORDER BY RAND() LIMIT  "+count);
	         return bookInfoMapper.selectList(bookWrapper);
        }else {
            
        	for(Integer id:recommendIdLists) {
        		recommendBook.add(bookInfoMapper.selectById(id));
        	}
        }
 
		return recommendBook;
	}
}
