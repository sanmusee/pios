<template>
  <div class="structural-variables">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div>
          <h2>结构变量面板</h2>
          <p class="subtitle">系统核心 | 最近半年，哪些变量在强化？</p>
        </div>
        <div class="header-actions">
          <el-radio-group v-model="timeRange" size="small">
            <el-radio-button label="1m">近1月</el-radio-button>
            <el-radio-button label="3m">近3月</el-radio-button>
            <el-radio-button label="6m">近6月</el-radio-button>
            <el-radio-button label="1y">近1年</el-radio-button>
          </el-radio-group>
        </div>
      </div>
    </div>

    <!-- 筛选栏 -->
    <div class="filter-bar">
      <div class="filter-group">
        <span class="filter-label">类型筛选：</span>
        <el-checkbox-group v-model="selectedTypes" size="small">
          <el-checkbox-button label="policy">政策</el-checkbox-button>
          <el-checkbox-button label="capital">资本</el-checkbox-button>
          <el-checkbox-button label="supply">供需</el-checkbox-button>
          <el-checkbox-button label="technology">技术</el-checkbox-button>
        </el-checkbox-group>
      </div>
      
      <div class="filter-group">
        <span class="filter-label">方向：</span>
        <el-checkbox-group v-model="selectedDirections" size="small">
          <el-checkbox-button label="positive">正向</el-checkbox-button>
          <el-checkbox-button label="neutral">中性</el-checkbox-button>
          <el-checkbox-button label="negative">负向</el-checkbox-button>
        </el-checkbox-group>
      </div>
      
      <el-input
        v-model="searchQuery"
        placeholder="搜索变量名称..."
        style="width: 250px"
        :prefix-icon="Search"
        clearable
        size="small"
      />
    </div>

    <!-- 变量卡片网格 -->
    <div class="variables-grid">
      <div
        v-for="variable in filteredVariables"
        :key="variable.id"
        class="variable-card"
        :class="{ 'strengthening': variable.isStrengthening, 'weakening': variable.isWeakening }"
        @click="openDetail(variable)"
      >
        <div class="card-header">
          <div class="variable-name">{{ variable.name }}</div>
          <div class="variable-badges">
            <el-tag :type="getTypeTagType(variable.type)" size="small">
              {{ getTypeLabel(variable.type) }}
            </el-tag>
            <el-tag 
              :type="getDirectionTagType(variable.direction)" 
              size="small"
              class="direction-tag"
            >
              {{ getDirectionLabel(variable.direction) }}
            </el-tag>
          </div>
        </div>

        <div class="card-stats">
          <div class="stat-row">
            <div class="stat-item">
              <div class="stat-label">出现频率</div>
              <div class="stat-value">
                {{ variable.frequency }}次
                <span 
                  class="trend-indicator"
                  :class="{ 'up': variable.freqTrend > 0, 'down': variable.freqTrend < 0 }"
                >
                  {{ variable.freqTrend > 0 ? '↑' : variable.freqTrend < 0 ? '↓' : '-' }}
                </span>
              </div>
            </div>
            
            <div class="stat-item">
              <div class="stat-label">累计影响分</div>
              <div class="stat-value impact" :class="getImpactClass(variable.totalImpact)">
                {{ variable.totalImpact }}
              </div>
            </div>
          </div>

          <div class="time-range">
            <span>{{ variable.firstAppear }} 首次出现</span>
            <el-divider direction="vertical" />
            <span>最近 {{ variable.lastAppear }}</span>
          </div>
        </div>

        <div class="trend-chart">
          <v-chart class="mini-chart" :option="getMiniChartOption(variable)" autoresize />
        </div>

        <div class="card-footer">
          <el-tag v-if="variable.isStrengthening" type="danger" effect="dark" size="small">
            <ArrowUp /> 强化中
          </el-tag>
          <el-tag v-else-if="variable.isWeakening" type="info" effect="dark" size="small">
            <ArrowDown /> 弱化中
          </el-tag>
          <el-tag v-else type="success" effect="dark" size="small">
            <Minus /> 稳定
          </el-tag>
        </div>
      </div>
    </div>

    <!-- 详情弹窗 -->
    <el-dialog
      v-model="detailVisible"
      title="变量详情"
      width="800px"
      destroy-on-close
    >
      <div v-if="selectedVariable" class="detail-content">
        <div class="detail-header">
          <h3>{{ selectedVariable.name }}</h3>
          <div class="detail-badges">
            <el-tag :type="getTypeTagType(selectedVariable.type)" size="large">
              {{ getTypeLabel(selectedVariable.type) }}
            </el-tag>
            <el-tag :type="getDirectionTagType(selectedVariable.direction)" size="large">
              {{ getDirectionLabel(selectedVariable.direction) }}
            </el-tag>
          </div>
        </div>

        <el-descriptions :column="2" border>
          <el-descriptions-item label="首次出现">{{ selectedVariable.firstAppear }}</el-descriptions-item>
          <el-descriptions-item label="最近出现">{{ selectedVariable.lastAppear }}</el-descriptions-item>
          <el-descriptions-item label="出现频率">{{ selectedVariable.frequency }}次</el-descriptions-item>
          <el-descriptions-item label="累计影响分">
            <span class="impact-score" :class="getImpactClass(selectedVariable.totalImpact)">
              {{ selectedVariable.totalImpact }}
            </span>
          </el-descriptions-item>
        </el-descriptions>

        <div class="detail-chart">
          <h4>趋势变化</h4>
          <v-chart class="trend-chart-large" :option="getDetailChartOption(selectedVariable)" autoresize />
        </div>

        <div class="related-articles">
          <h4>相关文章</h4>
          <el-timeline>
            <el-timeline-item
              v-for="article in selectedVariable.relatedArticles"
              :key="article.id"
              :timestamp="article.time"
            >
              <div class="article-title">{{ article.title }}</div>
              <div class="article-source">{{ article.source }} | Impact: {{ article.impact }}</div>
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { Search, ArrowUp, ArrowDown, Minus } from '@element-plus/icons-vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart, BarChart } from 'echarts/charts'
import { GridComponent, TooltipComponent } from 'echarts/components'
import VChart from 'vue-echarts'

