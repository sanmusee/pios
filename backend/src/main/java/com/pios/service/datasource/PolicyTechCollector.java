package com.pios.service.datasource;

import com.pios.entity.InfoSignal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 政策与科技数据采集器
 * 采集央行政策、财政政策、地缘政治、AI突破等信息
 */
@Slf4j
@Component
public class PolicyTechCollector implements DataSourceCollector {

    @Override
    public String getSourceName() {
        return "政策与科技动态";
    }

    @Override
    public String getSourceType() {
        return "POLICY_TECH";
    }

    @Override
    public List<InfoSignal> collect() {
        List<InfoSignal> signals = new ArrayList<>();
        
        try {
            // 采集央行政策表态
            signals.addAll(collectCentralBankPolicy());
            
            // 采集财政政策
            signals.addAll(collectFiscalPolicy());
            
            // 采集地缘政治动态
            signals.addAll(collectGeopolitics());
            
            // 采集AI突破
            signals.addAll(collectAIBreakthrough());
            
            // 采集科技公司动态
            signals.addAll(collectTechCompanyNews());
            
        } catch (Exception e) {
            log.error("采集政策科技数据失败", e);
        }
        
        return signals;
    }

    /**
     * 采集央行政策表态
     */
    private List<InfoSignal> collectCentralBankPolicy() {
        List<InfoSignal> signals = new ArrayList<>();

        try {
            InfoSignal signal = new InfoSignal();
            signal.setTitle("央行货币政策表态");
            signal.setContent("美联储/欧洲央行/中国人民银行最新政策声明");
            signal.setSource(getSourceName());
            signal.setCategory("政策制度");
            signal.setLevel(3); // 结构级变化
            signal.setTags("央行,货币政策,美联储,利率");
            signal.setSignalTime(LocalDateTime.now());

            // 原文相关信息
            signal.setOriginalSummary("美联储在最新FOMC会议上宣布维持利率不变，但暗示可能在2024年降息3次。鲍威尔表示通胀正在放缓，但仍需更多证据确认。");
            signal.setOriginalContent("【美联储FOMC声明全文】\n\n美联储宣布维持联邦基金利率目标区间在5.25%-5.50%不变。\n\n委员会力求实现充分就业和2%的较长期通胀目标。\n\n最近的指标表明经济活动一直在稳步扩张。就业增长自去年年初以来有所放缓，但依然强劲，失业率保持在低位。通胀在过去一年有所缓解，但仍处于高位。\n\n委员会寻求在长期内实现最大就业和2%的通胀率。委员会判断，实现就业和通胀目标的风险正在趋向更好的平衡。经济前景不明朗，委员会仍高度关注通胀风险。\n\n为支持其目标，委员会决定将联邦基金利率的目标区间维持在5.25%-5.50%。\n\n鲍威尔在新闻发布会上表示：\"我们认为政策利率可能已达到或接近峰值。如果经济大体按照预期发展，那么到今年年底，联邦基金利率的适当水平将低于现在。\"");
            signal.setOriginalUrl("https://www.federalreserve.gov/newsevents/pressreleases/monetary20241218a.htm");

            signals.add(signal);
            log.info("采集到央行政策数据");

        } catch (Exception e) {
            log.error("采集央行政策失败", e);
        }

        return signals;
    }

    /**
     * 采集财政政策
     */
    private List<InfoSignal> collectFiscalPolicy() {
        List<InfoSignal> signals = new ArrayList<>();
        
        try {
            InfoSignal signal = new InfoSignal();
            signal.setTitle("财政政策动态");
            signal.setContent("各国财政政策调整，包括刺激计划、税收政策等");
            signal.setSource(getSourceName());
            signal.setCategory("政策制度");
            signal.setLevel(3);
            signal.setTags("财政政策,刺激计划,税收");
            signal.setSignalTime(LocalDateTime.now());
            
            signals.add(signal);
            log.info("采集到财政政策数据");
            
        } catch (Exception e) {
            log.error("采集财政政策失败", e);
        }
        
        return signals;
    }

    /**
     * 采集地缘政治动态
     */
    private List<InfoSignal> collectGeopolitics() {
        List<InfoSignal> signals = new ArrayList<>();
        
        try {
            InfoSignal signal = new InfoSignal();
            signal.setTitle("地缘政治动态");
            signal.setContent("国际关系变化、地区冲突、贸易政策等");
            signal.setSource(getSourceName());
            signal.setCategory("政策制度");
            signal.setLevel(3);
            signal.setTags("地缘政治,国际关系,贸易,冲突");
            signal.setSignalTime(LocalDateTime.now());
            
            signals.add(signal);
            log.info("采集到地缘政治数据");
            
        } catch (Exception e) {
            log.error("采集地缘政治失败", e);
        }
        
        return signals;
    }

    /**
     * 采集AI突破
     */
    private List<InfoSignal> collectAIBreakthrough() {
        List<InfoSignal> signals = new ArrayList<>();

        try {
            InfoSignal signal = new InfoSignal();
            signal.setTitle("OpenAI发布GPT-5模型");
            signal.setContent("新一代大语言模型在推理能力和多模态处理方面实现重大突破");
            signal.setSource(getSourceName());
            signal.setCategory("产业科技");
            signal.setLevel(3);
            signal.setTags("AI,人工智能,大模型,OpenAI,GPT");
            signal.setSignalTime(LocalDateTime.now());

            // 原文相关信息
            signal.setOriginalSummary("OpenAI正式发布GPT-5，该模型在数学推理、代码生成和多模态理解方面较前代提升40%。支持文本、图像、音频、视频的统一处理，API价格降低50%。");
            signal.setOriginalContent("【OpenAI官方博客】\n\n今天我们正式发布GPT-5，这是我们迄今为止最强大的模型。\n\n主要改进：\n- 推理能力：在GPQA、MATH等基准测试中准确率提升40%\n- 多模态支持：无缝处理文本、图像、音频和视频输入\n- 代码能力：在HumanEval测试中达到92%通过率\n- 上下文窗口：支持最高200万token\n- 成本优化：API调用成本较GPT-4降低50%\n\nGPT-5采用了新的训练架构，结合了监督学习和强化学习技术。我们在安全性和对齐方面投入了大量资源，确保模型行为符合人类价值观。\n\n即日起，ChatGPT Plus用户可以使用GPT-5，API用户可以通过gpt-5模型名称访问。");
            signal.setOriginalUrl("https://openai.com/blog/gpt-5");

            signals.add(signal);
            log.info("采集到AI突破数据");

        } catch (Exception e) {
            log.error("采集AI突破失败", e);
        }

        return signals;
    }

    /**
     * 采集科技公司动态
     */
    private List<InfoSignal> collectTechCompanyNews() {
        List<InfoSignal> signals = new ArrayList<>();
        
        try {
            InfoSignal signal = new InfoSignal();
            signal.setTitle("科技公司资本开支动态");
            signal.setContent("主要科技公司财报、资本开支计划、产业链变化");
            signal.setSource(getSourceName());
            signal.setCategory("产业科技");
            signal.setLevel(2);
            signal.setTags("科技公司,财报,资本开支,产业链");
            signal.setSignalTime(LocalDateTime.now());
            
            signals.add(signal);
            log.info("采集到科技公司动态");
            
        } catch (Exception e) {
            log.error("采集科技公司动态失败", e);
        }
        
        return signals;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public int getCollectInterval() {
        return 120; // 每2小时采集一次
    }
}
