package com.pios.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 原始新闻数据传输对象
 */
@Data
public class NewsRawDTO {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Long id;
    private String title;
    private String content;
    private String source;
    private String category;
    private String url;
    private String publishTime;
    private String fetchTime;

    /**
     * 从实体创建 DTO
     */
    public static NewsRawDTO fromEntity(com.pios.entity.NewsRaw entity) {
        if (entity == null) {
            return null;
        }
        NewsRawDTO dto = new NewsRawDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setSource(entity.getSource());
        dto.setCategory(entity.getCategory());
        dto.setUrl(entity.getUrl());
        dto.setPublishTime(formatLocalDateTime(entity.getPublishTime()));
        dto.setFetchTime(formatDate(entity.getFetchTime()));
        return dto;
    }

    /**
     * 格式化 LocalDateTime
     */
    private static String formatLocalDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(FORMATTER);
    }

    /**
     * 格式化 Date
     */
    private static String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .format(FORMATTER);
    }
}
