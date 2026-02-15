<template>
  <div class="data-release-countdown">
    <div class="countdown-header">
      <el-icon><Clock /></el-icon>
      <span>数据发布倒计时</span>
    </div>
    
    <div class="countdown-items">
      <div v-for="item in upcomingReleases" :key="item.name" class="countdown-item">
        <div class="item-name">{{ item.name }}</div>
        <div class="item-date">{{ item.date }}</div>
        
        <div class="item-countdown" :class="getCountdownClass(item.days)">
          <span v-if="item.days === 0">今日发布</span>
          <span v-else-if="item.days === 1">明日发布</span>
          <span v-else>{{ item.days }} 天后</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { Clock } from '@element-plus/icons-vue'
import dayjs from 'dayjs'

const getNextReleaseDate = (dayOfMonth) => {
  const today = dayjs()
  const currentMonth = today.month()
  const currentYear = today.year()
  
  let releaseDate = dayjs().year(currentYear).month(currentMonth).date(dayOfMonth)
  
  if (releaseDate.isBefore(today, 'day')) {
    releaseDate = releaseDate.add(1, 'month')
  }
  
  return releaseDate
}

const upcomingReleases = computed(() => {
  const releases = [
    { name: 'CPI/PPI', day: 9 },
    { name: '金融数据', day: 12 },
    { name: 'PMI', day: 1 },
    { name: 'LPR', day: 20 }
  ]
  
  return releases.map(release => {
    const date = getNextReleaseDate(release.day)
    const days = date.diff(dayjs(), 'day')
    
    return {
      name: release.name,
      date: date.format('MM月DD日'),
      days: days
    }
  }).sort((a, b) => a.days - b.days)
})

const getCountdownClass = (days) => {
  if (days === 0) return 'urgent'
  if (days <= 3) return 'soon'
  return 'normal'
}
</script>

<style scoped>
.data-release-countdown {
  background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
  border-radius: 8px;
  padding: 16px 20px;
  color: white;
  margin-bottom: 16px;
}

.countdown-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 12px;
  padding-bottom: 12px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

.countdown-items {
  display: flex;
  gap: 24px;
}

.countdown-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.item-name {
  font-size: 14px;
  opacity: 0.8;
}

.item-date {
  font-size: 13px;
  opacity: 0.6;
}

.item-countdown {
  font-size: 14px;
  font-weight: 600;
  padding: 4px 12px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.1);
  display: inline-block;
  margin-top: 4px;
}

.item-countdown.urgent {
  background: #f5576c;
  animation: pulse 2s infinite;
}

.item-countdown.soon {
  background: #fac858;
  color: #333;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.7; }
}
</style>
