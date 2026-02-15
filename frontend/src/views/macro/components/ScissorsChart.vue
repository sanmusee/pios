<template>
  <v-chart class="chart" :option="chartOption" autoresize />
</template>

<script setup>
import { computed } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent, MarkLineComponent } from 'echarts/components'
import VChart from 'vue-echarts'

use([CanvasRenderer, BarChart, GridComponent, TooltipComponent, LegendComponent, MarkLineComponent])

const props = defineProps({
  data: Object
})

const chartOption = computed(() => ({
  tooltip: {
    trigger: 'axis',
    formatter: '{b}<br/>M1-M2: {c}%'
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
    name: 'M1-M2剪刀差',
    type: 'bar',
    data: props.data.value,
    barWidth: '50%',
    itemStyle: {
      color: (params) => params.value >= 0 ? '#91cc75' : '#ee6666'
    },
    markLine: {
      silent: true,
      data: [{
        yAxis: 0,
        lineStyle: { color: '#333', type: 'solid', width: 2 },
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
