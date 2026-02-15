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
  tooltip: {
    trigger: 'axis',
    formatter: '{b}<br/>中美利差: {c}%'
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
    axisLabel: { formatter: '{value}%' }
  },
  series: [{
    name: '中美利差',
    type: 'line',
    data: props.data.value,
    smooth: true,
    lineStyle: { color: '#ee6666', width: 3 },
    itemStyle: { color: '#ee6666' },
    areaStyle: {
      color: {
        type: 'linear',
        x: 0, y: 0, x2: 0, y2: 1,
        colorStops: [
          { offset: 0, color: 'rgba(238, 102, 102, 0.4)' },
          { offset: 1, color: 'rgba(238, 102, 102, 0.05)' }
        ]
      }
    },
    markLine: {
      silent: true,
      data: [{
        yAxis: 0,
        lineStyle: { color: '#333', type: 'dashed' },
        label: { formatter: '零轴', position: 'end' }
      }]
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
