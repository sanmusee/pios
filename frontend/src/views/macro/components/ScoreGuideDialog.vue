<template>
  <el-dialog
    v-model="visible"
    title="宏观评分系统使用指南"
    width="850px"
    :close-on-click-modal="true"
    class="guide-dialog"
  >
    <div class="guide-content">
      <div class="guide-intro">
        <p>三大评分维度，每个 0-5 分，总分 0-15 分。通过组合结构判定宏观相位。</p>
        <div class="dimensions">
          <div class="dimension-item">
            <div class="dim-score">0-5分</div>
            <div class="dim-title">流动性</div>
            <div class="dim-desc">Liquidity</div>
          </div>
          <div class="dimension-item">
            <div class="dim-score">0-5分</div>
            <div class="dim-title">经济动能</div>
            <div class="dim-desc">Growth</div>
          </div>
          <div class="dimension-item">
            <div class="dim-score">0-5分</div>
            <div class="dim-title">风险偏好</div>
            <div class="dim-desc">Risk</div>
          </div>
        </div>
      </div>

      <el-collapse v-model="activeNames">
        <el-collapse-item name="1">
          <template #title>
            <div class="collapse-title">
              <span class="number-badge">1</span>
              <span>流动性评分（0-5分）</span>
            </div>
          </template>
          
          <div class="guide-section">
            <div class="score-levels">
              <div class="level-item"><span class="level-tag success">4-5分</span>流动性宽松</div>
              <div class="level-item"><span class="level-tag warning">2-3分</span>中性</div>
              <div class="level-item"><span class="level-tag danger">0-1分</span>偏紧</div>
            </div>
            
            <div class="rule-list">
              <div class="rule-item"><strong>M2同比：</strong>≥11% → +2分；≥9% → +1分</div>
              <div class="rule-item"><strong>社融趋势：</strong>连续3个月上行 → +1分</div>
              <div class="rule-item"><strong>M1-M2剪刀差：</strong>缩小 → +1分</div>
              <div class="rule-item"><strong>DR007：</strong>低于近3年中位数 → +1分</div>
              <div class="rule-item"><strong>10Y国债：</strong>下行趋势 → +1分</div>
            </div>
          </div>
        </el-collapse-item>

        <el-collapse-item name="2">
          <template #title>
            <div class="collapse-title">
              <span class="number-badge">2</span>
              <span>经济动能评分（0-5分）</span>
            </div>
          </template>
          
          <div class="guide-section">
            <div class="score-levels">
              <div class="level-item"><span class="level-tag success">4-5分</span>扩张期</div>
              <div class="level-item"><span class="level-tag warning">2-3分</span>弱复苏</div>
              <div class="level-item"><span class="level-tag danger">0-1分</span>收缩</div>
            </div>
            
            <div class="rule-list">
              <div class="rule-item"><strong>PMI：</strong>≥50 → +1分；连续2月上行 → +1分</div>
              <div class="rule-item"><strong>PMI新订单：</strong>≥50 → +1分</div>
              <div class="rule-item"><strong>产成品库存：</strong>连续下降 → +1分</div>
              <div class="rule-item"><strong>工业企业利润：</strong>同比转正 → +1分</div>
              <div class="rule-item"><strong>PPI：</strong>回升趋势 → +1分</div>
            </div>
          </div>
        </el-collapse-item>

        <el-collapse-item name="3">
          <template #title>
            <div class="collapse-title">
              <span class="number-badge">3</span>
              <span>风险偏好评分（0-5分）</span>
            </div>
          </template>
          
          <div class="guide-section">
            <div class="score-levels">
              <div class="level-item"><span class="level-tag success">4-5分</span>风险偏好修复</div>
              <div class="level-item"><span class="level-tag warning">2-3分</span>中性</div>
              <div class="level-item"><span class="level-tag danger">0-1分</span>防御期</div>
            </div>
            
            <div class="rule-list">
              <div class="rule-item"><strong>中美利差：</strong>倒挂收窄 → +1分</div>
              <div class="rule-item"><strong>USD/CNH：</strong>稳定或升值 → +1分</div>
              <div class="rule-item"><strong>北向资金：</strong>连续5日净流入 → +1分</div>
              <div class="rule-item"><strong>ERP：</strong>+1σ → +1分；+2σ → +2分</div>
              <div class="rule-item"><strong>美债收益率：</strong>下行趋势 → +1分</div>
            </div>
          </div>
        </el-collapse-item>

        <el-collapse-item name="4">
          <template #title>
            <div class="collapse-title">
              <span class="number-badge phase">★</span>
              <span>宏观相位判定逻辑</span>
            </div>
          </template>
          
          <div class="guide-section">
            <div class="phase-list">
              <div class="phase-item">
                <div class="phase-title expansion">扩张期</div>
                <div class="phase-condition">流动性≥3 + 动能≥4 + 风险≥3</div>
                <div class="phase-desc">顺周期阶段，积极做多权益</div>
              </div>
              
              <div class="phase-item">
                <div class="phase-title structural">结构性行情</div>
                <div class="phase-condition">流动性≥4 + 动能≤3 + 风险≥3</div>
                <div class="phase-desc">流动性驱动，关注主题投资</div>
              </div>
              
              <div class="phase-item">
                <div class="phase-title policy">政策托底期</div>
                <div class="phase-condition">流动性≥4 + 动能≤2 + 风险≤2</div>
                <div class="phase-desc">政策强托，市场犹豫</div>
              </div>
              
              <div class="phase-item">
                <div class="phase-title defensive">防御期</div>
                <div class="phase-condition">流动性≤2 + 动能≤2 + 风险≤2</div>
                <div class="phase-desc">现金为王，等待出清</div>
              </div>
              
              <div class="phase-item">
                <div class="phase-title stagflation">滞胀风险期</div>
                <div class="phase-condition">流动性≥3 + PPI上行 + 动能下降</div>
                <div class="phase-desc">警惕成长股杀估值</div>
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
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
  padding: 16px 24px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.dim-score {
  font-size: 12px;
  color: #909399;
  margin-bottom: 4px;
}

