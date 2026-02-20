<template>
  <el-dialog
    v-model="visible"
    title="传导模块详情 —— 社融增量深度分析"
    width="95%"
    :close-on-click-modal="true"
    class="transmission-detail-dialog"
    destroy-on-close
  >
    <div class="detail-content">
      <!-- 第一部分：结构质量分析 -->
      <div class="section">
        <div class="section-header">
          <h3>1. 结构质量分析 (Quality Analysis)</h3>
          <span class="section-desc">资金的"含金量"决定经济复苏的持续性</span>
        </div>
        
        <el-row :gutter="16">
          <!-- 信贷结构占比趋势 -->
          <el-col :span="12">
            <div class="sub-section">
              <div class="chart-title">中长期贷款占比趋势 —— "实火"还是"虚火"</div>
              <div class="chart-container">
                <v-chart class="chart" :option="loanQualityOption" autoresize />
              </div>
              <div class="quality-insight">
                <el-tag :type="qualityTag.type" effect="dark" size="large">
                  {{ qualityTag.text }}
                </el-tag>
              </div>
            </div>
          </el-col>
          
          <!-- 政府债 vs 民间社融 -->
          <el-col :span="12">
            <div class="sub-section">
              <div class="chart-title">政府债脉冲 vs 民间社融 —— 谁在主导复苏</div>
              <div class="chart-container">
                <v-chart class="chart" :option="govVsPrivateOption" autoresize />
              </div>
              <div class="driver-insight">
                <el-tag :type="driverTag.type" effect="dark" size="large">
                  {{ driverTag.text }}
                </el-tag>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 第二部分：领先与验证指标 -->
      <div class="section">
        <div class="section-header">
          <h3>2. 领先与验证指标 (Leading & Validation)</h3>
          <span class="section-desc">深层关联验证"信用扩张"是否转化为"盈利修复"</span>
        </div>
        
        <el-row :gutter="16">
          <!-- M2-社融剪刀差 -->
          <el-col :span="12">
            <div class="sub-section">
              <div class="chart-title">M2 - 社融剪刀差 —— 钱是否流向实体经济</div>
              <div class="chart-container">
                <v-chart class="chart" :option="m2SocialScissorsOption" autoresize />
              </div>
              <div class="scissors-insight">
                <p v-if="scissorsInsight" class="insight-text">{{ scissorsInsight }}</p>
              </div>
            </div>
          </el-col>
          
          <!-- 社融 vs 盈利验证 -->
          <el-col :span="12">
            <div class="sub-section">
              <div class="chart-title">社融增速 vs 全A利润 (6个月领先)</div>
              <div class="chart-container">
                <v-chart class="chart" :option="socialVsProfitOption" autoresize />
              </div>
              <div class="validation-insight">
                <p class="insight-text">{{ validationInsight }}</p>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 第三部分：季节性与缺口分析 -->
      <div class="section">
        <div class="section-header">
          <h3>3. 季节性与缺口分析 (Seasonal & Gap)</h3>
          <span class="section-desc">剔除季节效应，识别真实的"超常"或"不及"</span>
        </div>
        
        <el-row :gutter="16">
          <!-- 季节性图表 -->
          <el-col :span="12">
            <div class="sub-section">
              <div class="chart-title">月度增量季节性对比 (今年 vs 近5年均值)</div>
              <div class="chart-container">
                <v-chart class="chart" :option="seasonalOption" autoresize />
              </div>
            </div>
          </el-col>
          
          <!-- 社融缺口监测 -->
          <el-col :span="12">
            <div class="sub-section">
              <div class="chart-title">社融缺口监测 (实际值 vs 一致预期)</div>
              <div class="gap-monitor">
                <div v-for="item in gapData" :key="item.month" class="gap-item">
                  <div class="gap-month">{{ item.month }}</div>
                  <div class="gap-bars">
                    <div class="gap-bar-group">
                      <div class="gap-label">预期</div>
                      <div class="gap-bar-bg">
                        <div class="gap-bar expected" :style="{ width: (item.expected / maxGapValue * 100) + '%' }">
                          <span class="bar-value">{{ item.expected }}</span>
                        </div>
                      </div>
                    </div>
                    <div class="gap-bar-group">
                      <div class="gap-label">实际</div>
                      <div class="gap-bar-bg">
                        <div 
                          class="gap-bar actual" 
                          :class="{ 'above': item.actual > item.expected, 'below': item.actual < item.expected }"
                          :style="{ width: (Math.abs(item.actual) / maxGapValue * 100) + '%' }"
                        >
                          <span class="bar-value">{{ item.actual }}</span>
                        </div>
                      </div>
                    </div>
                  </div>
                  
                  <div class="gap-deviation" :class="{ 'positive': item.deviation > 0, 'negative': item.deviation < 0 }">
                    {{ item.deviation > 0 ? '+' : '' }}{{ item.deviation }}%
                  </div>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 底部诊断文案 -->
      <div class="diagnosis-section">
        <div class="diagnosis-header">
          <el-icon :size="24" color="#f5222d"><Warning /></el-icon>
          <span>智能诊断</span>
        </div>
        <div class="diagnosis-content">
          <p>{{ diagnosisText }}</p>
        </div>
      </div>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <div class="footer-hints">
          <el-tag size="small" type="info">💡 中长期贷款占比 > 55% 表明实体投融资意愿强劲</el-tag>
        </div>
        <el-button type="primary" @click="visible = false">关闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, defineExpose } from 'vue'
