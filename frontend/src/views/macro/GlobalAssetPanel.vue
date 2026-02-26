<template>
  <div class="global-asset-panel">
    <div class="panel-header">
      <div class="header-content">
        <div>
          <h2>全球定价与大类资产锚点</h2>
          <p class="subtitle">资金流向的因果链：全球定价锚 → 汇率外资 → 风险溢价 → 实体前瞻</p>
        </div>
        <el-button type="primary" :icon="InfoFilled" @click="openGuide">
          面板说明
        </el-button>
      </div>
    </div>

    <!-- 第一行：全球定价之锚 + 汇率与外资 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :span="12">
        <ChartCard 
          title="模块一：全球定价之锚" 
          subtitle="10年美债 vs 10年国债 | 全球资产定价的终极参考点"
          tag="因"
          tagType="primary"
        >
          <GlobalAnchorChart :data="globalAnchorData" />
        </ChartCard>
      </el-col>
      <el-col :span="12">
        <ChartCard 
          title="模块二：汇率与外资情绪" 
          subtitle="USD/CNH vs 北向资金20日平滑 | 聪明钱的真实动作"
          tag="果"
          tagType="success"
        >
          <ExchangeFlowChart :data="exchangeFlowData" />
        </ChartCard>
      </el-col>
    </el-row>

    <!-- 第二行：风险溢价 + 实体需求前瞻 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :span="12">
        <ChartCard 
          title="模块三：风险补偿与估值水位" 
          subtitle="沪深300 ERP | 判定当前位置是便宜还是贵"
          tag="择时"
          tagType="warning"
        >
          <ERPChart :data="erpData" />
        </ChartCard>
      </el-col>
      <el-col :span="12">
        <ChartCard 
          title="模块四：实体需求前瞻" 
          subtitle="LME铜 vs 南华商品指数 | 铜博士的复苏信号"
          tag="前瞻"
          tagType="danger"
        >
          <CommodityForwardChart :data="commodityData" />
        </ChartCard>
      </el-col>
    </el-row>

    <GlobalAssetGuideDialog ref="guideDialogRef" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { InfoFilled } from '@element-plus/icons-vue'
import ChartCard from './components/ChartCard.vue'
import GlobalAnchorChart from './components/GlobalAnchorChart.vue'
import ExchangeFlowChart from './components/ExchangeFlowChart.vue'
import ERPChart from './components/ERPChart.vue'
import CommodityForwardChart from './components/CommodityForwardChart.vue'
import GlobalAssetGuideDialog from './components/GlobalAssetGuideDialog.vue'

// 模块一：全球定价之锚数据
const globalAnchorData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  usBond: [4.0, 4.1, 4.2, 4.5, 4.4, 4.3, 4.2, 4.1, 4.0, 3.9, 3.8, 3.7],
  cnBond: [2.65, 2.58, 2.45, 2.38, 2.32, 2.28, 2.25, 2.22, 2.20, 2.18, 2.15, 2.12],
  spread: [-1.35, -1.52, -1.75, -2.12, -2.08, -2.02, -1.95, -1.88, -1.80, -1.72, -1.65, -1.58]
})

// 模块二：汇率与外资情绪数据
const exchangeFlowData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  cnh: [7.18, 7.20, 7.22, 7.25, 7.28, 7.30, 7.32, 7.35, 7.33, 7.30, 7.28, 7.25],
  northBound: [120, 40, 190, 390, 570, 790, 940, 1040, 1220, 1470, 1770, 2120]
})

// 模块三：ERP数据
const erpData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  erp: [6.8, 6.9, 7.2, 7.5, 7.3, 7.1, 6.9, 6.7, 6.5, 6.3, 6.1, 5.9],
  mean: 5.5,
  std: 1.2,
  plus1sigma: 6.7,
  plus2sigma: 7.9,
  minus1sigma: 4.3,
  minus2sigma: 3.1
})

// 模块四：商品前瞻数据
const commodityData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  copper: [100, 102, 105, 108, 106, 104, 105, 107, 109, 108, 110, 112],
  commodity: [100, 103, 107, 111, 109, 106, 104, 105, 108, 111, 114, 117]
})

const guideDialogRef = ref(null)
const openGuide = () => {
  guideDialogRef.value?.open()
}
</script>

<style scoped>
.global-asset-panel {
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

.chart-row {
  margin-bottom: 16px;
}
</style>