.dim-title {
  font-weight: 600;
  font-size: 16px;
  color: #303133;
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
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 14px;
}

.number-badge.phase {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.guide-section {
  padding: 10px 0;
}

.score-levels {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
  flex-wrap: wrap;
}

.level-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #606266;
}

.level-tag {
  padding: 4px 12px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 600;
}

.level-tag.success {
  background: #f6ffed;
  color: #52c41a;
}

.level-tag.warning {
  background: #fff7e6;
  color: #fa8c16;
}

.level-tag.danger {
  background: #fff1f0;
  color: #f5222d;
}

.rule-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.rule-item {
  padding: 10px 12px;
  background: #f5f7fa;
  border-radius: 6px;
  font-size: 14px;
  color: #606266;
}

.rule-item strong {
  color: #303133;
}

.phase-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.phase-item {
  padding: 12px;
  background: #f5f7fa;
  border-radius: 6px;
  border-left: 4px solid;
}

.phase-title {
  font-weight: 600;
  font-size: 15px;
  margin-bottom: 4px;
}

.phase-title.expansion { color: #52c41a; border-color: #52c41a; }
.phase-title.structural { color: #1890ff; border-color: #1890ff; }
.phase-title.policy { color: #fa8c16; border-color: #fa8c16; }
.phase-title.defensive { color: #f5222d; border-color: #f5222d; }
.phase-title.stagflation { color: #722ed1; border-color: #722ed1; }

.phase-condition {
  font-size: 13px;
  color: #909399;
  margin-bottom: 4px;
}

.phase-desc {
  font-size: 13px;
  color: #606266;
}

:deep(.el-collapse-item__header) {
  padding: 16px;
  font-size: 15px;
}

:deep(.el-collapse-item__content) {
  padding: 0 16px 16px 56px;
}
</style>
