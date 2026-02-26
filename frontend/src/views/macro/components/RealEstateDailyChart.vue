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

const chartOption = computed(() => ({
  tooltip: {
    trigger: 'axis',
    axisPointer: { type: 'cross' }
  },
  legend: {
    data: ['30城成交面积', '7日移动平均'],
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
    data: props.data.dates,
    axisLabel: { fontSize: 10, rotate: 45 }
  },
  yAxis: [
    {
      type: 'value',
      name: '万㎡',
      position: 'left'
    }
  ],
  dataZoom: [
    {
      type: 'inside',
      start: 70,
      end: 100
    }
  ],
  series: [
    {
      name: '30城成交面积',
      type: 'bar',
      data: props.data.daily,
      barWidth: '60%',
      itemStyle: {
        color: (params) => {
          const val = params.value
          return val > 50 ? '#91cc75' : val > 30 ? '#5470c6' : '#ee6666'
        }
      }
    },
    {
      name: '7日移动平均',
      type: 'line',
      data: props.data.ma7,
      smooth: true,
      lineStyle: { color: '#fac858', width: 3 },
      itemStyle: { color: '#fac858' }
    }
  ]
}))
</script>

<style scoped>
.chart {
  width: 100%;
  height: 100%;
}
</style>
