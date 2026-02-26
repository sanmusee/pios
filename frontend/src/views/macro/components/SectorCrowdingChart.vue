<template>
  <v-chart class="chart" :option="chartOption" autoresize />
</template>

<script setup>
import { computed } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent } from 'echarts/components'
import VChart from 'vue-echarts'

use([CanvasRenderer, BarChart, GridComponent, TooltipComponent, LegendComponent])

const props = defineProps({
  data: Object
})

const chartOption = computed(() => {
  const { sectors, ratio, historyMax } = props.data
  
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' },
      formatter: function(params) {
        const current = params[0].value
        const max = params[1].value
        const percent = ((current / max) * 100).toFixed(1)
        return `${params[0].axisValue}<br/>` +
               `${params[0].marker} 当前占比: ${current}%<br/>` +
               `${params[1].marker} 历史峰值: ${max}%<br/>` +
               `拥挤度: ${percent}%`
      }
    },
    legend: {
      data: ['当前成交占比', '历史峰值'],
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
      data: sectors,
      axisLabel: { 
        fontSize: 11,
        interval: 0,
        rotate: 30
      }
    },
    yAxis: {
      type: 'value',
      name: '成交占比(%)',
      axisLabel: { formatter: '{value}%' }
    },
    series: [
      {
        name: '当前成交占比',
        type: 'bar',
        data: ratio,
        barWidth: '40%',
        itemStyle: {
          color: function(params) {
            const val = params.value
            const max = historyMax[params.dataIndex]
            const percent = val / max
            if (percent > 0.85) return '#f5222d'
            if (percent > 0.7) return '#fa8c16'
            return '#52c41a'
          },
          borderRadius: [4, 4, 0, 0]
        }
      },
      {
        name: '历史峰值',
        type: 'bar',
        data: historyMax,
        barWidth: '40%',
        barGap: '-100%',
        itemStyle: {
          color: 'rgba(200, 200, 200, 0.3)',
          borderRadius: [4, 4, 0, 0]
        },
        z: -1
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