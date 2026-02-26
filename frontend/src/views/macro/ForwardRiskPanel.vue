<template>
  <div class="forward-risk-panel">
    <div class="panel-header">
      <div class="header-content">
        <div>
          <h2>领先指标 + 风险偏好 + 全球扰动面板</h2>
          <p class="subtitle">判断未来3-6个月方向，捕捉预期放大器，监控外部冲击</p>
        </div>
        <el-button type="primary" :icon="InfoFilled" @click="openGuide">
          面板说明
        </el-button>
      </div>
    </div>

    <!-- 告警横幅 -->
    <AlertBanner
      :vix="22"
      :exchange-rate="7.25"
      :china-us-spread="-1.88"
      :credit-spread="75"
    />

    <!-- 领先变量模块 -->
    <div class="module-section">
      <div class="module-header">
        <div class="module-title">
          <el-icon><TrendCharts /></el-icon>
          <span>一、领先变量模块（Forward）</span>
        </div>
        <el-tag type="success" effect="dark">判断未来3-6个月方向</el-tag>
      </div>
      
      <el-row :gutter="16">
        <el-col :span="8">
          <ChartCard title="社融同比变化趋势" subtitle="信用扩张领先指标">
            <SocialFinancingChart :data="socialFinancingData" />
          </ChartCard>
        </el-col>
        <el-col :span="8">
          <ChartCard title="M1-M2剪刀差" subtitle="资金活化程度">
            <ScissorsChart :data="scissorsData" />
          </ChartCard>
        </el-col>
        <el-col :span="8">
          <ChartCard title="PMI新订单指数" subtitle="需求前瞻指标">
            <PMIOrdersChart :data="pmiOrdersData" />
          </ChartCard>
        </el-col>
      </el-row>

      <el-row :gutter="16" class="mt-16">
        <el-col :span="8">
          <ChartCard title="库存差（新订单-产成品库存）" subtitle="经济动能指标">
            <InventoryDiffChart :data="inventoryDiffData" />
          </ChartCard>
        </el-col>
        <el-col :span="8">
          <ChartCard title="30城新房成交（日频）" subtitle="比月报提前20天">
            <RealEstateDailyChart :data="realEstateDailyData" />
          </ChartCard>
        </el-col>
        <el-col :span="8">
          <ChartCard title="出口订单" subtitle="外需前瞻指标">
            <ExportChart :data="exportData" />
          </ChartCard>
        </el-col>
      </el-row>
    </div>

    <!-- 风险偏好模块 -->
    <div class="module-section">
      <div class="module-header">
        <div class="module-title">
          <el-icon><Money /></el-icon>
          <span>二、风险偏好模块（Risk Appetite）</span>
        </div>
        <el-tag type="warning" effect="dark">预期放大器</el-tag>
      </div>
      
      <el-row :gutter="16">
        <el-col :span="12">
          <ChartCard title="A股市场" subtitle="沪深300 vs 创业板">
            <StockMarketChart :data="stockMarketData" />
          </ChartCard>
        </el-col>
        <el-col :span="12">
          <ChartCard title="债券市场" subtitle="10Y国债收益率">
            <BondChart :data="bondData" />
          </ChartCard>
        </el-col>
      </el-row>

      <el-row :gutter="16" class="mt-16">
        <el-col :span="8">
          <ChartCard title="中美利差" subtitle="10Y国债利差">
            <ChinaUsSpreadChart :data="chinaUsSpreadData" />
          </ChartCard>
        </el-col>
        <el-col :span="8">
          <ChartCard title="信用利差" subtitle="AAA企业债 - 国债">
            <CreditSpreadChart :data="creditSpreadData" />
          </ChartCard>
        </el-col>
        <el-col :span="8">
          <ChartCard title="二永债利差" subtitle="银行资本补充压力">
            <BankCapitalSpreadChart :data="bankCapitalSpreadData" />
          </ChartCard>
        </el-col>
      </el-row>

      <el-row :gutter="16" class="mt-16">
        <el-col :span="12">
          <ChartCard title="汇率与资金流向" subtitle="人民币汇率 vs 北向资金">
            <CapitalFlowChart :data="capitalFlowData" />
          </ChartCard>
        </el-col>
      </el-row>
    </div>

    <!-- 全球扰动模块 -->
    <div class="module-section">
      <div class="module-header">
        <div class="module-title">
          <el-icon><Collection /></el-icon>
          <span>三、全球扰动模块（External Shock）</span>
        </div>
        <el-tag type="danger" effect="dark">外部冲击监控</el-tag>
      </div>
      
      <el-row :gutter="16">
        <el-col :span="8">
          <ChartCard title="美债收益率" subtitle="10Y美债收益率">
            <UsTreasuryChart :data="usTreasuryData" />
          </ChartCard>
        </el-col>
        <el-col :span="8">
          <ChartCard title="美元指数" subtitle="DXY">
            <DollarIndexChart :data="dollarIndexData" />
          </ChartCard>
        </el-col>
        <el-col :span="8">
          <ChartCard title="原油价格" subtitle="布伦特原油">
            <OilChart :data="oilData" />
          </ChartCard>
        </el-col>
      </el-row>

      <el-row :gutter="16" class="mt-16">
        <el-col :span="12">
          <ChartCard title="大宗商品指数" subtitle="CRB指数">
            <CommodityChart :data="commodityData" />
          </ChartCard>
        </el-col>
        <el-col :span="12">
          <ChartCard title="VIX恐慌指数" subtitle="市场波动率预期">
            <VIXChart :data="vixData" />
          </ChartCard>
        </el-col>
      </el-row>
    </div>

    <ForwardRiskGuideDialog ref="guideDialogRef" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { InfoFilled, TrendCharts, Money, Collection } from '@element-plus/icons-vue'
