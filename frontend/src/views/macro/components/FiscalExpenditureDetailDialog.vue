<template>
  <el-dialog
    v-model="visible"
    title="财政支出深度分析"
    width="98%"
    :close-on-click-modal="true"
    class="fiscal-detail-dialog"
    destroy-on-close
    top="2vh"
  >
    <!-- 顶部控制栏 -->
    <div class="control-bar">
      <div class="time-selector">
        <span class="label">时间范围：</span>
        <el-radio-group v-model="timeRange" size="small">
          <el-radio-button label="1y">近1年</el-radio-button>
          <el-radio-button label="3y">近3年</el-radio-button>
          <el-radio-button label="all">全部历史</el-radio-button>
        </el-radio-group>
      </div>
      
      <div class="category-filter">
        <span class="label">显示类别：</span>
        <el-checkbox-group v-model="selectedCategories" size="small">
          <el-checkbox-button 
            v-for="cat in categories" 
            :key="cat.key" 
            :label="cat.key"
            :style="{ '--cat-color': cat.color }"
          >
            <span class="cat-dot" :style="{ background: cat.color }"></span>
            {{ cat.name }}
          </el-checkbox-button>
        </el-checkbox-group>
      </div>
    </div>

    <div class="detail-content">
      <!-- 第一行：总规模趋势 + 政策倾向评分 -->
      <el-row :gutter="16" class="row-section">
        <el-col :span="16">
          <div class="module-card">
            <div class="module-header">
              <h4>模块一：总财政规模趋势</h4>
              <span class="module-desc">判断财政是否进入扩张周期</span>
            </div>
            <div class="chart-container">
              <v-chart class="chart" :option="totalScaleOption" autoresize />
            </div>
          </div>
        </el-col>
        
        <el-col :span="8">
          <div class="module-card">
            <div class="module-header">
              <h4>政策倾向评分</h4>
              <span class="module-desc">当前财政配置倾向</span>
            </div>
            <div class="chart-container small">
              <v-chart class="chart" :option="policyRadarOption" autoresize />
            </div>
            <div class="score-summary">
              <div class="score-item">
                <span class="score-label">稳增长指数</span>
                <el-progress :percentage="scores.stabilize" :color="'#52c41a'" />
              </div>
              <div class="score-item">
                <span class="score-label">产业升级指数</span>
                <el-progress :percentage="scores.upgrade" :color="'#faad14'" />
              </div>
              <div class="score-item">
                <span class="score-label">社会保障指数</span>
                <el-progress :percentage="scores.social" :color="'#1890ff'" />
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 第二行：分类绝对金额趋势 -->
      <el-row :gutter="16" class="row-section">
        <el-col :span="24">
          <div class="module-card">
            <div class="module-header">
              <h4>模块二：分类绝对金额趋势</h4>
              <span class="module-desc">点击图例可高亮单项，观察规模与结构变化</span>
            </div>
            <div class="chart-container">
              <v-chart 
                class="chart" 
                :option="absoluteTrendOption" 
                autoresize 
                @legendselectchanged="handleLegendChange"
              />
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 第三行：占比趋势 + 结构变化差值 -->
      <el-row :gutter="16" class="row-section">
        <el-col :span="12">
          <div class="module-card">
            <div class="module-header">
              <h4>模块三：分类占比趋势</h4>
              <span class="module-desc">识别结构倾斜，发现趋势性变化</span>
            </div>
            <div class="chart-container">
              <v-chart class="chart" :option="ratioTrendOption" autoresize />
            </div>
          </div>
        </el-col>
        
        <el-col :span="12">
          <div class="module-card">
            <div class="module-header">
              <h4>模块四：结构变化差值（边际变化）</h4>
              <span class="module-desc">当前占比 vs 去年同期 vs 三年均值</span>
            </div>
            <div class="chart-container">
              <v-chart class="chart" :option="changeDiffOption" autoresize />
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 第四行：金额 vs 占比联动 + 智能提示 -->
      <el-row :gutter="16" class="row-section">
        <el-col :span="12">
          <div class="module-card">
            <div class="module-header">
              <h4>模块五：金额 vs 占比联动分析</h4>
              <span class="module-desc">判断主动倾斜 vs 被动上涨</span>
            </div>
            <div class="linkage-selector">
              <el-select v-model="selectedLinkageCategory" size="small" style="width: 120px">
                <el-option 
                  v-for="cat in categories" 
                  :key="cat.key" 
                  :label="cat.name" 
                  :value="cat.key"
                />
              </el-select>
            </div>
            <div class="chart-container">
              <v-chart class="chart" :option="linkageOption" autoresize />
            </div>
          </div>
        </el-col>
        
        <el-col :span="12">
          <div class="module-card insight-card">
            <div class="module-header">
              <h4><DataAnalysis style="width: 18px; height: 18px; vertical-align: middle; margin-right: 6px;" />智能分析结论</h4>
            </div>
            <div class="insight-content">
              <div v-for="(insight, index) in insights" :key="index" class="insight-item">
                <div class="insight-icon" :class="insight.type">
                  <el-icon v-if="insight.type === 'up'"><ArrowUp /></el-icon>
                  <el-icon v-else-if="insight.type === 'down'"><ArrowDown /></el-icon>
                  <el-icon v-else><InfoFilled /></el-icon>
                </div>
                <div class="insight-text">
                  <strong>{{ insight.title }}</strong>
                  <p>{{ insight.content }}</p>
                </div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 底部数据摘要 -->
      <div class="summary-bar">
        <el-row :gutter="16">
          <el-col :span="4" v-for="cat in categories" :key="cat.key">
            <div class="summary-item">
              <span class="summary-dot" :style="{ background: cat.color }"></span>
              <div class="summary-info">
                <div class="summary-name">{{ cat.name }}</div>
                <div class="summary-value">
                  {{ getCurrentValue(cat.key) }}亿
                  <span class="summary-ratio">({{ getCurrentRatio(cat.key) }}%)</span>
                </div>
                <div class="summary-yoy" :class="{ 'up': getYoy(cat.key) > 0, 'down': getYoy(cat.key) < 0 }">
                  同比 {{ getYoy(cat.key) > 0 ? '+' : '' }}{{ getYoy(cat.key) }}%
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" plain @click="exportData">
          <Download style="width: 14px; height: 14px; margin-right: 4px;" />导出数据
        </el-button>
        <el-button type="primary" @click="visible = false">关闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, defineExpose, watch } from 'vue'
