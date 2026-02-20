<template>
  <el-dialog
    v-model="visible"
    title="结果模块详情 —— CPI/PPI深度分析"
    width="95%"
    :close-on-click-modal="true"
    class="outcome-detail-dialog"
    destroy-on-close
  >
    <div class="detail-content">
      <!-- 第一部分：核心与分项结构 -->
      <div class="section">
        <div class="section-header">
          <h3>1. 核心与分项结构 (Structure Analysis)</h3>
          <span class="section-desc">剔除单一因素干扰，观察经济"真实体温"</span>
        </div>
        
        <el-row :gutter="16">
          <!-- 核心CPI vs 核心PPI -->
          <el-col :span="12">
            <div class="sub-section">
              <div class="chart-title">核心通胀对比 (剔除食品和能源)</div>
              <div class="chart-container">
                <v-chart class="chart" :option="coreInflationOption" autoresize />
              </div>
              <div class="insight-box">
                <strong>逻辑解读：</strong>
                <p v-if="coreInsight">{{ coreInsight }}</p>
              </div>
            </div>
          </el-col>
          
          <!-- PMI五大子项雷达图 -->
          <el-col :span="12">
            <div class="sub-section">
              <div class="chart-title">PMI五大子项雷达图</div>
              <div class="chart-container">
                <v-chart class="chart" :option="pmiRadarOption" autoresize />
              </div>
              <div class="insight-box">
                <strong>驱动分析：</strong>
                <p>{{ pmiDriverInsight }}</p>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 第二部分：动态逻辑指标 -->
      <div class="section">
        <div class="section-header">
          <h3>2. 动态逻辑指标 (Derived Metrics)</h3>
          <span class="section-desc">直接指导交易逻辑的关键计算指标</span>
        </div>
        
        <el-row :gutter="16">
          <!-- 工业企业利润剪刀差 -->
          <el-col :span="12">
            <div class="sub-section">
              <div class="chart-title">工业企业利润剪刀差 (PPI - CPI)</div>
              <div class="chart-container">
                <v-chart class="chart" :option="profitScissorsOption" autoresize />
              </div>
              <div class="logic-tags">
                <el-tag v-if="profitFlow === 'upstream'" type="danger" effect="dark">
                  利润流向上游：采矿、原材料
                </el-tag>
                <el-tag v-else type="success" effect="dark">
                  利润流向下游：食品饮料、家电
                </el-tag>
              </div>
            </div>
          </el-col>
          
          <!-- PMI动能指标 -->
          <el-col :span="12">
            <div class="sub-section">
              <div class="chart-title">PMI动能指标 (新订单 - 产成品库存)</div>
              <div class="chart-container">
                <v-chart class="chart" :option="pmiMomentumOption" autoresize />
              </div>
              <div class="logic-tags">
                <el-tag v-if="momentumSignal === 'expansion'" type="success" effect="dark">
                  经济爆发信号：订单涨 + 库存低 → 必须补库
                </el-tag>
                <el-tag v-else-if="momentumSignal === 'warning'" type="warning" effect="dark">
                  警惕信号：订单弱 + 库存高
                </el-tag>
                <el-tag v-else type="info">
                  常态区间：动能平稳
                </el-tag>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 第三部分：历史相位与极值 -->
      <div class="section">
        <div class="section-header">
          <h3>3. 历史相位与极值 (Regime & Range)</h3>
          <span class="section-desc">在历史长河中定位当前数据，关注"赔率"</span>
        </div>
        
        <el-row :gutter="16">
          <!-- 百分位分布 -->
          <el-col :span="8">
            <div class="sub-section">
              <div class="chart-title">当前数值历史百分位 (近10年)</div>
              <div class="percentile-box">
                <div v-for="item in percentileData" :key="item.name" class="percentile-item">
                  <div class="percentile-label">{{ item.name }}</div>
                  <div class="percentile-bar-container">
                    <div class="percentile-bar-bg">
                      <div 
                        class="percentile-bar" 
                        :style="{ width: item.percentile + '%', background: item.color }"
                      ></div>
                    </div>
                    <div class="percentile-value">
                      <span class="current">{{ item.current }}</span>
                      <span class="percentile-tag">{{ item.percentile }}%</span>
                    </div>
                  </div>
                  
                  <div class="percentile-desc">{{ item.desc }}</div>
                </div>
              </div>
            </div>
          </el-col>
          
          <!-- 历史相位复盘图 -->
          <el-col :span="16">
            <div class="sub-section">
              <div class="chart-title">历史相位复盘图 —— 经济周期定位</div>
              <div class="chart-container large">
                <v-chart class="chart" :option="regimeChartOption" autoresize />
              </div>
              <div class="regime-legend">
                <div class="legend-item">
                  <span class="legend-color" style="background: #ff4d4f;"></span>
                  <span>过热 (CPI↑ PPI↑)</span>
                </div>
                <div class="legend-item">
                  <span class="legend-color" style="background: #52c41a;"></span>
                  <span>复苏 (CPI↓ PPI↑)</span>
                </div>
                <div class="legend-item">
                  <span class="legend-color" style="background: #faad14;"></span>
                  <span>滞胀 (CPI↑ PPI↓)</span>
                </div>
                <div class="legend-item">
                  <span class="legend-color" style="background: #1890ff;"></span>
                  <span>衰退 (CPI↓ PPI↓)</span>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <div class="footer-hints">
          <el-tag size="small" type="info">💡 核心CPI/PPI剔除食品和能源，反映真实通胀水平</el-tag>
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
import { LineChart, BarChart, RadarChart } from 'echarts/charts'
import { 
  GridComponent, 
  TooltipComponent, 
  LegendComponent, 
  RadarComponent,
  MarkLineComponent,
  MarkAreaComponent
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
  RadarComponent,
  MarkLineComponent,
  MarkAreaComponent
])

