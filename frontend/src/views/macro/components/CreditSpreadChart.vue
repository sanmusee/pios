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
    formatter: '{b}<br/>信用利差: {c}bp'
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
    name: 'bp'
  },
  series: [{
    name: '信用利差',
    type: 'line',
    data: props.data.value,
    smooth: true,
    lineStyle: { color: '#9a60b4', width: 3 },
    itemStyle: { color: '#9a60b4' },
    areaStyle: {
      color: {
        type: 'linear',
        x: 0, y: 0, x2: 0, y2: 1,
        colorStops: [
          { offset: 0, color: 'rgba(154, 96, 180, 0.4)' },
          { offset: 1, color: 'rgba(154, 96, 180, 0.05)' }
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
