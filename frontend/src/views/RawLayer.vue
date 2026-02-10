<template>
  <div class="page-container">
    <h2>信息采集层 (Raw Layer)</h2>
    
    <!-- 数据源配置卡片 -->
    <el-card class="mt-20">
      <template #header>
        <div class="card-header">
          <span>数据源配置</span>
          <div>
            <el-button type="success" size="small" @click="triggerCollectAll" :loading="collecting">
              立即采集
            </el-button>
            <el-button type="primary" size="small">添加数据源</el-button>
          </div>
        </div>
      </template>
      
      <el-table :data="dataSources" style="width: 100%" v-loading="loadingSources">
        <el-table-column prop="name" label="数据源名称" />
        <el-table-column prop="type" label="类型" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'ACTIVE' ? 'success' : 'info'">
              {{ scope.row.status === 'ACTIVE' ? '运行中' : '已停止' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastCollectTime" label="最后采集时间">
          <template #default="scope">
            {{ scope.row.lastCollectTime || '从未采集' }}
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button type="primary" size="small" @click="triggerCollect(scope.row.id)">采集</el-button>
            <el-button type="primary" size="small">编辑</el-button>
            <el-button type="danger" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 原始信息展示卡片 -->
    <el-card class="mt-20">
      <template #header>
        <div class="card-header">
          <span>采集到的原始信息</span>
          <div>
            <el-radio-group v-model="filterLevel" size="small" @change="handleFilterChange">
              <el-radio-button label="">全部</el-radio-button>
              <el-radio-button :label="1">Level 1</el-radio-button>
              <el-radio-button :label="2">Level 2</el-radio-button>
              <el-radio-button :label="3">Level 3</el-radio-button>
            </el-radio-group>
            <el-button type="primary" size="small" @click="refreshSignals" :loading="loadingSignals" class="ml-10">
              刷新
            </el-button>
          </div>
        </div>
      </template>
      
      <el-table :data="filteredSignals" style="width: 100%" v-loading="loadingSignals">
        <el-table-column prop="id" label="ID" width="60" />
        
        <el-table-column prop="title" label="标题" min-width="180" show-overflow-tooltip />
        
        <el-table-column prop="originalSummary" label="原文摘要" min-width="250" show-overflow-tooltip>
          <template #default="scope">
            <span v-if="scope.row.originalSummary" class="summary-text">
              {{ scope.row.originalSummary }}
            </span>
            <span v-else class="no-data">暂无摘要</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="source" label="来源" width="120" />
        
        <el-table-column prop="category" label="分类" width="100" />
        
        <el-table-column prop="level" label="等级" width="80">
          <template #default="scope">
            <el-tag :type="getLevelType(scope.row.level)">
              Level {{ scope.row.level }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="tags" label="标签" min-width="120" show-overflow-tooltip />
        
        <el-table-column prop="signalTime" label="信号时间" width="140">
          <template #default="scope">
            {{ formatTime(scope.row.signalTime) }}
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="viewDetail(scope.row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="totalSignals"
          layout="total, sizes, prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailVisible" title="信息详情" width="70%" top="5vh">
      <div v-if="currentSignal" class="signal-detail">
        <h3>{{ currentSignal.title }}</h3>
        
        <el-descriptions :column="2" border class="mt-20">
          <el-descriptions-item label="来源">{{ currentSignal.source }}</el-descriptions-item>
          <el-descriptions-item label="分类">{{ currentSignal.category }}</el-descriptions-item>
          <el-descriptions-item label="等级">
            <el-tag :type="getLevelType(currentSignal.level)">Level {{ currentSignal.level }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="标签">{{ currentSignal.tags }}</el-descriptions-item>
          <el-descriptions-item label="信号时间">{{ formatTime(currentSignal.signalTime) }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ formatTime(currentSignal.createdAt) }}</el-descriptions-item>
        </el-descriptions>

        <!-- 原文摘要 -->
        <div class="content-section mt-20" v-if="currentSignal.originalSummary">
          <h4>
            <el-icon><Document /></el-icon>
            原文摘要
          </h4>
          <el-alert
            :title="currentSignal.originalSummary"
            type="info"
            :closable="false"
            show-icon
          />
        </div>

        <!-- 原文内容 -->
        <div class="content-section mt-20" v-if="currentSignal.originalContent">
          <h4>
            <el-icon><Reading /></el-icon>
            原文内容
          </h4>
          <el-card class="original-content-card">
            <pre class="content-text">{{ currentSignal.originalContent }}</pre>
          </el-card>
        </div>

        <!-- 处理后的内容 -->
        <div class="content-section mt-20" v-if="currentSignal.content">
          <h4>
            <el-icon><Edit /></el-icon>
            处理内容
          </h4>
          <p class="content-text">{{ currentSignal.content }}</p>
        </div>

        <!-- 原文链接 -->
        <div class="content-section mt-20" v-if="currentSignal.originalUrl">
          <h4>
            <el-icon><Link /></el-icon>
            原文链接
          </h4>
          <el-link 
            type="primary" 
            :href="currentSignal.originalUrl" 
            target="_blank"
            :underline="false"
            class="original-link"
          >
            <el-button type="primary" plain>
              <el-icon><View /></el-icon>
              查看原文
            </el-button>
          </el-link>
          <el-input 
            v-model="currentSignal.originalUrl" 
            readonly 
            class="url-input mt-10"
          >
            <template #append>
              <el-button @click="copyUrl">
                <el-icon><CopyDocument /></el-icon>
                复制
              </el-button>
            </template>
          </el-input>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getInfoSignals } from '@/api/infoSignal'
import { Document, Reading, Edit, Link, View, CopyDocument } from '@element-plus/icons-vue'

// 数据源相关
const dataSources = ref([])
const loadingSources = ref(false)

// 信息信号相关
const signals = ref([])
const loadingSignals = ref(false)
const filterLevel = ref('')
const currentPage = ref(1)
const pageSize = ref(20)
const totalSignals = ref(0)

// 采集相关
const collecting = ref(false)

// 详情对话框
const detailVisible = ref(false)
const currentSignal = ref(null)

// 计算过滤后的信号
const filteredSignals = computed(() => {
  let result = signals.value
  if (filterLevel.value !== '') {
    result = result.filter(s => s.level === filterLevel.value)
  }
  // 分页
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return result.slice(start, end)
})

// 获取数据源列表
const fetchDataSources = async () => {
  loadingSources.value = true
  try {
    const res = await fetch('/api/v1/data-sources')
    const data = await res.json()
    if (data.code === 200) {
      dataSources.value = data.data
    }
  } catch (error) {
    console.error('获取数据源失败:', error)
    ElMessage.error('获取数据源失败')
  } finally {
    loadingSources.value = false
  }
}

// 获取信息信号列表
const fetchSignals = async () => {
  loadingSignals.value = true
  try {
    const res = await getInfoSignals()
    if (res.code === 200) {
      signals.value = res.data
      totalSignals.value = res.data.length
    }
  } catch (error) {
    console.error('获取信息信号失败:', error)
    ElMessage.error('获取信息信号失败')
  } finally {
    loadingSignals.value = false
  }
}

// 触发单个数据源采集
const triggerCollect = async (id) => {
  try {
    const res = await fetch(`/api/v1/data-sources/${id}/collect`, { method: 'POST' })
    const data = await res.json()
    if (data.code === 200) {
      ElMessage.success('采集任务已触发')
      setTimeout(() => {
        fetchDataSources()
        fetchSignals()
      }, 2000)
    }
  } catch (error) {
    console.error('触发采集失败:', error)
    ElMessage.error('触发采集失败')
  }
}

// 触发所有数据源采集
const triggerCollectAll = async () => {
  collecting.value = true
  try {
    const res = await fetch('/api/v1/data-sources/collect-all', { method: 'POST' })
    const data = await res.json()
    if (data.code === 200) {
      ElMessage.success('所有采集任务已触发')
      setTimeout(() => {
        fetchDataSources()
        fetchSignals()
      }, 3000)
    }
  } catch (error) {
    console.error('触发采集失败:', error)
    ElMessage.error('触发采集失败')
  } finally {
    collecting.value = false
  }
}

// 刷新信号列表
const refreshSignals = () => {
  fetchSignals()
}

// 查看详情
const viewDetail = (row) => {
  currentSignal.value = row
  detailVisible.value = true
}

// 复制链接
const copyUrl = () => {
  if (currentSignal.value?.originalUrl) {
    navigator.clipboard.writeText(currentSignal.value.originalUrl)
    ElMessage.success('链接已复制到剪贴板')
  }
}

// 获取等级标签类型
const getLevelType = (level) => {
  switch (level) {
    case 1: return 'info'
    case 2: return 'warning'
    case 3: return 'danger'
    default: return 'info'
  }
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return '-'
  const date = new Date(time)
  return date.toLocaleString('zh-CN')
}

// 处理筛选变化
const handleFilterChange = () => {
  currentPage.value = 1
}

// 处理分页变化
const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
}

const handleCurrentChange = (val) => {
  currentPage.value = val
}

// 页面加载时获取数据
onMounted(() => {
  fetchDataSources()
  fetchSignals()
})
</script>

<style scoped>
.page-container {
  padding: 20px;
}

.mt-20 {
  margin-top: 20px;
}

.mt-10 {
  margin-top: 10px;
}

.ml-10 {
  margin-left: 10px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.signal-detail h3 {
  margin: 0 0 20px 0;
  color: #303133;
  font-size: 20px;
}

.content-section h4 {
  margin: 0 0 12px 0;
  color: #606266;
  font-size: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.content-text {
  line-height: 1.8;
  color: #606266;
  white-space: pre-wrap;
  font-size: 14px;
}

.summary-text {
  color: #606266;
  font-size: 13px;
  line-height: 1.5;
}

.no-data {
  color: #909399;
  font-size: 13px;
  font-style: italic;
}

.original-content-card {
  background-color: #f5f7fa;
}

.original-content-card pre {
  margin: 0;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
  max-height: 400px;
  overflow-y: auto;
}

.original-link {
  display: inline-flex;
  align-items: center;
  gap: 5px;
}

.url-input {
  max-width: 600px;
}

:deep(.el-descriptions__label) {
  width: 100px;
}
</style>
