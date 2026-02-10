<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card class="welcome-card">
          <template #header>
            <div class="card-header">
              <span>欢迎使用 PIOS</span>
            </div>
          </template>
          <p>个人级世界结构认知操作系统</p>
          <p class="subtitle">帮助您在信息爆炸时代建立长期世界模型，识别结构级变化</p>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" class="mt-20">
      <el-col :span="6">
        <el-card>
          <template #header>
            <div class="card-header">
              <el-icon><Download /></el-icon>
              <span>信息采集</span>
            </div>
          </template>
          <div class="stat-number">0</div>
          <div class="stat-label">今日采集信号</div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card>
          <template #header>
            <div class="card-header">
              <el-icon><Filter /></el-icon>
              <span>信号分级</span>
            </div>
          </template>
          <div class="stat-number">0</div>
          <div class="stat-label">Level 3 信号</div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card>
          <template #header>
            <div class="card-header">
              <el-icon><Connection /></el-icon>
              <span>关联分析</span>
            </div>
          </template>
          <div class="stat-number">0</div>
          <div class="stat-label">活跃因果链</div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card>
          <template #header>
            <div class="card-header">
              <el-icon><Document /></el-icon>
              <span>日报中心</span>
            </div>
          </template>
          <div class="stat-number">0</div>
          <div class="stat-label">已生成日报</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getSystemInfo } from '@/api/system'

const systemInfo = ref(null)

onMounted(async () => {
  try {
    const res = await getSystemInfo()
    systemInfo.value = res.data
  } catch (error) {
    console.error('Failed to load system info:', error)
  }
})
</script>

<style scoped>
.dashboard {
  padding: 0;
}

.welcome-card {
  margin-bottom: 0;
}

.welcome-card p {
  font-size: 16px;
  margin: 10px 0;
}

.welcome-card .subtitle {
  color: #909399;
  font-size: 14px;
}

.mt-20 {
  margin-top: 20px;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
}

.stat-number {
  font-size: 32px;
  font-weight: bold;
  color: #409EFF;
  text-align: center;
  margin: 20px 0;
}

.stat-label {
  text-align: center;
  color: #909399;
  font-size: 14px;
}
</style>