const visible = ref(false)

// 详细数据
const detailData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  // 整体CPI/PPI
  cpi: [-0.8, 0.7, 0.1, 0.3, 0.4, 0.2, 0.5, 0.6, 0.4, 0.3, 0.2, 0.3],
  ppi: [-2.5, -2.7, -2.8, -2.5, -2.3, -2.1, -1.8, -1.5, -1.2, -1.0, -0.8, -0.5],
  // 核心CPI/PPI (剔除食品和能源)
  coreCpi: [0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 1.1, 1.0, 0.9, 0.8, 0.7],
  corePpi: [-1.5, -1.6, -1.7, -1.5, -1.3, -1.1, -0.9, -0.7, -0.5, -0.3, -0.2, 0.0],
  // PMI及子项 (最新月数据)
  pmi: 49.0,
  pmiComponents: {
    newOrders: 48.5,      // 新订单
    production: 50.2,     // 生产
    employment: 48.0,     // 从业人员
    delivery: 50.5,       // 供应商配送时间
    inventory: 47.8       // 原材料库存
  },
  // 产成品库存 (用于动能计算)
  finishedInventory: 48.2
})

// 核心通胀洞察
const coreInsight = computed(() => {
  const lastCoreCpi = detailData.value.coreCpi[detailData.value.coreCpi.length - 1]
  const lastCpi = detailData.value.cpi[detailData.value.cpi.length - 1]
  
  if (lastCoreCpi > 0.5 && lastCpi < 0.5) {
    return '整体CPI低但核心CPI稳步回升，说明大众消费需求其实在好转，只是被暂时的低油价或猪价掩盖了。'
  } else if (lastCoreCpi < 0.3) {
    return '核心CPI持续低迷，反映真实消费需求疲软，需警惕通缩风险。'
  }
  return '核心通胀处于合理区间，消费需求平稳。'
})

// PMI驱动分析
const pmiDriverInsight = computed(() => {
  const { newOrders, production } = detailData.value.pmiComponents
  if (newOrders > production) {
    return '需求驱动型：新订单强于生产，说明需求端复苏领先供给端。'
  } else if (production > newOrders + 2) {
    return '供给驱动型：生产强于新订单，需警惕库存积压风险。'
  }
  return '供需相对平衡：生产与新订单匹配度较高。'
})

