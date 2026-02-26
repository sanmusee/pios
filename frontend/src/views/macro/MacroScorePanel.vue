<template>
  <div class="macro-score-panel">
    <div class="panel-header">
      <div class="header-content">
        <div>
          <h2>宏观评分与相位判定系统</h2>
          <p class="subtitle">基于流动性、经济动能、风险偏好三大维度的量化评分</p>
        </div>
        <div class="header-actions">
          <el-button type="success" :icon="MagicStick" @click="generateAnalysis" :loading="analyzing">
            生成宏观分析
          </el-button>
          <el-button type="primary" :icon="InfoFilled" @click="openGuide">
            评分说明
          </el-button>
        </div>
      </div>
    </div>

    <!-- 总分卡片 -->
    <div class="score-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="total-score-card">
            <div class="score-label">宏观相位</div>
            <div class="phase-name" :class="phaseInfo.class">{{ phaseInfo.name }}</div>
            <div class="total-score">{{ totalScore }}/15</div>
          </div>
        </el-col>
        
        <el-col :span="6">
          <ScoreCard 
            title="流动性" 
            :score="liquidityScore" 
            :max="5"
            icon="Money"
            color="#5470c6"
          />
        </el-col>
        
        <el-col :span="6">
          <ScoreCard 
            title="经济动能" 
            :score="growthScore" 
            :max="5"
            icon="TrendCharts"
            color="#91cc75"
          />
        </el-col>
        
        <el-col :span="6">
          <ScoreCard 
            title="风险偏好" 
            :score="riskScore" 
            :max="5"
            icon="DataLine"
            color="#fac858"
          />
        </el-col>
      </el-row>
    </div>

    <!-- 评分详情 -->
    <div class="score-details">
      <!-- 流动性评分 -->
      <div class="score-section">
        <div class="section-header">
          <div class="section-title">
            <el-icon><Money /></el-icon>
            <span>一、流动性评分（{{ liquidityScore }}/5）</span>
          </div>
          <el-tag :type="liquidityScore >= 4 ? 'success' : liquidityScore >= 2 ? 'warning' : 'danger'">
            {{ liquidityScore >= 4 ? '宽松' : liquidityScore >= 2 ? '中性' : '偏紧' }}
          </el-tag>
        </div>
        
        <el-row :gutter="16">
          <el-col :span="12" v-for="item in liquidityRules" :key="item.name">
            <RuleCard :rule="item" @toggle="toggleRule('liquidity', item.name)" />
          </el-col>
        </el-row>
      </div>

      <!-- 经济动能评分 -->
      <div class="score-section">
        <div class="section-header">
          <div class="section-title">
            <el-icon><TrendCharts /></el-icon>
            <span>二、经济动能评分（{{ growthScore }}/5）</span>
          </div>
          <el-tag :type="growthScore >= 4 ? 'success' : growthScore >= 2 ? 'warning' : 'danger'">
            {{ growthScore >= 4 ? '扩张' : growthScore >= 2 ? '弱复苏' : '收缩' }}
          </el-tag>
        </div>
        
        <el-row :gutter="16">
          <el-col :span="12" v-for="item in growthRules" :key="item.name">
            <RuleCard :rule="item" @toggle="toggleRule('growth', item.name)" />
          </el-col>
        </el-row>
      </div>

      <!-- 风险偏好评分 -->
      <div class="score-section">
        <div class="section-header">
          <div class="section-title">
            <el-icon><DataLine /></el-icon>
            <span>三、风险偏好评分（{{ riskScore }}/5）</span>
          </div>
          <el-tag :type="riskScore >= 4 ? 'success' : riskScore >= 2 ? 'warning' : 'danger'">
            {{ riskScore >= 4 ? '修复' : riskScore >= 2 ? '中性' : '防御' }}
          </el-tag>
        </div>
        
        <el-row :gutter="16">
          <el-col :span="12" v-for="item in riskRules" :key="item.name">
            <RuleCard :rule="item" @toggle="toggleRule('risk', item.name)" />
          </el-col>
        </el-row>
      </div>
    </div>

    <!-- 结论输出 -->
    <div class="conclusion-section">
      <div class="conclusion-header">
        <span>系统结论</span>
      </div>
      <div class="conclusion-content">
        <p><strong>当前宏观相位：{{ phaseInfo.name }}</strong></p>
        <p>流动性评分：{{ liquidityScore }}/5 | 经济动能：{{ growthScore }}/5 | 风险偏好：{{ riskScore }}/5</p>
        <p class="conclusion-text">{{ phaseInfo.conclusion }}</p>
      </div>
    </div>

    <ScoreGuideDialog ref="guideDialogRef" />
    <AnalysisResultDialog ref="analysisDialogRef" />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { InfoFilled, Money, TrendCharts, DataLine, MagicStick } from '@element-plus/icons-vue'