import { Warning } from '@element-plus/icons-vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart, BarChart } from 'echarts/charts'
import { 
  GridComponent, 
  TooltipComponent, 
  LegendComponent,
  MarkLineComponent
} from 'echarts/components'
import VChart from 'vue-echarts'

use([
  CanvasRenderer, 
  LineChart, 
  BarChart, 
  GridComponent, 
  TooltipComponent, 
  LegendComponent,
  MarkLineComponent
])

const visible = ref(false)

// 详细数据
const detailData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06'],
  // 社融构成
  governmentBond: [8000, 7500, 9000, 8500, 9200, 8800],
  corporateBond: [5000, 4800, 5200, 5100, 5300, 5000],
  entrustLoan: [2000, 1800, 2200, 1900, 2100, 2000],
  shortTermLoan: [15000, 12000, 18000, 16000, 20000, 17000],
  longTermLoan: [30000, 26000, 34000, 32000, 35000, 33000],
  // M2数据
  m2: [10.2, 10.1, 9.8, 9.5, 9.3, 9.1],
  // 社融增速
  socialGrowth: [9.5, 9.3, 9.8, 9.6, 9.9, 9.7],
  // 全A利润增速 (6个月滞后)
  profitGrowth: [5.2, 4.8, 5.5, 6.2, 6.8, 7.2],
  // 季节性数据 (近5年均值)
  seasonalAvg: [38000, 32000, 42000, 40000, 45000, 41000]
})

// 计算总量
const totalAmount = computed(() => {
  const { governmentBond, corporateBond, entrustLoan, shortTermLoan, longTermLoan } = detailData.value
  return governmentBond.map((_, i) => 
    governmentBond[i] + corporateBond[i] + entrustLoan[i] + shortTermLoan[i] + longTermLoan[i]
  )
})

// 中长期贷款占比
const longTermRatio = computed(() => {
  const { longTermLoan } = detailData.value
  return longTermLoan.map((loan, i) => parseFloat((loan / totalAmount.value[i] * 100).toFixed(1)))
})