// 利润流向判断
const profitFlow = computed(() => {
  const lastPpi = detailData.value.ppi[detailData.value.ppi.length - 1]
  const lastCpi = detailData.value.cpi[detailData.value.cpi.length - 1]
  return (lastPpi - lastCpi) > -1 ? 'upstream' : 'downstream'
})

// 动能信号
const momentumSignal = computed(() => {
  const { newOrders } = detailData.value.pmiComponents
  const { finishedInventory } = detailData.value
  const momentum = newOrders - finishedInventory
  
  if (momentum > 2) return 'expansion'
  if (momentum < -2) return 'warning'
  return 'normal'
})

// 百分位数据
const percentileData = computed(() => {
  const lastCpi = detailData.value.cpi[detailData.value.cpi.length - 1]
  const lastPpi = detailData.value.ppi[detailData.value.ppi.length - 1]
  const lastCoreCpi = detailData.value.coreCpi[detailData.value.coreCpi.length - 1]
  
  return [
    {
      name: 'CPI同比',
      current: lastCpi + '%',
      percentile: 15,
      color: '#52c41a',
      desc: '处于历史15%分位，下行空间有限'
    },
    {
      name: 'PPI同比',
      current: lastPpi + '%',
      percentile: 8,
      color: '#1890ff',
      desc: '处于历史8%分位，接近底部区域'
    },
    {
      name: '核心CPI',
      current: lastCoreCpi + '%',
      percentile: 35,
      color: '#faad14',
      desc: '处于历史35%分位，中等偏低水平'
    }
  ]
})

// 核心通胀对比图配置
const coreInflationOption = computed(() => {
  const { dates, coreCpi, corePpi } = detailData.value
  
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'cross' }
    },
    legend: {
      data: ['核心CPI', '核心PPI'],
      bottom: 0
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '15%',
      top: '10%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: dates,
      axisLabel: { fontSize: 10 }
    },
    yAxis: {
      type: 'value',
      name: '同比(%)',
      axisLabel: { formatter: '{value}%' }
    },
    series: [
      {
        name: '核心CPI',
        type: 'line',
        data: coreCpi,
        smooth: true,
        lineStyle: { color: '#f5576c', width: 3 },
        itemStyle: { color: '#f5576c' },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [
              { offset: 0, color: 'rgba(245, 87, 108, 0.3)' },
              { offset: 1, color: 'rgba(245, 87, 108, 0.05)' }
            ]
          }
        }
      },
      {
        name: '核心PPI',
        type: 'line',
        data: corePpi,
        smooth: true,
        lineStyle: { color: '#4facfe', width: 3 },
        itemStyle: { color: '#4facfe' },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [
              { offset: 0, color: 'rgba(79, 172, 254, 0.3)' },
              { offset: 1, color: 'rgba(79, 172, 254, 0.05)' }
            ]
          }
        }
      }
    ]
  }
})

// PMI雷达图配置
const pmiRadarOption = computed(() => {
  const { newOrders, production, employment, delivery, inventory } = detailData.value.pmiComponents
  
  return {
    tooltip: {},
    radar: {
      indicator: [
        { name: '新订单', max: 60 },
        { name: '生产', max: 60 },
        { name: '从业人员', max: 60 },
        { name: '配送时间', max: 60 },
        { name: '原材料库存', max: 60 }
      ],
      center: ['50%', '55%'],
      radius: '65%'
    },
    series: [
      {
        name: 'PMI分项',
        type: 'radar',
        data: [
          {
            value: [newOrders, production, employment, delivery, inventory],
            name: '当前值',
            areaStyle: {
              color: 'rgba(245, 87, 108, 0.3)'
            },
            lineStyle: {
              color: '#f5576c',
              width: 2
            },
            itemStyle: {
              color: '#f5576c'
            }
          },
          {
            value: [50, 50, 50, 50, 50],
            name: '荣枯线',
            lineStyle: {
              color: '#999',
              type: 'dashed',
              width: 1
            },
            symbol: 'none'
          }
        ]
      }
    ]
  }
})

