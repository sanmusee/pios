<template>
  <div class="stock-dashboard">
    <!-- 顶部统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ stats.totalStocks }}</div>
          <div class="stat-label">股票总数</div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ stats.buySignals }}</div>
          <div class="stat-label">买入信号</div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ stats.hotIndustries }}</div>
          <div class="stat-label">热门行业</div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ stats.avgScore }}</div>
          <div class="stat-label">平均评分</div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 推荐股票和热门行业 -->
    <el-row :gutter="20" class="content-row">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>推荐股票</span>
              <el-button type="primary" size="small" @click="$router.push('/stock/screener')">
                去选股
              </el-button>
            </div>
          </template>
          
          <el-table :data="recommendations" style="width: 100%">
            <el-table-column prop="code" label="代码" />
            <el-table-column prop="name" label="名称" />
            <el-table-column prop="totalScore" label="综合评分">
              <template #default="{ row }">
                <el-tag :type="getScoreTagType(row.totalScore)">
                  {{ row.totalScore?.toFixed(2) }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>热门行业</span>
          </template>
          
          <el-table :data="hotIndustries" style="width: 100%">
            <el-table-column prop="industryName" label="行业" />
            <el-table-column prop="totalScore" label="综合评分">
              <template #default="{ row }">
                <el-tag :type="getScoreTagType(row.totalScore)">
                  {{ row.totalScore?.toFixed(2) }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 最新交易信号 -->
    <el-row :gutter="20" class="content-row">
      <el-col :span="24">
        <el-card>
          <template #header>
            <span>最新交易信号</span>
          </template>
          
          <el-timeline>
            <el-timeline-item
              v-for="signal in tradeSignals"
              :key="signal.id"
              :type="signal.signalType === 'BUY' ? 'success' : 'danger'"
              :timestamp="formatDate(signal.signalDate)"
            >
              <div class="signal-item">
                <span class="signal-code">{{ signal.code }}</span>
                <el-tag :type="signal.signalType === 'BUY' ? 'success' : 'danger'">
                  {{ signal.signalType }}
                </el-tag>
                <span class="signal-score">评分: {{ signal.totalScoreSnapshot?.toFixed(2) }}</span>
                <span class="signal-reason">{{ signal.reason }}</span>
              </div>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

const stats = ref({
  totalStocks: 0,
  buySignals: 0,
  hotIndustries: 0,
  avgScore: 0
})

const recommendations = ref([])
const hotIndustries = ref([])
const tradeSignals = ref([])

const fetchDashboardData = async () => {
  try {
    // 获取推荐股票
    const recRes = await fetch('/api/v1/stock/recommendations?limit=10')
    const recResult = await recRes.json()
    if (recResult.code === 200) {
      recommendations.value = recResult.data
    }
    
    // 获取热门行业
    const indRes = await fetch('/api/v1/industry/hot?limit=10')
    const indResult = await indRes.json()
    if (indResult.code === 200) {
      hotIndustries.value = indResult.data
    }
    
    // 获取交易信号
    const sigRes = await fetch('/api/v1/signals/buy?limit=10')
    const sigResult = await sigRes.json()
    if (sigResult.code === 200) {
      tradeSignals.value = sigResult.data
    }
    
    // 更新统计数据
    stats.value = {
      totalStocks: 5000,
      buySignals: tradeSignals.value.length,
      hotIndustries: hotIndustries.value.length,
      avgScore: 75.5
    }
  } catch (error) {
    ElMessage.error('获取数据失败')
  }
}

const getScoreTagType = (score) => {
  if (score >= 80) return 'success'
  if (score >= 60) return 'warning'
  return 'danger'
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  return new Date(dateStr).toLocaleDateString('zh-CN')
}

onMounted(() => {
  fetchDashboardData()
})
</script>

<style scoped>
.stock-dashboard {
  padding: 20px;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  text-align: center;
}

.stat-value {
  font-size: 36px;
  font-weight: bold;
  color: #409eff;
}

.stat-label {
  margin-top: 10px;
  color: #666;
}

.content-row {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.signal-item {
  display: flex;
  align-items: center;
  gap: 15px;
}

.signal-code {
  font-weight: bold;
  font-size: 16px;
}

.signal-score {
  color: #666;
}

.signal-reason {
  color: #999;
  flex: 1;
}
</style>
