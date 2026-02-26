<template>
  <div class="strategic-observation-pool">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div>
          <h2>战略观察池</h2>
          <p class="subtitle">只展示 Impact Score ≥ 阈值的高价值信息 | 战略级变量监控中心</p>
        </div>
        <div class="header-stats">
          <div class="threshold-setting">
            <span>Impact阈值：</span>
            <el-slider v-model="impactThreshold" :min="5" :max="10" :step="0.5" style="width: 150px" />
            <span class="threshold-value">{{ impactThreshold }}</span>
          </div>
          <div class="pool-stats">
            <el-tag type="danger" size="large" effect="dark">
              战略池变量: {{ strategicVariables.length }}
            </el-tag>
          </div>
        </div>
      </div>
    </div>

    <!-- 筛选栏 -->
    <div class="filter-bar">
      <div class="filter-group">
        <span class="filter-label">变量归类：</span>
        <el-checkbox-group v-model="selectedCategories" size="small">
          <el-checkbox-button label="policy">政策监管</el-checkbox-button>
          <el-checkbox-button label="capital">资本流向</el-checkbox-button>
          <el-checkbox-button label="industry">行业变革</el-checkbox-button>
          <el-checkbox-button label="technology">技术突破</el-checkbox-button>
          <el-checkbox-button label="supply">供应链</el-checkbox-button>
        </el-checkbox-group>
      </div>
      
      <div class="filter-group">
        <span class="filter-label">确认状态：</span>
        <el-select v-model="confirmationStatus" size="small" style="width: 120px">
          <el-option label="全部" value="all" />
          <el-option label="已确认" value="confirmed" />
          <el-option label="待确认" value="pending" />
        </el-select>
      </div>
      
      <div class="filter-group">
        <span class="filter-label">推演任务：</span>
        <el-select v-model="inferenceStatus" size="small" style="width: 120px">
          <el-option label="全部" value="all" />
          <el-option label="已生成" value="generated" />
          <el-option label="未生成" value="none" />
        </el-select>
      </div>
    </div>

    <!-- 战略变量卡片列表 -->
    <div class="strategic-list">
      <div
        v-for="variable in filteredStrategicVariables"
        :key="variable.id"
        class="strategic-card"
        :class="{ 'confirmed': variable.isConfirmed, 'unconfirmed': !variable.isConfirmed }"
      >
        <div class="card-main">
          <div class="card-header">
            <div class="variable-info">
              <h3 class="variable-name">{{ variable.name }}</h3>
              <div class="variable-tags">
                <el-tag :type="getCategoryType(variable.category)" size="small">
                  {{ getCategoryLabel(variable.category) }}
                </el-tag>
                <el-tag 
                  v-for="industry in variable.industries" 
                  :key="industry"
                  type="info" 
                  size="small"
                  class="industry-tag"
                >
                  {{ industry }}
                </el-tag>
              </div>
            </div>
            
            <div class="impact-badge">
              <div class="impact-value" :class="getImpactClass(variable.impactScore)">
                {{ variable.impactScore }}
              </div>
              <div class="impact-label">Impact Score</div>
            </div>
          </div>

          <div class="card-stats">
            <div class="stat-item">
              <div class="stat-icon">
                <el-icon><Bell /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-value">{{ variable.triggerCount }}次</div>
                <div class="stat-label">触发次数</div>
              </div>
            </div>
            
            <div class="stat-item">
              <div class="stat-icon">
                <el-icon><Timer /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-value">{{ variable.lastTrigger }}</div>
                <div class="stat-label">最近触发</div>
              </div>
            </div>
            
            <div class="stat-item">
              <div class="stat-icon">
                <el-icon><TrendCharts /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-value" :class="variable.trend">
                  {{ variable.trend === 'up' ? '↑ 强化' : variable.trend === 'down' ? '↓ 弱化' : '→ 稳定' }}
                </div>
                <div class="stat-label">趋势</div>
              </div>
            </div>
          </div>
        </div>

        <div class="card-actions">
          <div class="action-status">
            <el-tag v-if="variable.isConfirmed" type="success" effect="dark">
              <Check /> 已人工确认
            </el-tag>
            <el-tag v-else type="warning">
              <Warning /> 待确认
            </el-tag>
          </div>
          
          <div class="action-buttons">
            <el-button 
              v-if="!variable.isConfirmed"
              type="success" 
              size="small"
              @click="confirmVariable(variable)"
            >
              <Check /> 确认
            </el-button>
            
            <el-button
              v-if="!variable.inferenceTask"
              type="primary"
              size="small"
              @click="generateInference(variable)"
            >
              <TrendCharts /> 生成推演
            </el-button>
            
            <el-button
              v-else
              type="info"
              size="small"
              plain
              @click="viewInference(variable)"
            >
              <View /> 查看推演
            </el-button>
            
            <el-button type="danger" size="small" plain @click="removeFromPool(variable)">
              <Delete /> 移出
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <el-empty
      v-if="filteredStrategicVariables.length === 0"
      description="暂无符合条件的战略变量"
    >
      <template #image>
        <el-icon :size="60" color="#909399"><Collection /></el-icon>
      </template>
    </el-empty>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { 
  Bell, 
  Timer, 
  TrendCharts, 
  Check, 
  Warning, 
  Delete, 
  View,
  Collection
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 阈值设置
const impactThreshold = ref(7.5)

// 筛选条件
const selectedCategories = ref(['policy', 'capital', 'industry', 'technology', 'supply'])
const confirmationStatus = ref('all')
const inferenceStatus = ref('all')

// 模拟战略变量数据
const strategicVariables = ref([
  {
    id: 1,
    name: '新能源补贴全面退坡',
    category: 'policy',
    industries: ['新能源汽车', '锂电池'],
    impactScore: 8.5,
    triggerCount: 12,
    lastTrigger: '2小时前',
    trend: 'up',
    isConfirmed: true,
    inferenceTask: { id: 101, status: 'completed' }
  },
  {
    id: 2,
    name: 'AI大模型融资集中化',
    category: 'capital',
    industries: ['人工智能', '云计算'],
    impactScore: 8.2,
    triggerCount: 8,
    lastTrigger: '5小时前',
    trend: 'up',
    isConfirmed: true,
    inferenceTask: null
  },
  {
    id: 3,
    name: '半导体设备进口受限升级',
    category: 'supply',
    industries: ['半导体', '芯片制造'],
    impactScore: 8.8,
    triggerCount: 15,
    lastTrigger: '1小时前',
    trend: 'up',
    isConfirmed: false,
    inferenceTask: null
  },
  {
    id: 4,
    name: '房地产三道红线政策加码',
    category: 'policy',
    industries: ['房地产', '银行'],
    impactScore: 7.8,
    triggerCount: 6,
    lastTrigger: '1天前',
    trend: 'stable',
    isConfirmed: true,
    inferenceTask: { id: 102, status: 'completed' }
  },
  {
    id: 5,
    name: '碳中和路线图更新',
    category: 'policy',
    industries: ['新能源', '环保', '钢铁'],
    impactScore: 7.5,
    triggerCount: 9,
    lastTrigger: '3小时前',
    trend: 'up',
    isConfirmed: false,
    inferenceTask: null
  },
  {
    id: 6,
    name: '储能技术成本大幅下降',
    category: 'technology',
    industries: ['储能', '新能源'],
    impactScore: 8.0,
    triggerCount: 7,
    lastTrigger: '8小时前',
    trend: 'up',
    isConfirmed: true,
    inferenceTask: { id: 103, status: 'in_progress' }
  }
])

// 过滤后的变量
const filteredStrategicVariables = computed(() => {
  return strategicVariables.value.filter(v => {
    // 阈值过滤
    if (v.impactScore < impactThreshold.value) return false
    
    // 类别过滤
    if (!selectedCategories.value.includes(v.category)) return false
    
    // 确认状态过滤
    if (confirmationStatus.value !== 'all') {
      if (confirmationStatus.value === 'confirmed' && !v.isConfirmed) return false
      if (confirmationStatus.value === 'pending' && v.isConfirmed) return false
    }
    
    // 推演任务过滤
    if (inferenceStatus.value !== 'all') {
      const hasTask = v.inferenceTask !== null
      if (inferenceStatus.value === 'generated' && !hasTask) return false
      if (inferenceStatus.value === 'none' && hasTask) return false
    }
    
    return true
  })
})

// 类别标签
const getCategoryType = (category) => {
  const map = {
    policy: 'danger',
    capital: 'warning',
    industry: 'success',
    technology: 'primary',
    supply: 'info'
  }
  return map[category] || 'info'
}

const getCategoryLabel = (category) => {
  const map = {
    policy: '政策监管',
    capital: '资本流向',
    industry: '行业变革',
    technology: '技术突破',
    supply: '供应链'
  }
  return map[category] || category
}

// Impact分数样式
const getImpactClass = (score) => {
  if (score >= 8.5) return 'critical'
  if (score >= 8.0) return 'high'
  if (score >= 7.5) return 'medium'
  return 'low'
}

// 确认变量
const confirmVariable = async (variable) => {
  try {
    await ElMessageBox.confirm(
      `确认将 "${variable.name}" 标记为已确认？`,
      '人工确认',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    variable.isConfirmed = true
    ElMessage.success('已确认该战略变量')
  } catch {
    // 取消
  }
}

// 生成推演任务
const generateInference = (variable) => {
  variable.inferenceTask = { id: Date.now(), status: 'in_progress' }
  ElMessage.success(`已为 "${variable.name}" 生成推演任务`)
}

// 查看推演
const viewInference = (variable) => {
  ElMessage.info(`查看推演任务 #${variable.inferenceTask.id}`)
}

// 移出战略池
const removeFromPool = async (variable) => {
  try {
    await ElMessageBox.confirm(
      `确定将 "${variable.name}" 移出战略观察池？`,
      '移出确认',
      {
        confirmButtonText: '移出',
        cancelButtonText: '取消',
        type: 'danger'
      }
    )
    const index = strategicVariables.value.findIndex(v => v.id === variable.id)
    if (index > -1) {
      strategicVariables.value.splice(index, 1)
      ElMessage.success('已移出战略观察池')
    }
  } catch {
    // 取消
  }
}
</script>

<style scoped>
.strategic-observation-pool {
  padding: 0;
}

.page-header {
  margin-bottom: 20px;
  padding: 20px;
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  border-radius: 8px;
  color: white;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-content h2 {
  margin: 0 0 8px 0;
  font-size: 24px;
}

.subtitle {
  margin: 0;
  opacity: 0.9;
  font-size: 14px;
}

/* 头部统计 */
.header-stats {
  display: flex;
  align-items: center;
  gap: 24px;
}

.threshold-setting {
  display: flex;
  align-items: center;
  gap: 12px;
  background: rgba(255, 255, 255, 0.15);
  padding: 12px 16px;
  border-radius: 8px;
}

.threshold-value {
  font-weight: bold;
  font-size: 18px;
  min-width: 30px;
}

/* 筛选栏 */
.filter-bar {
  background: white;
  padding: 16px 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  flex-wrap: wrap;
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 8px;
}

.filter-label {
  font-size: 13px;
  color: #606266;
  font-weight: 500;
}

/* 战略变量列表 */
.strategic-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.strategic-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  border-left: 4px solid #f5222d;
  transition: all 0.3s ease;
}

.strategic-card:hover {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.strategic-card.confirmed {
  border-left-color: #52c41a;
}

.strategic-card.unconfirmed {
  border-left-color: #faad14;
}

.card-main {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}

.variable-name {
  margin: 0 0 8px 0;
  font-size: 20px;
  color: #303133;
}

.variable-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.industry-tag {
  margin-left: 4px;
}

.impact-badge {
  text-align: center;
  padding: 8px 16px;
  background: #fff1f0;
  border-radius: 8px;
}

.impact-value {
  font-size: 28px;
  font-weight: bold;
  line-height: 1;
}

.impact-value.critical {
  color: #cf1322;
}

.impact-value.high {
  color: #f5222d;
}

.impact-value.medium {
  color: #fa8c16;
}

.impact-value.low {
  color: #52c41a;
}

.impact-label {
  font-size: 11px;
  color: #909399;
  margin-top: 4px;
}

/* 卡片统计 */
.card-stats {
  display: flex;
  gap: 32px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 12px;
}

.stat-icon {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  background: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: #606266;
}

.stat-value {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}

.stat-value.up {
  color: #f5222d;
}

.stat-value.down {
  color: #52c41a;
}

.stat-label {
  font-size: 12px;
  color: #909399;
}

/* 卡片操作区 */
.card-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 20px;
  background: #f8f9fa;
  border-top: 1px solid #ebeef5;
}

.action-buttons {
  display: flex;
  gap: 8px;
}

/* 空状态 */
:deep(.el-empty) {
  padding: 60px 0;
}
</style>