<template>
  <v-chart class="chart" :option="chartOption" autoresize />
</template>

<script setup>
import { computed } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart, LineChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent } from 'echarts/components'
import VChart from 'vue-echarts'

use([CanvasRenderer, BarChart, LineChart, GridComponent, TooltipComponent, LegendComponent])

const props = defineProps({
  data: Object
})

const chartOption = computed(() => ({
  tooltip: { trigger: 'axis' },
  legend: { data: ['累计发行', '目标额度'], bottom: 0 },
  grid: { left: '3%', right: '4%', bottom: '15%', top: '10%', containLabel: true },
  xAxis: { type: 'category', data: props.data.months, axisLabel: { fontSize: 11 } },
  yAxis: { type: 'value', name: '金额(亿)' },
  series: [
    { name: '累计发行', type: 'bar', data: props.data.issued, barWidth: '50%', itemStyle: { color: '#5470c6' } },
    { name: '目标额度', type: 'line', data: props.data.months.map(() => props.data.target), lineStyle: { color: '#f5576c', type: 'dashed', width: 2 }, itemStyle: { color: '#f5576c' }, symbol: 'none' }
  ]
}))
</script>

<style scoped>.chart { width: 100%; height: 100%; }</style>
