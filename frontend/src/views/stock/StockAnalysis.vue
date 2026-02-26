<template>
  <div class="stock-analysis">
    <el-row :gutter="20">
      <!-- 左侧：股票搜索和列表 -->
      <el-col :span="6">
        <el-card class="stock-list-card">
          <template #header>
            <div class="card-header">
              <span>股票列表</span>
              <el-input
                v-model="searchKeyword"
                placeholder="搜索股票代码/名称"
                size="small"
                :prefix-icon="Search"
                clearable
                @input="handleSearch"
              />
            </div>
          </template>
          
          <el-table
            :data="stockList"
            style="width: 100%"
            height="calc(100vh - 250px)"
            highlight-current-row
            @row-click="handleStockSelect"
          >
            <el-table-column prop="code" label="代码" width="80" />
            <el-table-column prop="name" label="名称" />
            <el-table-column prop="exchange" label="交易所" width="70" />
          </el-table>
          
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :total="total"
            layout="prev, pager, next"
            small
            @change="fetchStockList"
          />
        </el-card>
      </el-col>
      
      <!-- 右侧：股票详情 -->
      <el-col :span="18">
        <div v-if="selectedStock" class="stock-detail">
          <!-- 基本信息 -->
          <el-card class="basic-info-card">
            <template #header>
              <div class="stock-header">
                <div class="stock-title">
                  <h2>{{ selectedStock.name }} ({{ selectedStock.code }})</h2>
                  <el-tag :type="selectedStock.isSt ? 'danger' : 'success'">
                    {{ selectedStock.isSt ? 'ST' : '正常' }}
                  </el-tag>
                </div>
                <div class="stock-meta">
                  <span>交易所: {{ selectedStock.exchange }}</span>
                  <span>上市日期: {{ formatDate(selectedStock.listDate) }}</span>
                </div>
              </div>
            </template>
            
            <!-- 综合评分 -->
            <div v-if="stockScore" class="score-section">
              <div class="total-score">
                <el-progress
                  type="dashboard"
                  :percentage="Math.round(stockScore.totalScore)"
                  :color="scoreColors"
                />
                <div class="score-label">综合评分</div>
              </div>
              
              <div class="dimension-scores">
                <div class="dimension-item">
                  <span class="dimension-name">宏观得分</span>
                  <el-progress :percentage="Math.round(stockScore.macroScore || 0)" />
                </div>
                <div class="dimension-item">
                  <span class="dimension-name">行业得分</span>
                  <el-progress :percentage="Math.round(stockScore.industryScore || 0)" />
                </div>
                <div class="dimension-item">
                  <span class="dimension-name">质量得分</span>
                  <el-progress :percentage="Math.round(stockScore.qualityScore || 0)" />
                </div>
                <div class="dimension-item">
                  <span class="dimension-name">估值得分</span>
                  <el-progress :percentage="Math.round(stockScore.valuationScore || 0)" />
                </div>
                <div class="dimension-item">
                  <span class="dimension-name">趋势得分</span>
                  <el-progress :percentage="Math.round(stockScore.trendScore || 0)" />
                </div>
              </div>
            </div>
          </el-card>
          
          <!-- 行情图表 -->
          <el-card class="chart-card">
            <template #header>
              <span>行情走势</span>
            </template>
            <div ref="priceChart" class="chart-container"></div>
          </el-card>
          
          <!-- 财务数据 -->
          <el-card class="financial-card">
            <template #header>
              <span>财务指标</span>
            </template>
            <el-table :data="financialData" style="width: 100%">
              <el-table-column prop="reportDate" label="报告期" width="120">
                <template #default="{ row }">
                  {{ formatDate(row.reportDate) }}
                </template>
              </el-table-column>
              <el-table-column prop="revenue" label="营业收入">
                <template #default="{ row }">
                  {{ formatMoney(row.revenue) }}
                </template>
              </el-table-column>
              <el-table-column prop="netProfit" label="净利润">
                <template #default="{ row }">
                  {{ formatMoney(row.netProfit) }}
                </template>
              </el-table-column>
              <el-table-column prop="roe" label="ROE">
                <template #default="{ row }">
                  {{ formatPercent(row.roe) }}
                </template>
              </el-table-column>
              <el-table-column prop="grossMargin" label="毛利率">
                <template #default="{ row }">
                  {{ formatPercent(row.grossMargin) }}
                </template>
              </el-table-column>
              <el-table-column prop="yoyRevenue" label="营收同比">
                <template #default="{ row }">
                  <span :class="getChangeClass(row.yoyRevenue)">
                    {{ formatPercent(row.yoyRevenue) }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="yoyProfit" label="利润同比">
                <template #default="{ row }">
                  <span :class="getChangeClass(row.yoyProfit)">
                    {{ formatPercent(row.yoyProfit) }}
                  </span>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </div>
        
        <div v-else class="empty-state">
          <el-empty description="请选择一只股票查看详情" />
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { Search } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { ElMessage } from 'element-plus'

const searchKeyword = ref('')
const stockList = ref([])
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)
const selectedStock = ref(null)
const stockScore = ref(null)
const financialData = ref([])
const priceData = ref([])
const priceChart = ref(null)
let chartInstance = null