import { InfoFilled, ArrowUp, ArrowDown, DataAnalysis, Download } from '@element-plus/icons-vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart, BarChart, RadarChart } from 'echarts/charts'
import { 
  GridComponent, 
  TooltipComponent, 
  LegendComponent, 
  DataZoomComponent,
  RadarComponent
} from 'echarts/components'
import VChart from 'vue-echarts'

use([
  CanvasRenderer, 
  LineChart, 
  BarChart, 
  RadarChart,
  GridComponent, 
  TooltipComponent, 
  LegendComponent, 
  DataZoomComponent,
  RadarComponent
])

const visible = ref(false)
const timeRange = ref('1y')
const selectedCategories = ref(['livelihood', 'infrastructure', 'technology', 'environment', 'defense'])
const selectedLinkageCategory = ref('technology')

// 颜色配置
const categories = [
  { key: 'livelihood', name: '民生', color: '#5470c6' },
  { key: 'infrastructure', name: '基建', color: '#91cc75' },
  { key: 'technology', name: '科技', color: '#fac858' },
  { key: 'environment', name: '环保', color: '#ee6666' },
  { key: 'defense', name: '国防', color: '#73c0de' }
]

// 生成36个月的历史数据
const generateHistoricalData = () => {
  const dates = []
  const livelihood = []
  const infrastructure = []
  const technology = []
  const environment = []
  const defense = []
  const total = []
  
  const baseDate = new Date(2022, 0, 1)
  
  for (let i = 0; i < 36; i++) {
    const date = new Date(baseDate.getFullYear(), baseDate.getMonth() + i, 1)
    dates.push(`${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}`)
    
    // 模拟数据，带有趋势性变化
    const trend = i / 36
    livelihood.push(Math.round(5000 + trend * 1500 + Math.random() * 500))
    infrastructure.push(Math.round(4000 + Math.sin(trend * Math.PI) * 1000 + Math.random() * 400))
    technology.push(Math.round(1500 + trend * 1200 + Math.random() * 200))
    environment.push(Math.round(1200 + trend * 800 + Math.random() * 150))
    defense.push(Math.round(1000 + trend * 600 + Math.random() * 100))
  }
  
  for (let i = 0; i < 36; i++) {
    total.push(livelihood[i] + infrastructure[i] + technology[i] + environment[i] + defense[i])
  }
  
  return { dates, livelihood, infrastructure, technology, environment, defense, total }
}

