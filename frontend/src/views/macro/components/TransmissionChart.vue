<template>
  <v-chart class="transmission-chart" :option="chartOption" autoresize />
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
  const { dates, governmentBond, corporateBond, entrustLoan, shortTermLoan, longTermLoan } = props.data
  
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' }
    },
    legend: {
      data: ['政府债', '企业债', '委托贷款', '短期贷款', '中长期贷款'],
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
      data: dates
    },
    yAxis: {
      type: 'value',
      name: '金额 (亿元)',
      axisLabel: {
        formatter: (value) => value / 1000 + 'k'
      }
    },
    series: [
      {
        name: '政府债',
        type: 'bar',
        stack: 'total',
        data: governmentBond,
        itemStyle: { color: '#5470c6' }
      },
      {
        name: '企业债',
        type: 'bar',
        stack: 'total',
        data: corporateBond,
        itemStyle: { color: '#91cc75' }
      },
      {
        name: '委托贷款',
        type: 'bar',
        stack: 'total',
        data: entrustLoan,
        itemStyle: { color: '#fac858' }
      },
      {
        name: '短期贷款',
        type: 'bar',
        stack: 'total',
        data: shortTermLoan,
        itemStyle: { color: '#ee6666', opacity: 0.7 }
      },
      {
        name: '中长期贷款',
        type: 'bar',
        stack: 'total',
        data: longTermLoan,
        itemStyle: { color: '#ee6666', opacity: 1 }
      }
    ]
  }
})
</script>

<style scoped>
.transmission-chart {
  width: 100%;
  height: 100%;
}
</style>