const scoreColors = [
  { color: '#f56c6c', percentage: 60 },
  { color: '#e6a23c', percentage: 75 },
  { color: '#67c23a', percentage: 90 }
]

const fetchStockList = async () => {
  try {
    const res = await fetch(`/api/v1/stock/list?page=${currentPage.value}&size=${pageSize.value}&keyword=${searchKeyword.value}`)
    const result = await res.json()
    if (result.code === 200) {
      stockList.value = result.data.records
      total.value = result.data.total
    }
  } catch (error) {
    ElMessage.error('获取股票列表失败')
  }
}

const handleSearch = () => {
  currentPage.value = 1
  fetchStockList()
}

const handleStockSelect = async (row) => {
  selectedStock.value = row
  await Promise.all([
    fetchStockScore(row.code),
    fetchFinancialData(row.code),
    fetchPriceData(row.code)
  ])
  nextTick(() => {
    initChart()
  })
}

const fetchStockScore = async (code) => {
  try {
    const res = await fetch(`/api/v1/stock/${code}/score`)
    const result = await res.json()
    if (result.code === 200) {
      stockScore.value = result.data
    }
  } catch (error) {
    console.error('获取评分失败', error)
  }
}

const fetchFinancialData = async (code) => {
  try {
    const res = await fetch(`/api/v1/stock/${code}/financial`)
    const result = await res.json()
    if (result.code === 200) {
      financialData.value = result.data
    }
  } catch (error) {
    console.error('获取财务数据失败', error)
  }
}

const fetchPriceData = async (code) => {
  try {
    const res = await fetch(`/api/v1/stock/${code}/price`)
    const result = await res.json()
    if (result.code === 200) {
      priceData.value = result.data
    }
  } catch (error) {
    console.error('获取行情数据失败', error)
  }
}

const initChart = () => {
  if (!priceChart.value || priceData.value.length === 0) return
  
  if (chartInstance) {
    chartInstance.dispose()
  }
  
  chartInstance = echarts.init(priceChart.value)
  
  const dates = priceData.value.map(item => formatDate(item.tradeDate))
  const prices = priceData.value.map(item => item.closePrice)
  const ma20 = priceData.value.map(item => item.ma20)
  const ma120 = priceData.value.map(item => item.ma120)
  
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['收盘价', 'MA20', 'MA120']
    },
    xAxis: {
      type: 'category',
      data: dates
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '收盘价',
        type: 'line',
        data: prices,
        smooth: true
      },
      {
        name: 'MA20',
        type: 'line',
        data: ma20,
        smooth: true,
        lineStyle: { opacity: 0.5 }
      },
      {
        name: 'MA120',
        type: 'line',
        data: ma120,
        smooth: true,
        lineStyle: { opacity: 0.5 }
      }
    ]
  }
  
  chartInstance.setOption(option)
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN')
}

const formatMoney = (value) => {
  if (!value) return '-'
  if (value >= 100000000) {
    return (value / 100000000).toFixed(2) + '亿'
  }
  if (value >= 10000) {
    return (value / 10000).toFixed(2) + '万'
  }
  return value.toFixed(2)
}

const formatPercent = (value) => {
  if (!value) return '-'
  return (value * 100).toFixed(2) + '%'
}

const getChangeClass = (value) => {
  if (!value) return ''
  return value > 0 ? 'positive' : value < 0 ? 'negative' : ''
}

onMounted(() => {
  fetchStockList()
  window.addEventListener('resize', () => {
    chartInstance?.resize()
  })
})
</script>

<style scoped>
.stock-analysis {
  padding: 20px;
}

.stock-list-card {
  height: calc(100vh - 100px);
}

.card-header {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.stock-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stock-title {
  display: flex;
  align-items: center;
  gap: 10px;
}

.stock-title h2 {
  margin: 0;
}

.stock-meta {
  display: flex;
  gap: 20px;
  color: #666;
}

.score-section {
  display: flex;
  gap: 40px;
  padding: 20px 0;
}

.total-score {
  text-align: center;
}

.score-label {
  margin-top: 10px;
  font-size: 16px;
  font-weight: bold;
}

.dimension-scores {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.dimension-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.dimension-name {
  width: 80px;
  flex-shrink: 0;
}

.chart-container {
  height: 400px;
}

.empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  height: calc(100vh - 100px);
}

.positive {
  color: #f56c6c;
}

.negative {
  color: #67c23a;
}

:deep(.el-card) {
  margin-bottom: 20px;
}
</style>
