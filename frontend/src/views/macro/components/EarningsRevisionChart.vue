<template>
  <v-chart class="chart" :option="chartOption" autoresize />
</template>

<script setup>
import { computed } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart, LineChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent, MarkLineComponent } from 'echarts/components'
import VChart from 'vue-echarts'

use([CanvasRenderer, BarChart, LineChart, GridComponent, TooltipComponent, LegendComponent, MarkLineComponent])

const props = defineProps({
  data: Object
})

const chartOption = computed(() => {
  const { dates, upgrade, downgrade, net } = props.data
  
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' }
    },
    legend: {
      data: ['上调', '下调', '净修正'],
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
      name: '家数'
    },
    series: [
      {
        name: '上调',
        type: 'bar',
        stack: 'total',
        data: upgrade,
        barWidth: '40%',
        itemStyle: { color: '#52c41a' }
      },
      {
        name: '下调',
        type: 'bar',
        stack: 'total',
        data: downgrade.map(v => -v),
        barWidth: '40%',
        itemStyle: { color: '#f5222d' }
      },
      {
        name: '净修正',
        type: 'line',
        data: net,
        smooth: true,
        lineStyle: { color: '#5470c6', width: 3 },
        itemStyle: { color: '#5470c6' },
        markLine: {
          silent: true,
          data: [{
            yAxis: 0,
            lineStyle: { color: '#333', type: 'dashed' },
            label: { formatter: '零轴', position: 'end' }
          }]
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
