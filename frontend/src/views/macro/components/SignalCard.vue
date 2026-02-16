<template>
  <div class="signal-card" :class="type">
    <div class="signal-icon">
      <el-icon :size="32">
        <component :is="icon" />
      </el-icon>
    </div>    
    <div class="signal-content">
      <div class="signal-title">{{ title }}</div>
      <div class="signal-value">{{ value }}</div>
      <div class="signal-subtext">{{ subtext }}</div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { TrendCharts, Histogram, DataLine, PieChart } from '@element-plus/icons-vue'

const props = defineProps({
  title: String,
  value: String,
  subtext: String,
  type: {
    type: String,
    default: 'primary',
    validator: (value) => ['primary', 'success', 'warning', 'danger', 'info'].includes(value)
  },
  icon: {
    type: String,
    default: 'TrendCharts'
  }
})

const iconComponent = computed(() => {
  const iconMap = {
    'TrendCharts': TrendCharts,
    'Histogram': Histogram,
    'DataLine': DataLine,
    'PieChart': PieChart
  }
  return iconMap[props.icon] || TrendCharts
})
</script>

<style scoped>
.signal-card {
  display: flex;
  align-items: center;
  padding: 16px 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: transform 0.2s;
}

.signal-card:hover {
  transform: translateY(-2px);
}

.signal-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
}

.signal-card.primary .signal-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.signal-card.success .signal-icon {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
  color: white;
}

.signal-card.warning .signal-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
}

.signal-card.danger .signal-icon {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
  color: white;
}

.signal-card.info .signal-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
}

.signal-content {
  flex: 1;
}

.signal-title {
  font-size: 13px;
  color: #909399;
  margin-bottom: 4px;
}

.signal-value {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}

.signal-subtext {
  font-size: 12px;
  color: #606266;
}
</style>