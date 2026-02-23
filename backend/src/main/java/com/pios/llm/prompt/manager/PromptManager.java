package com.pios.llm.prompt.manager;

import com.pios.llm.prompt.template.PromptTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 提示词管理器
 * 统一管理所有提示词模板
 */
@Slf4j
@Component
public class PromptManager {
    
    private final Map<String, PromptTemplate> templates = new ConcurrentHashMap<>();
    
    @Autowired(required = false)
    private Collection<PromptTemplate> templateCollection;
    
    @PostConstruct
    public void init() {
        if (templateCollection != null) {
            templateCollection.forEach(this::register);
            log.info("Registered {} prompt templates", templates.size());
        }
    }
    
    /**
     * 注册提示词模板
     */
    public void register(PromptTemplate template) {
        templates.put(template.getName(), template);
        log.debug("Registered prompt template: {} v{}", template.getName(), template.getVersion());
    }
    
    /**
     * 获取提示词模板
     */
    public Optional<PromptTemplate> getTemplate(String name) {
        return Optional.ofNullable(templates.get(name));
    }
    
    /**
     * 获取所有模板名称
     */
    public Collection<String> getTemplateNames() {
        return templates.keySet();
    }
    
    /**
     * 获取所有模板
     */
    public Collection<PromptTemplate> getAllTemplates() {
        return templates.values();
    }
    
    /**
     * 检查模板是否存在
     */
    public boolean hasTemplate(String name) {
        return templates.containsKey(name);
    }
}