import ChartCard from './components/ChartCard.vue'
import AlertBanner from './components/AlertBanner.vue'
import SocialFinancingChart from './components/SocialFinancingChart.vue'
import ScissorsChart from './components/ScissorsChart.vue'
import PMIOrdersChart from './components/PMIOrdersChart.vue'
import InventoryDiffChart from './components/InventoryDiffChart.vue'
import RealEstateDailyChart from './components/RealEstateDailyChart.vue'
import ExportChart from './components/ExportChart.vue'
import StockMarketChart from './components/StockMarketChart.vue'
import BondChart from './components/BondChart.vue'
import ChinaUsSpreadChart from './components/ChinaUsSpreadChart.vue'
import CreditSpreadChart from './components/CreditSpreadChart.vue'
import BankCapitalSpreadChart from './components/BankCapitalSpreadChart.vue'
import CapitalFlowChart from './components/CapitalFlowChart.vue'
import UsTreasuryChart from './components/UsTreasuryChart.vue'
import DollarIndexChart from './components/DollarIndexChart.vue'
import OilChart from './components/OilChart.vue'
import CommodityChart from './components/CommodityChart.vue'
import VIXChart from './components/VIXChart.vue'
import ForwardRiskGuideDialog from './components/ForwardRiskGuideDialog.vue'

// 领先变量数据
const socialFinancingData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  value: [9.5, 9.2, 8.8, 8.5, 8.3, 8.1, 7.9, 7.8, 7.6, 7.5, 7.4, 7.3]
})

const scissorsData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  value: [-4.3, -4.3, -4.3, -4.3, -4.5, -4.6, -4.7, -4.8, -5.0, -5.1, -5.3, -5.5]
})

const pmiOrdersData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  value: [49.0, 48.5, 51.2, 50.8, 49.5, 49.2, 48.8, 48.5, 49.2, 49.8, 50.2, 48.9]
})

const inventoryData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  value: [2.1, 2.3, 2.5, 2.8, 3.0, 3.2, 3.1, 2.9, 2.7, 2.5, 2.3, 2.1]
})

