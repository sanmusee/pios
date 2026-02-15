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
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
