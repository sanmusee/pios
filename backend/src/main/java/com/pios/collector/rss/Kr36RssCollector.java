package com.pios.collector.rss;

import com.pios.entity.NewsRaw;
import com.pios.service.NewsRawService;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class Kr36RssCollector implements RssCollector {

    private final NewsRawService newsRawService;

    private static final String RSS_URL = "https://36kr.com/feed";
    private static final String SOURCE_NAME = "36Kr";

    @Override
    public String getSourceName() {
        return SOURCE_NAME;
    }

    @Override
    public String getRssUrl() {
        return RSS_URL;
    }

    @Override
    public List<NewsRaw> collect() {
        List<NewsRaw> newsList = new ArrayList<>();
        
        try {
            URL feedUrl = new URL(RSS_URL);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));
            
            log.info("开始从 {} 采集RSS数据，共 {} 条", SOURCE_NAME, feed.getEntries().size());
            
            for (SyndEntry entry : feed.getEntries()) {
                try {
                    String url = entry.getLink();
                    
                    if (newsRawService.existsByUrl(url)) {
                        log.debug("URL已存在，跳过: {}", url);
                        continue;
                    }
                    
                    NewsRaw news = new NewsRaw();
                    news.setTitle(entry.getTitle());
                    news.setContent(entry.getDescription() != null ? entry.getDescription().getValue() : "");
                    news.setSource(SOURCE_NAME);
                    news.setUrl(url);
                    news.setCategory(detectCategory(entry.getTitle(), entry.getCategories()));
                    news.setPublishTime(convertToLocalDateTime(entry.getPublishedDate()));
                    
                    newsRawService.save(news);
                    newsList.add(news);
                    
                    log.debug("成功采集新闻: {}", news.getTitle());
                    
                } catch (Exception e) {
                    log.error("处理单条新闻失败: {}", entry.getTitle(), e);
                }
            }
            
            log.info("从 {} 采集完成，新增 {} 条新闻", SOURCE_NAME, newsList.size());
            
        } catch (Exception e) {
            log.error("从 {} 采集RSS数据失败", SOURCE_NAME, e);
        }
        
        return newsList;
    }
    
    private LocalDateTime convertToLocalDateTime(Date date) {
        if (date == null) {
            return LocalDateTime.now();
        }
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
    
    private String detectCategory(String title, List<com.rometools.rome.feed.synd.SyndCategory> categories) {
        String titleLower = title.toLowerCase();
        
        if (titleLower.contains("融资") || titleLower.contains("投资") || titleLower.contains("ipo") || titleLower.contains("上市")) {
            return "资本";
        }
        if (titleLower.contains("政策") || titleLower.contains("监管") || titleLower.contains("法规") || titleLower.contains("政府")) {
            return "政策";
        }
        if (titleLower.contains("ai") || titleLower.contains("人工智能") || titleLower.contains("技术") || titleLower.contains("科技")) {
            return "科技";
        }
        if (titleLower.contains("产业") || titleLower.contains("行业") || titleLower.contains("市场")) {
            return "产业";
        }
        
        if (categories != null && !categories.isEmpty()) {
            String catName = categories.get(0).getName();
            if (catName.contains("科技") || catName.contains("创新")) {
                return "科技";
            }
            if (catName.contains("财经") || catName.contains("金融")) {
                return "资本";
            }
        }
        
        return "综合";
    }
}
