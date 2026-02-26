package com.pios.collector.rss;

import com.pios.entity.NewsRaw;

import java.util.List;

public interface RssCollector {
    
    String getSourceName();
    
    String getRssUrl();
    
    List<NewsRaw> collect();
}
