<template>
  <v-chart class="outcome-chart" :option="chartOption" autoresize />
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
  const { dates, cpi, ppi, pmi } = props.data
  
  // 计算CPI/PPI范围用于对齐PMI轴
  const cpiMin = Math.min(...cpi)
  const cpiMax = Math.max(...cpi)
  const ppiMin = Math.min(...ppi)
  const ppiMax = Math.max(...ppi)
  const leftMin = Math.min(cpiMin, ppiMin)
  const leftMax = Math.max(cpiMax, ppiMax)
  const leftRange = leftMax - leftMin
  
  // PMI轴范围设为45-55，对应左侧的百分比范围
  const pmiMin = 45
  const pmiMax = 55
  
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'cross' }
    },
    legend: {
      data: ['CPI 同比', 'PPI 同比', 'PMI'],
      bottom: 0
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '15%',
      top: '15%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: dates,
      axisLabel: { rotate: 45 }
    },
    yAxis: [
      {
        type: 'value',
        name: 'CPI/PPI (%)',
        position: 'left',
        min: leftMin - leftRange * 0.1,
        max: leftMax + leftRange * 0.1,
        axisLabel: { formatter: '{value}%' }
      },
      {
        type: 'value',
        name: 'PMI',
        position: 'right',
        min: pmiMin,
        max: pmiMax
      }
    ],
    series: [
      {
        name: 'CPI 同比',
        type: 'line',
        data: cpi,
        smooth: true,
        lineStyle: { color: '#f5576c', width: 3 },
        itemStyle: { color: '#f5576c' },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [
              { offset: 0, color: 'rgba(245, 87, 108, 0.4)' },
              { offset: 1, color: 'rgba(245, 87, 108, 0.05)' }
            ]
          }
        }
      },
      {
        name: 'PPI 同比',
        type: 'line',
        data: ppi,
        smooth: true,
        lineStyle: { color: '#4facfe', width: 3 },
        itemStyle: { color: '#4facfe' },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [
              { offset: 0, color: 'rgba(79, 172, 254, 0.4)' },
              { offset: 1, color: 'rgba(79, 172, 254, 0.05)' }
            ]
          }
        }
      },
      {
        name: 'PMI',
        type: 'line',
        yAxisIndex: 1,
        data: pmi,
        smooth: true,
        lineStyle: { color: '#f093fb', width: 2 },
        itemStyle: { color: '#f093fb' },
        markLine: {
          silent: true,
          symbol: 'none',
          data: [
            {
              yAxis: 50,
              lineStyle: { 
                color: '#ff0000', 
                type: 'solid', 
                width: 3 
              },
              label: { 
                formatter: '荣枯线 50', 
                position: 'end',
                color: '#ff0000',
                fontWeight: 'bold'
              }
            }
          ]
        }
      }
    ]
  }
})
</script>

<style scoped>
.outcome-chart {
  width: 100%;
  height: 100%;
}
</style>