const rawData = ref(generateHistoricalData())

// 根据时间范围过滤数据
const filteredData = computed(() => {
  const { dates, ...rest } = rawData.value
  let startIndex = 0
  
  if (timeRange.value === '1y') {
    startIndex = Math.max(0, dates.length - 12)
  } else if (timeRange.value === '3y') {
    startIndex = Math.max(0, dates.length - 36)
  }
  
  return {
    dates: dates.slice(startIndex),
    livelihood: rest.livelihood.slice(startIndex),
    infrastructure: rest.infrastructure.slice(startIndex),
    technology: rest.technology.slice(startIndex),
    environment: rest.environment.slice(startIndex),
    defense: rest.defense.slice(startIndex),
    total: rest.total.slice(startIndex)
  }
})

// 计算各项占比
const ratioData = computed(() => {
  const { livelihood, infrastructure, technology, environment, defense, total } = filteredData.value
  return {
    livelihood: livelihood.map((v, i) => parseFloat((v / total[i] * 100).toFixed(1))),
    infrastructure: infrastructure.map((v, i) => parseFloat((v / total[i] * 100).toFixed(1))),
    technology: technology.map((v, i) => parseFloat((v / total[i] * 100).toFixed(1))),
    environment: environment.map((v, i) => parseFloat((v / total[i] * 100).toFixed(1))),
    defense: defense.map((v, i) => parseFloat((v / total[i] * 100).toFixed(1)))
  }
})

// 计算同比增速
const yoyData = computed(() => {
  const result = {}
  const keys = ['livelihood', 'infrastructure', 'technology', 'environment', 'defense', 'total']
  
  keys.forEach(key => {
    const arr = filteredData.value[key]
    result[key] = arr.map((v, i) => {
      if (i < 12) return 0
      const prevYear = arr[i - 12]
      return parseFloat(((v - prevYear) / prevYear * 100).toFixed(1))
    })
  })
  
  return result
})

// 政策倾向评分
const scores = computed(() => {
  const lastIdx = filteredData.value.dates.length - 1
  const ratios = {
    livelihood: ratioData.value.livelihood[lastIdx],
    infrastructure: ratioData.value.infrastructure[lastIdx],
    technology: ratioData.value.technology[lastIdx]
  }
  
  return {
    stabilize: Math.round(ratios.infrastructure * 1.5 + ratios.livelihood * 0.5),
    upgrade: Math.round(ratios.technology * 3),
    social: Math.round(ratios.livelihood * 1.8)
  }
})

