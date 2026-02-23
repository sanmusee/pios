package com.pios.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pios.entity.NewsRaw;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface NewsRawMapper extends BaseMapper<NewsRaw> {

    @Select("SELECT * FROM news_raw WHERE source = #{source} ORDER BY publish_time DESC LIMIT #{limit}")
    List<NewsRaw> selectBySource(@Param("source") String source, @Param("limit") Integer limit);

    @Select("SELECT * FROM news_raw WHERE category = #{category} ORDER BY publish_time DESC LIMIT #{limit}")
    List<NewsRaw> selectByCategory(@Param("category") String category, @Param("limit") Integer limit);

    @Select("SELECT * FROM news_raw WHERE publish_time BETWEEN #{startTime} AND #{endTime} ORDER BY publish_time DESC")
    List<NewsRaw> selectByTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Select("SELECT COUNT(*) FROM news_raw WHERE DATE(fetch_time) = CURDATE()")
    Long countToday();

    @Select("SELECT * FROM news_raw WHERE url = #{url}")
    NewsRaw selectByUrl(@Param("url") String url);

    int insertBatch(@Param("list") List<NewsRaw> list);
}
