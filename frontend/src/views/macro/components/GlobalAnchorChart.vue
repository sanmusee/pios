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
  const { dates, usBond, cnBond, spread } = props.data
  
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'cross' }
    },
    legend: {
      data: ['10Y美债', '10Y国债', '中美利差'],
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
        name: '收益率(%)',
        position: 'left',
        min: 1.5,
        max: 5.0,
        axisLabel: { formatter: '{value}%' }
      },
      {
        type: 'value',
        name: '利差(%)',
        position: 'right',
        axisLabel: { formatter: '{value}%' }
      }
    ],
    series: [
      {
        name: '10Y美债',
        type: 'line',
        data: usBond,
        smooth: true,
        lineStyle: { color: '#5470c6', width: 3 },
        itemStyle: { color: '#5470c6' }
      },
      {
        name: '10Y国债',
        type: 'line',
        data: cnBond,
        smooth: true,
        lineStyle: { color: '#91cc75', width: 3 },
        itemStyle: { color: '#91cc75' }
      },
      {
        name: '中美利差',
        type: 'bar',
        yAxisIndex: 1,
        data: spread,
        barWidth: '40%',
        itemStyle: {
          color: (params) => params.value < -2.0 ? '#ee6666' : '#fac858'
        },
        markLine: {
          silent: true,
          data: [
            {
              yAxis: -1.5,
              lineStyle: { color: '#ff6b6b', type: 'dashed', width: 2 },
              label: { formatter: '警戒线-150bp', position: 'end' }
            }
          ]
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