// 智能分析结论
const insights = computed(() => {
  const result = []
  const lastIdx = filteredData.value.dates.length - 1
  const prevIdx = Math.max(0, lastIdx - 6)
  
  // 科技分析
  const techRatioChange = ratioData.value.technology[lastIdx] - ratioData.value.technology[prevIdx]
  if (techRatioChange > 2) {
    result.push({
      type: 'up',
      title: '产业升级信号',
      content: `近6个月科技占比提升${techRatioChange.toFixed(1)}个百分点，财政明显向创新驱动倾斜。`
    })
  }
  
  // 基建分析
  const infraRatioChange = ratioData.value.infrastructure[lastIdx] - ratioData.value.infrastructure[prevIdx]
  const infraValueChange = (filteredData.value.infrastructure[lastIdx] - filteredData.value.infrastructure[prevIdx]) / filteredData.value.infrastructure[prevIdx]
  if (infraRatioChange < -2 && infraValueChange > 0) {
    result.push({
      type: 'info',
      title: '基建结构性调整',
      content: '基建支出绝对值增长但占比下降，属于正常的结构调整，非刺激退坡。'
    })
  }
  
  // 民生分析
  const liveRatio = ratioData.value.livelihood[lastIdx]
  if (liveRatio > 38) {
    result.push({
      type: 'up',
      title: '民生保障优先',
      content: `民生占比达${liveRatio}%，创阶段性新高，政策重心偏向稳就业保民生。`
    })
  }
  
  // 总规模分析
  const totalYoy = yoyData.value.total[lastIdx]
  if (totalYoy > 8) {
    result.push({
      type: 'up',
      title: '财政扩张周期',
      content: `总支出同比增长${totalYoy}%，财政进入积极扩张周期，逆周期调节力度加大。`
    })
  } else if (totalYoy < 3) {
    result.push({
      type: 'down',
      title: '财政趋于保守',
      content: `总支出同比增长仅${totalYoy}%，财政支出趋于审慎，注重资金使用效率。`
    })
  }
  
  return result.length > 0 ? result : [{
    type: 'info',
    title: '结构相对均衡',
    content: '各项支出占比变化平稳，财政配置处于相对均衡状态。'
  }]
})

// 模块一：总财政规模趋势
const totalScaleOption = computed(() => {
  const { dates, total } = filteredData.value
  const yoy = yoyData.value.total
  
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'cross' },
      formatter: function(params) {
        let html = `<strong>${params[0].axisValue}</strong><br/>`
        params.forEach(p => {
          if (p.seriesName === '总支出') {
            html += `${p.marker} ${p.seriesName}: ${p.value}亿元<br/>`
          } else {
            html += `${p.marker} ${p.seriesName}: ${p.value}%<br/>`
          }
        })
        return html
      }
    },
    legend: { data: ['总支出', '同比增速'], top: 0 },
    grid: { left: 60, right: 60, bottom: 50, top: 40 },
    xAxis: {
      type: 'category',
      data: dates,
      axisLabel: { rotate: 30, fontSize: 10 }
    },
    yAxis: [
      {
        type: 'value',
        name: '金额(亿元)',
        position: 'left',
        axisLabel: { fontSize: 10 }
      },
      {
        type: 'value',
        name: '同比(%)',
        position: 'right',
        axisLabel: { formatter: '{value}%', fontSize: 10 }
      }
    ],
    dataZoom: [{ type: 'inside', start: 0, end: 100 }],
    series: [
      {
        name: '总支出',
        type: 'line',
        data: total,
        smooth: true,
        lineStyle: { width: 3, color: '#1890ff' },
        itemStyle: { color: '#1890ff' },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [
              { offset: 0, color: 'rgba(24, 144, 255, 0.3)' },
              { offset: 1, color: 'rgba(24, 144, 255, 0.05)' }
            ]
          }
        }
      },
      {
        name: '同比增速',
        type: 'line',
        yAxisIndex: 1,
        data: yoy,
        smooth: true,
        lineStyle: { width: 2, color: '#52c41a', type: 'dashed' },
        itemStyle: { color: '#52c41a' }
      }
    ]
  }
})

// 政策倾向雷达图
const policyRadarOption = computed(() => {
  return {
    tooltip: {},
    radar: {
      indicator: [
        { name: '稳增长', max: 100 },
        { name: '产业升级', max: 100 },
        { name: '社会保障', max: 100 }
      ],
      center: ['50%', '45%'],
      radius: '60%'
    },
    series: [{
      type: 'radar',
      data: [{
        value: [scores.value.stabilize, scores.value.upgrade, scores.value.social],
        name: '当前配置',
        areaStyle: {
          color: 'rgba(24, 144, 255, 0.3)'
        },
        lineStyle: { color: '#1890ff', width: 2 },
        itemStyle: { color: '#1890ff' }
      }]
    }]
  }
})

