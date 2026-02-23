package com.pios.llm.agent;

import com.pios.entity.NewsRaw;
import com.pios.entity.NewsStructuralAnalysis;
import com.pios.service.NewsRawService;
import com.pios.service.NewsStructuralAnalysisService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 结构性变量识别 Agent 集成测试
 */
@SpringBootTest
class StructuralVariableAgentIntegrationTest {

    @Autowired
    private NewsRawService newsRawService;

    @Autowired
    private NewsStructuralAnalysisService analysisService;

    private static final Long TEST_NEWS_ID = 2024850331464118339L;

    @Test
    @EnabledIfEnvironmentVariable(named = "KIMI_API_KEY", matches = ".+")
    void testAnalyzeSpecificNews() {
        // 1. 查询指定ID的新闻
        Optional<NewsRaw> newsOpt = newsRawService.findById(TEST_NEWS_ID);
        assertTrue(newsOpt.isPresent(), "News with ID " + TEST_NEWS_ID + " not found");

        NewsRaw news = newsOpt.get();
        System.out.println("News Title: " + news.getTitle());
        System.out.println("News Content Length: " + (news.getContent() != null ? news.getContent().length() : 0));

        // 2. 调用分析服务
        NewsStructuralAnalysis analysis = analysisService.analyzeNews(news, "kimi");

        // 3. 验证分析结果
        assertNotNull(analysis);
        assertEquals(TEST_NEWS_ID, analysis.getNewsRawId());
        assertNotNull(analysis.getAnalysisStatus());

        System.out.println("\n=== Analysis Result ===");
        System.out.println("Status: " + analysis.getAnalysisStatus());
        System.out.println("Has Structural Change: " + analysis.getHasStructuralChange());
        System.out.println("Change Dimension Count: " + analysis.getChangeDimensionCount());

        // 4. 如果分析成功，打印详细结果
        if ("completed".equals(analysis.getAnalysisStatus())) {
            System.out.println("\n=== Detailed Results ===");
            printDimensionResult("Institutional Rule Change", 
                analysis.getInstitutionalRuleChange(), 
                analysis.getInstitutionalRuleEvidence());
            printDimensionResult("Policy Direction Shift", 
                analysis.getPolicyDirectionShift(), 
                analysis.getPolicyDirectionEvidence());
            printDimensionResult("Capital Flow Change", 
                analysis.getCapitalFlowChange(), 
                analysis.getCapitalFlowEvidence());
            printDimensionResult("Cost Structure Change", 
                analysis.getCostStructureChange(), 
                analysis.getCostStructureEvidence());
            printDimensionResult("Supply Demand Change", 
                analysis.getSupplyDemandChange(), 
                analysis.getSupplyDemandEvidence());
            printDimensionResult("Technology Paradigm Shift", 
                analysis.getTechnologyParadigmShift(), 
                analysis.getTechnologyParadigmEvidence());
            printDimensionResult("Behavior Expectation Shift", 
                analysis.getBehaviorExpectationShift(), 
                analysis.getBehaviorExpectationEvidence());
            printDimensionResult("Long Term Impact (>1Y)", 
                analysis.getLongTermImpactOver1y(), 
                analysis.getLongTermImpactEvidence());
        } else if ("failed".equals(analysis.getAnalysisStatus())) {
            System.out.println("Analysis Failed: " + analysis.getErrorMessage());
        }

        // 5. 验证结果已保存到数据库
        Optional<NewsStructuralAnalysis> savedAnalysis = analysisService.findByNewsRawId(TEST_NEWS_ID);
        assertTrue(savedAnalysis.isPresent(), "Analysis result should be saved to database");
    }

    private void printDimensionResult(String dimension, String answer, String evidence) {
        System.out.println("\n" + dimension + ":");
        System.out.println("  Answer: " + answer);
        if (evidence != null && !evidence.isEmpty()) {
            System.out.println("  Evidence: " + evidence.substring(0, Math.min(evidence.length(), 100)) + "...");
        }
    }
}