use([CanvasRenderer, LineChart, BarChart, GridComponent, TooltipComponent])

const timeRange = ref('6m')
const selectedTypes = ref(['policy', 'capital', 'supply', 'technology'])
const selectedDirections = ref(['positive', 'neutral', 'negative'])
const searchQuery = ref('')
const detailVisible = ref(false)
const selectedVariable = ref(null)

// 模拟变量数据
const variables = ref([
  {
    id: 1,
    name: '新能源补贴取消',
    type: 'policy',
    direction: 'negative',
    firstAppear: '2024-06',
    lastAppear: '2024-12',
    frequency: 45,
    freqTrend: 15,
    totalImpact: 892,
    isStrengthening: true,
    isWeakening: false,
    trendData: [10, 15, 20, 25, 35, 45],
    relatedArticles: [
      { id: 1, title: '新能源汽车补贴政策调整：退坡幅度超预期', source: '财经网', time: '2024-12-15', impact: 8.5 },
      { id: 2, title: '地方新能源补贴陆续退出时间表公布', source: '证券时报', time: '2024-11-20', impact: 7.2 },
      { id: 3, title: '补贴退坡对新能源车市场影响分析', source: '产业观察', time: '2024-10-08', impact: 6.8 }
    ]
  },
  {
    id: 2,
    name: 'AI基础模型融资集中化',
    type: 'capital',
    direction: 'positive',
    firstAppear: '2024-03',
    lastAppear: '2024-12',
    frequency: 68,
    freqTrend: 25,
    totalImpact: 1256,
    isStrengthening: true,
    isWeakening: false,
    trendData: [5, 12, 25, 38, 52, 68],
    relatedArticles: [
      { id: 4, title: 'AI基础模型融资集中化：头部效应加剧', source: '科技日报', time: '2024-12-15', impact: 7.8 },
      { id: 5, title: '大模型赛道融资额创新高', source: '投资界', time: '2024-11-28', impact: 8.2 }
    ]
  },
  {
    id: 3,
    name: '房地产行业监管收紧',
    type: 'policy',
    direction: 'negative',
    firstAppear: '2024-01',
    lastAppear: '2024-12',
    frequency: 120,
    freqTrend: -10,
    totalImpact: 2150,
    isStrengthening: false,
    isWeakening: true,
    trendData: [30, 35, 42, 48, 52, 50],
    relatedArticles: [
      { id: 6, title: '房地产行业监管收紧：三道红线政策升级', source: '证券时报', time: '2024-12-14', impact: 6.5 },
      { id: 7, title: '房地产融资环境持续收紧', source: '经济观察报', time: '2024-11-15', impact: 7.0 }
    ]
  },
  {
    id: 4,
    name: '半导体设备进口受限',
    type: 'supply',
    direction: 'negative',
    firstAppear: '2024-02',
    lastAppear: '2024-12',
    frequency: 56,
    freqTrend: 8,
    totalImpact: 768,
    isStrengthening: true,
    isWeakening: false,
    trendData: [8, 15, 28, 35, 48, 56],
    relatedArticles: [
      { id: 8, title: '半导体设备进口受限：国产替代加速', source: '产业观察', time: '2024-12-14', impact: 7.2 },
      { id: 9, title: '关键设备进口许可审批周期延长', source: '科技日报', time: '2024-11-30', impact: 6.8 }
    ]
  },
  {
    id: 5,
    name: '消费复苏信号',
    type: 'supply',
    direction: 'positive',
    firstAppear: '2024-04',
    lastAppear: '2024-12',
    frequency: 38,
    freqTrend: -5,
    totalImpact: 425,
    isStrengthening: false,
    isWeakening: false,
    trendData: [5, 12, 22, 28, 35, 38],
    relatedArticles: [
      { id: 10, title: '消费复苏信号：社零数据超预期增长', source: '经济参考报', time: '2024-12-14', impact: 5.8 },
      { id: 11, title: '消费市场回暖迹象明显', source: '财经杂志', time: '2024-11-20', impact: 6.2 }
    ]
  },
  {
    id: 6,
    name: '碳中和政策加码',
    type: 'policy',
    direction: 'positive',
    firstAppear: '2024-01',
    lastAppear: '2024-12',
    frequency: 82,
    freqTrend: 12,
    totalImpact: 1150,
    isStrengthening: true,
    isWeakening: false,
    trendData: [15, 28, 42, 55, 68, 82],
    relatedArticles: [
      { id: 12, title: '碳中和路线图更新：目标更加明确', source: '环保时报', time: '2024-12-10', impact: 7.5 }
    ]
  }
])

