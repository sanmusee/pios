<template>
  <el-dialog
    v-model="visible"
    title="源头模块详情 —— M1-M2剪刀差深度分析"
    width="95%"
    :close-on-click-modal="true"
    class="source-detail-dialog"
    destroy-on-close
  >
    <div class="detail-content">
      <!-- 顶部：归一化对比图 -->
      <div class="section standardized-section">
        <div class="section-header">
          <h3>归一化对比图 (Standardized Chart)</h3>
          <span class="section-desc">以2024-01-01为基准(100点)，观察M1-M2变动过程中各资产的相对强弱</span>
        </div>
        <div class="chart-container large">
          <v-chart class="chart" :option="standardizedChartOption" autoresize @updateAxisPointer="handleAxisPointer" />
        </div>
      </div>

      <!-- 中部：多图并排 (Small Multiples) -->
      <div class="section small-multiples-section">
        <div class="section-header">
          <h3>多图并排 (Small Multiples) —— 相关性验证</h3>
          <span class="section-desc">控制变量法观察剪刀差对不同属性资产的解释力度</span>
        </div>
        <el-row :gutter="16">
          <el-col :span="6">
            <div class="small-chart-container">
              <div class="chart-title">M1-M2 vs 中证1000</div>
              <v-chart class="chart" :option="smallMultipleOptions.zz1000" autoresize @updateAxisPointer="handleAxisPointer" />
            </div>
          </el-col>
          <el-col :span="6">
            <div class="small-chart-container">
              <div class="chart-title">M1-M2 vs 创业板指</div>
              <v-chart class="chart" :option="smallMultipleOptions.cyb" autoresize @updateAxisPointer="handleAxisPointer" />
            </div>
          </el-col>
          <el-col :span="6">
            <div class="small-chart-container">
              <div class="chart-title">M1-M2 vs 万得全A</div>
              <v-chart class="chart" :option="smallMultipleOptions.windA" autoresize @updateAxisPointer="handleAxisPointer" />
            </div>
          </el-col>
          <el-col :span="6">
            <div class="small-chart-container">
              <div class="chart-title">M1-M2 vs 10Y国债</div>
              <v-chart class="chart" :option="smallMultipleOptions.bond10y" autoresize @updateAxisPointer="handleAxisPointer" />
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 底部：逻辑计算辅助 -->
      <div class="section ratio-section">
        <div class="section-header">
          <h3>逻辑计算辅助 (Logic Metrics)</h3>
          <span class="section-desc">中证1000/沪深300比值 —— 直观展示市场偏向"大盘价值"还是"小盘成长"</span>
        </div>
        <div class="chart-container medium">
          <v-chart class="chart" :option="ratioChartOption" autoresize @updateAxisPointer="handleAxisPointer" />
        </div>
      </div>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <div class="footer-hints">
          <el-tag size="small" type="info">💡 提示：所有图表支持同步十字准星，鼠标悬停可查看对齐数据</el-tag>
        </div>
        <el-button type="primary" @click="visible = false">关闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, defineExpose } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart, BarChart } from 'echarts/charts'
import { 
  GridComponent, 
  TooltipComponent, 
  LegendComponent, 
  DataZoomComponent,
  MarkLineComponent,
  MarkAreaComponent
} from 'echarts/components'
import VChart from 'vue-echarts'

use([
  CanvasRenderer, 
  LineChart, 
  BarChart, 
  GridComponent, 
  TooltipComponent, 
  LegendComponent, 
  DataZoomComponent,
  MarkLineComponent,
  MarkAreaComponent
])

const visible = ref(false)
const currentAxisPointer = ref(null)

// 详细数据（包含更多指数）
const detailData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  m1: [5.9, 5.8, 5.5, 5.2, 4.8, 4.5, 4.2, 3.9, 3.5, 3.2, 2.8, 2.5],
  m2: [10.2, 10.1, 9.8, 9.5, 9.3, 9.1, 8.9, 8.7, 8.5, 8.3, 8.1, 8.0],
  // 各指数点位数据
  hs300: [3300, 3350, 3400, 3380, 3450, 3500, 3480, 3550, 3600, 3580, 3650, 3700],
  zz1000: [5500, 5600, 5800, 5750, 5900, 6000, 5950, 6100, 6200, 6150, 6300, 6400],
  cyb: [1850, 1800, 1950, 2000, 2050, 2100, 2150, 2200, 2250, 2200, 2300, 2350],
  windA: [4500, 4550, 4650, 4600, 4700, 4750, 4720, 4800, 4850, 4820, 4900, 4950],
  bond10y: [2.55, 2.48, 2.35, 2.42, 2.38, 2.30, 2.25, 2.20, 2.15, 2.18, 2.12, 2.08]
})

// 计算剪刀差
const scissors = computed(() => {
  return detailData.value.m1.map((val, idx) => parseFloat((val - detailData.value.m2[idx]).toFixed(2)))
})