// 模块二：分类绝对金额趋势（堆叠面积图 - 所有子项都从0开始）
const absoluteTrendOption = computed(() => {
  const { dates, total } = filteredData.value
  const series = []
  
  // 首先添加总量作为参考线（不堆叠，独立显示）
  series.push({
    name: '总支出',
    type: 'line',
    smooth: true,
    lineStyle: { width: 3, color: '#333', type: 'dashed' },
    showSymbol: false,
    itemStyle: { color: '#333' },
    data: total,
    z: 100  // 确保总量线在最上层
  })
  
  // 所有子项都从0开始，各自独立显示面积
  // 使用不同的stack名称，让它们都独立堆叠（实际上都是从0开始）
  categories.forEach((cat, index) => {
    if (selectedCategories.value.includes(cat.key)) {
      series.push({
        name: cat.name,
        type: 'line',
        // 每个子项使用不同的stack，这样它们都独立从0开始
        stack: `Stack_${cat.key}`,
        smooth: true,
        lineStyle: { width: 2 },
        showSymbol: false,
        areaStyle: { 
          opacity: 0.6,
          color: {
            type: 'linear',
            x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [
              { offset: 0, color: cat.color },
              { offset: 1, color: cat.color + '60' }
            ]
          }
        },
        emphasis: { focus: 'series' },
        itemStyle: { color: cat.color },
        data: filteredData.value[cat.key],
        z: 10 + index
      })
    }
  })
  
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'cross' },
      formatter: function(params) {
        let html = `<strong>${params[0].axisValue}</strong><br/>`
        let totalVal = 0
        
        // 先找总量
        const totalParam = params.find(p => p.seriesName === '总支出')
        if (totalParam) {
          html += `${totalParam.marker} <strong>总支出: ${totalParam.value}亿元</strong><br/>`
          totalVal = totalParam.value
          html += `<div style="margin: 8px 0; border-top: 1px solid #eee;"></div>`
        }
        
        // 显示各项实际值
        params.forEach(p => {
          if (p.seriesName !== '总支出') {
            const actualValue = p.value
            const percent = totalVal > 0 ? ((actualValue / totalVal) * 100).toFixed(1) : 0
            html += `${p.marker} ${p.seriesName}: ${actualValue}亿元 (${percent}%)<br/>`
          }
        })
        
        return html
      }
    },
    legend: { 
      data: ['总支出', ...categories.filter(c => selectedCategories.value.includes(c.key)).map(c => c.name)],
      top: 0 
    },
    grid: { left: 60, right: 40, bottom: 50, top: 40 },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: dates,
      axisLabel: { rotate: 30, fontSize: 10 }
    },
    yAxis: {
      type: 'value',
      name: '支出金额(亿元)',
      axisLabel: { fontSize: 10 },
      max: value => {
        // Y轴最大值设为总量的110%，确保总量线可见
        const maxTotal = Math.max(...total)
        return Math.round(maxTotal * 1.1)
      }
    },
    dataZoom: [{ type: 'inside', start: 0, end: 100 }],
    series
  }
})

// 模块三：分类占比趋势
const ratioTrendOption = computed(() => {
  const { dates } = filteredData.value
  const series = []
  
  categories.forEach(cat => {
    if (selectedCategories.value.includes(cat.key)) {
      series.push({
        name: cat.name,
        type: 'line',
        smooth: true,
        lineStyle: { width: 2 },
        itemStyle: { color: cat.color },
        data: ratioData.value[cat.key]
      })
    }
  })
  
  return {
    tooltip: {
      trigger: 'axis',
      formatter: function(params) {
        let html = `<strong>${params[0].axisValue}</strong><br/>`
        params.forEach(p => {
          html += `${p.marker} ${p.seriesName}: ${p.value}%<br/>`
        })
        return html
      }
    },
    legend: { 
      data: categories.filter(c => selectedCategories.value.includes(c.key)).map(c => c.name),
      top: 0 
    },
    grid: { left: 50, right: 40, bottom: 50, top: 40 },
    xAxis: {
      type: 'category',
      data: dates,
      axisLabel: { rotate: 30, fontSize: 10 }
    },
    yAxis: {
      type: 'value',
      name: '占比(%)',
      axisLabel: { formatter: '{value}%', fontSize: 10 }
    },
    dataZoom: [{ type: 'inside', start: 0, end: 100 }],
    series
  }
})

