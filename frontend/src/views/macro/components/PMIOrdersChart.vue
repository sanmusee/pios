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

const chartOption = computed(() => ({
  tooltip: {
    trigger: 'axis',
    formatter: '{b}<br/>新订单: {c}'
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    top: '10%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    data: props.data.dates,
    axisLabel: { fontSize: 11 }
  },
  yAxis: {
    type: 'value',
    min: 45,
    max: 55
  },
  series: [{
    name: 'PMI新订单',
    type: 'line',
    data: props.data.value,
    smooth: true,
    lineStyle: { color: '#fac858', width: 3 },
    itemStyle: { color: '#fac858' },
    markLine: {
      silent: true,
      data: [{
        yAxis: 50,
        lineStyle: { color: '#ff0000', type: 'solid', width: 3 },
        label: { formatter: '荣枯线50', position: 'end', color: '#ff0000', fontWeight: 'bold' }
      }]
    }
  }]
}))
</script>

<style scoped>
.chart {
  width: 100%;
  height: 100%;
}
</style>
