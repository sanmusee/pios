<template>
  <v-chart class="chart" :option="chartOption" autoresize />
</template>

<script setup>
import { computed } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart, BarChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent } from 'echarts/components'
import VChart from 'vue-echarts'

use([CanvasRenderer, LineChart, BarChart, GridComponent, TooltipComponent, LegendComponent])

const props = defineProps({
  data: Object
})

const chartOption = computed(() => ({
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    data: ['人民币汇率', '北向资金'],
    bottom: 0
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '15%',
    top: '10%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    data: props.data.dates,
    axisLabel: { fontSize: 11 }
  },
  yAxis: [
    {
      type: 'value',
      name: '汇率',
      position: 'left',
      min: 7.0,
      max: 7.5,
      axisLabel: { formatter: '{value}' }
    },
    {
      type: 'value',
      name: '北向(亿)',
      position: 'right',
      axisLabel: { formatter: '{value}' }
    }
  ],
  series: [
    {
      name: '人民币汇率',
      type: 'line',
      data: props.data.exchangeRate,
      smooth: true,
      lineStyle: { color: '#5470c6', width: 3 },
      itemStyle: { color: '#5470c6' }
    },
    {
      name: '北向资金',
      type: 'bar',
      yAxisIndex: 1,
      data: props.data.northBound,
      barWidth: '40%',
      itemStyle: {
        color: (params) => params.value >= 0 ? '#91cc75' : '#ee6666'
      }
    }
  ]
}))
</script>

<style scoped>
.chart {
  width: 100%;
  height: 100%;
}
</style>
