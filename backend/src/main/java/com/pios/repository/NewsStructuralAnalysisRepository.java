package com.pios.repository;

import com.pios.entity.NewsStructuralAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 新闻结构变量分析结果 Repository
 */
@Repository
public interface NewsStructuralAnalysisRepository extends JpaRepository<NewsStructuralAnalysis, Long> {
    
    /**
     * 根据新闻ID查询分析结果
     */
    Optional<NewsStructuralAnalysis> findByNewsRawId(Long newsRawId);
    
    /**
     * 查询存在结构性变化的所有分析结果
     */
    List<NewsStructuralAnalysis> findByHasStructuralChangeTrue();
    
    /**
     * 根据分析状态查询
     */
    List<NewsStructuralAnalysis> findByAnalysisStatus(String status);
    
    /**
     * 查询指定维度的所有 Yes 结果
     */
    @Query("SELECT n FROM NewsStructuralAnalysis n WHERE n.policyDirectionShift = 'Yes'")
    List<NewsStructuralAnalysis> findByPolicyDirectionShiftYes();
    
    /**
     * 统计存在结构性变化的数量
     */
    long countByHasStructuralChangeTrue();
    
    /**
     * 检查指定新闻是否已分析
     */
    boolean existsByNewsRawId(Long newsRawId);
}
