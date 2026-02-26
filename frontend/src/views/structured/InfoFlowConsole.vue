<template>
  <div class="info-flow-console">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div>
          <h2>信息流控制台</h2>
          <p class="subtitle">看"今天发生了什么"，但已经过结构过滤 | 不是看新闻，而是看"变量识别结果"</p>
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
            <div class="stat-value">{{ stats.todayArticles }}</div>
            <div class="stat-label">今日新增文章</div>
          </div>
        </div>
      </el-col>
      
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon green">
            <el-icon><Check /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.structuredCount }}</div>
            <div class="stat-label">通过结构识别</div>
          </div>
        </div>
      </el-col>
      
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon gray">
            <el-icon><Close /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.noiseCount }}</div>
            <div class="stat-label">被判为噪音</div>
          </div>
        </div>
      </el-col>
      
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon orange">
            <el-icon><TrendCharts /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.avgImpactScore }}</div>
            <div class="stat-label">平均 Impact Score</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 主内容区 -->
    <div class="main-content">
      <!-- 文章列表 -->
      <div class="article-list">
        <div class="list-header">
          <h3>变量识别结果列表</h3>
          <div class="header-actions">
            <el-input
              v-model="searchQuery"
              placeholder="搜索标题或变量..."
              style="width: 300px"
              :prefix-icon="Search"
              clearable
            />
          </div>
        </div>

        <el-table
          :data="filteredArticles"
          style="width: 100%"
          @row-click="handleRowClick"
          highlight-current-row
        >
          <el-table-column label="标题" min-width="300">
            <template #default="{ row }">
              <div class="article-title">
                <span class="title-text">{{ row.title }}</span>
                <el-tag
                  v-if="row.isStrategic"
                  type="danger"
                  size="small"
                  class="strategic-tag"
                >
                  战略池
                </el-tag>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="结构变量标签" width="180">
            <template #default="{ row }">
              <el-tag
                v-for="tag in row.variableTags"
                :key="tag"
                size="small"
                class="variable-tag"
              >
                {{ tag }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column label="Impact Score" width="120">
            <template #default="{ row }">
              <div class="impact-score" :class="getImpactClass(row.impactScore)">
                {{ row.impactScore }}
              </div>
            </template>
          </el-table-column>

          <el-table-column label="变量数" width="80">
            <template #default="{ row }">
              <el-tag type="info" size="small">{{ row.variableCount }}</el-tag>
            </template>
          </el-table-column>

          <el-table-column prop="source" label="来源" width="120" />

          <el-table-column label="发布时间" width="160">
            <template #default="{ row }">
              {{ formatTime(row.publishTime) }}
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div class="pagination">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :total="totalCount"
            layout="total, prev, pager, next"
          />
        </div>
      </div>
    </div>

    <!-- 右侧详情抽屉 -->
    <el-drawer
      v-model="drawerVisible"
      title="文章结构分析详情"
      size="500px"
      destroy-on-close
    >
      <div v-if="selectedArticle" class="drawer-content">
        <!-- 基本信息 -->
        <div class="drawer-section">
          <h4><Document /> 文章信息</h4>
          <div class="info-item">
            <span class="label">标题：</span>
            <span class="value">{{ selectedArticle.title }}</span>
          </div>
          <div class="info-item">
            <span class="label">来源：</span>
            <span class="value">{{ selectedArticle.source }}</span>
          </div>
          <div class="info-item">
            <span class="label">发布时间：</span>
            <span class="value">{{ selectedArticle.publishTime }}</span>
          </div>
        </div>

        <!-- 模型识别JSON -->
        <div class="drawer-section">
          <h4><DataAnalysis /> 模型识别结果</h4>
          <pre class="json-block">{{ JSON.stringify(selectedArticle.modelResult, null, 2) }}</pre>
        </div>

        <!-- 证据句 -->
        <div class="drawer-section">
          <h4><ChatDotRound /> 关键证据句</h4>
          <div
            v-for="(evidence, index) in selectedArticle.evidenceSentences"
            :key="index"
            class="evidence-item"
          >
            <div class="evidence-text">"{{ evidence.text }}"</div>
            <div class="evidence-meta">
              置信度: {{ evidence.confidence }}% | 类型: {{ evidence.type }}
            </div>
          </div>
        </div>

        <!-- 规则评分 -->
        <div class="drawer-section">
          <h4><Star /> 规则评分结果</h4>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="结构完整性">
              <el-rate v-model="selectedArticle.scores.structure" disabled show-score />
            </el-descriptions-item>
            <el-descriptions-item label="信息密度">
              <el-rate v-model="selectedArticle.scores.density" disabled show-score />
            </el-descriptions-item>
            <el-descriptions-item label="时效性">
              <el-rate v-model="selectedArticle.scores.timeliness" disabled show-score />
            </el-descriptions-item>
            <el-descriptions-item label="Impact Score">
              <div class="impact-display">
                <span class="score">{{ selectedArticle.impactScore }}</span>
                <el-tag :type="getImpactTagType(selectedArticle.impactScore)">
                  {{ getImpactLabel(selectedArticle.impactScore) }}
                </el-tag>
              </div>
            </el-descriptions-item>
          </el-descriptions>
        </div>

        <!-- 战略池状态 -->
        <div class="drawer-section">
          <h4><Collection /> 战略观察池状态</h4>
          <div class="strategic-status">
            <el-result
              :icon="selectedArticle.isStrategic ? 'success' : 'info'"
              :title="selectedArticle.isStrategic ? '已进入战略观察池' : '未进入战略观察池'"
              :sub-title="selectedArticle.isStrategic ? 'Impact Score ≥ 阈值，已触发战略关注' : 'Impact Score 未达到阈值标准'"
            >
              <template #extra>
                <el-button 
                  v-if="!selectedArticle.isStrategic" 
                  type="primary"
                  @click="addToStrategicPool"
                >
                  手动加入战略池
                </el-button>
                <el-button 
                  v-else 
                  type="danger" 
                  plain
                  @click="removeFromStrategicPool"
                >
                  移出战略池
                </el-button>
              </template>
            </el-result>
          </div>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { 
  Document, 
  Check, 
  Close, 
  TrendCharts, 
  Search,
  DataAnalysis,
  ChatDotRound,
  Star,
  Collection
} from '@element-plus/icons-vue'

// 统计数据
const stats = ref({
  todayArticles: 156,
  structuredCount: 89,
  noiseCount: 67,
  avgImpactScore: 7.2
})

// 搜索和分页
const searchQuery = ref('')
const currentPage = ref(1)
const pageSize = ref(20)
const totalCount = ref(156)

// 抽屉控制
const drawerVisible = ref(false)
const selectedArticle = ref(null)

// 模拟文章数据
const articles = ref([
  {
    id: 1,
    title: '新能源汽车补贴政策调整：退坡幅度超预期',
    variableTags: ['政策变化', '资本流向'],
    impactScore: 8.5,
    variableCount: 3,
    source: '财经网',
    publishTime: '2024-12-15 10:30:00',
    isStrategic: true,
    modelResult: {
      structureType: 'policy_change',
      confidence: 0.92,
      variables: [
        { name: '新能源补贴', type: 'policy', direction: 'negative' },
        { name: '产业链成本', type: 'supply_chain', direction: 'positive' }
      ]
    },
    evidenceSentences: [
      { text: '补贴退坡幅度将达到30%，远超市场预期的20%', confidence: 95, type: 'quantitative' },
      { text: '地方政府配套政策将陆续跟进调整', confidence: 78, type: 'predictive' }
    ],
    scores: {
      structure: 4,
      density: 5,
      timeliness: 4
    }
  },
  {
    id: 2,
    title: 'AI基础模型融资集中化：头部效应加剧',
    variableTags: ['资本流向', '技术变革'],
    impactScore: 7.8,
    variableCount: 2,
    source: '科技日报',
    publishTime: '2024-12-15 09:15:00',
    isStrategic: true,
    modelResult: {
      structureType: 'capital_concentration',
      confidence: 0.88,
      variables: [
        { name: 'AI融资', type: 'capital', direction: 'concentrated' },
        { name: '行业集中度', type: 'market_structure', direction: 'increasing' }
      ]
    },
    evidenceSentences: [
      { text: '前五大AI公司融资额占全行业78%', confidence: 92, type: 'quantitative' }
    ],
    scores: {
      structure: 4,
      density: 4,
      timeliness: 5
    }
  },
  {
    id: 3,
    title: '房地产行业监管收紧：三道红线政策升级',
    variableTags: ['政策变化', '行业监管'],
    impactScore: 6.5,
    variableCount: 2,
    source: '证券时报',
    publishTime: '2024-12-14 16:45:00',
    isStrategic: false,
    modelResult: {
      structureType: 'regulatory_tightening',
      confidence: 0.85,
      variables: [
        { name: '房地产监管', type: 'policy', direction: 'tightening' }
      ]
    },
    evidenceSentences: [
      { text: '监管部门要求严格执行三道红线标准', confidence: 88, type: 'policy' }
    ],
    scores: {
      structure: 3,
      density: 4,
      timeliness: 3
    }
  },
  {
    id: 4,
    title: '半导体设备进口受限：国产替代加速',
    variableTags: ['供应链', '技术变革'],
    impactScore: 7.2,
    variableCount: 3,
    source: '产业观察',
    publishTime: '2024-12-14 14:20:00',
    isStrategic: true,
    modelResult: {
      structureType: 'supply_chain_shock',
      confidence: 0.90,
      variables: [
        { name: '半导体设备', type: 'supply_chain', direction: 'restricted' },
        { name: '国产替代', type: 'technology', direction: 'accelerating' }
      ]
    },
    evidenceSentences: [
      { text: '关键设备进口许可审批周期延长至6个月', confidence: 90, type: 'quantitative' }
    ],
    scores: {
      structure: 4,
      density: 5,
      timeliness: 4
    }
  },
  {
    id: 5,
    title: '消费复苏信号：社零数据超预期增长',
    variableTags: ['宏观数据', '需求变化'],
    impactScore: 5.8,
    variableCount: 1,
    source: '经济参考报',
    publishTime: '2024-12-14 11:00:00',
    isStrategic: false,
    modelResult: {
      structureType: 'macro_indicator',
      confidence: 0.82,
      variables: [
        { name: '消费复苏', type: 'demand', direction: 'positive' }
      ]
    },
    evidenceSentences: [
      { text: '11月社零同比增长5.2%，超预期0.8个百分点', confidence: 95, type: 'quantitative' }
    ],
    scores: {
      structure: 3,
      density: 3,
      timeliness: 4
    }
  }
])

// 过滤后的文章列表
const filteredArticles = computed(() => {
  if (!searchQuery.value) return articles.value
  const query = searchQuery.value.toLowerCase()
  return articles.value.filter(article => 
    article.title.toLowerCase().includes(query) ||
    article.variableTags.some(tag => tag.toLowerCase().includes(query))
  )
})

// 获取Impact分数样式
const getImpactClass = (score) => {
  if (score >= 8) return 'high'
  if (score >= 6) return 'medium'
  return 'low'
}

// 获取Impact标签类型
const getImpactTagType = (score) => {
  if (score >= 8) return 'danger'
  if (score >= 6) return 'warning'
  return 'info'
}

// 获取Impact标签文字
const getImpactLabel = (score) => {
  if (score >= 8) return '高影响'
  if (score >= 6) return '中影响'
  return '低影响'
}

// 格式化时间
const formatTime = (time) => {
  return time
}

// 处理行点击
const handleRowClick = (row) => {
  selectedArticle.value = row
  drawerVisible.value = true
}

// 加入战略池
const addToStrategicPool = () => {
  if (selectedArticle.value) {
    selectedArticle.value.isStrategic = true
  }
}

// 移出战略池
const removeFromStrategicPool = () => {
  if (selectedArticle.value) {
    selectedArticle.value.isStrategic = false
  }
}
</script>

<style scoped>
.info-flow-console {
  padding: 0;
}

.page-header {
  margin-bottom: 20px;
  padding: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 8px;
  color: white;
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

.stat-icon.gray {
  background: #f5f5f5;
  color: #8c8c8c;
}

.stat-icon.orange {
  background: #fff7e6;
  color: #fa8c16;
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

/* 主内容区 */
.main-content {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.list-header h3 {
  margin: 0;
  font-size: 18px;
  color: #303133;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

/* 表格样式 */
.article-title {
  display: flex;
  align-items: center;
  gap: 8px;
}

.title-text {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.strategic-tag {
  flex-shrink: 0;
}

.variable-tag {
  margin-right: 4px;
  margin-bottom: 2px;
}

.impact-score {
  font-weight: bold;
  font-size: 16px;
}

.impact-score.high {
  color: #f5222d;
}

.impact-score.medium {
  color: #fa8c16;
}

.impact-score.low {
  color: #52c41a;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

/* 抽屉内容 */
.drawer-content {
  padding: 0 10px;
}

.drawer-section {
  margin-bottom: 24px;
}

.drawer-section h4 {
  margin: 0 0 12px 0;
  font-size: 16px;
  color: #303133;
  display: flex;
  align-items: center;
  gap: 8px;
}

.info-item {
  margin-bottom: 8px;
  display: flex;
}

.info-item .label {
  color: #909399;
  width: 80px;
  flex-shrink: 0;
}

.info-item .value {
  color: #303133;
  flex: 1;
}

.json-block {
  background: #f5f7fa;
  padding: 12px;
  border-radius: 4px;
  font-size: 12px;
  overflow-x: auto;
}

.evidence-item {
  background: #f6ffed;
  border-left: 3px solid #52c41a;
  padding: 12px;
  margin-bottom: 12px;
  border-radius: 4px;
}

.evidence-text {
  font-style: italic;
  color: #303133;
  margin-bottom: 4px;
}

.evidence-meta {
  font-size: 12px;
  color: #909399;
}

.impact-display {
  display: flex;
  align-items: center;
  gap: 12px;
}

.impact-display .score {
  font-size: 24px;
  font-weight: bold;
  color: #f5222d;
}

.strategic-status {
  padding: 20px 0;
}
</style>