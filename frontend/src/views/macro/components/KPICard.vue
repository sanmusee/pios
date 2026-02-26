<template>
  <div class="kpi-card" :class="{ 'up': isUp, 'down': isDown }">
    <div class="kpi-header">
      <span class="kpi-title">{{ title }}</span>
      <el-tooltip v-if="expectation !== null" :content="`预期: ${expectation}${unit || ''}`" placement="top">
        <span class="expectation-tag" :class="expectationClass">
          预期 {{ formattedExpectation }}
        </span>
      </el-tooltip>
    </div>
    
    <div class="kpi-value">
      <span class="number">{{ formattedValue }}</span>
      <span v-if="unit" class="unit">{{ unit }}</span>
    </div>
    
    <div class="kpi-change" :class="{ 'positive': change > 0, 'negative': change < 0 }">
      <el-icon>
        <ArrowUp v-if="change > 0" />
        <ArrowDown v-else-if="change < 0" />
        <Minus v-else />
      </el-icon>
      <span>{{ Math.abs(change) }}</span>
      <span class="change-label">环比上月</span>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { ArrowUp, ArrowDown, Minus } from '@element-plus/icons-vue'

const props = defineProps({
  title: String,
  value: Number,
  change: Number,
  unit: String,
  expectation: Number
})

const formattedValue = computed(() => {
  return props.value.toFixed(1)
})

const formattedExpectation = computed(() => {
  if (props.expectation === null || props.expectation === undefined) return ''
  return props.expectation.toFixed(1)
})

const isUp = computed(() => props.change > 0)
const isDown = computed(() => props.change < 0)

const expectationClass = computed(() => {
  if (props.expectation === null || props.expectation === undefined) return ''
  const diff = props.value - props.expectation
  if (diff > 0) return 'better'
  if (diff < 0) return 'worse'
  return 'match'
})
</script>

<style scoped>
.kpi-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 8px;
  padding: 20px;
  color: white;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.kpi-card.up {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.kpi-card.down {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.kpi-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.kpi-title {
  font-size: 14px;
  opacity: 0.9;
}

.expectation-tag {
  font-size: 11px;
  padding: 2px 8px;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.2);
  cursor: help;
}

.expectation-tag.better {
  background: rgba(76, 175, 80, 0.3);
}

.expectation-tag.worse {
  background: rgba(244, 67, 54, 0.3);
}

.expectation-tag.match {
  background: rgba(255, 193, 7, 0.3);
}

.kpi-value {
  display: flex;
  align-items: baseline;
  gap: 4px;
  margin-bottom: 8px;
}

.number {
  font-size: 32px;
  font-weight: bold;
}

.unit {
  font-size: 14px;
  opacity: 0.8;
}

.kpi-change {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
  opacity: 0.9;
}

.kpi-change.positive {
  color: #ffeb3b;
}

.kpi-change.negative {
  color: #ffcdd2;
}

.change-label {
  margin-left: 4px;
  font-size: 12px;
  opacity: 0.7;
}
</style>
