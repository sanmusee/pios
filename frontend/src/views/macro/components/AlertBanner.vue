<template>
  <div v-if="visible" class="alert-banner" :class="alertLevel">
    <div class="alert-content">
      <el-icon class="alert-icon"><Warning /></el-icon>
      <span class="alert-text">{{ alertMessage }}</span>
      <el-button type="text" class="close-btn" @click="visible = false">
        <el-icon><Close /></el-icon>
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { Warning, Close } from '@element-plus/icons-vue'

const props = defineProps({
  vix: Number,
  exchangeRate: Number,
  chinaUsSpread: Number,
  creditSpread: Number
})

const visible = ref(true)

const alertInfo = computed(() => {
  const alerts = []
  let level = 'info'
  
  // VIX > 25 && 离岸人民币 > 7.3
  if (props.vix > 25 && props.exchangeRate > 7.3) {
    alerts.push('全球市场恐慌(VIX>25) + 汇率承压(>7.3)')
    level = 'danger'
  } else if (props.vix > 25) {
    alerts.push('全球市场恐慌(VIX>25)')
    level = 'warning'
  } else if (props.exchangeRate > 7.3) {
    alerts.push('汇率承压(>7.3)')
    level = 'warning'
  }
  
  // 中美利差倒挂深度 > -150bp
  if (props.chinaUsSpread < -1.5) {
    alerts.push(`中美利差深度倒挂(${props.chinaUsSpread}%)`)
    if (level !== 'danger') level = 'warning'
  }
  
  // 信用利差 > 80bp
  if (props.creditSpread > 80) {
    alerts.push(`信用利差扩大(${props.creditSpread}bp)`)
    if (level !== 'danger') level = 'warning'
  }
  
  return {
    message: alerts.join(' | '),
    level: level,
    hasAlert: alerts.length > 0
  }
})

const alertMessage = computed(() => alertInfo.value.message)
const alertLevel = computed(() => alertInfo.value.level)

watch(() => alertInfo.value.hasAlert, (hasAlert) => {
  if (hasAlert) visible.value = true
}, { immediate: true })
</script>

<style scoped>
.alert-banner {
  padding: 12px 20px;
  margin-bottom: 16px;
  border-radius: 8px;
  animation: slideDown 0.3s ease;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.alert-banner.info {
  background: #e6f7ff;
  border: 1px solid #91d5ff;
}

.alert-banner.warning {
  background: #fff7e6;
  border: 1px solid #ffd591;
}

.alert-banner.danger {
  background: #fff1f0;
  border: 1px solid #ffa39e;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% { box-shadow: 0 0 0 0 rgba(255, 77, 79, 0.4); }
  50% { box-shadow: 0 0 0 4px rgba(255, 77, 79, 0.1); }
}

.alert-content {
  display: flex;
  align-items: center;
  gap: 12px;
}

.alert-icon {
  font-size: 20px;
}

.alert-banner.warning .alert-icon {
  color: #fa8c16;
}

.alert-banner.danger .alert-icon {
  color: #f5222d;
}

.alert-text {
  flex: 1;
  font-weight: 600;
  font-size: 14px;
}

.alert-banner.warning .alert-text {
  color: #d46b08;
}

.alert-banner.danger .alert-text {
  color: #cf1322;
}

.close-btn {
  padding: 4px;
  color: inherit;
}
</style>