import ScoreCard from './components/ScoreCard.vue'
import RuleCard from './components/RuleCard.vue'
import ScoreGuideDialog from './components/ScoreGuideDialog.vue'
import AnalysisResultDialog from './components/AnalysisResultDialog.vue'

// 流动性规则
const liquidityRules = ref([
  { name: 'M2同比', desc: 'M2同比 ≥ 11% → +2分；≥ 9% → +1分', score: 2, active: true },
  { name: '社融趋势', desc: '连续3个月上行 → +1分；单月脉冲 → 0分', score: 1, active: true },
  { name: 'M1-M2剪刀差', desc: '剪刀差缩小 → +1分；扩大 → 0分', score: 1, active: true },
  { name: 'DR007', desc: '低于近3年中位数 → +1分', score: 1, active: true },
  { name: '10Y国债', desc: '下行趋势 → +1分', score: 0, active: false }
])

// 经济动能规则
const growthRules = ref([
  { name: 'PMI', desc: 'PMI ≥ 50 → +1分；连续2月上行 → +1分', score: 2, active: true },
  { name: 'PMI新订单', desc: '新订单 ≥ 50 → +1分', score: 1, active: true },
  { name: '产成品库存', desc: '库存同比连续下降 → +1分', score: 0, active: false },
  { name: '工业企业利润', desc: '利润同比转正 → +1分', score: 0, active: false },
  { name: 'PPI', desc: 'PPI回升趋势 → +1分', score: 0, active: false }
])

// 风险偏好规则
const riskRules = ref([
  { name: '中美利差', desc: '倒挂收窄 → +1分', score: 0, active: false },
  { name: 'USD/CNH', desc: '稳定或升值趋势 → +1分', score: 1, active: true },
  { name: '北向资金', desc: '连续5日净流入 → +1分', score: 1, active: true },
  { name: 'ERP', desc: '+1σ → +1分；+2σ → +2分', score: 1, active: true },
  { name: '美债收益率', desc: '下行趋势 → +1分', score: 0, active: false }
])

// 计算各维度得分
const liquidityScore = computed(() => 
  liquidityRules.value.filter(r => r.active).reduce((sum, r) => sum + r.score, 0)
)

const growthScore = computed(() => 
  growthRules.value.filter(r => r.active).reduce((sum, r) => sum + r.score, 0)
)

const riskScore = computed(() => 
  riskRules.value.filter(r => r.active).reduce((sum, r) => sum + r.score, 0)
)

const totalScore = computed(() => 
  liquidityScore.value + growthScore.value + riskScore.value
)

// 宏观相位判定
const phaseInfo = computed(() => {
  const l = liquidityScore.value
  const g = growthScore.value
  const r = riskScore.value
  
  // 扩张期
  if (l >= 3 && g >= 4 && r >= 3) {
    return {
      name: '扩张期',
      class: 'expansion',
      conclusion: '流动性充裕，经济动能强劲，风险偏好修复。适合顺周期配置，积极做多权益资产。'
    }
  }
  
  // 结构性行情
  if (l >= 4 && g <= 3 && r >= 3) {
    return {
      name: '结构性行情',
      class: 'structural',
      conclusion: '流动性充裕，但实体动能仍弱，风险偏好正在修复。适合结构性配置，关注主题投资机会。'
    }
  }
  
  // 政策托底期
  if (l >= 4 && g <= 2 && r <= 2) {
    return {
      name: '政策托底期',
      class: 'policy',
      conclusion: '流动性宽松，但经济动能和风险偏好双弱。政策强托底，市场仍在犹豫，等待信号明朗。'
    }
  }
  
  // 防御期
  if (l <= 2 && g <= 2 && r <= 2) {
    return {
      name: '防御期',
      class: 'defensive',
      conclusion: '流动性、经济动能、风险偏好三弱。现金为王，降低仓位，等待市场出清。'
    }
  }
  
  // 滞胀风险期（简化判断）
  if (l >= 3 && g <= 2) {
    return {
      name: '滞胀风险期',
      class: 'stagflation',
      conclusion: '流动性充裕但经济动能弱，警惕成长股杀估值风险。关注价值股和通胀受益板块。'
    }
  }
  
  // 默认中性
  return {
    name: '震荡期',
    class: 'neutral',
    conclusion: '各项指标交织，市场方向不明。建议均衡配置，控制仓位，等待明确信号。'
  }
})

