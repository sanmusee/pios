<template>
  <div 
    class="rule-card" 
    :class="{ active: rule.active }"
    @click="$emit('toggle')"
  >
    <div class="rule-header">
      <div class="rule-name">{{ rule.name }}</div>
      <div class="rule-score" :class="{ zero: rule.score === 0 }">
        +{{ rule.score }}分
      </div>
    </div>    
    
    <div class="rule-desc">{{ rule.desc }}</div>
    
    <div class="rule-status">
      <el-tag :type="rule.active ? 'success' : 'info'" size="small">
        {{ rule.active ? '已触发' : '未触发' }}
      </el-tag>
    </div>
  </div>
</template>

<script setup>
defineProps({
  rule: Object
})

defineEmits(['toggle'])
</script>

<style scoped>
.rule-card {
  background: #f5f7fa;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.rule-card:hover {
  background: #e6f7ff;
  border-color: #91d5ff;
}

.rule-card.active {
  background: #f6ffed;
  border-color: #b7eb8f;
}

.rule-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.rule-name {
  font-weight: 600;
  font-size: 15px;
  color: #303133;
}

.rule-score {
  font-size: 14px;
  font-weight: bold;
  color: #52c41a;
  background: #f6ffed;
  padding: 4px 8px;
  border-radius: 4px;
}

.rule-score.zero {
  color: #8c8c8c;
  background: #f5f5f5;
}

.rule-desc {
  font-size: 13px;
  color: #606266;
  line-height: 1.5;
  margin-bottom: 12px;
}

.rule-status {
  display: flex;
  justify-content: flex-end;
}
</style>
