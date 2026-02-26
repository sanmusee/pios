<template>
  <v-chart class="chart" :option="chartOption" autoresize />
</template>

<script setup>
import { computed } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent, MarkLineComponent } from 'echarts/components'
import VChart from 'vue-echarts'

use([CanvasRenderer, BarChart, GridComponent, TooltipComponent, LegendComponent, MarkLineComponent])

const props = defineProps({
  data: Object
})

const chartOption = computed(() => {
  const { dates, newOrders, inventory, diff } = props.data
  
  return {
    tooltip: {
      trigger: 'axis',
      formatter: function(params) {
        let result = params[0].name + '<br/>'
        params.forEach(item => {
          if (item.seriesName === '库存差') {
            result += item.marker + ' ' + item.seriesName + ': ' + item.value + '<br/>'
          } else {
            result += item.marker + ' ' + item.seriesName + ': ' + item.value + '<br/>'
          }
        })
        return result
      }
    },
    legend: {
      data: ['新订单', '产成品库存', '库存差'],
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
        name: '指数',
        position: 'left',
        min: 40,
        max: 60
      },
      {
        type: 'value',
        name: '差值',
        position: 'right'
      }
    ],
    series: [
      {
        name: '新订单',
        type: 'bar',
        data: newOrders,
        barWidth: '30%',
        itemStyle: { color: '#5470c6', opacity: 0.7 }
      },
      {
        name: '产成品库存',
        type: 'bar',
        data: inventory,
        barWidth: '30%',
        itemStyle: { color: '#ee6666', opacity: 0.7 }
      },
      {
        name: '库存差',
        type: 'bar',
        yAxisIndex: 1,
        data: diff,
        barWidth: '20%',
        itemStyle: {
          color: (params) => params.value >= 0 ? '#91cc75' : '#fac858'
        },
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
