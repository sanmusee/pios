<template>
  <v-chart class="chart" :option="chartOption" autoresize />
</template>

<script setup>
import { computed } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent, MarkLineComponent, MarkAreaComponent } from 'echarts/components'
import VChart from 'vue-echarts'

use([CanvasRenderer, LineChart, GridComponent, TooltipComponent, LegendComponent, MarkLineComponent, MarkAreaComponent])

const props = defineProps({
  data: Object
})

const chartOption = computed(() => {
  const { dates, erp, mean, plus1sigma, plus2sigma, minus1sigma, minus2sigma } = props.data
  
  return {
    tooltip: {
      trigger: 'axis',
      formatter: '{b}<br/>ERP: {c}%'
    },
    legend: {
      data: ['ERP', '均值', '+1σ', '+2σ', '-1σ', '-2σ'],
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
      data: dates,
      axisLabel: { fontSize: 11 }
    },
    yAxis: {
      type: 'value',
      name: 'ERP(%)',
      min: 3,
      max: 9,
      axisLabel: { formatter: '{value}%' }
    },
    series: [
      {
        name: 'ERP',
        type: 'line',
        data: erp,
        smooth: true,
        lineStyle: { color: '#5470c6', width: 3 },
        itemStyle: { color: '#5470c6' },
        markArea: {
          silent: true,
          data: [
            [
              { yAxis: plus2sigma, itemStyle: { color: 'rgba(82, 196, 26, 0.1)' } },
              { yAxis: plus1sigma }
            ],
            [
              { yAxis: plus1sigma, itemStyle: { color: 'rgba(82, 196, 26, 0.05)' } },
              { yAxis: mean }
            ],
            [
              { yAxis: mean, itemStyle: { color: 'rgba(250, 140, 22, 0.05)' } },
              { yAxis: minus1sigma }
            ],
            [
              { yAxis: minus1sigma, itemStyle: { color: 'rgba(245, 34, 45, 0.1)' } },
              { yAxis: minus2sigma }
            ]
          ]
        }
      },
      {
        name: '均值',
        type: 'line',
        data: dates.map(() => mean),
        lineStyle: { color: '#999', type: 'dashed', width: 1 },
        symbol: 'none'
      },
      {
        name: '+1σ',
        type: 'line',
        data: dates.map(() => plus1sigma),
        lineStyle: { color: '#52c41a', type: 'dashed', width: 1 },
        symbol: 'none'
      },
      {
        name: '+2σ',
        type: 'line',
        data: dates.map(() => plus2sigma),
        lineStyle: { color: '#52c41a', type: 'dashed', width: 2 },
        symbol: 'none'
      },
      {
        name: '-1σ',
        type: 'line',
        data: dates.map(() => minus1sigma),
        lineStyle: { color: '#f5222d', type: 'dashed', width: 1 },
        symbol: 'none'
      },
      {
        name: '-2σ',
        type: 'line',
        data: dates.map(() => minus2sigma),
        lineStyle: { color: '#f5222d', type: 'dashed', width: 2 },
        symbol: 'none'
      }
    ]
  }
})
</script>

<style scoped>
.chart {
  width: 100%;
  height: 100%;
}
</style>
