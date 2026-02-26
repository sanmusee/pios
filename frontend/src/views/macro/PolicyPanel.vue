<template>
  <div class="policy-panel">
    <div class="panel-header">
      <div class="header-content">
        <div>
          <h2>财政与政策强度面板</h2>
          <p class="subtitle">财政 = 真实需求的启动器 | 判断复苏是市场自发还是政策托底</p>
        </div>
        <el-button type="primary" :icon="InfoFilled" @click="openGuide">
          面板说明
        </el-button>
      </div>
    </div>

    <!-- 政策强度仪表盘 -->
    <el-row :gutter="16" class="kpi-row">
      <el-col :span="6">
        <div class="gauge-card">
          <div class="gauge-title">专项债进度</div>
          <div class="gauge-value" :style="{ color: getProgressColor(68) }">68%</div>
          <div class="gauge-bar">
            <div class="gauge-fill" :style="{ width: '68%', background: getProgressColor(68) }" />
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <KPICard title="财政支出同比" :value="8.5" unit="%" :change="1.2" />
      </el-col>
      <el-col :span="6">
        <KPICard title="基建投资" :value="6.2" unit="%" :change="0.8" />
      </el-col>
      <el-col :span="6">
        <div class="policy-strength" :class="policyStrength.class">
          <div class="strength-label">政策强度</div>
          <div class="strength-value">{{ policyStrength.text }}</div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :span="12">
        <ChartCard title="专项债发行进度" subtitle="全年3.8万亿额度">
          <SpecialBondChart :data="specialBondData" />
        </ChartCard>
      </el-col>
      <el-col :span="12">
        <ChartCard title="财政支出结构" subtitle="民生/基建/科技">
          <template #header-actions>
            <el-button
              type="primary"
              link
              size="small"
              :icon="ArrowRight"
              @click="openFiscalDetail"
            >
              详情
            </el-button>
          </template>
          <FiscalExpenditureChart :data="fiscalExpenditureData" />
        </ChartCard>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="chart-row">
      <el-col :span="12">
        <ChartCard title="基建投资细分" subtitle="交运/水利/能源">
          <InfrastructureChart :data="infrastructureData" />
        </ChartCard>
      </el-col>
      <el-col :span="12">
        <ChartCard title="政府债净融资" subtitle="国债+地方债">
          <GovtBondChart :data="govtBondData" />
        </ChartCard>
      </el-col>
    </el-row>

    <PolicyGuideDialog ref="guideDialogRef" />
    <FiscalExpenditureDetailDialog ref="fiscalDetailDialogRef" />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { InfoFilled, ArrowRight } from '@element-plus/icons-vue'
import KPICard from './components/KPICard.vue'
import ChartCard from './components/ChartCard.vue'
import SpecialBondChart from './components/SpecialBondChart.vue'
import FiscalExpenditureChart from './components/FiscalExpenditureChart.vue'
import InfrastructureChart from './components/InfrastructureChart.vue'
import GovtBondChart from './components/GovtBondChart.vue'
import PolicyGuideDialog from './components/PolicyGuideDialog.vue'
import FiscalExpenditureDetailDialog from './components/FiscalExpenditureDetailDialog.vue'

const getProgressColor = (val) => {
  if (val >= 80) return '#52c41a'
  if (val >= 60) return '#fa8c16'
  return '#f5222d'
}

const policyStrength = computed(() => {
  const score = 68 + 8.5 + 6.2
  if (score >= 90) return { text: '强刺激', class: 'strong' }
  if (score >= 75) return { text: '积极', class: 'active' }
  if (score >= 60) return { text: '温和', class: 'moderate' }
  return { text: '保守', class: 'conservative' }
})

const specialBondData = ref({
  months: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
  issued: [2800, 3200, 4100, 5200, 6100, 7200, 8100, 8900, 9500, 10200, 10800, 11500],
  target: 38000
})

const fiscalExpenditureData = ref({
  categories: ['民生', '基建', '科技', '环保', '国防'],
  values: [35, 28, 15, 12, 10]
})

const infrastructureData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  transport: [5.2, 5.5, 5.8, 6.0, 6.2, 6.5, 6.8, 7.0, 7.2, 7.5, 7.8, 8.0],
  water: [3.8, 4.0, 4.2, 4.5, 4.8, 5.0, 5.2, 5.5, 5.8, 6.0, 6.2, 6.5],
  energy: [4.5, 4.8, 5.0, 5.2, 5.5, 5.8, 6.0, 6.2, 6.5, 6.8, 7.0, 7.2]
})

const govtBondData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  netFinancing: [4200, 3800, 4500, 5200, 4800, 5500, 5100, 4900, 5300, 5800, 6200, 6500]
})

const guideDialogRef = ref(null)
const fiscalDetailDialogRef = ref(null)

const openGuide = () => {
  guideDialogRef.value?.open()
}

const openFiscalDetail = () => {
  fiscalDetailDialogRef.value?.open()
}
</script>

<style scoped>
.policy-panel {
  padding: 0;
}

.panel-header {
  margin-bottom: 20px;
  padding: 20px;
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
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

.gauge-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  text-align: center;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
  height: 100%;
}

.gauge-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 12px;
}

.gauge-value {
  font-size: 36px;
  font-weight: bold;
  margin-bottom: 12px;
}

.gauge-bar {
  height: 8px;
  background: #ebeef5;
  border-radius: 4px;
  overflow: hidden;
}

.gauge-fill {
  height: 100%;
  border-radius: 4px;
  transition: width 0.3s ease;
}

.policy-strength {
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

.policy-strength.strong {
  border-left-color: #f5222d;
}

.policy-strength.active {
  border-left-color: #fa8c16;
}

.policy-strength.moderate {
  border-left-color: #52c41a;
}

.policy-strength.conservative {
  border-left-color: #8c8c8c;
}

.strength-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.strength-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}

.chart-row {
  margin-bottom: 16px;
}
</style>