// 质量标签
const qualityTag = computed(() => {
  const avgRatio = longTermRatio.value.reduce((a, b) => a + b, 0) / longTermRatio.value.length
  if (avgRatio > 55) {
    return { type: 'success', text: `实体投融资意愿强劲，复苏含金量高 (${avgRatio.toFixed(1)}%)` }
  } else if (avgRatio > 45) {
    return { type: 'warning', text: `结构尚可，但需关注持续性 (${avgRatio.toFixed(1)}%)` }
  }
  return { type: 'danger', text: `短期资金占比过高，虚火成分大 (${avgRatio.toFixed(1)}%)` }
})

// 政府债占比
const govBondRatio = computed(() => {
  const { governmentBond } = detailData.value
  return governmentBond.map((gov, i) => parseFloat((gov / totalAmount.value[i] * 100).toFixed(1)))
})

// 驱动标签
const driverTag = computed(() => {
  const avgGovRatio = govBondRatio.value.reduce((a, b) => a + b, 0) / govBondRatio.value.length
  if (avgGovRatio > 40) {
    return { type: 'warning', text: `主要依靠政策托底，民间动力尚待激活 (${avgGovRatio.toFixed(1)}%)` }
  } else if (avgGovRatio < 25) {
    return { type: 'success', text: `民间融资活跃，内生动力强劲 (${avgGovRatio.toFixed(1)}%)` }
  }
  return { type: 'info', text: `政府与民间协同发力，结构相对均衡 (${avgGovRatio.toFixed(1)}%)` }
})

// M2-社融剪刀差洞察
const scissorsInsight = computed(() => {
  const { m2, socialGrowth } = detailData.value
  const latestScissors = m2[m2.length - 1] - socialGrowth[socialGrowth.length - 1]
  
  if (latestScissors > 1.5) {
    return '差值为正且扩大：钱放出来了但没借出去，系统内流动性过剩（资产荒），利好债市。'
  } else if (latestScissors < 0.5) {
    return '差值收窄：钱开始从金融体系流向实体经济，信用扩张见效，利好股市。'
  }
  return '差值平稳：资金流向处于相对均衡状态。'
})

// 验证洞察
const validationInsight = computed(() => {
  const { socialGrowth, profitGrowth } = detailData.value
  // 社融领先6个月，对比6个月前的社融与当前利润
  const social6mAgo = socialGrowth[0]
  const currentProfit = profitGrowth[profitGrowth.length - 1]
  
  if (currentProfit > social6mAgo - 2) {
    return '验证成功：6个月前的信用扩张已转化为当前的盈利修复，传导链条通畅。'
  }
  return '验证滞后：社融增长尚未完全传导至盈利端，需关注企业ROE改善进度。'
})

// 缺口数据
const gapData = computed(() => {
  const { dates, socialGrowth } = detailData.value
  // 模拟预期数据
  const expected = [9.8, 9.5, 10.2, 9.8, 10.5, 10.0]
  
  return dates.map((month, i) => ({
    month,
    expected: expected[i],
    actual: socialGrowth[i],
    deviation: parseFloat(((socialGrowth[i] - expected[i]) / expected[i] * 100).toFixed(1))
  }))
})

const maxGapValue = computed(() => {
  return Math.max(...gapData.value.map(d => Math.max(d.expected, Math.abs(d.actual))))
})

// 智能诊断
const diagnosisText = computed(() => {
  const avgLongTermRatio = longTermRatio.value.reduce((a, b) => a + b, 0) / longTermRatio.value.length
  const avgGovRatio = govBondRatio.value.reduce((a, b) => a + b, 0) / govBondRatio.value.length
  
  if (avgLongTermRatio > 55 && avgGovRatio < 30) {
    return '【强劲复苏信号】中长期贷款占比高且民间融资活跃，表明实体经济内生动力充足，复苏含金量高，建议增配权益资产。'
  } else if (avgLongTermRatio > 55 && avgGovRatio > 40) {
    return '【政策驱动复苏】中长期贷款占比虽高，但政府债占主导，表明复苏主要由政策托底，需观察民间跟进情况，建议均衡配置。'
  } else if (avgLongTermRatio < 45 && avgGovRatio > 40) {
    return '【托底模式】短期资金占比高且政府主导，实体投融资意愿不足，复苏基础不牢，建议防御配置，关注政策加码信号。'
  } else if (avgLongTermRatio < 45) {
    return '【虚火警告】短期套利资金占比过高，资金空转风险上升，需警惕信用扩张的持续性，建议降低风险敞口。'
  }
  return '【结构观察期】各项指标处于中性区间，建议持续跟踪信贷结构改善情况，等待更明确信号。'
})

