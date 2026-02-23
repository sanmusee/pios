package com.pios.llm;

import com.pios.llm.client.LlmClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LlmClientTest {

    @Autowired
    private LlmClient llmClient;

    @Test
    @EnabledIfEnvironmentVariable(named = "KIMI_API_KEY", matches = ".+")
    void testSimpleChat() {
        String prompt = "你好，请用一句话介绍自己。";
        String response = llmClient.chat(prompt);

        assertNotNull(response);
        assertFalse(response.isEmpty());
        System.out.println("Response: " + response);
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "KIMI_API_KEY", matches = ".+")
    void testNewsAnalysis() {
        String news = "新能源汽车补贴政策调整：退坡幅度超预期，将达到30%。";
        String prompt = "请分析以下新闻的关键信息，用JSON格式返回：标题、影响领域、政策方向（正面/负面/中性）。新闻内容：" + news;

        String response = llmClient.chat(prompt);

        assertNotNull(response);
        assertFalse(response.isEmpty());
        System.out.println("Response: " + response);
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "KIMI_API_KEY", matches = ".+")
    void testVariableExtraction() {
        String prompt = "请从以下新闻中提取结构变量，以JSON数组格式返回：" +
                "每个变量包含：name(变量名)、type(类型：政策/资本/供应链/技术)、direction(方向：positive/negative/neutral)" +
                "新闻：AI大模型融资集中化：头部效应加剧，前五大公司融资额占全行业78%。";

        String response = llmClient.chat(prompt);

        assertNotNull(response);
        assertFalse(response.isEmpty());
        System.out.println("Response: " + response);
    }
}
