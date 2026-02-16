<template>
  <v-chart class="chart" :option="chartOption" autoresize />
</template>

<script setup>
import { computed } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart, BarChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent, DataZoomComponent } from 'echarts/components'
import VChart from 'vue-echarts'

use([CanvasRenderer, LineChart, BarChart, GridComponent, TooltipComponent, LegendComponent, DataZoomComponent])

const props = defineProps({
  data: Object
})

const chartOption = computed(() => {
  const { dates, ratio, hs300, cyb } = props.data
  
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'cross' },
      formatter: function(params) {
        let html = params[0].axisValue + '<br/>'
        params.forEach(p => {
          if (p.seriesName === '风格比值') {
            html += `${p.marker} ${p.seriesName}: ${p.value}<br/>`
          } else {
            html += `${p.marker} ${p.seriesName}: ${p.value}点<br/>`
          }
        })
        return html
      }
    },
    legend: {
      data: ['风格比值', '沪深300', '创业板指'],
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
        name: '风格比值',
        position: 'left',
        axisLabel: { formatter: '{value}' }
      },
      {
        type: 'value',
        name: '指数点位',
        position: 'right',
        axisLabel: { formatter: '{value}' }
      }
    ],
    dataZoom: [
      {
        type: 'inside',
        start: 0,
        end: 100
      }
    ],
    series: [
      {
        name: '风格比值',
        type: 'line',
        data: ratio,
        smooth: true,
        lineStyle: { color: '#fa709a', width: 3 },
        itemStyle: { color: '#fa709a' },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [
              { offset: 0, color: 'rgba(250, 112, 154, 0.3)' },
              { offset: 1, color: 'rgba(250, 112, 154, 0.05)' }
            ]
          }
        }
      },
      {
        name: '沪深300',
        type: 'line',
        yAxisIndex: 1,
        data: hs300,
        smooth: true,
        lineStyle: { color: '#5470c6', width: 2 },
        itemStyle: { color: '#5470c6' }
      },
      {
        name: '创业板指',
        type: 'line',
        yAxisIndex: 1,
        data: cyb,
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