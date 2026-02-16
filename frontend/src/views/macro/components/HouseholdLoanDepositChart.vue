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
  tooltip: { trigger: 'axis' },
  legend: { data: ['存款增速', '贷款增速'], bottom: 0 },
  grid: { left: '3%', right: '4%', bottom: '15%', top: '10%', containLabel: true },
  xAxis: { type: 'category', data: props.data.dates, axisLabel: { fontSize: 11 } },
  yAxis: { type: 'value', name: '同比(%)', axisLabel: { formatter: '{value}%' } },
  series: [
    { name: '存款增速', type: 'line', data: props.data.deposit, smooth: true, lineStyle: { color: '#5470c6', width: 3 }, itemStyle: { color: '#5470c6' } },
    { name: '贷款增速', type: 'line', data: props.data.loan, smooth: true, lineStyle: { color: '#91cc75', width: 3 }, itemStyle: { color: '#91cc75' } }
  ]
}))
</script>

<style scoped>.chart { width: 100%; height: 100%; }</style>