// 归一化函数（以第一个值为基准100）
const normalize = (data) => {
  const base = data[0]
  return data.map(v => parseFloat(((v / base) * 100).toFixed(2)))
}

// 同步十字准星处理
const handleAxisPointer = (params) => {
  if (params && params.dataIndex !== undefined) {
    currentAxisPointer.value = params.dataIndex
  }
}

// 顶部归一化对比图配置
const standardizedChartOption = computed(() => {
  const { dates, hs300, zz1000, cyb, windA, bond10y } = detailData.value
  
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: { 
        type: 'cross',
        animation: false
      },
      formatter: function(params) {
        let html = `<strong>${params[0].axisValue}</strong><br/>`
        params.forEach(p => {
          html += `${p.marker} ${p.seriesName}: ${p.value}点<br/>`
        })
        return html
      }
    },
    legend: {
      data: ['M1-M2剪刀差', '沪深300', '中证1000', '创业板指', '万得全A', '10Y国债'],
      top: 0
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '15%',
      top: '12%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: dates,
      axisLabel: { fontSize: 11 },
      axisPointer: {
        type: 'line',
        lineStyle: { color: '#999', type: 'dashed' }
      }
    },
    yAxis: [
      {
        type: 'value',
        name: '归一化指数(基准=100)',
        position: 'left',
        axisLabel: { formatter: '{value}' }
      },
      {
        type: 'value',
        name: '剪刀差(%)',
        position: 'right',
        axisLabel: { formatter: '{value}%' }
      }
    ],
    dataZoom: [
      { type: 'inside', start: 0, end: 100 },
      { type: 'slider', start: 0, end: 100, bottom: 10 }
    ],
    series: [
      {
        name: 'M1-M2剪刀差',
        type: 'bar',
        yAxisIndex: 1,
        data: scissors.value,
        barWidth: '30%',
        itemStyle: {
          color: (params) => params.value >= 0 ? 'rgba(82, 196, 26, 0.6)' : 'rgba(245, 34, 45, 0.6)'
        }
      },
      {
        name: '沪深300',
        type: 'line',
        data: normalize(hs300),
        smooth: true,
        lineStyle: { color: '#5470c6', width: 2 },
        itemStyle: { color: '#5470c6' },
        symbol: 'circle',
        symbolSize: 4
      },
      {
        name: '中证1000',
        type: 'line',
        data: normalize(zz1000),
        smooth: true,
        lineStyle: { color: '#91cc75', width: 2 },
        itemStyle: { color: '#91cc75' },
        symbol: 'circle',
        symbolSize: 4
      },
      {
        name: '创业板指',
        type: 'line',
        data: normalize(cyb),
        smooth: true,
        lineStyle: { color: '#fac858', width: 2 },
        itemStyle: { color: '#fac858' },
        symbol: 'circle',
        symbolSize: 4
      },
      {
        name: '万得全A',
        type: 'line',
        data: normalize(windA),
        smooth: true,
        lineStyle: { color: '#ee6666', width: 2 },
        itemStyle: { color: '#ee6666' },
        symbol: 'circle',
        symbolSize: 4
      },
      {
        name: '10Y国债',
        type: 'line',
        data: normalize(bond10y.map(v => v * 1000)), // 放大以便观察
        smooth: true,
        lineStyle: { color: '#73c0de', width: 2, type: 'dashed' },
        itemStyle: { color: '#73c0de' },
        symbol: 'circle',
        symbolSize: 4
      }
    ]
  }
})

// 生成小图配置
const createSmallMultipleOption = (indexName, indexData, isBond = false) => {
  const { dates, m1, m2 } = detailData.value
  const scissorsData = scissors.value
  
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'cross' },
      formatter: function(params) {
        return `<strong>${params[0].axisValue}</strong><br/>` +
               `${params[0].marker} M1-M2: ${scissorsData[params[0].dataIndex]}%<br/>` +
               `${params[1].marker} ${indexName}: ${params[1].value}${isBond ? '%' : ''}`
      }
    },
    grid: {
      left: '10%',
      right: '10%',
      bottom: '15%',
      top: '10%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: dates,
      axisLabel: { fontSize: 9, rotate: 45 },
      axisPointer: {
        type: 'line',
        lineStyle: { color: '#999', type: 'dashed' }
      }
    },
    yAxis: [
      {
        type: 'value',
        name: '剪刀差',
        position: 'left',
        axisLabel: { fontSize: 9, formatter: '{value}%' },
        splitLine: { show: false }
      },
      {
        type: 'value',
        name: isBond ? '收益率' : '点位',
        position: 'right',
        axisLabel: { fontSize: 9, formatter: isBond ? '{value}%' : '{value}' },
        scale: true // Y轴自适应但不包含0
      }
    ],
    series: [
      {
        name: 'M1-M2',
        type: 'bar',
        data: scissorsData,
        barWidth: '40%',
        itemStyle: {
          color: (params) => params.value >= 0 ? 'rgba(82, 196, 26, 0.5)' : 'rgba(245, 34, 45, 0.5)'
        }
      },
      {
        name: indexName,
        type: 'line',
        yAxisIndex: 1,
        data: indexData,
        smooth: true,
        lineStyle: { width: 2 },
        symbol: 'none'
      }
    ]
  }
}

