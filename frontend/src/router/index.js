import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/components/Layout.vue'

const routes = [
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '概览' }
      },
      {
        path: 'raw-layer',
        name: 'RawLayer',
        component: () => import('@/views/RawLayer.vue'),
        meta: { title: '信息采集' }
      },
      {
        path: 'filter-layer',
        name: 'FilterLayer',
        component: () => import('@/views/FilterLayer.vue'),
        meta: { title: '信号分级' }
      },
      {
        path: 'correlation-layer',
        name: 'CorrelationLayer',
        component: () => import('@/views/CorrelationLayer.vue'),
        meta: { title: '关联分析' }
      },
      {
        path: 'inference-layer',
        name: 'InferenceLayer',
        component: () => import('@/views/InferenceLayer.vue'),
        meta: { title: '推演系统' }
      },
      {
        path: 'report-layer',
        name: 'ReportLayer',
        component: () => import('@/views/ReportLayer.vue'),
        meta: { title: '日报中心' }
      },
      {
        path: 'macro-dashboard',
        name: 'MacroDashboard',
        component: () => import('@/views/macro/MacroDashboard.vue'),
        meta: { title: '宏观经济链路监控' }
      },
      {
        path: 'forward-risk-panel',
        name: 'ForwardRiskPanel',
        component: () => import('@/views/macro/ForwardRiskPanel.vue'),
        meta: { title: '领先指标与风险偏好' }
      },
      {
        path: 'macro-score-panel',
        name: 'MacroScorePanel',
        component: () => import('@/views/macro/MacroScorePanel.vue'),
        meta: { title: '宏观评分与相位判定' }
      },
      {
        path: 'global-asset-panel',
        name: 'GlobalAssetPanel',
        component: () => import('@/views/macro/GlobalAssetPanel.vue'),
        meta: { title: '全球定价与资产锚点' }
      },
      {
        path: 'profit-cash-panel',
        name: 'ProfitCashPanel',
        component: () => import('@/views/macro/ProfitCashPanel.vue'),
        meta: { title: '盈利与现金流' }
      },
      {
        path: 'policy-panel',
        name: 'PolicyPanel',
        component: () => import('@/views/macro/PolicyPanel.vue'),
        meta: { title: '财政与政策强度' }
      },
      {
        path: 'household-panel',
        name: 'HouseholdPanel',
        component: () => import('@/views/macro/HouseholdPanel.vue'),
        meta: { title: '居民资产负债表' }
      },
      {
        path: 'sector-rotation-panel',
        name: 'SectorRotationPanel',
        component: () => import('@/views/macro/SectorRotationPanel.vue'),
        meta: { title: '行业景气度与风格轮动' }
      },
      {
        path: 'raw-news-list',
        name: 'RawNewsList',
        component: () => import('@/views/structured/RawNewsList.vue'),
        meta: { title: '原始数据列表' }
      },
      {
        path: 'info-flow-console',
        name: 'InfoFlowConsole',
        component: () => import('@/views/structured/InfoFlowConsole.vue'),
        meta: { title: '信息流控制台' }
      },
      {
        path: 'structural-variables',
        name: 'StructuralVariables',
        component: () => import('@/views/structured/StructuralVariables.vue'),
        meta: { title: '结构变量面板' }
      },
      {
        path: 'strategic-observation-pool',
        name: 'StrategicObservationPool',
        component: () => import('@/views/structured/StrategicObservationPool.vue'),
        meta: { title: '战略观察池' }
      },
      {
        path: 'stock-dashboard',
        name: 'StockDashboard',
        component: () => import('@/views/stock/StockDashboard.vue'),
        meta: { title: '量化仪表盘' }
      },
      {
        path: 'stock-analysis',
        name: 'StockAnalysis',
        component: () => import('@/views/stock/StockAnalysis.vue'),
        meta: { title: '股票分析' }
      },
      {
        path: 'stock-screener',
        name: 'StockScreener',
        component: () => import('@/views/stock/StockScreener.vue'),
        meta: { title: '智能选股器' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
