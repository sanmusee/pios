package com.pios.llm.prompt.template;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 抽象提示词模板
 * 提供模板渲染的基础实现
 */
@Component
public abstract class AbstractPromptTemplate implements PromptTemplate {
    
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");
    
    protected final String name;
    protected final String description;
    protected final String version;
    protected final String template;
    protected final String systemPrompt;
    
    public AbstractPromptTemplate(String name, String description, String version, 
                                   String template, String systemPrompt) {
        this.name = name;
        this.description = description;
        this.version = version;
        this.template = template;
        this.systemPrompt = systemPrompt;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    @Override
    public String getVersion() {
        return version;
    }
    
    @Override
    public String getSystemPrompt() {
        return systemPrompt;
    }
    
    @Override
    public String render(PromptContext context) {
        if (template == null) {
            return "";
        }
        
        String result = template;
        Map<String, Object> variables = context.getVariables();
        
        Matcher matcher = VARIABLE_PATTERN.matcher(result);
        StringBuffer sb = new StringBuffer();
        
        while (matcher.find()) {
            String key = matcher.group(1);
            Object value = variables.get(key);
            if (value != null) {
                matcher.appendReplacement(sb, Matcher.quoteReplacement(value.toString()));
            }
        }
        matcher.appendTail(sb);
        
        return sb.toString();
    }
    
    protected Map<String, Object> getVariables() {
        return new ConcurrentHashMap<>();
    }
}
