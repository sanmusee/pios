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

const chartOption = computed(() => ({
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    data: ['沪深300', '创业板'],
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
    data: props.data.dates,
    axisLabel: { fontSize: 11 }
  },
  yAxis: [
    {
      type: 'value',
      name: '沪深300',
      position: 'left'
    },
    {
      type: 'value',
      name: '创业板',
      position: 'right'
    }
  ],
  series: [
    {
      name: '沪深300',
      type: 'line',
      data: props.data.hs300,
      smooth: true,
      lineStyle: { color: '#5470c6', width: 3 },
      itemStyle: { color: '#5470c6' }
    },
    {
      name: '创业板',
      type: 'line',
      yAxisIndex: 1,
      data: props.data.cyb,
      smooth: true,
      lineStyle: { color: '#91cc75', width: 3 },
      itemStyle: { color: '#91cc75' }
    }
  ]
}))
</script>

<style scoped>
.chart {
  width: 100%;
  height: 100%;
}
</style>
