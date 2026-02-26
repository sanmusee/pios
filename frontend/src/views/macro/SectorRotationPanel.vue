<template>
  <div class="sector-rotation-panel">
    <div class="panel-header">
      <div class="header-content">
        <div>
          <h2>行业景气度与风格轮动 (Level 3)</h2>
          <p class="subtitle">宏观定仓位，风格定节奏，行业定品种 | 三层防御体系的最后一环</p>
        </div>
        <el-button type="primary" :icon="InfoFilled" @click="openGuide">
          面板说明
        </el-button>
      </div>
    </div>

    <!-- 核心信号卡片 -->
    <el-row :gutter="16" class="signal-row">
      <el-col :span="6">
        <SignalCard 
          title="当前风格" 
          :value="styleSignal.value" 
          :subtext="styleSignal.subtext"
          :type="styleSignal.type"
          icon="TrendCharts"
        />
      </el-col>
      <el-col :span="6">
        <SignalCard 
          title="最拥挤行业" 
          :value="crowdedSector.name" 
          :subtext="`成交占比 ${crowdedSector.ratio}%`"
          type="warning"
          icon="Histogram"
        />
      </el-col>
      <el-col :span="6">
        <SignalCard 
          title="盈利预期" 
          :value="epsSignal.value" 
          :subtext="epsSignal.subtext"
          :type="epsSignal.type"
          icon="DataLine"
        />
      </el-col>
      <el-col :span="6">
        <SignalCard 
          title="市场广度" 
          :value="breadthSignal.value" 
          :subtext="breadthSignal.subtext"
          :type="breadthSignal.type"
          icon="PieChart"
        />
      </el-col>
    </el-row>

    <!-- 第一行：风格指数 + 行业拥挤度 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :span="12">
        <ChartCard 
          title="模块一：市场风格指数" 
          subtitle="价值(沪深300) vs 成长(创业板指) 相对强弱"
          tag="风格"
          tagType="primary"
        >
          <StyleRatioChart :data="styleRatioData" />
        </ChartCard>
      </el-col>
      <el-col :span="12">
        <ChartCard 
          title="模块二：行业拥挤度监控" 
          subtitle="成交额占比 | 逆向策略寻找底部行业"
          tag="拥挤度"
          tagType="warning"
        >
          <SectorCrowdingChart :data="sectorCrowdingData" />
        </ChartCard>
      </el-col>
    </el-row>

    <!-- 第二行：盈利预期 + 市场广度 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :span="12">
        <ChartCard 
          title="模块三：盈利预期斜率" 
          subtitle="Forward EPS 变化率 | 估值压制与反弹潜力"
          tag="盈利"
          tagType="success"
        >
          <EPSRevisionChart :data="epsRevisionData" />
        </ChartCard>
      </el-col>
      <el-col :span="12">
        <ChartCard 
          title="模块四：个股结构与广度" 
          subtitle="站上均线个股百分比 | 背离分析与底部确认"
          tag="广度"
          tagType="danger"
        >
          <MarketBreadthChart :data="marketBreadthData" />
        </ChartCard>
      </el-col>
    </el-row>

    <SectorRotationGuideDialog ref="guideDialogRef" />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { InfoFilled } from '@element-plus/icons-vue'
import SignalCard from './components/SignalCard.vue'
import ChartCard from './components/ChartCard.vue'
import StyleRatioChart from './components/StyleRatioChart.vue'
import SectorCrowdingChart from './components/SectorCrowdingChart.vue'
import EPSRevisionChart from './components/EPSRevisionChart.vue'
import MarketBreadthChart from './components/MarketBreadthChart.vue'
import SectorRotationGuideDialog from './components/SectorRotationGuideDialog.vue'

// 信号卡片数据
const styleSignal = computed(() => {
  const ratio = styleRatioData.value.ratio[styleRatioData.value.ratio.length - 1]
  const prev = styleRatioData.value.ratio[styleRatioData.value.ratio.length - 2]
  
  if (ratio > prev) {
    return { value: '价值占优', subtext: '拥抱蓝筹', type: 'primary' }
  }
  return { value: '成长占优', subtext: '布局小票', type: 'success' }
})

const crowdedSector = ref({ name: '电子', ratio: 18.5 })

const epsSignal = computed(() => {
  const revision = epsRevisionData.value.revision
  const last = revision[revision.length - 1]
  
  if (last > 2) {
    return { value: '上调加速', subtext: '盈利改善', type: 'success' }
  } else if (last < -2) {
    return { value: '下调加速', subtext: '警惕风险', type: 'danger' }
  }
  return { value: '预期平稳', subtext: '观望为主', type: 'info' }
})

const breadthSignal = computed(() => {
  const ma20 = marketBreadthData.value.ma20
  const last = ma20[ma20.length - 1]
  
  if (last < 20) {
    return { value: '极度冰点', subtext: '跌不动', type: 'success' }
  } else if (last > 80) {
    return { value: '过热风险', subtext: '注意回调', type: 'danger' }
  }
  return { value: '正常区间', subtext: '结构分化', type: 'warning' }
})

// 图表数据
const styleRatioData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  ratio: [1.25, 1.28, 1.22, 1.18, 1.15, 1.12, 1.08, 1.05, 1.02, 0.98, 0.95, 0.92],
  hs300: [3300, 3350, 3400, 3380, 3450, 3500, 3480, 3550, 3600, 3580, 3650, 3700],
  cyb: [1850, 1800, 1950, 2000, 2050, 2100, 2150, 2200, 2250, 2200, 2300, 2350]
})

const sectorCrowdingData = ref({
  sectors: ['电子', '医药', '电力设备', '计算机', '食品饮料', '银行', '非银', '汽车', '机械', '化工'],
  ratio: [18.5, 12.3, 10.8, 9.5, 8.2, 7.5, 6.8, 6.2, 5.5, 4.8],
  historyMax: [22.0, 15.5, 18.0, 16.0, 12.0, 10.0, 9.5, 8.0, 7.5, 6.5]
})

const epsRevisionData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  revision: [2.5, 1.8, 0.5, -1.2, -2.5, -3.2, -2.8, -1.5, -0.5, 0.8, 1.5, 2.2],
  hs300: [5.2, 5.0, 4.8, 4.5, 4.2, 4.0, 4.1, 4.3, 4.5, 4.8, 5.0, 5.2],
  cyb: [15.2, 14.5, 13.0, 11.5, 10.0, 8.5, 9.0, 10.5, 12.0, 13.5, 14.8, 15.5]
})

const marketBreadthData = ref({
  dates: ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06', '2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12'],
  ma20: [65, 58, 45, 35, 28, 22, 25, 32, 42, 55, 62, 68],
  ma60: [72, 68, 62, 55, 48, 42, 38, 40, 45, 52, 58, 62]
})

const guideDialogRef = ref(null)
const openGuide = () => {
  guideDialogRef.value?.open()
}
</script>

<style scoped>
.sector-rotation-panel {
  padding: 0;
}

.panel-header {
  margin-bottom: 20px;
  padding: 20px;
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
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

.signal-row {
  margin-bottom: 16px;
}

.chart-row {
  margin-bottom: 16px;
}
</style>
