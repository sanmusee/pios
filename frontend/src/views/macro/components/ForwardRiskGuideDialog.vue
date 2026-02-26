<template>
  <el-dialog
    v-model="visible"
    title="领先指标 + 风险偏好 + 全球扰动面板使用指南"
    width="850px"
    :close-on-click-modal="true"
    class="guide-dialog"
  >
    <div class="guide-content">
      <div class="guide-intro">
        <p>这个面板帮助你从三个维度预判市场走势：</p>
        <div class="dimensions">
          <div class="dimension-item">
            <div class="dim-title">领先变量</div>
            <div class="dim-desc">判断未来3-6个月经济方向</div>
          </div>
          <div class="dimension-item">
            <div class="dim-title">风险偏好</div>
            <div class="dim-desc">捕捉资金预期与情绪</div>
          </div>
          <div class="dimension-item">
            <div class="dim-title">全球扰动</div>
            <div class="dim-desc">监控外部冲击风险</div>
          </div>
        </div>
      </div>

      <el-collapse v-model="activeNames">
        <el-collapse-item name="1">
          <template #title>
            <div class="collapse-title">
              <span class="number-badge forward">1</span>
              <span>领先变量模块（Forward）</span>
            </div>
          </template>
          
          <div class="guide-section">
            <div class="indicator-list">
              <div class="indicator-item">
                <strong>社融同比</strong>：信用扩张的领先指标，社融增速见底通常领先经济见底3-6个月
              </div>
              <div class="indicator-item">
                <strong>M1-M2剪刀差</strong>：资金活化程度。负值扩大=资金沉淀，转正=经济活跃
              </div>
              <div class="indicator-item">
                <strong>PMI新订单</strong>：需求前瞻指标，>50表示需求扩张
              </div>
              <div class="indicator-item">
                <strong>库存周期</strong>：产成品库存同比，被动去库存=复苏信号
              </div>
              <div class="indicator-item">
                <strong>房地产销售</strong>：同比转正=地产周期见底
              </div>
              <div class="indicator-item">
                <strong>出口订单</strong>：外需前瞻，全球制造业PMI的镜像
              </div>
            </div>
          </div>
        </el-collapse-item>

        <el-collapse-item name="2">
          <template #title>
            <div class="collapse-title">
              <span class="number-badge risk">2</span>
              <span>风险偏好模块（Risk Appetite）</span>
            </div>
          </template>
          
          <div class="guide-section">
            <p class="section-intro">资产市场是<strong>预期放大器</strong>。经济数据还没变好，但股市提前上涨，说明资金在交易未来。</p>
            
            <div class="indicator-list">
              <div class="indicator-item">
                <strong>沪深300 vs 创业板</strong>：大盘价值vs小盘成长，风格切换信号
              </div>
              <div class="indicator-item">
                <strong>10Y国债收益率</strong>：下行=避险，上行=复苏预期
              </div>
              <div class="indicator-item">
                <strong>中美利差</strong>：负值扩大=资本外流压力，收窄=外资回流
              </div>
              <div class="indicator-item">
                <strong>信用利差</strong>：扩大=风险偏好下降，收窄=信用环境改善
              </div>
              <div class="indicator-item">
                <strong>人民币汇率</strong>：贬值=外资流出，升值=外资流入
              </div>
              <div class="indicator-item">
                <strong>北向资金</strong>：连续流入=外资看好A股
              </div>
            </div>
          </div>
        </el-collapse-item>

        <el-collapse-item name="3">
          <template #title>
            <div class="collapse-title">
              <span class="number-badge external">3</span>
              <span>全球扰动模块（External Shock）</span>
            </div>
          </template>
          
          <div class="guide-section">
            <p class="section-intro">中国宏观永远不是封闭系统。外部冲击可能打断国内周期。</p>
            
            <div class="indicator-list">
              <div class="indicator-item">
                <strong>10Y美债收益率</strong>：全球资产定价锚，上升=全球流动性收紧
              </div>
              <div class="indicator-item">
                <strong>美元指数(DXY)</strong>：走强=新兴市场承压，走弱=风险资产上涨
              </div>
              <div class="indicator-item">
                <strong>原油价格</strong>：通胀输入指标，暴涨=滞胀风险
              </div>
              <div class="indicator-item">
                <strong>CRB商品指数</strong>：全球需求晴雨表
              </div>
              <div class="indicator-item">
                <strong>VIX恐慌指数</strong>：>20=全球市场恐慌，<15=风险偏好高
              </div>
            </div>
          </div>
        </el-collapse-item>
      </el-collapse>
    </div>

    <template #footer>
      <el-button type="primary" @click="visible = false">知道了</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, defineExpose } from 'vue'

const visible = ref(false)
const activeNames = ref(['1'])

const open = () => {
  visible.value = true
}

defineExpose({
  open
})
</script>

<style scoped>
.guide-dialog :deep(.el-dialog__header) {
  background: linear-gradient(135deg, #1a2980 0%, #26d0ce 100%);
  margin-right: 0;
  padding: 20px;
}

.guide-dialog :deep(.el-dialog__title) {
  color: white;
  font-weight: 600;
}

.guide-dialog :deep(.el-dialog__headerbtn .el-dialog__close) {
  color: white;
}

.guide-content {
  padding: 10px 0;
}

.guide-intro {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.guide-intro p {
  margin: 0 0 16px 0;
  color: #606266;
  font-size: 14px;
}

.dimensions {
  display: flex;
  gap: 16px;
  justify-content: center;
}

.dimension-item {
  text-align: center;
  padding: 12px 24px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.dim-title {
  font-weight: 600;
  font-size: 15px;
  color: #303133;
  margin-bottom: 4px;
}

.dim-desc {
  font-size: 12px;
  color: #909399;
}

.collapse-title {
  display: flex;
  align-items: center;
  gap: 12px;
  font-weight: 600;
  font-size: 15px;
}

.number-badge {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 14px;
  color: white;
}

.number-badge.forward {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.number-badge.risk {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.number-badge.external {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.guide-section {
  padding: 10px 0;
}

.section-intro {
  margin: 0 0 16px 0;
  padding: 12px;
  background: #fff7e6;
  border-left: 4px solid #fa8c16;
  border-radius: 4px;
  color: #606266;
  line-height: 1.6;
}

.indicator-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.indicator-item {
  padding: 10px 12px;
  background: #f5f7fa;
  border-radius: 6px;
  font-size: 14px;
  color: #606266;
  line-height: 1.5;
}

.indicator-item strong {
  color: #303133;
}

:deep(.el-collapse-item__header) {
  padding: 16px;
  font-size: 15px;
}

:deep(.el-collapse-item__content) {
  padding: 0 16px 16px 56px;
}
</style>