// 利润剪刀差图配置
const profitScissorsOption = computed(() => {
  const { dates, cpi, ppi } = detailData.value
  const scissors = ppi.map((p, i) => parseFloat((p - cpi[i]).toFixed(2)))
  
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'cross' },
      formatter: function(params) {
        const val = params[0].value
        const flow = val > -1 ? '上游占优' : '下游占优'
        return `${params[0].axisValue}<br/>` +
               `${params[0].marker} 剪刀差: ${val}%<br/>` +
               `<span style="color:${val > -1 ? '#f5222d' : '#52c41a'}">${flow}</span>`
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '10%',
      top: '10%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: dates,
      axisLabel: { fontSize: 10 }
    },
    yAxis: {
      type: 'value',
      name: '剪刀差(%)',
      axisLabel: { formatter: '{value}%' }
    },
    series: [
      {
        name: 'PPI-CPI剪刀差',
        type: 'bar',
        data: scissors,
        barWidth: '50%',
        itemStyle: {
          color: function(params) {
            return params.value > -1 
              ? {
                  type: 'linear',
                  x: 0, y: 0, x2: 0, y2: 1,
                  colorStops: [
                    { offset: 0, color: '#ff7875' },
                    { offset: 1, color: '#ff4d4f' }
                  ]
                }
              : {
                  type: 'linear',
                  x: 0, y: 0, x2: 0, y2: 1,
                  colorStops: [
                    { offset: 0, color: '#95de64' },
                    { offset: 1, color: '#52c41a' }
                  ]
                }
          }
        },
        markLine: {
          silent: true,
          data: [
            {
              yAxis: -1,
              lineStyle: { color: '#999', type: 'dashed' },
              label: { formatter: '分界点', fontSize: 10 }
            }
          ]
        }
      }
    ]
  }
})

// PMI动能指标图配置
const pmiMomentumOption = computed(() => {
  const { dates } = detailData.value
  // 模拟历史动能数据
  const momentumData = [-2.5, -1.8, -0.5, 0.2, 1.5, 2.8, 3.2, 2.5, 1.8, 0.5, -0.8, -1.2]
  
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'cross' },
      formatter: function(params) {
        const val = params[0].value
        let signal = ''
        if (val > 2) signal = '经济爆发信号'
        else if (val < -2) signal = '警惕信号'
        else signal = '常态区间'
        return `${params[0].axisValue}<br/>` +
               `${params[0].marker} 动能: ${val}<br/>` +
               `<span style="color:${val > 2 ? '#52c41a' : val < -2 ? '#f5222d' : '#999'}">${signal}</span>`
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '10%',
      top: '10%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: dates,
      axisLabel: { fontSize: 10 }
    },
    yAxis: {
      type: 'value',
      name: '动能值',
      axisLabel: { formatter: '{value}' }
    },
    series: [
      {
        name: 'PMI动能',
        type: 'line',
        data: momentumData,
        smooth: true,
        lineStyle: { color: '#722ed1', width: 3 },
        itemStyle: { color: '#722ed1' },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [
              { offset: 0, color: 'rgba(114, 46, 209, 0.4)' },
              { offset: 0.5, color: 'rgba(114, 46, 209, 0.1)' },
              { offset: 1, color: 'rgba(114, 46, 209, 0.0)' }
            ]
          }
        },
        markLine: {
          silent: true,
          data: [
            {
              yAxis: 2,
              lineStyle: { color: '#52c41a', type: 'dashed' },
              label: { formatter: '爆发线', fontSize: 10 }
            },
            {
              yAxis: -2,
              lineStyle: { color: '#f5222d', type: 'dashed' },
              label: { formatter: '警戒线', fontSize: 10 }
            },
            {
              yAxis: 0,
              lineStyle: { color: '#999', type: 'solid', width: 1 }
            }
          ]
        }
      }
    ]
  }
})

