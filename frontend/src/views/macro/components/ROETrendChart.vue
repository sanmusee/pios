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

const chartOption = computed(() => {
  const { dates, roe, margin, turnover, leverage } = props.data
  
  return {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['ROE', '净利率', '周转率', '杠杆率'],
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
        name: 'ROE/净利率(%)',
        position: 'left',
        axisLabel: { formatter: '{value}%' }
      },
      {
        type: 'value',
        name: '周转率/杠杆率',
        position: 'right'
      }
    ],
    series: [
      {
        name: 'ROE',
        type: 'line',
        data: roe,
        smooth: true,
        lineStyle: { color: '#5470c6', width: 4 },
        itemStyle: { color: '#5470c6' }
      },
      {
        name: '净利率',
        type: 'line',
        data: margin,
        smooth: true,
        lineStyle: { color: '#91cc75', width: 2 },
        itemStyle: { color: '#91cc75' }
      },
      {
        name: '周转率',
        type: 'line',
        yAxisIndex: 1,
        data: turnover,
        smooth: true,
        lineStyle: { color: '#fac858', width: 2 },
        itemStyle: { color: '#fac858' }
      },
      {
        name: '杠杆率',
        type: 'line',
        yAxisIndex: 1,
        data: leverage,
        smooth: true,
        lineStyle: { color: '#ee6666', width: 2 },
        itemStyle: { color: '#ee6666' }
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
