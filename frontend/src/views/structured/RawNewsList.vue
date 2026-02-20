<template>
  <div class="raw-news-list">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div>
          <h2>原始数据列表</h2>
          <p class="subtitle">原始新闻数据采集与管理 | 查看所有采集源的最新数据</p>
        </div>
        <div class="header-actions">
          <el-button
            type="success"
            :icon="Download"
            :loading="isCollecting"
            @click="collect36Kr"
          >
            {{ isCollecting ? '采集中...' : '采集36Kr' }}
          </el-button>
          <el-button type="primary" :icon="Refresh" @click="refreshList">
            刷新列表
          </el-button>
        </div>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stats-row">
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon blue">
            <el-icon><Document /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.totalCount }}</div>
            <div class="stat-label">总文章数</div>
          </div>
        </div>
      </el-col>
      
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon green">
            <el-icon><Calendar /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.todayCount }}</div>
            <div class="stat-label">今日新增</div>
          </div>
        </div>
      </el-col>
      
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon orange">
            <el-icon><Collection /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.sourceCount }}</div>
            <div class="stat-label">采集源数</div>
          </div>
        </div>
      </el-col>
      
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon purple">
            <el-icon><Timer /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.lastUpdate }}</div>
            <div class="stat-label">最近更新</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 筛选栏 -->
    <div class="filter-bar">
      <div class="filter-group">
        <span class="filter-label">来源：</span>
        <el-select v-model="filterSource" placeholder="全部来源" clearable style="width: 150px">
          <el-option label="36Kr" value="36Kr" />
          <el-option label="财新网" value="财新网" />
          <el-option label="证券时报" value="证券时报" />
          <el-option label="科技日报" value="科技日报" />
        </el-select>
      </div>
      
      <div class="filter-group">
        <span class="filter-label">分类：</span>
        <el-select v-model="filterCategory" placeholder="全部分类" clearable style="width: 150px">
          <el-option label="科技" value="科技" />
          <el-option label="资本" value="资本" />
          <el-option label="政策" value="政策" />
          <el-option label="产业" value="产业" />
          <el-option label="综合" value="综合" />
        </el-select>
      </div>
      
      <div class="filter-group">
        <span class="filter-label">时间：</span>
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 240px"
        />
      </div>
      
      <el-input
        v-model="searchQuery"
        placeholder="搜索标题..."
        style="width: 250px"
        :prefix-icon="Search"
        clearable
      />
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        :data="filteredNewsList"
        style="width: 100%"
        v-loading="loading"
        @row-click="handleRowClick"
      >
        <el-table-column type="index" width="50" />
        
        <el-table-column label="标题" min-width="350">
          <template #default="{ row }">
            <div class="news-title">
              <span class="title-text">{{ row.title }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="来源" width="120">
          <template #default="{ row }">
            <el-tag :type="getSourceType(row.source)" size="small">
              {{ row.source }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="分类" width="100">
          <template #default="{ row }">
            <el-tag type="info" size="small" effect="plain">
              {{ row.category }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="发布时间" width="160">
          <template #default="{ row }">
            {{ formatDateTime(row.publishTime) }}
          </template>
        </el-table-column>

        <el-table-column label="采集时间" width="160">
          <template #default="{ row }">
            {{ formatDateTime(row.fetchTime) }}
          </template>
        </el-table-column>

        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click.stop="viewDetail(row)">
              查看
            </el-button>
            <el-button type="danger" link size="small" @click.stop="deleteNews(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="totalCount"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next"
        />
      </div>
    </div>

    <!-- 详情弹窗 -->
    <el-dialog
      v-model="detailVisible"
      title="新闻详情"
      width="800px"
      destroy-on-close
    >
      <div v-if="selectedNews" class="detail-content">
        <div class="detail-header">
          <h3>{{ selectedNews.title }}</h3>
          <div class="detail-meta">
            <el-tag :type="getSourceType(selectedNews.source)">
              {{ selectedNews.source }}
            </el-tag>
            <el-tag type="info" effect="plain">
              {{ selectedNews.category }}
            </el-tag>
          </div>
        </div>

        <el-descriptions :column="2" border>
          <el-descriptions-item label="原文链接">
            <el-link :href="selectedNews.url" target="_blank" type="primary">
              {{ selectedNews.url }}
            </el-link>
          </el-descriptions-item>
          <el-descriptions-item label="发布时间">
            {{ formatDateTime(selectedNews.publishTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="采集时间">
            {{ formatDateTime(selectedNews.fetchTime) }}
          </el-descriptions-item>
        </el-descriptions>

        <div class="detail-content-text">
          <h4>正文内容</h4>
          <div class="content-box">
            {{ selectedNews.content }}
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Document, Calendar, Collection, Timer, Search, Refresh, Download } from '@element-plus/icons-vue'

// 统计数据
const stats = ref({
  totalCount: 0,
  todayCount: 0,
  sourceCount: 0,
  lastUpdate: '-'
})

// 加载状态
const loading = ref(false)

// 筛选条件
const filterSource = ref('')
const filterCategory = ref('')
const dateRange = ref(null)
const searchQuery = ref('')

// 分页
const currentPage = ref(1)
const pageSize = ref(20)
const totalCount = ref(0)

// 详情弹窗
const detailVisible = ref(false)
const selectedNews = ref(null)

// 新闻数据
const newsList = ref([])

// 过滤后的列表
const filteredNewsList = computed(() => {
  let result = newsList.value
  
  if (filterSource.value) {
    result = result.filter(item => item.source === filterSource.value)
  }
  
  if (filterCategory.value) {
    result = result.filter(item => item.category === filterCategory.value)
  }
  
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter(item => item.title.toLowerCase().includes(query))
  }
  
  return result
})

// 获取来源标签类型
const getSourceType = (source) => {
  const map = {
    '36Kr': 'primary',
    '财新网': 'success',
    '证券时报': 'warning',
    '科技日报': 'info'
  }
  return map[source] || 'info'
}

// 格式化日期时间
const formatDateTime = (datetime) => {
  if (!datetime) return '-'
  return datetime
}

// 获取新闻列表
const fetchNewsList = async () => {
  loading.value = true
  try {
    const res = await fetch('/api/v1/news-raw')
    const result = await res.json()
    if (result.code === 200) {
      newsList.value = result.data || []
      totalCount.value = newsList.value.length
    } else {
      ElMessage.error(result.message || '获取数据失败')
    }
  } catch (error) {
    ElMessage.error('获取数据失败：' + error.message)
  } finally {
    loading.value = false
  }
}

// 获取统计数据
const fetchStats = async () => {
  try {
    // 获取今日数量
    const todayRes = await fetch('/api/v1/news-raw/count/today')
    const todayResult = await todayRes.json()
    if (todayResult.code === 200) {
      stats.value.todayCount = todayResult.data || 0
    }
    
    // 获取总数（通过列表长度）
    const listRes = await fetch('/api/v1/news-raw')
    const listResult = await listRes.json()
    if (listResult.code === 200) {
      const list = listResult.data || []
      stats.value.totalCount = list.length
      
      // 计算来源数量
      const sources = new Set(list.map(item => item.source))
      stats.value.sourceCount = sources.size
      
      // 最近更新时间
      if (list.length > 0) {
        const latest = list.sort((a, b) => new Date(b.fetchTime) - new Date(a.fetchTime))[0]
        stats.value.lastUpdate = formatTimeAgo(latest.fetchTime)
      }
    }
  } catch (error) {
    console.error('获取统计数据失败：', error)
  }
}

// 格式化相对时间
const formatTimeAgo = (datetime) => {
  if (!datetime) return '-'
  const date = new Date(datetime)
  const now = new Date()
  const diff = Math.floor((now - date) / 1000)
  
  if (diff < 60) return '刚刚'
  if (diff < 3600) return Math.floor(diff / 60) + '分钟前'
  if (diff < 86400) return Math.floor(diff / 3600) + '小时前'
  if (diff < 604800) return Math.floor(diff / 86400) + '天前'
  return datetime
}

// 刷新列表
const refreshList = async () => {
  loading.value = true
  try {
    await Promise.all([fetchNewsList(), fetchStats()])
    ElMessage.success('列表已刷新')
  } catch (error) {
    ElMessage.error('刷新失败：' + error.message)
  } finally {
    loading.value = false
  }
}

// 页面加载时获取数据
onMounted(() => {
  fetchNewsList()
  fetchStats()
})

// 查看详情
const viewDetail = (row) => {
  selectedNews.value = row
  detailVisible.value = true
}

// 行点击
const handleRowClick = (row) => {
  viewDetail(row)
}

// 删除新闻
const deleteNews = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定删除新闻 "${row.title}" 吗？`,
      '删除确认',
      {
        confirmButtonText: '删除',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const res = await fetch(`/api/v1/news-raw/${row.id}`, { method: 'DELETE' })
    const result = await res.json()
    
    if (result.code === 200) {
      const index = newsList.value.findIndex(item => item.id === row.id)
      if (index > -1) {
        newsList.value.splice(index, 1)
        totalCount.value--
        stats.value.totalCount--
      }
      ElMessage.success('删除成功')
    } else {
      ElMessage.error(result.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败：' + error.message)
    }
  }
}

// 采集状态
const isCollecting = ref(false)

// 采集36Kr数据
const collect36Kr = async () => {
  isCollecting.value = true
  try {
    const response = await fetch('/api/v1/collector/rss/36kr', { method: 'POST' })
    const result = await response.json()
    
    if (result.code === 200) {
      ElMessage.success(`36Kr数据采集完成，新增 ${result.data.length} 条新闻`)
      // 刷新列表数据
      await fetchNewsList()
      await fetchStats()
    } else {
      ElMessage.error(result.message || '采集失败')
    }
  } catch (error) {
    ElMessage.error('采集失败：' + error.message)
  } finally {
    isCollecting.value = false
  }
}
</script>

<style scoped>
.raw-news-list {
  padding: 0;
}

.page-header {
  margin-bottom: 20px;
  padding: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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

/* 统计卡片 */
.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
}

.stat-icon.blue {
  background: #e6f7ff;
  color: #1890ff;
}

.stat-icon.green {
  background: #f6ffed;
  color: #52c41a;
}

.stat-icon.orange {
  background: #fff7e6;
  color: #fa8c16;
}

.stat-icon.purple {
  background: #f9f0ff;
  color: #722ed1;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 4px;
}

/* 筛选栏 */
.filter-bar {
  background: white;
  padding: 16px 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
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

/* 表格容器 */
.table-container {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.news-title {
  display: flex;
  align-items: center;
}

.title-text {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

/* 详情弹窗 */
.detail-content {
  padding: 0 10px;
}

.detail-header {
  margin-bottom: 20px;
}

.detail-header h3 {
  margin: 0 0 12px 0;
  font-size: 20px;
  color: #303133;
}

.detail-meta {
  display: flex;
  gap: 8px;
}

.detail-content-text {
  margin-top: 20px;
}

.detail-content-text h4 {
  margin: 0 0 12px 0;
  font-size: 16px;
  color: #303133;
}

.content-box {
  background: #f5f7fa;
  padding: 16px;
  border-radius: 8px;
  line-height: 1.8;
  color: #606266;
  max-height: 400px;
  overflow-y: auto;
}
</style>