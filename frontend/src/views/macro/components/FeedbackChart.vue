<template>
  <v-chart class="feedback-chart" :option="chartOption" autoresize />
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
  const { dates, lpr1y, lpr5y, dr007 } = props.data
  
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'cross' }
    },
    legend: {
      data: ['1年期 LPR', '5年期 LPR', 'DR007'],
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
      axisLabel: { rotate: 45 }
    },
    yAxis: {
      type: 'value',
      name: '利率 (%)',
      min: 1.5,
      max: 4.5,
      axisLabel: { formatter: '{value}%' }
    },
    series: [
      {
        name: '1年期 LPR',
        type: 'line',
        data: lpr1y,
        step: 'end',
        lineStyle: { color: '#5470c6', width: 3 },
        itemStyle: { color: '#5470c6' }
      },
      {
        name: '5年期 LPR',
        type: 'line',
        data: lpr5y,
        step: 'end',
        lineStyle: { color: '#91cc75', width: 3 },
        itemStyle: { color: '#91cc75' }
      },
      {
        name: 'DR007',
        type: 'line',
        data: dr007,
        smooth: true,
        lineStyle: { color: '#fac858', width: 2, type: 'dashed' },
        itemStyle: { color: '#fac858' },
        markLine: {
          silent: true,
          data: [
            {
              yAxis: 1.8,
              lineStyle: { color: '#ff6b6b', type: 'dotted', width: 2 },
              label: { formatter: '7天逆回购利率', position: 'end' }
            }
          ]
        }
      }
    ]
  }
})
</script>

<style scoped>
.feedback-chart {
  width: 100%;
  height: 100%;
}
</style>
