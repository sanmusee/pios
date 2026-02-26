<template>
  <el-dialog
    v-model="visible"
    title="盈利与现金流面板 - 使用指南"
    width="800px"
    :close-on-click-modal="true"
    class="guide-dialog"
  >
    <div class="guide-content">
      <div class="guide-intro">
        <p>利润 → 现金流 → 分红能力 → 股价持续性。避免"估值修复行情"误判为"盈利驱动行情"。</p>
      </div>

      <el-collapse v-model="activeNames">
        <el-collapse-item name="1" title="工业企业利润 vs 全A净利润">
          <div class="guide-section">
            <p>工业企业利润领先上市公司利润1-2个月。如果工业企业利润回升但上市公司利润未跟上，说明盈利修复尚未传导至股市。</p>
            <div class="insight-box warning">
              <strong>盈利匹配度警告：</strong><br/>
              • 偏离：工业企业利润 > 上市公司利润 +3%<br/>
              • 滞后：工业企业利润 < 上市公司利润 -2%
            </div>
          </div>
        </el-collapse-item>

        <el-collapse-item name="2" title="ROE趋势分解">
          <div class="guide-section">
            <p>杜邦分析：ROE = 净利率 × 资产周转率 × 杠杆率。净利率提升=议价能力增强；周转率提升=需求回暖。</p>
          </div>
        </el-collapse-item>

        <el-collapse-item name="3" title="自由现金流与分红">
          <div class="guide-section">
            <p>现金流质量决定分红可持续性。FCF > 分红=安全；FCF < 分红=透支。</p>
          </div>
        </el-collapse-item>

        <el-collapse-item name="4" title="盈利预期修正">
          <div class="guide-section">
            <p>分析师一致预期变化。上调家数 > 下调家数=乐观；连续3月净下调=警惕。</p>
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
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
  margin-right: 0;
  padding: 20px;
}

.guide-dialog :deep(.el-dialog__title) {
  color: white;
  font-weight: 600;
}

.guide-content {
  padding: 10px 0;
}

.guide-intro {
  background: #f5f7fa;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 16px;
}

.guide-section {
  padding: 10px 0;
}

.insight-box {
  background: #fff7e6;
  border-left: 4px solid #fa8c16;
  padding: 12px;
  border-radius: 4px;
  margin-top: 12px;
}

.insight-box.warning {
  background: #fff1f0;
  border-left-color: #f5222d;
}
</style>
