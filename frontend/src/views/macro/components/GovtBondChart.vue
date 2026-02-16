<template>
  <v-chart class="chart" :option="chartOption" autoresize />
</template>

<script setup>
import { computed } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent } from 'echarts/components'
import VChart from 'vue-echarts'

use([CanvasRenderer, BarChart, GridComponent, TooltipComponent, LegendComponent])

const props = defineProps({
  data: Object
})

const chartOption = computed(() => ({
  tooltip: { trigger: 'axis', formatter: '{b}<br/>净融资: {c}亿' },
  grid: { left: '3%', right: '4%', bottom: '10%', top: '10%', containLabel: true },
  xAxis: { type: 'category', data: props.data.dates, axisLabel: { fontSize: 11 } },
  yAxis: { type: 'value', name: '净融资(亿)' },
  series: [{
    name: '政府债净融资',
    type: 'bar',
    data: props.data.netFinancing,
    barWidth: '60%',
    itemStyle: {
      color: (params) => params.value > 5000 ? '#f5576c' : '#5470c6'
    }
  }]
}))
</script>

<style scoped>.chart { width: 100%; height: 100%; }</style>