// 中长期贷款占比趋势图
const loanQualityOption = computed(() => {
  const { dates } = detailData.value
  
  return {
    tooltip: {
      trigger: 'axis',
      formatter: function(params) {
        return `${params[0].axisValue}<br/>` +
               `${params[0].marker} 中长期贷款占比: ${params[0].value}%`
      }
    },
    grid: {
      left: 60,
      right: 40,
      bottom: 60,
      top: 50,
      containLabel: false
    },
    xAxis: {
      type: 'category',
      data: dates,
      axisLabel: { 
        fontSize: 10,
        interval: 0,
        rotate: 0
      }
    },
    yAxis: {
      type: 'value',
      name: '占比(%)',
      min: 30,
      max: 70,
      axisLabel: { 
        formatter: '{value}%',
        fontSize: 11,
        margin: 8
      },
      nameTextStyle: {
        align: 'left',
        padding: [0, 0, 0, -30]
      }
    },
    series: [
      {
        name: '中长期贷款占比',
        type: 'line',
        data: longTermRatio.value,
        smooth: true,
        lineStyle: { color: '#52c41a', width: 3 },
        itemStyle: { color: '#52c41a' },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [
              { offset: 0, color: 'rgba(82, 196, 26, 0.4)' },
              { offset: 1, color: 'rgba(82, 196, 26, 0.05)' }
            ]
          }
        },
        markLine: {
          silent: true,
          data: [
            {
              yAxis: 55,
              lineStyle: { color: '#52c41a', type: 'dashed' },
              label: { formatter: '健康线 55%', fontSize: 10 }
            },
            {
              yAxis: 45,
              lineStyle: { color: '#f5222d', type: 'dashed' },
              label: { formatter: '警戒线 45%', fontSize: 10 }
            }
          ]
        }
      }
    ]
  }
})

// 政府债 vs 民间社融图
const govVsPrivateOption = computed(() => {
  const { dates, governmentBond } = detailData.value
  const privateAmount = totalAmount.value.map((total, i) => total - governmentBond[i])
  
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' }
    },
    legend: {
      data: ['政府债', '民间社融'],
      bottom: 0
    },
    grid: {
      left: 60,
      right: 40,
      bottom: 60,
      top: 50,
      containLabel: false
    },
    xAxis: {
      type: 'category',
      data: dates,
      axisLabel: { 
        fontSize: 10,
        interval: 0,
        rotate: 0
      }
    },
    yAxis: {
      type: 'value',
      name: '金额(亿元)',
      axisLabel: {
        formatter: (v) => v / 1000 + 'k',
        fontSize: 11,
        margin: 8
      },
      nameTextStyle: {
        align: 'left',
        padding: [0, 0, 0, -30]
      }
    },
    series: [
      {
        name: '政府债',
        type: 'bar',
        stack: 'total',
        data: governmentBond,
        itemStyle: { color: '#1890ff' }
      },
      {
        name: '民间社融',
        type: 'bar',
        stack: 'total',
        data: privateAmount,
        itemStyle: { color: '#52c41a' }
      }
    ]
  }
})