// 模块四：结构变化差值
const changeDiffOption = computed(() => {
  const lastIdx = filteredData.value.dates.length - 1
  const yearAgoIdx = Math.max(0, lastIdx - 12)
  const categories_filtered = categories.filter(c => selectedCategories.value.includes(c.key))
  
  const yoyChange = categories_filtered.map(cat => {
    const current = ratioData.value[cat.key][lastIdx]
    const yearAgo = ratioData.value[cat.key][yearAgoIdx] || current
    return parseFloat((current - yearAgo).toFixed(1))
  })
  
  // 计算三年均值变化
  const avg3yChange = categories_filtered.map(cat => {
    const current = ratioData.value[cat.key][lastIdx]
    const avg3y = ratioData.value[cat.key].slice(-36).reduce((a, b) => a + b, 0) / Math.min(36, ratioData.value[cat.key].length)
    return parseFloat((current - avg3y).toFixed(1))
  })
  
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' },
      formatter: function(params) {
        let html = `<strong>${params[0].axisValue}</strong><br/>`
        params.forEach(p => {
          const val = p.value
          const sign = val > 0 ? '+' : ''
          html += `${p.marker} ${p.seriesName}: ${sign}${val}%<br/>`
        })
        return html
      }
    },
    legend: { data: ['同比去年', 'vs三年均值'], top: 0 },
    grid: { left: 60, right: 40, bottom: 50, top: 40 },
    xAxis: {
      type: 'category',
      data: categories_filtered.map(c => c.name),
      axisLabel: { fontSize: 11 }
    },
    yAxis: {
      type: 'value',
      name: '占比变化(%)',
      axisLabel: { formatter: '{value}%', fontSize: 10 }
    },
    series: [
      {
        name: '同比去年',
        type: 'bar',
        data: yoyChange,
        itemStyle: {
          color: function(params) {
            return params.value >= 0 ? '#52c41a' : '#f5222d'
          }
        }
      },
      {
        name: 'vs三年均值',
        type: 'bar',
        data: avg3yChange,
        itemStyle: {
          color: function(params) {
            return params.value >= 0 ? '#1890ff' : '#faad14'
          }
        }
      }
    ]
  }
})

// 模块五：金额 vs 占比联动
const linkageOption = computed(() => {
  const { dates } = filteredData.value
  const cat = categories.find(c => c.key === selectedLinkageCategory.value)
  const amount = filteredData.value[selectedLinkageCategory.value]
  const ratio = ratioData.value[selectedLinkageCategory.value]
  
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'cross' },
      formatter: function(params) {
        let html = `<strong>${params[0].axisValue}</strong><br/>`
        params.forEach(p => {
          if (p.seriesName.includes('金额')) {
            html += `${p.marker} ${p.seriesName}: ${p.value}亿元<br/>`
          } else {
            html += `${p.marker} ${p.seriesName}: ${p.value}%<br/>`
          }
        })
        return html
      }
    },
    legend: { data: [`${cat.name}金额`, `${cat.name}占比`], top: 0 },
    grid: { left: 60, right: 60, bottom: 50, top: 40 },
    xAxis: {
      type: 'category',
      data: dates,
      axisLabel: { rotate: 30, fontSize: 10 }
    },
    yAxis: [
      {
        type: 'value',
        name: '金额(亿元)',
        position: 'left',
        axisLabel: { fontSize: 10 }
      },
      {
        type: 'value',
        name: '占比(%)',
        position: 'right',
        axisLabel: { formatter: '{value}%', fontSize: 10 }
      }
    ],
    series: [
      {
        name: `${cat.name}金额`,
        type: 'line',
        data: amount,
        smooth: true,
        lineStyle: { width: 3, color: cat.color },
        itemStyle: { color: cat.color },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [
              { offset: 0, color: cat.color + '4D' },
              { offset: 1, color: cat.color + '0D' }
            ]
          }
        }
      },
      {
        name: `${cat.name}占比`,
        type: 'line',
        yAxisIndex: 1,
        data: ratio,
        smooth: true,
        lineStyle: { width: 2, color: '#f5222d', type: 'dashed' },
        itemStyle: { color: '#f5222d' }
      }
    ]
  }
})

