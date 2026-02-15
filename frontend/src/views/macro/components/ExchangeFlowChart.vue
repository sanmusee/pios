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

const chartOption = computed(() => {
  const { dates, cnh, northBound } = props.data
  
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'cross' }
    },
    legend: {
      data: ['USD/CNH', '北向资金累计'],
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
        name: '汇率',
        position: 'left',
        min: 7.0,
        max: 7.5,
        axisLabel: { formatter: '{value}' }
      },
      {
        type: 'value',
        name: '累计净流入(亿)',
        position: 'right'
      }
    ],
    series: [
      {
        name: 'USD/CNH',
        type: 'line',
        data: cnh,
        smooth: true,
        lineStyle: { color: '#ee6666', width: 3 },
        itemStyle: { color: '#ee6666' },
        markLine: {
          silent: true,
          data: [
            {
              yAxis: 7.3,
              lineStyle: { color: '#ff6b6b', type: 'dashed', width: 2 },
              label: { formatter: '关键位7.3', position: 'end' }
            }
          ]
        }
      },
      {
        name: '北向资金累计',
        type: 'line',
        yAxisIndex: 1,
        data: northBound,
        smooth: true,
        lineStyle: { color: '#5470c6', width: 3 },
        itemStyle: { color: '#5470c6' },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [
              { offset: 0, color: 'rgba(84, 112, 198, 0.4)' },
              { offset: 1, color: 'rgba(84, 112, 198, 0.05)' }
            ]
          }
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