// M2-社融剪刀差图
const m2SocialScissorsOption = computed(() => {
  const { dates, m2, socialGrowth } = detailData.value
  const scissors = m2.map((m, i) => parseFloat((m - socialGrowth[i]).toFixed(2)))
  
  return {
    tooltip: {
      trigger: 'axis',
      formatter: function(params) {
        const val = params[0].value
        const status = val > 1.5 ? '资产荒' : val < 0.5 ? '资金流入实体' : '均衡'
        return `${params[0].axisValue}<br/>` +
               `${params[0].marker} 剪刀差: ${val}%<br/>` +
               `<span style="color:${val > 1.5 ? '#f5222d' : val < 0.5 ? '#52c41a' : '#999'}">${status}</span>`
      }
    },
    grid: {
      left: 60,
      right: 40,
      bottom: 60,
      top: 50,
      containLabel: false
    },
    xAxis: {
      type: 'category',
      data: dates,
      axisLabel: {
        fontSize: 10,
        interval: 0,
        rotate: 0
      }
    },
    yAxis: {
      type: 'value',
      name: '剪刀差(%)',
      axisLabel: {
        formatter: '{value}%',
        fontSize: 11,
        margin: 8
      },
      nameTextStyle: {
        align: 'left',
        padding: [0, 0, 0, -30]
      }
    },
    series: [
      {
        name: 'M2-社融剪刀差',
        type: 'bar',
        data: scissors,
        barWidth: '50%',
        itemStyle: {
          color: function(params) {
            const val = params.value
            return val > 1.5 ? '#ff4d4f' : val < 0.5 ? '#52c41a' : '#faad14'
          }
        },
        markLine: {
          silent: true,
          data: [
            { yAxis: 1.5, lineStyle: { color: '#ff4d4f', type: 'dashed' }, label: { formatter: '资产荒线', fontSize: 10 } },
            { yAxis: 0.5, lineStyle: { color: '#52c41a', type: 'dashed' }, label: { formatter: '实体线', fontSize: 10 } }
          ]
        }
      }
    ]
  }
})

// 社融 vs 盈利验证图
const socialVsProfitOption = computed(() => {
  const { dates, socialGrowth, profitGrowth } = detailData.value
  
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'cross' }
    },
    legend: {
      data: ['社融增速(领先6月)', '全A利润增速'],
      bottom: 0
    },
    grid: {
      left: 60,
      right: 40,
      bottom: 60,
      top: 50,
      containLabel: false
    },
    xAxis: {
      type: 'category',
      data: dates,
      axisLabel: {
        fontSize: 10,
        interval: 0,
        rotate: 0
      }
    },
    yAxis: {
      type: 'value',
      name: '增速(%)',
      axisLabel: {
        formatter: '{value}%',
        fontSize: 11,
        margin: 8
      },
      nameTextStyle: {
        align: 'left',
        padding: [0, 0, 0, -30]
      }
    },
    series: [
      {
        name: '社融增速(领先6月)',
        type: 'line',
        data: socialGrowth,
        smooth: true,
        lineStyle: { color: '#1890ff', width: 2, type: 'dashed' },
        itemStyle: { color: '#1890ff' }
      },
      {
        name: '全A利润增速',
        type: 'line',
        data: profitGrowth,
        smooth: true,
        lineStyle: { color: '#52c41a', width: 3 },
        itemStyle: { color: '#52c41a' },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [
              { offset: 0, color: 'rgba(82, 196, 26, 0.3)' },
              { offset: 1, color: 'rgba(82, 196, 26, 0.05)' }
            ]
          }
        }
      }
    ]
  }
})

