<template>
  <div class="household-panel">
    <div class="panel-header">
      <div class="header-content">
        <div>
          <h2>居民资产负债表面板</h2>
          <p class="subtitle">居民是否愿意加杠杆 | 判断消费复苏是否可持续</p>
        </div>
        <el-button type="primary" :icon="InfoFilled" @click="openGuide">
          面板说明
        </el-button>
      </div>
    </div>

    <!-- 核心指标 -->
    <el-row :gutter="16" class="kpi-row">
      <el-col :span="4">
        <KPICard title="居民存款同比" :value="12.8" unit="%" :change="-0.5" />
      </el-col>
      <el-col :span="4">
        <KPICard title="居民贷款同比" :value="5.2" unit="%" :change="-1.8" />
      </el-col>
      <el-col :span="4">
        <KPICard title="房贷占比" :value="62.5" unit="%" :change="-2.1" />
      </el-col>
      <el-col :span="4">
        <KPICard title="消费贷增速" :value="8.5" unit="%" :change="2.3" />
      </el-col>
      <el-col :span="4">
        <KPICard title="储蓄率" :value="34.2" unit="%" :change="1.5" />
      </el-col>
      <el-col :span="4">
        <div class="balance-health" :class="balanceHealth.class">
          <div class="health-label">资产负债表</div>
          <div class="health-value">{{ balanceHealth.text }}</div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :span="12">
        <ChartCard title="居民存贷款增速" subtitle="存款高增 vs 贷款低迷">
          <HouseholdLoanDepositChart :data="loanDepositData" />
        </ChartCard>
      </el-col>
      <el-col :span="12">
        <ChartCard title="房贷与消费贷结构" subtitle="去杠杆进程">
          <LoanStructureChart :data="loanStructureData" />
        </ChartCard>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="chart-row">
      <el-col :span="12">
        <ChartCard title="储蓄率趋势" subtitle="预防性储蓄是否见顶">
          <SavingsRateChart :data="savingsRateData" />
        </ChartCard>
      </el-col>
      <el-col :span="12">
        <ChartCard title="居民杠杆率" subtitle="债务/GDP">
          <LeverageChart :data="leverageData" />
        </ChartCard>
      </el-col>
    </el-row>

    <HouseholdGuideDialog ref="guideDialogRef" />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { InfoFilled } from '@element-plus/icons-vue'
import KPICard from './components/KPICard.vue'
import ChartCard from './components/ChartCard.vue'
import HouseholdLoanDepositChart from './components/HouseholdLoanDepositChart.vue'
import LoanStructureChart from './components/LoanStructureChart.vue'
import SavingsRateChart from './components/SavingsRateChart.vue'
import LeverageChart from './components/LeverageChart.vue'
import HouseholdGuideDialog from './components/HouseholdGuideDialog.vue'

const balanceHealth = computed(() => {
  const depositGrowth = 12.8
  const loanGrowth = 5.2
  const savingsRate = 34.2
  
  if (depositGrowth > 10 && loanGrowth < 6 && savingsRate > 32) {
    return { text: '修复中', class: 'repairing' }
  } else if (depositGrowth < 8 && loanGrowth > 8) {
    return { text: '扩张', class: 'expanding' }
  }
  return { text: '稳定', class: 'stable' }
})

const loanDepositData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  deposit: [13.5, 13.2, 12.8, 12.5, 12.2, 11.8, 11.5, 11.2, 10.8, 10.5, 10.2, 9.8],
  loan: [7.2, 6.8, 6.5, 6.2, 5.8, 5.5, 5.2, 4.8, 4.5, 4.2, 3.8, 3.5]
})

const loanStructureData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  mortgage: [65.2, 64.8, 64.2, 63.8, 63.2, 62.8, 62.5, 62.0, 61.5, 61.0, 60.5, 60.0],
  consumer: [18.5, 18.8, 19.2, 19.5, 19.8, 20.2, 20.5, 20.8, 21.2, 21.5, 21.8, 22.0],
  other: [16.3, 16.4, 16.6, 16.7, 17.0, 17.0, 17.0, 17.2, 17.3, 17.5, 17.7, 18.0]
})

const savingsRateData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  rate: [36.5, 36.2, 35.8, 35.5, 35.2, 34.8, 34.5, 34.2, 33.8, 33.5, 33.2, 32.8]
})

const leverageData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  leverage: [62.8, 62.9, 63.0, 63.1, 63.2, 63.1, 63.0, 62.9, 62.8, 62.6, 62.4, 62.2]
})

const guideDialogRef = ref(null)
const openGuide = () => {
  guideDialogRef.value?.open()
}
</script>

<style scoped>
.household-panel {
  padding: 0;
}

.panel-header {
  margin-bottom: 20px;
  padding: 20px;
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
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

.balance-health {
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

.balance-health.repairing {
  border-left-color: #fa8c16;
}

.balance-health.expanding {
  border-left-color: #52c41a;
}

.balance-health.stable {
  border-left-color: #1890ff;
}

.health-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.health-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}

.chart-row {
  margin-bottom: 16px;
}
</style>