// 过滤变量
const filteredVariables = computed(() => {
  return variables.value.filter(v => {
    const typeMatch = selectedTypes.value.includes(v.type)
    const directionMatch = selectedDirections.value.includes(v.direction)
    const searchMatch = !searchQuery.value || v.name.toLowerCase().includes(searchQuery.value.toLowerCase())
    return typeMatch && directionMatch && searchMatch
  })
})

// 类型标签
const getTypeTagType = (type) => {
  const map = { policy: 'danger', capital: 'warning', supply: 'success', technology: 'primary' }
  return map[type] || 'info'
}

const getTypeLabel = (type) => {
  const map = { policy: '政策', capital: '资本', supply: '供需', technology: '技术' }
  return map[type] || type
}

// 方向标签
const getDirectionTagType = (direction) => {
  const map = { positive: 'success', neutral: 'info', negative: 'danger' }
  return map[direction] || 'info'
}

const getDirectionLabel = (direction) => {
  const map = { positive: '正向', neutral: '中性', negative: '负向' }
  return map[direction] || direction
}

// 影响分样式
const getImpactClass = (score) => {
  if (score >= 1000) return 'high'
  if (score >= 500) return 'medium'
  return 'low'
}

// 迷你图表配置
const getMiniChartOption = (variable) => {
  return {
    grid: { left: 0, right: 0, top: 5, bottom: 5 },
    xAxis: { type: 'category', show: false, data: ['1月', '2月', '3月', '4月', '5月', '6月'] },
    yAxis: { type: 'value', show: false },
    tooltip: { trigger: 'axis' },
    series: [{
      type: 'line',
      data: variable.trendData,
      smooth: true,
      showSymbol: false,
      lineStyle: { 
        width: 2, 
        color: variable.isStrengthening ? '#f5222d' : variable.isWeakening ? '#8c8c8c' : '#52c41a' 
      },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0, y: 0, x2: 0, y2: 1,
          colorStops: [
            { offset: 0, color: variable.isStrengthening ? 'rgba(245, 34, 45, 0.3)' : 'rgba(82, 196, 26, 0.3)' },
            { offset: 1, color: 'transparent' }
          ]
        }
      }
    }]
  }
}

