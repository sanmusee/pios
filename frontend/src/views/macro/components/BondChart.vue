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
    trigger: 'axis',
    formatter: '{b}<br/>10Y国债: {c}%'
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    top: '10%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    data: props.data.dates,
    axisLabel: { fontSize: 11 }
  },
  yAxis: {
    type: 'value',
    name: '%',
    min: 2.0,
    max: 2.8,
    axisLabel: { formatter: '{value}%' }
  },
  series: [{
    name: '10Y国债',
    type: 'line',
    data: props.data.value,
    smooth: true,
    lineStyle: { color: '#fac858', width: 3 },
    itemStyle: { color: '#fac858' },
    areaStyle: {
      color: {
        type: 'linear',
        x: 0, y: 0, x2: 0, y2: 1,
        colorStops: [
          { offset: 0, color: 'rgba(250, 200, 88, 0.4)' },
          { offset: 1, color: 'rgba(250, 200, 88, 0.05)' }
        ]
      }
    }
  }]
}))
</script>

<style scoped>
.chart {
  width: 100%;
  height: 100%;
}
</style>
