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
  const { dates, revision, hs300, cyb } = props.data
  
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'cross' },
      formatter: function(params) {
        let html = params[0].axisValue + '<br/>'
        params.forEach(p => {
          if (p.seriesName === 'EPS变化率') {
            html += `${p.marker} ${p.seriesName}: ${p.value > 0 ? '+' : ''}${p.value}%<br/>`
          } else {
            html += `${p.marker} ${p.seriesName}: ${p.value}%<br/>`
          }
        })
        return html
      }
    },
    legend: {
      data: ['EPS变化率', '沪深300盈利增速', '创业板指盈利增速'],
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
        name: '变化率(%)',
        position: 'left',
        axisLabel: { formatter: '{value}%' }
      },
      {
        type: 'value',
        name: '盈利增速(%)',
        position: 'right',
        axisLabel: { formatter: '{value}%' }
      }
    ],
    series: [
      {
        name: 'EPS变化率',
        type: 'bar',
        data: revision,
        barWidth: '30%',
        itemStyle: {
          color: function(params) {
            return params.value >= 0 ? '#52c41a' : '#f5222d'
          }
        }
      },
      {
        name: '沪深300盈利增速',
        type: 'line',
        yAxisIndex: 1,
        data: hs300,
        smooth: true,
        lineStyle: { color: '#5470c6', width: 2 },
        itemStyle: { color: '#5470c6' }
      },
      {
        name: '创业板指盈利增速',
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