const smallMultipleOptions = computed(() => {
  const { zz1000, cyb, windA, bond10y } = detailData.value
  return {
    zz1000: createSmallMultipleOption('中证1000', zz1000),
    cyb: createSmallMultipleOption('创业板指', cyb),
    windA: createSmallMultipleOption('万得全A', windA),
    bond10y: createSmallMultipleOption('10Y国债', bond10y, true)
  }
})

// 底部比值图配置
const ratioChartOption = computed(() => {
  const { dates, zz1000, hs300 } = detailData.value
  const ratio = zz1000.map((v, i) => parseFloat((v / hs300[i]).toFixed(3)))
  const ratioMA5 = ratio.map((_, i) => {
    if (i < 4) return null
    const avg = ratio.slice(i-4, i+1).reduce((a, b) => a + b, 0) / 5
    return parseFloat(avg.toFixed(3))
  })
  
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'cross' },
      formatter: function(params) {
        let html = `<strong>${params[0].axisValue}</strong><br/>`
        params.forEach(p => {
          if (p.value !== null && p.value !== undefined) {
            html += `${p.marker} ${p.seriesName}: ${p.value}<br/>`
          }
        })
        html += `<br/><span style="color:#999">解读: ${ratio[params[0].dataIndex] > 1.6 ? '小盘成长占优' : '大盘价值占优'}</span>`
        return html
      }
    },
    legend: {
      data: ['中证1000/沪深300', '5期移动平均'],
      top: 0
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '10%',
      top: '15%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: dates,
      axisLabel: { fontSize: 11 },
      axisPointer: {
        type: 'line',
        lineStyle: { color: '#999', type: 'dashed' }
      }
    },
    yAxis: {
      type: 'value',
      name: '比值',
      axisLabel: { formatter: '{value}' },
      splitLine: { lineStyle: { type: 'dashed' } }
    },
    series: [
      {
        name: '中证1000/沪深300',
        type: 'line',
        data: ratio,
        smooth: true,
        lineStyle: { color: '#5470c6', width: 3 },
        itemStyle: { color: '#5470c6' },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [
              { offset: 0, color: 'rgba(84, 112, 198, 0.3)' },
              { offset: 1, color: 'rgba(84, 112, 198, 0.05)' }
            ]
          }
        },
        markLine: {
          silent: true,
          data: [
            { 
              yAxis: 1.6, 
              lineStyle: { color: '#52c41a', type: 'dashed' }, 
              label: { formatter: '小盘占优线 1.6' } 
            },
            { 
              yAxis: 1.4, 
              lineStyle: { color: '#f5222d', type: 'dashed' }, 
              label: { formatter: '大盘占优线 1.4' } 
            }
          ]
        }
      },
      {
        name: '5期移动平均',
        type: 'line',
        data: ratioMA5,
        smooth: true,
        lineStyle: { color: '#fa8c16', width: 2, type: 'dashed' },
        itemStyle: { color: '#fa8c16' },
        symbol: 'none'
      }
    ]
  }
})

const open = () => {
  visible.value = true
}

defineExpose({
  open
})
</script>

<style scoped>
.source-detail-dialog :deep(.el-dialog__header) {
  background: linear-gradient(135deg, #f5576c 0%, #f093fb 100%);
  margin-right: 0;
  padding: 20px;
}

.source-detail-dialog :deep(.el-dialog__title) {
  color: white;
  font-weight: 600;
  font-size: 18px;
}

.source-detail-dialog :deep(.el-dialog__headerbtn .el-dialog__close) {
  color: white;
}

.source-detail-dialog :deep(.el-dialog__body) {
  padding: 20px;
  max-height: 75vh;
  overflow-y: auto;
}

.detail-content {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.section {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 16px;
}

.section-header {
  margin-bottom: 12px;
}

.section-header h3 {
  margin: 0 0 4px 0;
  font-size: 16px;
  color: #303133;
}

.section-desc {
  font-size: 12px;
  color: #909399;
}

.chart-container {
  background: white;
  border-radius: 4px;
  padding: 8px;
}

.chart-container.large {
  height: 350px;
}

.chart-container.medium {
  height: 280px;
}

.chart {
  width: 100%;
  height: 100%;
}

.small-multiples-section {
  background: transparent;
  padding: 0;
}

.small-chart-container {
  background: white;
  border-radius: 8px;
  padding: 12px;
  height: 280px;
}

.chart-title {
  font-size: 13px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
  text-align: center;
}

.dialog-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.footer-hints {
  display: flex;
  gap: 8px;
}
</style>