// 切换规则状态
const toggleRule = (category, ruleName) => {
  const rules = category === 'liquidity' ? liquidityRules : 
                category === 'growth' ? growthRules : riskRules
  const rule = rules.value.find(r => r.name === ruleName)
  if (rule) {
    rule.active = !rule.active
  }
}

const guideDialogRef = ref(null)
const openGuide = () => {
  guideDialogRef.value?.open()
}

// 上期数据（模拟）
const lastMonthData = {
  liquidityScore: 3,
  growthScore: 1,
  riskScore: 2,
  phase: 'policy'
}

// 生成趋势
const getTrend = (current, last) => {
  if (current > last) return 'improving'
  if (current < last) return 'weakening'
  return 'stable'
}

// 检测关键变化
const getKeyChanges = () => {
  const changes = []
  
  // 检查各维度变化
  if (liquidityScore.value > lastMonthData.liquidityScore) {
    changes.push('流动性评分回升')
  }
  if (growthScore.value > lastMonthData.growthScore) {
    changes.push('PMI新订单回升至49.8')
  }
  if (riskScore.value > lastMonthData.riskScore) {
    changes.push('M1-M2剪刀差收窄')
  }
  
  // 检查具体规则变化
  const m2Rule = liquidityRules.value.find(r => r.name === 'M2同比')
  if (m2Rule?.active && m2Rule.score >= 2) {
    changes.push('M2同比突破11%')
  }
  
  const pmiRule = growthRules.value.find(r => r.name === 'PMI')
  if (pmiRule?.active && pmiRule.score >= 2) {
    changes.push('PMI连续2月上行')
  }
  
  const northRule = riskRules.value.find(r => r.name === '北向资金')
  if (northRule?.active) {
    changes.push('北向资金连续净流入')
  }
  
  return changes.length > 0 ? changes : ['各项指标保持稳定']
}

// 检测关键矛盾点
const getContradictions = () => {
  const contradictions = []
  
  // 流动性宽松但订单未改善
  if (liquidityScore.value >= 4 && growthScore.value <= 2) {
    contradictions.push('流动性宽松但实体订单未明显改善')
  }
  
  // 风险偏好回升但汇率仍弱
  if (riskScore.value >= 3) {
    const cnhRule = riskRules.value.find(r => r.name === 'USD/CNH')
    if (!cnhRule?.active) {
      contradictions.push('风险偏好回升但人民币汇率仍承压')
    }
  }
  
  // 动能弱但库存下降（被动去库存）
  if (growthScore.value <= 2) {
    const inventoryRule = growthRules.value.find(r => r.name === '产成品库存')
    if (inventoryRule?.active) {
      contradictions.push('经济动能弱但库存被动去化，复苏信号初现')
    }
  }
  
  return contradictions.length > 0 ? contradictions : ['暂无显著矛盾']
}

// 外部压力评估
const getExternalPressure = () => {
  const usBondRule = riskRules.value.find(r => r.name === '美债收益率')
  const spreadRule = riskRules.value.find(r => r.name === '中美利差')
  
  if (!usBondRule?.active && spreadRule?.active) {
    return 'easing'
  }
  if (usBondRule?.active && !spreadRule?.active) {
    return 'rising'
  }
  return 'neutral'
}

// 生成宏观状态摘要对象
const generateMacroSummary = () => {
  return {
    liquidity_score: liquidityScore.value,
    growth_score: growthScore.value,
    risk_score: riskScore.value,
    liquidity_trend: getTrend(liquidityScore.value, lastMonthData.liquidityScore),
    growth_trend: getTrend(growthScore.value, lastMonthData.growthScore),
    risk_trend: getTrend(riskScore.value, lastMonthData.riskScore),
    external_pressure: getExternalPressure(),
    phase: phaseInfo.value.class,
    phase_name: phaseInfo.value.name,
    key_changes: getKeyChanges(),
    contradictions: getContradictions(),
    total_score: totalScore.value,
    delta: {
      liquidity: liquidityScore.value - lastMonthData.liquidityScore,
      growth: growthScore.value - lastMonthData.growthScore,
      risk: riskScore.value - lastMonthData.riskScore
    }
  }
}

// LLM分析
const analyzing = ref(false)
const analysisDialogRef = ref(null)

const generateAnalysis = async () => {
  analyzing.value = true

  // 模拟API调用延迟
  await new Promise(resolve => setTimeout(resolve, 1500))

  const summary = generateMacroSummary()

  // 模拟LLM返回结果
  const result = {
    summary: generateLLMResponse(summary),
    raw_data: summary
  }

  analyzing.value = false
  analysisDialogRef.value?.open(result)
}

