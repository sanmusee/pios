<template>
  <el-dialog
    v-model="visible"
    title="AI 宏观分析报告"
    width="800px"
    :close-on-click-modal="true"
    class="analysis-dialog"
  >
    <div v-if="result" class="analysis-content">
      <!-- 核心判断 -->
      <div class="analysis-section">
        <div class="section-title">
          <el-icon><ChatDotRound /></el-icon>
          <span>本期宏观判断</span>
        </div>
        <div class="section-content judgment">
          {{ result.summary.judgment }}
        </div>
      </div>

      <!-- 驱动因素 -->
      <div class="analysis-section">
        <div class="section-title">
          <el-icon><TrendCharts /></el-icon>
          <span>主要驱动因素</span>
        </div>
        <div class="section-content">
          {{ result.summary.drivers }}
        </div>
      </div>

      <!-- 风险提示 -->
      <div class="analysis-section warning">
        <div class="section-title">
          <el-icon><Warning /></el-icon>
          <span>风险提示</span>
        </div>
        <div class="section-content">
          {{ result.summary.risks }}
        </div>
      </div>

      <!-- 下期关注 -->
      <div class="analysis-section focus">
        <div class="section-title">
          <el-icon><View /></el-icon>
          <span>下期关注点</span>
        </div>
        <div class="section-content">
          {{ result.summary.focus }}
        </div>
      </div>

      <!-- 数据结构展示 -->
      <div class="raw-data-section">
        <el-collapse>
          <el-collapse-item title="输入数据结构（供LLM使用）">
            <pre class="json-display">{{ JSON.stringify(result.raw_data, null, 2) }}</pre>
          </el-collapse-item>
        </el-collapse>
      </div>
    </div>

    <div v-else class="empty-state">
      <el-empty description="暂无分析结果" />
    </div>

    <template #footer>
      <el-button @click="visible = false">关闭</el-button>
      <el-button type="primary" @click="copyToClipboard" v-if="result">
        复制报告
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, defineExpose } from 'vue'
import { ChatDotRound, TrendCharts, Warning, View } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const visible = ref(false)
const result = ref(null)

const open = (data) => {
  result.value = data
  visible.value = true
}

const copyToClipboard = () => {
  if (!result.value) return
  
  const text = `
宏观分析报告

【本期宏观判断】
${result.value.summary.judgment}

【主要驱动因素】
${result.value.summary.drivers}

【风险提示】
${result.value.summary.risks}

【下期关注点】
${result.value.summary.focus}
  `.trim()
  
  navigator.clipboard.writeText(text).then(() => {
    ElMessage.success('报告已复制到剪贴板')
  }).catch(() => {
    ElMessage.error('复制失败')
  })
}

defineExpose({
  open
})
</script>

<style scoped>
.analysis-dialog :deep(.el-dialog__header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  margin-right: 0;
  padding: 20px;
}

.analysis-dialog :deep(.el-dialog__title) {
  color: white;
  font-weight: 600;
}

.analysis-dialog :deep(.el-dialog__headerbtn .el-dialog__close) {
  color: white;
}

.analysis-content {
  padding: 10px 0;
}

.analysis-section {
  margin-bottom: 20px;
  padding: 16px;
  background: #f5f7fa;
  border-radius: 8px;
  border-left: 4px solid #409eff;
}

.analysis-section.warning {
  border-left-color: #fa8c16;
  background: #fff7e6;
}

.analysis-section.focus {
  border-left-color: #52c41a;
  background: #f6ffed;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
  font-size: 15px;
  color: #303133;
  margin-bottom: 12px;
}

.section-title .el-icon {
  font-size: 18px;
  color: #409eff;
}

.analysis-section.warning .section-title .el-icon {
  color: #fa8c16;
}

.analysis-section.focus .section-title .el-icon {
  color: #52c41a;
}

.section-content {
  font-size: 14px;
  line-height: 1.8;
  color: #606266;
}

.section-content.judgment {
  font-size: 15px;
  font-weight: 500;
  color: #303133;
}

.raw-data-section {
  margin-top: 20px;
}

.json-display {
  background: #1e1e1e;
  color: #d4d4d4;
  padding: 16px;
  border-radius: 6px;
  font-family: 'Monaco', 'Menlo', monospace;
  font-size: 12px;
  line-height: 1.5;
  overflow-x: auto;
  max-height: 300px;
  overflow-y: auto;
}

.empty-state {
  padding: 40px;
}
</style>
