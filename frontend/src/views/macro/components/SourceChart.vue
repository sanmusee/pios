<template>
  <v-chart class="source-chart" :option="chartOption" autoresize />
</template>

<script setup>
import { computed } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart, BarChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent, DataZoomComponent } from 'echarts/components'
import VChart from 'vue-echarts'

use([CanvasRenderer, LineChart, BarChart, GridComponent, TooltipComponent, LegendComponent, DataZoomComponent])

const props = defineProps({
  data: Object
})

const chartOption = computed(() => {
  const { dates, m1, m2, hs300 } = props.data
  
  const scissors = m1.map((val, idx) => parseFloat((val - m2[idx]).toFixed(2)))
  
  const hs300Min = Math.min(...hs300)
  const hs300Max = Math.max(...hs300)
  const hs300Range = hs300Max - hs300Min
  const hs300AxisMin = Math.floor(hs300Min - hs300Range * 0.1)
  const hs300AxisMax = Math.ceil(hs300Max + hs300Range * 0.1)
  
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'cross' }
    },
    legend: {
      data: ['M1 增速', 'M2 增速', '沪深300', 'M1-M2剪刀差'],
      bottom: 0
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '15%',
      top: '15%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: dates,
      axisLabel: { rotate: 45 }
    },
    yAxis: [
      {
        type: 'value',
        name: '增速 (%)',
        position: 'left',
        axisLabel: { formatter: '{value}%' }
      },
      {
        type: 'value',
        name: '沪深300',
        position: 'right',
        min: hs300AxisMin,
        max: hs300AxisMax,
        axisLabel: { formatter: '{value}' }
      }
    ],
    series: [
      {
        name: 'M1 增速',
        type: 'line',
        data: m1,
        smooth: true,
        lineStyle: { color: '#f5576c', width: 2 },
        itemStyle: { color: '#f5576c' },
        symbol: 'none'
      },
      {
        name: 'M2 增速',
        type: 'line',
        data: m2,
        smooth: true,
        lineStyle: { color: '#4facfe', width: 2 },
        itemStyle: { color: '#4facfe' },
        symbol: 'none'
      },
      {
        name: 'M1-M2剪刀差',
        type: 'bar',
        data: scissors,
        barWidth: '40%',
        itemStyle: {
          color: (params) => {
            return params.value >= 0 ? '#91cc75' : '#ee6666'
          },
          opacity: 0.7
        }
      },
      {
        name: '沪深300',
        type: 'line',
        yAxisIndex: 1,
        data: hs300,
        smooth: true,
        lineStyle: { color: '#f093fb', width: 3, type: 'solid' },
        itemStyle: { color: '#f093fb' },
        symbol: 'circle',
        symbolSize: 6
      }
    ]
  }
})
</script>

<style scoped>
.source-chart {
  width: 100%;
  height: 100%;
}
</style>