// 季节性图表
const seasonalOption = computed(() => {
  const { dates, socialGrowth, seasonalAvg } = detailData.value
  // 计算今年各月与季节性均值的偏差
  const currentYear = socialGrowth.map((s, i) => {
    const avg = seasonalAvg[i] / 4000 // 转换为增速单位
    return parseFloat((s + (Math.random() * 2 - 1)).toFixed(1))
  })
  
  return {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['今年', '近5年均值'],
      bottom: 0
    },
    grid: {
      left: 60,
      right: 40,
      bottom: 60,
      top: 50,
      containLabel: false
    },
    xAxis: {
      type: 'category',
      data: dates,
      axisLabel: {
        fontSize: 10,
        interval: 0,
        rotate: 0
      }
    },
    yAxis: {
      type: 'value',
      name: '社融增量(千亿)',
      axisLabel: {
        formatter: (v) => v,
        fontSize: 11,
        margin: 8
      },
      nameTextStyle: {
        align: 'left',
        padding: [0, 0, 0, -30]
      }
    },
    series: [
      {
        name: '今年',
        type: 'bar',
        data: currentYear.map((v, i) => totalAmount.value[i] / 1000),
        itemStyle: { 
          color: function(params) {
            const current = params.value
            const avg = seasonalAvg[params.dataIndex] / 1000
            return current > avg * 1.05 ? '#52c41a' : current < avg * 0.95 ? '#f5222d' : '#faad14'
          }
        }
      },
      {
        name: '近5年均值',
        type: 'line',
        data: seasonalAvg.map(v => v / 1000),
        smooth: true,
        lineStyle: { color: '#999', width: 2, type: 'dashed' },
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
.transmission-detail-dialog :deep(.el-dialog__header) {
  background: linear-gradient(135deg, #1890ff 0%, #52c41a 100%);
  margin-right: 0;
  padding: 20px;
}

.transmission-detail-dialog :deep(.el-dialog__title) {
  color: white;
  font-weight: 600;
  font-size: 18px;
}

.transmission-detail-dialog :deep(.el-dialog__headerbtn .el-dialog__close) {
  color: white;
}

.transmission-detail-dialog :deep(.el-dialog__body) {
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
  position: relative;
  overflow: visible;
}

.chart {
  width: 100%;
  height: 100%;
}

.quality-insight,
.driver-insight {
  margin-top: 12px;
  text-align: center;
}

.scissors-insight,
.validation-insight {
  margin-top: 12px;
  padding: 10px;
  background: #e6f7ff;
  border-left: 3px solid #1890ff;
  border-radius: 4px;
}

.insight-text {
  margin: 0;
  font-size: 12px;
  color: #606266;
  line-height: 1.6;
}

/* 缺口监测样式 */
.gap-monitor {
  padding: 16px;
}

.gap-item {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.gap-item:last-child {
  margin-bottom: 0;
  padding-bottom: 0;
  border-bottom: none;
}

.gap-month {
  width: 60px;
  font-size: 13px;
  font-weight: 600;
  color: #303133;
}

.gap-bars {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.gap-bar-group {
  display: flex;
  align-items: center;
  gap: 8px;
}

.gap-label {
  width: 40px;
  font-size: 11px;
  color: #909399;
}

.gap-bar-bg {
  flex: 1;
  height: 20px;
  background: #f5f5f5;
  border-radius: 4px;
  overflow: hidden;
}

.gap-bar {
  height: 100%;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding-right: 8px;
  transition: width 0.5s ease;
}

.gap-bar.expected {
  background: #d9d9d9;
}

.gap-bar.actual {
  background: #1890ff;
}

.gap-bar.actual.above {
  background: #52c41a;
}

.gap-bar.actual.below {
  background: #f5222d;
}

.bar-value {
  font-size: 11px;
  color: white;
  font-weight: 600;
}

.gap-deviation {
  width: 60px;
  text-align: right;
  font-size: 13px;
  font-weight: 600;
}

.gap-deviation.positive {
  color: #52c41a;
}

.gap-deviation.negative {
  color: #f5222d;
}

/* 诊断区域 */
.diagnosis-section {
  background: linear-gradient(135deg, #fff1f0 0%, #fff7e6 100%);
  border: 2px solid #ff4d4f;
  border-radius: 8px;
  padding: 20px;
}

.diagnosis-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  font-size: 16px;
  font-weight: 600;
  color: #f5222d;
}

.diagnosis-content {
  font-size: 14px;
  line-height: 1.8;
  color: #434343;
}

.diagnosis-content p {
  margin: 0;
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