<template>
  <el-container class="layout-container">
    <el-aside width="200px" class="sidebar">
      <div class="logo">
        <h2>PIOS</h2>
        <p>认知操作系统</p>
      </div>
      <el-menu
        :default-active="$route.path"
        router
        class="el-menu-vertical"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/dashboard">
          <el-icon><DataLine /></el-icon>
          <span>概览</span>
        </el-menu-item>
        
        <el-menu-item index="/raw-layer">
          <el-icon><Download /></el-icon>
          <span>信息采集</span>
        </el-menu-item>
        
        <el-menu-item index="/filter-layer">
          <el-icon><Filter /></el-icon>
          <span>信号分级</span>
        </el-menu-item>
        
        <el-menu-item index="/correlation-layer">
          <el-icon><Connection /></el-icon>
          <span>关联分析</span>
        </el-menu-item>
        
        <el-menu-item index="/inference-layer">
          <el-icon><TrendCharts /></el-icon>
          <span>推演系统</span>
        </el-menu-item>
        
        <el-menu-item index="/report-layer">
          <el-icon><Document /></el-icon>
          <span>日报中心</span>
        </el-menu-item>

        <el-sub-menu index="/macro">
          <template #title>
            <el-icon><TrendCharts /></el-icon>
            <span>宏观监控</span>
          </template>
          <el-menu-item index="/macro-dashboard">
            <span>经济链路监控</span>
          </el-menu-item>
          <el-menu-item index="/forward-risk-panel">
            <span>领先指标与风险</span>
          </el-menu-item>
          <el-menu-item index="/macro-score-panel">
            <span>评分与相位判定</span>
          </el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>
    
    <el-container>
      <el-header class="header">
        <div class="header-left">
          <breadcrumb />
        </div>
        <div class="header-right">
          <el-dropdown>
            <span class="user-info">
              <el-icon><User /></el-icon>
              管理员
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>个人设置</el-dropdown-item>
                <el-dropdown-item divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import Breadcrumb from './Breadcrumb.vue'
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.sidebar {
  background-color: #304156;
}

.logo {
  height: 60px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: #fff;
  border-bottom: 1px solid #1f2d3d;
}

.logo h2 {
  margin: 0;
  font-size: 20px;
}

.logo p {
  margin: 0;
  font-size: 12px;
  color: #909399;
}

.el-menu-vertical {
  border-right: none;
}

.header {
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
}

.main-content {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>
