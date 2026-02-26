package com.pios.service;

import com.pios.llm.agent.StructuralVariableAgent;
import com.pios.llm.agent.model.StructuralVariableResult;
import com.pios.entity.NewsRaw;
import com.pios.entity.NewsStructuralAnalysis;
import com.pios.repository.NewsStructuralAnalysisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 新闻结构变量分析服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NewsStructuralAnalysisService {
    
    private final NewsStructuralAnalysisRepository analysisRepository;
    private final StructuralVariableAgent structuralVariableAgent;
    
    /**
     * 分析新闻的结构变量
     */
    @Transactional
    public NewsStructuralAnalysis analyzeNews(NewsRaw newsRaw, String provider) {
        // 检查是否已分析
        Optional<NewsStructuralAnalysis> existing = analysisRepository.findByNewsRawId(newsRaw.getId());
        if (existing.isPresent()) {
            log.info("News {} already analyzed, skipping", newsRaw.getId());
            return existing.get();
        }
        
        // 创建分析记录
        NewsStructuralAnalysis analysis = new NewsStructuralAnalysis();
        analysis.setNewsRawId(newsRaw.getId());
        analysis.setAnalysisStatus("processing");
        analysis.setLlmProvider(provider);
        analysis = analysisRepository.save(analysis);
        
        try {
            // 调用 Agent 进行分析
            String articleText = newsRaw.getTitle() + "\n\n" + newsRaw.getContent();
            StructuralVariableResult result = structuralVariableAgent.analyze(articleText, provider);
            
            // 填充分析结果
            fillAnalysisResult(analysis, result);
            
            analysis.setAnalysisStatus("completed");
            analysis.setAnalysisTime(LocalDateTime.now());
            
            log.info("News {} analyzed successfully, has structural change: {}", 
                    newsRaw.getId(), analysis.getHasStructuralChange());
            
        } catch (Exception e) {
            log.error("Failed to analyze news {}", newsRaw.getId(), e);
            analysis.setAnalysisStatus("failed");
            analysis.setErrorMessage(e.getMessage());
        }
        
        return analysisRepository.save(analysis);
    }
    
    /**
     * 填充分析结果到实体
     */
    private void fillAnalysisResult(NewsStructuralAnalysis analysis, StructuralVariableResult result) {
        // 制度或规则改变
        if (result.getInstitutionalRuleChange() != null) {
            analysis.setInstitutionalRuleChange(result.getInstitutionalRuleChange().getAnswer());
            analysis.setInstitutionalRuleEvidence(result.getInstitutionalRuleChange().getEvidence());
        }
        
        // 政策方向改变
        if (result.getPolicyDirectionShift() != null) {
            analysis.setPolicyDirectionShift(result.getPolicyDirectionShift().getAnswer());
            analysis.setPolicyDirectionEvidence(result.getPolicyDirectionShift().getEvidence());
        }
        
        // 资本流向改变
        if (result.getCapitalFlowChange() != null) {
            analysis.setCapitalFlowChange(result.getCapitalFlowChange().getAnswer());
            analysis.setCapitalFlowEvidence(result.getCapitalFlowChange().getEvidence());
        }
        
        // 成本结构改变
        if (result.getCostStructureChange() != null) {
            analysis.setCostStructureChange(result.getCostStructureChange().getAnswer());
            analysis.setCostStructureEvidence(result.getCostStructureChange().getEvidence());
        }
        
        // 供需关系改变
        if (result.getSupplyDemandChange() != null) {
            analysis.setSupplyDemandChange(result.getSupplyDemandChange().getAnswer());
            analysis.setSupplyDemandEvidence(result.getSupplyDemandChange().getEvidence());
        }
        
        // 技术范式改变
        if (result.getTechnologyParadigmShift() != null) {
            analysis.setTechnologyParadigmShift(result.getTechnologyParadigmShift().getAnswer());
            analysis.setTechnologyParadigmEvidence(result.getTechnologyParadigmShift().getEvidence());
        }
        
        // 行为预期改变
        if (result.getBehaviorExpectationShift() != null) {
            analysis.setBehaviorExpectationShift(result.getBehaviorExpectationShift().getAnswer());
            analysis.setBehaviorExpectationEvidence(result.getBehaviorExpectationShift().getEvidence());
        }
        
        // 长期影响
        if (result.getLongTermImpactOver1y() != null) {
            analysis.setLongTermImpactOver1y(result.getLongTermImpactOver1y().getAnswer());
            analysis.setLongTermImpactEvidence(result.getLongTermImpactOver1y().getEvidence());
        }
        
        // 统计信息
        analysis.setHasStructuralChange(result.hasAnyStructuralChange());
        analysis.setChangeDimensionCount(countYesDimensions(result));
    }
    
    /**
     * 统计 Yes 的维度数量
     */
    private int countYesDimensions(StructuralVariableResult result) {
        int count = 0;
        if (isYes(result.getInstitutionalRuleChange())) count++;
        if (isYes(result.getPolicyDirectionShift())) count++;
        if (isYes(result.getCapitalFlowChange())) count++;
        if (isYes(result.getCostStructureChange())) count++;
        if (isYes(result.getSupplyDemandChange())) count++;
        if (isYes(result.getTechnologyParadigmShift())) count++;
        if (isYes(result.getBehaviorExpectationShift())) count++;
        if (isYes(result.getLongTermImpactOver1y())) count++;
        return count;
    }
    
    private boolean isYes(StructuralVariableResult.DimensionResult result) {
        return result != null && "Yes".equalsIgnoreCase(result.getAnswer());
    }
    
    /**
     * 根据新闻ID获取分析结果
     */
    public Optional<NewsStructuralAnalysis> findByNewsRawId(Long newsRawId) {
        return analysisRepository.findByNewsRawId(newsRawId);
    }
    
    /**
     * 检查新闻是否已分析
     */
    public boolean isAnalyzed(Long newsRawId) {
        return analysisRepository.existsByNewsRawId(newsRawId);
    }
}
