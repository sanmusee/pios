<template>
  <el-card class="chart-card" :body-style="{ padding: '0' }">
    <template #header>
      <div class="card-header">
        <div class="title-section">
          <span class="title">{{ title }}</span>
          <span v-if="subtitle" class="subtitle">{{ subtitle }}</span>
        </div>
        <el-tag v-if="tag" :type="tagType" size="small">{{ tag }}</el-tag>
      </div>
    </template>
    <div class="chart-content">
      <slot />
    </div>
  </el-card>
</template>

<script setup>
defineProps({
  title: String,
  subtitle: String,
  tag: String,
  tagType: {
    type: String,
    default: 'info'
  }
})
</script>

<style scoped>
.chart-card {
  height: 400px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.chart-card :deep(.el-card__header) {
  padding: 12px 16px;
  border-bottom: 1px solid #ebeef5;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title-section {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.subtitle {
  font-size: 12px;
  color: #909399;
}

.chart-content {
  height: 320px;
  padding: 16px;
}
</style>
