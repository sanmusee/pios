<template>
  <div class="stock-screener">
    <el-card>
      <template #header>
        <div class="header-content">
          <h2>智能选股器</h2>
          <el-button type="primary" @click="runScreening">开始选股</el-button>
        </div>
      </template>
      
      <!-- 筛选条件 -->
      <div class="filter-section">
        <h3>评分条件</h3>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="filter-item">
              <label>综合评分</label>
              <el-slider v-model="filters.totalScore" range :max="100" />
              <span>{{ filters.totalScore[0] }} - {{ filters.totalScore[1] }}</span>
            </div>
          </el-col>
          
          <el-col :span="8">
            <div class="filter-item">
              <label>行业得分</label>
              <el-slider v-model="filters.industryScore" range :max="100" />
              <span>{{ filters.industryScore[0] }} - {{ filters.industryScore[1] }}</span>
            </div>
          </el-col>
          
          <el-col :span="8">
            <div class="filter-item">
              <label>质量得分</label>
              <el-slider v-model="filters.qualityScore" range :max="100" />
              <span>{{ filters.qualityScore[0] }} - {{ filters.qualityScore[1] }}</span>
            </div>
          </el-col>
        </el-row>
        
        <h3>基本面条件</h3>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="ROE">
              <el-select v-model="filters.roeOperator">
                <el-option label=">" value="gt" />
                <el-option label="<" value="lt" />
              </el-select>
              <el-input-number v-model="filters.roeValue" :precision="2" />
            </el-form-item>
          </el-col>
          
          <el-col :span="8">
            <el-form-item label="净利润同比">
              <el-select v-model="filters.profitGrowthOperator">
                <el-option label=">" value="gt" />
                <el-option label="<" value="lt" />
              </el-select>
              <el-input-number v-model="filters.profitGrowthValue" :precision="2" />
            </el-form-item>
          </el-col>
          
          <el-col :span="8">
            <el-form-item label="PE-TTM">
              <el-select v-model="filters.peOperator">
                <el-option label=">" value="gt" />
                <el-option label="<" value="lt" />
              </el-select>
              <el-input-number v-model="filters.peValue" :precision="2" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <h3>其他条件</h3>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="排除ST">
              <el-switch v-model="filters.excludeSt" />
            </el-form-item>
          </el-col>
          
          <el-col :span="8">
            <el-form-item label="行业">
              <el-select v-model="filters.industryId" clearable>
                <el-option
                  v-for="industry in industries"
                  :key="industry.id"
                  :label="industry.name"
                  :value="industry.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          
          <el-col :span="8">
            <el-form-item label="交易所">
              <el-select v-model="filters.exchange" clearable>
                <el-option label="上海证券交易所" value="SH" />
                <el-option label="深圳证券交易所" value="SZ" />
                <el-option label="北京证券交易所" value="BJ" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </div>
      
      <!-- 筛选结果 -->
      <div v-if="results.length > 0" class="results-section">
        <h3>筛选结果 ({{ results.length }} 只)</h3>
        
        <el-table :data="results" style="width: 100%">
          <el-table-column prop="code" label="代码" />
          <el-table-column prop="name" label="名称" />
          <el-table-column prop="exchange" label="交易所" />
          <el-table-column prop="totalScore" label="综合评分">
            <template #default="{ row }">
              <el-tag :type="getScoreTagType(row.totalScore)">
                {{ row.totalScore?.toFixed(2) }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="industryScore" label="行业得分" />
          
          <el-table-column prop="qualityScore" label="质量得分" />
          
          <el-table-column prop="valuationScore" label="估值得分" />
          
          <el-table-column label="操作">
            <template #default="{ row }">
              <el-button type="primary" size="small" @click="viewDetail(row)">查看</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()

const filters = ref({
  totalScore: [60, 100],
  industryScore: [60, 100],
  qualityScore: [60, 100],
  roeOperator: 'gt',
  roeValue: 0.1,
  profitGrowthOperator: 'gt',
  profitGrowthValue: 0.1,
  peOperator: 'lt',
  peValue: 30,
  excludeSt: true,
  industryId: null,
  exchange: null
})

const industries = ref([])
const results = ref([])

const fetchIndustries = async () => {
  try {
    const res = await fetch('/api/v1/industry/list')
    const result = await res.json()
    if (result.code === 200) {
      industries.value = result.data
    }
  } catch (error) {
    console.error('获取行业列表失败', error)
  }
}

const runScreening = async () => {
  try {
    // 这里应该调用后端筛选接口
    // 暂时使用推荐股票接口作为示例
    const res = await fetch('/api/v1/stock/recommendations?limit=50')
    const result = await res.json()
    if (result.code === 200) {
      results.value = result.data
      ElMessage.success(`筛选完成，找到 ${results.value.length} 只股票`)
    }
  } catch (error) {
    ElMessage.error('筛选失败')
  }
}

const viewDetail = (row) => {
  router.push(`/stock/analysis?code=${row.code}`)
}

const getScoreTagType = (score) => {
  if (score >= 80) return 'success'
  if (score >= 60) return 'warning'
  return 'danger'
}

onMounted(() => {
  fetchIndustries()
})
</script>

<style scoped>
.stock-screener {
  padding: 20px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.filter-section {
  margin-bottom: 30px;
}

.filter-section h3 {
  margin: 20px 0 15px 0;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.filter-item {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.filter-item label {
  font-weight: bold;
}

.results-section {
  margin-top: 30px;
}

.results-section h3 {
  margin-bottom: 15px;
}
</style>