const realEstateData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  value: [-15.2, -18.5, -12.3, -8.5, -5.2, -3.8, -2.5, -1.8, -0.5, 1.2, 2.5, 3.8]
})

const exportData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  value: [2.3, 5.8, 1.2, 4.5, 7.8, 8.2, 6.5, 5.2, 4.8, 6.2, 7.5, 8.8]
})

const inventoryDiffData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  newOrders: [49.0, 48.5, 51.2, 50.8, 49.5, 49.2, 48.8, 48.5, 49.2, 49.8, 50.2, 48.9],
  inventory: [48.5, 48.8, 48.2, 47.5, 47.8, 48.5, 49.2, 49.5, 49.0, 48.2, 47.5, 47.2],
  diff: [0.5, -0.3, 3.0, 3.3, 1.7, 0.7, -0.4, -1.0, 0.2, 1.6, 2.7, 1.7]
})

const realEstateDailyData = ref({
  dates: ['01-01', '01-05', '01-10', '01-15', '01-20', '01-25', '01-30', '02-05', '02-10', '02-15', '02-20', '02-25', '03-01', '03-05', '03-10', '03-15', '03-20', '03-25', '03-30'],
  daily: [25, 28, 32, 35, 30, 28, 26, 30, 35, 40, 45, 42, 38, 42, 48, 52, 55, 58, 60],
  ma7: [null, null, null, null, null, null, 29, 30, 31, 33, 35, 37, 38, 39, 41, 43, 46, 49, 52]
})

// 风险偏好数据
const stockMarketData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  hs300: [3300, 3350, 3400, 3380, 3450, 3500, 3480, 3550, 3600, 3580, 3650, 3700],
  cyb: [1850, 1900, 1950, 1920, 1980, 2050, 2020, 2100, 2150, 2120, 2200, 2280]
})

const bondData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  value: [2.65, 2.58, 2.45, 2.38, 2.32, 2.28, 2.25, 2.22, 2.20, 2.18, 2.15, 2.12]
})

const chinaUsSpreadData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  value: [-1.35, -1.42, -1.55, -1.62, -1.68, -1.72, -1.75, -1.78, -1.80, -1.82, -1.85, -1.88]
})

const creditSpreadData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  value: [65, 68, 72, 75, 78, 80, 82, 85, 83, 80, 78, 75]
})

const capitalFlowData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  exchangeRate: [7.18, 7.20, 7.22, 7.25, 7.28, 7.30, 7.32, 7.35, 7.33, 7.30, 7.28, 7.25],
  northBound: [120, -80, 150, 200, 180, 220, 150, 100, 180, 250, 300, 350]
})

// 全球扰动数据
const usTreasuryData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  value: [4.0, 4.1, 4.2, 4.5, 4.4, 4.3, 4.2, 4.1, 4.0, 3.9, 3.8, 3.7]
})

const dollarIndexData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  value: [103, 104, 105, 106, 105, 104, 103, 102, 101, 100, 99, 98]
})

const oilData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  value: [78, 82, 85, 88, 82, 79, 82, 85, 88, 85, 82, 80]
})

const commodityData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  value: [265, 270, 275, 280, 278, 275, 272, 270, 275, 280, 285, 288]
})

const vixData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  value: [14, 15, 16, 18, 15, 13, 14, 15, 16, 18, 20, 22]
})

const bankCapitalSpreadData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  value: [65, 68, 72, 75, 78, 82, 85, 88, 85, 82, 78, 75]
})

const guideDialogRef = ref(null)

const openGuide = () => {
  guideDialogRef.value?.open()
}
</script>

<style scoped>
.forward-risk-panel {
  padding: 0;
}

.panel-header {
  margin-bottom: 20px;
  padding: 20px;
  background: linear-gradient(135deg, #1a2980 0%, #26d0ce 100%);
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

.module-section {
  margin-bottom: 24px;
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.module-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 2px solid #ebeef5;
}

.module-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.module-title .el-icon {
  font-size: 22px;
  color: #409eff;
}

.mt-16 {
  margin-top: 16px;
}
</style>
