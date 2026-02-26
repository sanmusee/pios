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

const chartOption = computed(() => {
  const { dates, fcf, dividend, payout } = props.data
  
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'cross' }
    },
    legend: {
      data: ['自由现金流', '分红金额', '分红率'],
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
    yAxis: [
      {
        type: 'value',
        name: '金额(亿)',
        position: 'left'
      },
      {
        type: 'value',
        name: '分红率(%)',
        position: 'right',
        axisLabel: { formatter: '{value}%' }
      }
    ],
    series: [
      {
        name: '自由现金流',
        type: 'bar',
        data: fcf,
        barWidth: '30%',
        itemStyle: { color: '#5470c6' }
      },
      {
        name: '分红金额',
        type: 'bar',
        data: dividend,
        barWidth: '30%',
        itemStyle: { color: '#91cc75' }
      },
      {
        name: '分红率',
        type: 'line',
        yAxisIndex: 1,
        data: payout,
        smooth: true,
        lineStyle: { color: '#fac858', width: 3 },
        itemStyle: { color: '#fac858' }
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
