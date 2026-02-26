<template>
  <div class="macro-dashboard">
    <div class="dashboard-header">
      <div class="header-content">
        <div class="header-left">
          <h2>宏观经济链路监控看板 (Level 1)</h2>
          <p class="subtitle">通过四个关键模块的对比，判断当前处于周期的哪个位置</p>
        </div>
        <div class="header-right">
          <div class="countdown-section">
            <div class="countdown-label">
              <el-icon><Clock /></el-icon>
              <span>数据发布</span>
            </div>
            <div class="countdown-items">
              <div v-for="item in upcomingReleases" :key="item.name" class="countdown-item">
                <span class="item-name">{{ item.name }}</span>
                <span class="item-days" :class="getCountdownClass(item.days)">{{ item.days }}天</span>
              </div>
            </div>
          </div>
          <el-button type="warning" :icon="QuestionFilled" @click="openGuide" size="small">
            看图指南
          </el-button>
        </div>
      </div>
    </div>

    <el-row :gutter="16" class="kpi-row">
      <el-col :span="6">
        <KPICard title="M2 增速" :value="m2Value" :change="m2Change" unit="%" :expectation="m2Expectation" />
      </el-col>
      <el-col :span="6">
        <KPICard title="CPI 同比" :value="cpiValue" :change="cpiChange" unit="%" :expectation="cpiExpectation" />
      </el-col>
      <el-col :span="6">
        <KPICard title="PPI 同比" :value="ppiValue" :change="ppiChange" unit="%" :expectation="ppiExpectation" />
      </el-col>
      <el-col :span="6">
        <KPICard title="制造业 PMI" :value="pmiValue" :change="pmiChange" :expectation="pmiExpectation" />
      </el-col>
    </el-row>

    <el-row :gutter="16" class="chart-row">
      <el-col :span="12">
        <ChartCard title="M1-M2 剪刀差 vs 沪深300" subtitle="源头 (Source) —— 货币闸门">
          <template #header-actions>
            <el-button 
              type="primary" 
              link 
              size="small" 
              :icon="ArrowRight"
              @click="openSourceDetail"
            >
              详情
            </el-button>
          </template>
          <SourceChart :data="sourceData" />
        </ChartCard>
      </el-col>
      <el-col :span="12">
        <ChartCard title="社融增量构成" subtitle="传导 (Transmission) —— 信用扩张">
          <template #header-actions>
            <el-button
              type="primary"
              link
              size="small"
              :icon="ArrowRight"
              @click="openTransmissionDetail"
            >
              详情
            </el-button>
          </template>
          <TransmissionChart :data="transmissionData" />
        </ChartCard>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="chart-row">
      <el-col :span="12">
        <ChartCard title="CPI/PPI 波动区间" subtitle="结果 (Outcome) —— 经济温度">
          <template #header-actions>
            <el-button 
              type="primary" 
              link 
              size="small" 
              :icon="ArrowRight"
              @click="openOutcomeDetail"
            >
              详情
            </el-button>
          </template>
          <OutcomeChart :data="outcomeData" />
        </ChartCard>
      </el-col>
      <el-col :span="12">
        <ChartCard title="1年期与5年期 LPR 利率走势" subtitle="反馈 (Feedback) —— 政策应对">
          <FeedbackChart :data="feedbackData" />
        </ChartCard>
      </el-col>
    </el-row>

    <GuideDialog ref="guideDialogRef" />
    <SourceDetailDialog ref="sourceDetailDialogRef" />
    <OutcomeDetailDialog ref="outcomeDetailDialogRef" />
    <TransmissionDetailDialog ref="transmissionDetailDialogRef" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { QuestionFilled, ArrowRight, Clock } from '@element-plus/icons-vue'
import KPICard from './components/KPICard.vue'
import ChartCard from './components/ChartCard.vue'
import SourceChart from './components/SourceChart.vue'
import TransmissionChart from './components/TransmissionChart.vue'
import OutcomeChart from './components/OutcomeChart.vue'
import FeedbackChart from './components/FeedbackChart.vue'
import GuideDialog from './components/GuideDialog.vue'
import SourceDetailDialog from './components/SourceDetailDialog.vue'
import OutcomeDetailDialog from './components/OutcomeDetailDialog.vue'
import TransmissionDetailDialog from './components/TransmissionDetailDialog.vue'

