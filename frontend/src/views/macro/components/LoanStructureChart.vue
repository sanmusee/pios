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
  legend: { data: ['房贷', '消费贷', '其他'], bottom: 0 },
  grid: { left: '3%', right: '4%', bottom: '15%', top: '10%', containLabel: true },
  xAxis: { type: 'category', data: props.data.dates, axisLabel: { fontSize: 11 } },
  yAxis: { type: 'value', name: '占比(%)', axisLabel: { formatter: '{value}%' } },
  series: [
    { name: '房贷', type: 'line', data: props.data.mortgage, smooth: true, lineStyle: { color: '#5470c6', width: 2 }, itemStyle: { color: '#5470c6' }, areaStyle: { color: { type: 'linear', x: 0, y: 0, x2: 0, y2: 1, colorStops: [{ offset: 0, color: 'rgba(84, 112, 198, 0.3)' }, { offset: 1, color: 'rgba(84, 112, 198, 0.05)' }] } } },
    { name: '消费贷', type: 'line', data: props.data.consumer, smooth: true, lineStyle: { color: '#91cc75', width: 2 }, itemStyle: { color: '#91cc75' } },
    { name: '其他', type: 'line', data: props.data.other, smooth: true, lineStyle: { color: '#fac858', width: 2 }, itemStyle: { color: '#fac858' } }
  ]
}))
</script>

<style scoped>.chart { width: 100%; height: 100%; }</style>
