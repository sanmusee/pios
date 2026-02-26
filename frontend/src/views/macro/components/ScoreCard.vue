<template>
  <div class="score-card" :style="{ borderColor: color }">
    <div class="score-icon" :style="{ backgroundColor: color + '20', color: color }">
      <el-icon><component :is="icon" /></el-icon>
    </div>
    <div class="score-info">
      <div class="score-title">{{ title }}</div>
      <div class="score-value" :style="{ color: color }">
        {{ score }}<span class="score-max">/{{ max }}</span>
      </div>    
    </div>
    <div class="score-progress">
      <div 
        class="progress-bar" 
        :style="{ 
          width: (score / max * 100) + '%',
          backgroundColor: color 
        }"
      />
    </div>
  </div>
</template>

<script setup>
defineProps({
  title: String,
  score: Number,
  max: Number,
  icon: String,
  color: String
})
</script>

<style scoped>
.score-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
  height: 100%;
  border-left: 4px solid;
  display: flex;
  flex-direction: column;
}

.score-icon {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  margin-bottom: 12px;
}

.score-info {
  flex: 1;
}

.score-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.score-value {
  font-size: 32px;
  font-weight: bold;
}

.score-max {
  font-size: 16px;
  color: #c0c4cc;
  font-weight: normal;
}

.score-progress {
  height: 6px;
  background: #ebeef5;
  border-radius: 3px;
  margin-top: 12px;
  overflow: hidden;
}

.progress-bar {
  height: 100%;
  border-radius: 3px;
  transition: width 0.3s ease;
}
</style>
