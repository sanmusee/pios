<template>
  <div class="profit-cash-panel">
    <div class="panel-header">
      <div class="header-content">
        <div>
          <h2>盈利与现金流面板</h2>
          <p class="subtitle">利润 → 现金流 → 分红能力 → 股价持续性 | 避免估值修复误判为盈利驱动</p>
        </div>
        <el-button type="primary" :icon="InfoFilled" @click="openGuide">
          面板说明
        </el-button>
      </div>
    </div>

    <!-- 核心指标卡片 -->
    <el-row :gutter="16" class="kpi-row">
      <el-col :span="4">
        <KPICard title="工业企业利润" :value="industrialProfit" unit="%" :change="2.1" />
      </el-col>
      <el-col :span="4">
        <KPICard title="全A净利润" :value="listedProfit" unit="%" :change="-0.5" />
      </el-col>
      <el-col :span="4">
        <KPICard title="ROE" :value="roeValue" unit="%" :change="0.3" />
      </el-col>
      <el-col :span="4">
        <KPICard title="自由现金流" :value="fcfValue" unit="亿" :change="15.2" />
      </el-col>
      <el-col :span="4">
        <KPICard title="分红率" :value="dividendRate" unit="%" :change="1.8" />
      </el-col>
      <el-col :span="4">
        <div class="warning-card" :class="profitWarning.class">
          <div class="warning-title">盈利匹配度</div>
          <div class="warning-value">{{ profitWarning.text }}</div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :span="12">
        <ChartCard title="工业企业利润 vs 全A净利润" subtitle="盈利修复是否传导至上市公司">
          <ProfitComparisonChart :data="profitComparisonData" />
        </ChartCard>
      </el-col>
      <el-col :span="12">
        <ChartCard title="ROE趋势分解" subtitle="杜邦分析：净利率 × 周转率 × 杠杆率">
          <ROETrendChart :data="roeTrendData" />
        </ChartCard>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="chart-row">
      <el-col :span="12">
        <ChartCard title="自由现金流与分红" subtitle="现金流质量决定分红可持续性">
          <CashFlowChart :data="cashFlowData" />
        </ChartCard>
      </el-col>
      <el-col :span="12">
        <ChartCard title="盈利预期修正" subtitle="分析师一致预期变化">
          <EarningsRevisionChart :data="earningsRevisionData" />
        </ChartCard>
      </el-col>
    </el-row>

    <ProfitCashGuideDialog ref="guideDialogRef" />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { InfoFilled } from '@element-plus/icons-vue'
import KPICard from './components/KPICard.vue'
import ChartCard from './components/ChartCard.vue'
import ProfitComparisonChart from './components/ProfitComparisonChart.vue'
import ROETrendChart from './components/ROETrendChart.vue'
import CashFlowChart from './components/CashFlowChart.vue'
import EarningsRevisionChart from './components/EarningsRevisionChart.vue'
import ProfitCashGuideDialog from './components/ProfitCashGuideDialog.vue'

// KPI数据
const industrialProfit = ref(4.3)
const listedProfit = ref(2.1)
const roeValue = ref(9.2)
const fcfValue = ref(2850)
const dividendRate = ref(38.5)

// 盈利匹配度警告
const profitWarning = computed(() => {
  const diff = industrialProfit.value - listedProfit.value
  if (diff > 3) {
    return { text: '偏离', class: 'warning' }
  } else if (diff < -2) {
    return { text: '滞后', class: 'danger' }
  }
  return { text: '匹配', class: 'success' }
})

// 图表数据
const profitComparisonData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  industrial: [2.3, 1.8, 0.5, -1.2, -0.8, 0.2, 1.5, 2.8, 3.5, 4.0, 4.2, 4.3],
  listed: [5.2, 4.8, 3.5, 2.1, 1.5, 0.8, 0.5, 0.2, 0.8, 1.2, 1.8, 2.1]
})

const roeTrendData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  roe: [10.5, 10.2, 9.8, 9.2, 8.8, 8.5, 8.6, 8.8, 9.0, 9.1, 9.2, 9.2],
  margin: [8.2, 8.0, 7.8, 7.5, 7.2, 7.0, 7.1, 7.2, 7.3, 7.4, 7.5, 7.5],
  turnover: [0.85, 0.84, 0.82, 0.80, 0.78, 0.77, 0.78, 0.79, 0.80, 0.81, 0.82, 0.82],
  leverage: [1.52, 1.51, 1.50, 1.48, 1.47, 1.46, 1.47, 1.48, 1.49, 1.50, 1.51, 1.51]
})

const cashFlowData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  fcf: [2200, 2100, 1950, 1850, 1920, 2100, 2250, 2400, 2550, 2680, 2780, 2850],
  dividend: [850, 820, 780, 720, 750, 820, 880, 920, 980, 1020, 1050, 1100],
  payout: [38.6, 39.0, 40.0, 38.9, 39.1, 39.0, 39.1, 38.3, 38.4, 38.1, 37.8, 38.6]
})

const earningsRevisionData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  upgrade: [25, 22, 18, 15, 12, 10, 12, 15, 18, 20, 22, 24],
  downgrade: [15, 18, 22, 28, 32, 35, 30, 25, 20, 18, 16, 15],
  net: [10, 4, -4, -13, -20, -25, -18, -10, -2, 2, 6, 9]
})

const guideDialogRef = ref(null)
const openGuide = () => {
  guideDialogRef.value?.open()
}
</script>

<style scoped>
.profit-cash-panel {
  padding: 0;
}

.panel-header {
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

.panel-header h2 {
  margin: 0 0 8px 0;
  font-size: 24px;
}

.subtitle {
  margin: 0;
  opacity: 0.9;
  font-size: 14px;
}

.kpi-row {
  margin-bottom: 16px;
}

.warning-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  text-align: center;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  border-left: 4px solid;
}

.warning-card.success {
  border-left-color: #52c41a;
}

.warning-card.warning {
  border-left-color: #fa8c16;
}

.warning-card.danger {
  border-left-color: #f5222d;
}

.warning-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.warning-value {
  font-size: 24px;
  font-weight: bold;
}

.warning-card.success .warning-value {
  color: #52c41a;
}

.warning-card.warning .warning-value {
  color: #fa8c16;
}

.warning-card.danger .warning-value {
  color: #f5222d;
}

.chart-row {
  margin-bottom: 16px;
}
</style>
