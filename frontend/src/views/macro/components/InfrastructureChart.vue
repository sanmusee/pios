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
  legend: { data: ['交运', '水利', '能源'], bottom: 0 },
  grid: { left: '3%', right: '4%', bottom: '15%', top: '10%', containLabel: true },
  xAxis: { type: 'category', data: props.data.dates, axisLabel: { fontSize: 11 } },
  yAxis: { type: 'value', name: '同比(%)', axisLabel: { formatter: '{value}%' } },
  series: [
    { name: '交运', type: 'line', data: props.data.transport, smooth: true, lineStyle: { color: '#5470c6', width: 2 }, itemStyle: { color: '#5470c6' } },
    { name: '水利', type: 'line', data: props.data.water, smooth: true, lineStyle: { color: '#91cc75', width: 2 }, itemStyle: { color: '#91cc75' } },
    { name: '能源', type: 'line', data: props.data.energy, smooth: true, lineStyle: { color: '#fac858', width: 2 }, itemStyle: { color: '#fac858' } }
  ]
}))
</script>

<style scoped>.chart { width: 100%; height: 100%; }</style>
