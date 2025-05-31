package com.example.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 协同过滤算法
 *
 */
public class CoreMath {
	 
   /**
    * 根据前三个最相似的用户进行推荐
    * @param userId 用户id
    * @param list 推荐的商品idList集合
    * @return
    */
   public List<Integer> recommend(Integer userId, List<CosVO> list) {
       //相关强度： 相关系数 0.8-1.0 极强相关 0.6-0.8 强相关 0.4-0.6 中等程度相关 0.2-0.4 弱相关 0.0-0.2 极弱相关或无相关
       Map<Double, Integer> distances = computeNearestNeighbor(userId, list);
       // 找出前三个相似的用户
       List<Integer> similarUserIdList = new ArrayList<>();
       List<Integer> values = new ArrayList<>(distances.values());
       int size = values.size();
       if(size >= 3) {
           Collections.sort(values,Collections.reverseOrder());
           similarUserIdList = values.stream().limit(3).collect(Collectors.toList());
       }else {
    	   similarUserIdList=values;
       }
       //对每个用户的购买商品记录进行分组
       Map<Integer, List<CosVO>> userMap =list.stream().collect(Collectors.groupingBy(CosVO::getUserId));
       //前三名相似用户购买过的商品
       List<Integer> similarProductIdList = new ArrayList<>();
       for (Integer similarUserId : similarUserIdList) {
           //获取相似用户购买商品的记录
           List<Integer> collect = userMap.get(similarUserId).stream().map(e -> e.getBookId()).collect(Collectors.toList());
           //过滤掉重复的商品
           List<Integer> collect1 = collect.stream().filter(e -> !similarProductIdList.contains(e)).collect(Collectors.toList());
           similarProductIdList.addAll(collect1);
       }
       List<Integer> listWithoutDuplicates = similarProductIdList.stream().distinct().collect(Collectors.toList());
       Collections.sort(similarProductIdList);
       return listWithoutDuplicates;
   }

   /**
    * 在给定userId的情况下，计算其他用户和它的相关系数并排序
    * @param userId
    * @param list
    * @return
    */
   private Map<Double, Integer> computeNearestNeighbor(Integer userId, List<CosVO> list) {
       Map<Integer, List<CosVO>> userMap = list.stream().collect(Collectors.groupingBy(CosVO::getUserId));
       Map<Double, Integer> distances = new TreeMap<>();
       userMap.forEach((k,v)->{
           if(k.intValue()!=userId.intValue()){
               double distance = pearson_dis(v,userMap.get(k));
               distances.put(distance, k);
           }
       });
       return distances;
   }

   /**
    * 计算两个序列间的相关系数
    *
    * @param xList
    * @param yList
    * @return
    */
   private double pearson_dis(List<CosVO> xList, List<CosVO> yList) {
       List<Integer> xs= new ArrayList<>();
       List<Integer> ys= new ArrayList<>();
       
       xList.forEach(x->{
           yList.forEach(y->{
               //加入书架 书籍相同，交集
               if(x.getBookId().intValue()==y.getBookId().intValue()){
                   xs.add(x.getIndex());
                   ys.add(y.getIndex());
               }
           });
       });
       return getRelate(xs,ys);
   }

   /**
    * 余弦相似度：越接近于 1 ，说明两个用户的行为越相似
    * @param xs
    * @param ys
    * @Return {@link Double}
    */
   public static Double getRelate(List<Integer> xs, List<Integer> ys){
       int n=xs.size();
       double Ex= xs.stream().mapToDouble(x->x).sum();
       double Ey=ys.stream().mapToDouble(y->y).sum();
       double Ex2=xs.stream().mapToDouble(x-> Math.pow(x,2)).sum();
       double Ey2=ys.stream().mapToDouble(y-> Math.pow(y,2)).sum();
       double Exy= IntStream.range(0,n).mapToDouble(i->xs.get(i)*ys.get(i)).sum();
       double numerator=Exy-Ex*Ey/n;
       double denominator= Math.sqrt((Ex2- Math.pow(Ex,2)/n)*(Ey2- Math.pow(Ey,2)/n));
       if (denominator==0) {
           return 0.0;
       }
       return numerator/denominator;
   }
}
