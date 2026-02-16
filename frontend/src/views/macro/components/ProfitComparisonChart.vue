<template>
  <v-chart class="chart" :option="chartOption" autoresize />
</template>

<script setup>
import { computed } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart, BarChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent, MarkLineComponent } from 'echarts/components'
import VChart from 'vue-echarts'

use([CanvasRenderer, LineChart, BarChart, GridComponent, TooltipComponent, LegendComponent, MarkLineComponent])

const props = defineProps({
  data: Object
})

const chartOption = computed(() => {
  const { dates, industrial, listed } = props.data
  
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'cross' }
    },
    legend: {
      data: ['工业企业利润', '全A净利润'],
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
      name: '同比(%)',
      axisLabel: { formatter: '{value}%' }
    },
    series: [
      {
        name: '工业企业利润',
        type: 'line',
        data: industrial,
        smooth: true,
        lineStyle: { color: '#5470c6', width: 3 },
        itemStyle: { color: '#5470c6' }
      },
      {
        name: '全A净利润',
        type: 'line',
        data: listed,
        smooth: true,
        lineStyle: { color: '#91cc75', width: 3 },
        itemStyle: { color: '#91cc75' }
      },
      {
        name: '偏离度',
        type: 'bar',
        data: industrial.map((v, i) => parseFloat((v - listed[i]).toFixed(2))),
        barWidth: '30%',
        itemStyle: {
          color: (params) => {
            const val = params.value
            return val > 2 ? '#fa8c16' : val < -2 ? '#f5222d' : '#52c41a'
          },
          opacity: 0.6
        }
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
