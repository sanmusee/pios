<template>
  <v-chart class="chart" :option="chartOption" autoresize />
</template>

<script setup>
import { computed } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent, MarkLineComponent } from 'echarts/components'
import VChart from 'vue-echarts'

use([CanvasRenderer, LineChart, GridComponent, TooltipComponent, LegendComponent, MarkLineComponent])

const props = defineProps({
  data: Object
})

const chartOption = computed(() => ({
  tooltip: { trigger: 'axis', formatter: '{b}<br/>储蓄率: {c}%' },
  grid: { left: '3%', right: '4%', bottom: '10%', top: '10%', containLabel: true },
  xAxis: { type: 'category', data: props.data.dates, axisLabel: { fontSize: 11 } },
  yAxis: { type: 'value', name: '储蓄率(%)', min: 30, max: 40, axisLabel: { formatter: '{value}%' } },
  series: [{
    name: '储蓄率',
    type: 'line',
    data: props.data.rate,
    smooth: true,
    lineStyle: { color: '#5470c6', width: 3 },
    itemStyle: { color: '#5470c6' },
    areaStyle: { color: { type: 'linear', x: 0, y: 0, x2: 0, y2: 1, colorStops: [{ offset: 0, color: 'rgba(84, 112, 198, 0.4)' }, { offset: 1, color: 'rgba(84, 112, 198, 0.05)' }] } },
    markLine: { silent: true, data: [{ yAxis: 35, lineStyle: { color: '#fa8c16', type: 'dashed' }, label: { formatter: '均值35%', position: 'insideEndTop' } }] }
  }]
}))
</script>

<style scoped>.chart { width: 100%; height: 100%; }</style>