// 详情图表配置
const getDetailChartOption = (variable) => {
  return {
    grid: { left: 50, right: 30, top: 30, bottom: 50 },
    xAxis: { 
      type: 'category', 
      data: ['1月', '2月', '3月', '4月', '5月', '6月'],
      axisLabel: { fontSize: 12 }
    },
    yAxis: { 
      type: 'value', 
      name: '出现次数',
      axisLabel: { fontSize: 12 }
    },
    tooltip: { trigger: 'axis' },
    series: [{
      type: 'bar',
      data: variable.trendData,
      itemStyle: {
        color: {
          type: 'linear',
          x: 0, y: 0, x2: 0, y2: 1,
          colorStops: [
            { offset: 0, color: '#667eea' },
            { offset: 1, color: '#764ba2' }
          ]
        }
      }
    }]
  }
}

// 打开详情
const openDetail = (variable) => {
  selectedVariable.value = variable
  detailVisible.value = true
}
</script>

<style scoped>
.structural-variables {
  padding: 0;
}

.page-header {
  margin-bottom: 20px;
  padding: 20px;
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
  border-radius: 8px;
  color: white;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-content h2 {
  margin: 0 0 8px 0;
  font-size: 24px;
}

.subtitle {
  margin: 0;
  opacity: 0.9;
  font-size: 14px;
}

/* 筛选栏 */
.filter-bar {
  background: white;
  padding: 16px 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 8px;
}

.filter-label {
  font-size: 13px;
  color: #606266;
  font-weight: 500;
}

/* 变量网格 */
.variables-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 16px;
}

.variable-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.variable-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.variable-card.strengthening {
  border-color: #f5222d;
}

.variable-card.weakening {
  border-color: #8c8c8c;
}

.card-header {
  margin-bottom: 16px;
}

.variable-name {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}

.variable-badges {
  display: flex;
  gap: 8px;
}

.direction-tag {
  margin-left: 4px;
}

/* 卡片统计 */
.card-stats {
  margin-bottom: 16px;
}

.stat-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12px;
}

.stat-item {
  text-align: center;
}

.stat-label {
  font-size: 12px;
  color: #909399;
  margin-bottom: 4px;
}

.stat-value {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
}

.stat-value.impact {
  font-size: 24px;
}

.stat-value.impact.high {
  color: #f5222d;
}

.stat-value.impact.medium {
  color: #fa8c16;
}

.stat-value.impact.low {
  color: #52c41a;
}

.trend-indicator {
  font-size: 14px;
  margin-left: 4px;
}

.trend-indicator.up {
  color: #f5222d;
}

.trend-indicator.down {
  color: #52c41a;
}

.time-range {
  font-size: 12px;
  color: #909399;
  text-align: center;
}

/* 趋势图表 */
.trend-chart {
  height: 80px;
  margin-bottom: 12px;
}

.mini-chart {
  width: 100%;
  height: 100%;
}

/* 卡片底部 */
.card-footer {
  display: flex;
  justify-content: center;
}

/* 详情弹窗 */
.detail-content {
  padding: 0 10px;
}

.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.detail-header h3 {
  margin: 0;
  font-size: 22px;
  color: #303133;
}

.detail-badges {
  display: flex;
  gap: 8px;
}

.impact-score {
  font-size: 20px;
  font-weight: bold;
}

.impact-score.high {
  color: #f5222d;
}

.impact-score.medium {
  color: #fa8c16;
}

.impact-score.low {
  color: #52c41a;
}

.detail-chart {
  margin: 20px 0;
}

.detail-chart h4 {
  margin: 0 0 12px 0;
  font-size: 16px;
  color: #303133;
}

.trend-chart-large {
  height: 250px;
}

.related-articles h4 {
  margin: 0 0 16px 0;
  font-size: 16px;
  color: #303133;
}

.article-title {
  font-weight: 500;
  color: #303133;
  margin-bottom: 4px;
}

.article-source {
  font-size: 12px;
  color: #909399;
}
</style>