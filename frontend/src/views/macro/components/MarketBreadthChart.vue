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
  const { dates, ma20, ma60 } = props.data
  
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'cross' },
      formatter: function(params) {
        let html = params[0].axisValue + '<br/>'
        params.forEach(p => {
          html += `${p.marker} ${p.seriesName}: ${p.value}%<br/>`
        })
        return html
      }
    },
    legend: {
      data: ['MA20占比', 'MA60占比'],
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
      name: '占比(%)',
      min: 0,
      max: 100,
      axisLabel: { formatter: '{value}%' }
    },
    series: [
      {
        name: 'MA20占比',
        type: 'line',
        data: ma20,
        smooth: true,
        lineStyle: { color: '#5470c6', width: 3 },
        itemStyle: { color: '#5470c6' },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [
              { offset: 0, color: 'rgba(84, 112, 198, 0.3)' },
              { offset: 1, color: 'rgba(84, 112, 198, 0.05)' }
            ]
          }
        },
        markLine: {
          silent: true,
          data: [
            { yAxis: 20, lineStyle: { color: '#52c41a', type: 'dashed' }, label: { formatter: '冰点线 20%' } },
            { yAxis: 80, lineStyle: { color: '#f5222d', type: 'dashed' }, label: { formatter: '过热线 80%' } }
          ]
        }
      },
      {
        name: 'MA60占比',
        type: 'line',
        data: ma60,
        smooth: true,
        lineStyle: { color: '#91cc75', width: 2 },
        itemStyle: { color: '#91cc75' }
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