const m2Value = ref(10.1)
const m2Change = ref(-0.2)
const m2Expectation = ref(10.3)
const cpiValue = ref(0.3)
const cpiChange = ref(0.1)
const cpiExpectation = ref(0.4)
const ppiValue = ref(-2.5)
const ppiChange = ref(0.3)
const ppiExpectation = ref(-2.2)
const pmiValue = ref(49.0)
const pmiChange = ref(-0.4)
const pmiExpectation = ref(49.5)

const sourceData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  m1: [5.9, 5.8, 5.5, 5.2, 4.8, 4.5, 4.2, 3.9, 3.5, 3.2, 2.8, 2.5],
  m2: [10.2, 10.1, 9.8, 9.5, 9.3, 9.1, 8.9, 8.7, 8.5, 8.3, 8.1, 8.0],
  hs300: [3300, 3350, 3400, 3380, 3450, 3500, 3480, 3550, 3600, 3580, 3650, 3700]
})

const transmissionData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06'],
  governmentBond: [8000, 7500, 9000, 8500, 9200, 8800],
  corporateBond: [5000, 4800, 5200, 5100, 5300, 5000],
  entrustLoan: [2000, 1800, 2200, 1900, 2100, 2000],
  shortTermLoan: [15000, 12000, 18000, 16000, 20000, 17000],
  longTermLoan: [30000, 26000, 34000, 32000, 35000, 33000]
})

const outcomeData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  cpi: [-0.8, 0.7, 0.1, 0.3, 0.4, 0.2, 0.5, 0.6, 0.4, 0.3, 0.2, 0.3],
  ppi: [-2.5, -2.7, -2.8, -2.5, -2.3, -2.1, -1.8, -1.5, -1.2, -1.0, -0.8, -0.5],
  pmi: [49.2, 49.1, 50.8, 50.4, 49.5, 49.5, 49.4, 49.1, 49.8, 50.1, 50.3, 49.0]
})

const feedbackData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  lpr1y: [3.45, 3.45, 3.45, 3.45, 3.45, 3.45, 3.35, 3.35, 3.35, 3.10, 3.10, 3.10],
  lpr5y: [4.20, 4.20, 3.95, 3.95, 3.95, 3.95, 3.85, 3.85, 3.85, 3.60, 3.60, 3.60],
  dr007: [2.15, 2.20, 2.10, 2.05, 2.12, 2.08, 1.95, 1.88, 1.92, 1.85, 1.80, 1.75]
})

// 数据发布倒计时逻辑
const upcomingReleases = ref([
  { name: 'CPI/PPI', days: 2 },
  { name: '金融数据', days: 5 },
  { name: 'PMI', days: 12 },
  { name: 'LPR', days: 18 }
])

const getCountdownClass = (days) => {
  if (days <= 3) return 'urgent'
  if (days <= 7) return 'soon'
  return 'normal'
}

const guideDialogRef = ref(null)
const sourceDetailDialogRef = ref(null)
const outcomeDetailDialogRef = ref(null)
const transmissionDetailDialogRef = ref(null)

const openGuide = () => {
  guideDialogRef.value?.open()
}

const openSourceDetail = () => {
  sourceDetailDialogRef.value?.open()
}

const openOutcomeDetail = () => {
  outcomeDetailDialogRef.value?.open()
}

const openTransmissionDetail = () => {
  transmissionDetailDialogRef.value?.open()
}
</script>

<style scoped>
.macro-dashboard {
  padding: 0;
}

.dashboard-header {
  margin-bottom: 20px;
  padding: 20px;
  background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
  border-radius: 8px;
  color: white;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  flex: 1;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

/* 倒计时区域 */
.countdown-section {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 8px;
}

.countdown-label {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  opacity: 0.9;
  white-space: nowrap;
}

.countdown-items {
  display: flex;
  gap: 12px;
}

.countdown-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
}

.item-name {
  font-size: 11px;
  opacity: 0.7;
}

.item-days {
  font-size: 12px;
  font-weight: 600;
  padding: 2px 8px;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.15);
}

.item-days.urgent {
  background: #f5576c;
}

.item-days.soon {
  background: #fac858;
  color: #333;
}

.dashboard-header h2 {
  margin: 0 0 8px 0;
  font-size: 24px;
}

.subtitle {
  margin: 0;
  opacity: 0.8;
  font-size: 14px;
}

.kpi-row {
  margin-bottom: 16px;
}

.chart-row {
  margin-bottom: 16px;
}
</style>