// 获取当前值
const getCurrentValue = (key) => {
  const lastIdx = filteredData.value.dates.length - 1
  return filteredData.value[key][lastIdx]
}

// 获取当前占比
const getCurrentRatio = (key) => {
  const lastIdx = filteredData.value.dates.length - 1
  return ratioData.value[key][lastIdx]
}

// 获取同比增速
const getYoy = (key) => {
  const lastIdx = filteredData.value.dates.length - 1
  return yoyData.value[key][lastIdx]
}

// 处理图例变化
const handleLegendChange = (params) => {
  // 可以在这里添加高亮逻辑
}

// 导出数据
const exportData = () => {
  console.log('导出数据', filteredData.value)
}

const open = () => {
  visible.value = true
}

defineExpose({
  open
})
</script>

<style scoped>
.fiscal-detail-dialog :deep(.el-dialog__header) {
  background: linear-gradient(135deg, #1890ff 0%, #36cfc9 100%);
  margin-right: 0;
  padding: 16px 20px;
}

.fiscal-detail-dialog :deep(.el-dialog__title) {
  color: white;
  font-weight: 600;
  font-size: 18px;
}

.fiscal-detail-dialog :deep(.el-dialog__headerbtn .el-dialog__close) {
  color: white;
}

.fiscal-detail-dialog :deep(.el-dialog__body) {
  padding: 16px 20px;
  max-height: 85vh;
  overflow-y: auto;
}

/* 控制栏 */
.control-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding: 12px 16px;
  background: #f5f7fa;
  border-radius: 8px;
}

.time-selector,
.category-filter {
  display: flex;
  align-items: center;
  gap: 8px;
}

.label {
  font-size: 13px;
  color: #606266;
  font-weight: 500;
}

.cat-dot {
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-right: 4px;
}

/* 内容区域 */
.detail-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.row-section {
  margin: 0 !important;
}

/* 模块卡片 */
.module-card {
  background: white;
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  height: 100%;
}

.module-header {
  margin-bottom: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.module-header h4 {
  margin: 0;
  font-size: 15px;
  color: #303133;
  font-weight: 600;
}

.module-desc {
  font-size: 12px;
  color: #909399;
}

.chart-container {
  height: 280px;
}

.chart-container.small {
  height: 200px;
}

.chart {
  width: 100%;
  height: 100%;
}

/* 评分摘要 */
.score-summary {
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
}

.score-item {
  margin-bottom: 12px;
}

.score-item:last-child {
  margin-bottom: 0;
}

.score-label {
  display: block;
  font-size: 12px;
  color: #606266;
  margin-bottom: 4px;
}

/* 联动选择器 */
.linkage-selector {
  margin-bottom: 8px;
}

/* 洞察卡片 */
.insight-card {
  background: linear-gradient(135deg, #f6ffed 0%, #e6f7ff 100%);
}

.insight-content {
  max-height: 280px;
  overflow-y: auto;
}

.insight-item {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
}

.insight-item:last-child {
  margin-bottom: 0;
  padding-bottom: 0;
  border-bottom: none;
}

.insight-icon {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.insight-icon.up {
  background: #f6ffed;
  color: #52c41a;
}

.insight-icon.down {
  background: #fff1f0;
  color: #f5222d;
}

.insight-icon.info {
  background: #e6f7ff;
  color: #1890ff;
}

.insight-text strong {
  display: block;
  font-size: 13px;
  color: #303133;
  margin-bottom: 4px;
}

.insight-text p {
  margin: 0;
  font-size: 12px;
  color: #606266;
  line-height: 1.6;
}

/* 底部摘要栏 */
.summary-bar {
  background: white;
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.summary-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.summary-dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  flex-shrink: 0;
}

.summary-info {
  flex: 1;
}

.summary-name {
  font-size: 12px;
  color: #909399;
}

.summary-value {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.summary-ratio {
  font-size: 12px;
  color: #606266;
  font-weight: normal;
  margin-left: 4px;
}

.summary-yoy {
  font-size: 11px;
  font-weight: 500;
}

.summary-yoy.up {
  color: #52c41a;
}

.summary-yoy.down {
  color: #f5222d;
}

/* 对话框底部 */
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>