// 历史相位图配置
const regimeChartOption = computed(() => {
  const { dates, cpi, ppi } = detailData.value
  
  // 定义相位区间 (模拟历史数据)
  const markAreas = [
    // 衰退期 (CPI↓ PPI↓)
    {
      name: '衰退',
      itemStyle: { color: 'rgba(24, 144, 255, 0.15)' },
      data: [[{ xAxis: '2024-01' }, { xAxis: '2024-03' }]]
    },
    // 复苏期 (CPI↓ PPI↑)
    {
      name: '复苏',
      itemStyle: { color: 'rgba(82, 196, 26, 0.15)' },
      data: [[{ xAxis: '2024-04' }, { xAxis: '2024-06' }]]
    },
    // 滞胀期 (CPI↑ PPI↓)
    {
      name: '滞胀',
      itemStyle: { color: 'rgba(250, 173, 20, 0.15)' },
      data: [[{ xAxis: '2024-07' }, { xAxis: '2024-09' }]]
    }
  ]
  
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'cross' }
    },
    legend: {
      data: ['CPI同比', 'PPI同比'],
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
      axisLabel: { fontSize: 11 }
    },
    yAxis: {
      type: 'value',
      name: '同比(%)',
      axisLabel: { formatter: '{value}%' }
    },
    series: [
      {
        name: 'CPI同比',
        type: 'line',
        data: cpi,
        smooth: true,
        lineStyle: { color: '#f5576c', width: 3 },
        itemStyle: { color: '#f5576c' }
      },
      {
        name: 'PPI同比',
        type: 'line',
        data: ppi,
        smooth: true,
        lineStyle: { color: '#4facfe', width: 3 },
        itemStyle: { color: '#4facfe' }
      },
      {
        name: '相位背景',
        type: 'line',
        markArea: {
          silent: true,
          data: markAreas.map(r => r.data[0])
        }
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
.outcome-detail-dialog :deep(.el-dialog__header) {
  background: linear-gradient(135deg, #f5576c 0%, #4facfe 100%);
  margin-right: 0;
  padding: 20px;
}

.outcome-detail-dialog :deep(.el-dialog__title) {
  color: white;
  font-weight: 600;
  font-size: 18px;
}

.outcome-detail-dialog :deep(.el-dialog__headerbtn .el-dialog__close) {
  color: white;
}

.outcome-detail-dialog :deep(.el-dialog__body) {
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
  margin-bottom: 16px;
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

.sub-section {
  background: white;
  border-radius: 8px;
  padding: 12px;
  height: 100%;
}

.chart-title {
  font-size: 13px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
  text-align: center;
}

.chart-container {
  height: 220px;
}

.chart-container.large {
  height: 300px;
}

.chart {
  width: 100%;
  height: 100%;
}

.insight-box {
  margin-top: 12px;
  padding: 10px;
  background: #f6ffed;
  border-left: 3px solid #52c41a;
  border-radius: 4px;
  font-size: 12px;
}

.insight-box strong {
  color: #52c41a;
}

.insight-box p {
  margin: 4px 0 0 0;
  color: #606266;
  line-height: 1.5;
}

.logic-tags {
  margin-top: 12px;
  text-align: center;
}

.percentile-box {
  padding: 16px;
}

.percentile-item {
  margin-bottom: 20px;
}

.percentile-item:last-child {
  margin-bottom: 0;
}

.percentile-label {
  font-size: 13px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}

.percentile-bar-container {
  display: flex;
  align-items: center;
  gap: 12px;
}

.percentile-bar-bg {
  flex: 1;
  height: 20px;
  background: #f0f0f0;
  border-radius: 10px;
  overflow: hidden;
}

.percentile-bar {
  height: 100%;
  border-radius: 10px;
  transition: width 0.5s ease;
}

.percentile-value {
  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 100px;
}

.percentile-value .current {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.percentile-tag {
  font-size: 12px;
  padding: 2px 8px;
  background: #f0f0f0;
  border-radius: 10px;
  color: #606266;
}

.percentile-desc {
  margin-top: 4px;
  font-size: 11px;
  color: #909399;
}

.regime-legend {
  display: flex;
  justify-content: center;
  gap: 24px;
  margin-top: 12px;
  flex-wrap: wrap;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: #606266;
}

.legend-color {
  width: 16px;
  height: 16px;
  border-radius: 4px;
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