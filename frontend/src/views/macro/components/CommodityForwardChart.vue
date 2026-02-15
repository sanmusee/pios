<template>
  <v-chart class="chart" :option="chartOption" autoresize />
</template>

<script setup>
import { computed } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent } from 'echarts/components'
import VChart from 'vue-echarts'

use([CanvasRenderer, LineChart, GridComponent, TooltipComponent, LegendComponent])

const props = defineProps({
  data: Object
})

const chartOption = computed(() => {
  const { dates, copper, commodity } = props.data
  
  return {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['LME铜(归一化)', '南华商品指数(归一化)'],
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
      axisLabel: { fontSize: 11 }
    },
    yAxis: {
      type: 'value',
      name: '归一化指数',
      min: 95,
      max: 120
    },
    series: [
      {
        name: 'LME铜(归一化)',
        type: 'line',
        data: copper,
        smooth: true,
        lineStyle: { color: '#b87333', width: 3 },
        itemStyle: { color: '#b87333' },
        symbol: 'circle',
        symbolSize: 6
      },
      {
        name: '南华商品指数(归一化)',
        type: 'line',
        data: commodity,
        smooth: true,
        lineStyle: { color: '#5470c6', width: 3 },
        itemStyle: { color: '#5470c6' },
        symbol: 'circle',
        symbolSize: 6
      }
    ]
  }
})
</script>

<style scoped>
.chart {
  width: 100%;
  height: 100%;
}
</style>