// 模拟LLM生成响应
const generateLLMResponse = (data) => {
  const phaseMap = {
    'expansion': '扩张期',
    'structural': '结构性行情',
    'policy': '政策托底期',
    'defensive': '防御期',
    'stagflation': '滞胀风险期',
    'neutral': '震荡期'
  }
  
  let judgment = ''
  let drivers = ''
  let risks = ''
  let focus = ''
  
  // 根据相位生成判断
  switch(data.phase) {
    case 'expansion':
      judgment = '当前处于扩张期，流动性充裕，经济动能强劲，风险偏好修复。市场呈现典型的顺周期特征。'
      drivers = '主要驱动因素：1）M2同比维持高位，社融持续改善；2）PMI新订单回升，企业补库存意愿增强；3）北向资金持续流入，ERP处于合理区间。'
      risks = '潜在风险：需关注通胀预期升温可能引发的货币政策收紧信号。'
      focus = '下期关注点：PPI走势、工业企业利润数据、美联储议息会议。'
      break
    case 'structural':
      judgment = '当前处于结构性行情阶段，流动性充裕但实体动能仍弱，风险偏好正在修复。市场更多体现为估值修复而非盈利改善。'
      drivers = '主要驱动因素：1）流动性宽松支撑估值；2）政策预期改善提振情绪；3）部分行业出现结构性机会。'
      risks = '潜在风险：经济基本面未能跟上估值修复节奏，可能引发回调。'
      focus = '下期关注点：PMI能否突破荣枯线、地产销售数据、出口订单持续性。'
      break
    case 'policy':
      judgment = '当前处于政策托底期，流动性宽松但经济动能和风险偏好双弱。政策强托底，市场仍在犹豫。'
      drivers = '主要驱动因素：1）货币政策持续宽松；2）财政发力预期；3）但实体信心恢复缓慢。'
      risks = '潜在风险：政策效果不及预期，流动性陷阱。'
      focus = '下期关注点：政策落地效果、M1-M2剪刀差变化、地产政策效果。'
      break
    default:
      judgment = `当前处于${phaseMap[data.phase] || '震荡期'}，各项指标交织，市场方向有待明确。`
      drivers = `流动性评分${data.liquidity_score}/5，经济动能${data.growth_score}/5，风险偏好${data.risk_score}/5。`
      risks = '需关注外部环境变化和内部政策节奏。'
      focus = '建议密切跟踪关键经济指标变化。'
  }
  
  // 添加关键变化
  if (data.key_changes.length > 0 && data.key_changes[0] !== '各项指标保持稳定') {
    judgment += `本期关键变化：${data.key_changes.join('、')}。`
  }
  
  // 添加矛盾点提示
  if (data.contradictions.length > 0 && data.contradictions[0] !== '暂无显著矛盾') {
    risks += `特别注意：${data.contradictions.join('；')}。`
  }
  
  return {
    judgment,
    drivers,
    risks,
    focus
  }
}
</script>

<style scoped>
.macro-score-panel {
  padding: 0;
}

.panel-header {
  margin-bottom: 20px;
  padding: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 8px;
  color: white;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.panel-header h2 {
  margin: 0 0 8px 0;
  font-size: 24px;
}

.subtitle {
  margin: 0;
  opacity: 0.9;
  font-size: 14px;
}

.score-overview {
  margin-bottom: 24px;
}

.total-score-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  text-align: center;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.score-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.phase-name {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 8px;
}

.phase-name.expansion { color: #52c41a; }
.phase-name.structural { color: #1890ff; }
.phase-name.policy { color: #fa8c16; }
.phase-name.defensive { color: #f5222d; }
.phase-name.stagflation { color: #722ed1; }
.phase-name.neutral { color: #8c8c8c; }

.total-score {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
}

.score-details {
  margin-bottom: 24px;
}

.score-section {
  background: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 16px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 2px solid #ebeef5;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.section-title .el-icon {
  font-size: 22px;
  color: #409eff;
}

.conclusion-section {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
}

.conclusion-header {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 12px;
  padding-bottom: 12px;
  border-bottom: 1px solid #ebeef5;
}

.conclusion-content {
  line-height: 1.8;
  color: #606266;
}

.conclusion-content p {
  margin: 8px 0;
}

.conclusion-text {
  margin-top: 12px;
  padding: 12px;
  background: #f5f7fa;
  border-radius: 6px;
  font-weight: 500;
}
</style>
