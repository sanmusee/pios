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
  tooltip: { trigger: 'axis', formatter: '{b}<br/>杠杆率: {c}%' },
  grid: { left: '3%', right: '4%', bottom: '10%', top: '10%', containLabel: true },
  xAxis: { type: 'category', data: props.data.dates, axisLabel: { fontSize: 11 } },
  yAxis: { type: 'value', name: '杠杆率(%)', min: 60, max: 65, axisLabel: { formatter: '{value}%' } },
  series: [{
    name: '居民杠杆率',
    type: 'line',
    data: props.data.leverage,
    smooth: true,
    lineStyle: { color: '#f5576c', width: 3 },
    itemStyle: { color: '#f5576c' },
    markLine: { silent: true, data: [{ yAxis: 62, lineStyle: { color: '#ff6b6b', type: 'dashed', width: 2 }, label: { formatter: '警戒线62%', position: 'end' } }] }
  }]
}))
</script>

<style scoped>.chart { width: 100%; height: 100%; }</style>
