<template>
  <el-dialog
    v-model="visible"
    title="居民资产负债表 - 使用指南"
    width="800px"
    :close-on-click-modal="true"
    class="guide-dialog"
  >
    <div class="guide-content">
      <div class="guide-intro">
        <p>宏观的核心不是GDP，是居民是否愿意加杠杆。判断消费复苏是否可持续。</p>
      </div>

      <el-collapse v-model="activeNames">
        <el-collapse-item name="1" title="居民存贷款增速">
          <div class="guide-section">
            <p>存款高增 + 贷款低迷 = 资产负债表修复期。居民在去杠杆而非加杠杆，复苏很难强。</p>
            <div class="insight-box">
              <strong>关键信号：</strong><br/>
              • 存款增速 > 10% + 贷款增速 < 6% = 修复中<br/>
              • 存款增速 < 8% + 贷款增速 > 8% = 扩张期
            </div>
          </div>
        </el-collapse-item>

        <el-collapse-item name="2" title="房贷与消费贷结构">
          <div class="guide-section">
            <p>房贷占比下降=地产去杠杆；消费贷占比上升=消费意愿恢复。关注两者拐点。</p>
          </div>
        </el-collapse-item>

        <el-collapse-item name="3" title="储蓄率趋势">
          <div class="guide-section">
            <p>预防性储蓄是否见顶。储蓄率 > 35% = 防御心态；储蓄率 < 32% = 消费释放。</p>
          </div>
        </el-collapse-item>

        <el-collapse-item name="4" title="居民杠杆率">
          <div class="guide-section">
            <p>债务/GDP。62%为警戒线，超过可能触发去杠杆。当前中国居民杠杆率已接近发达国家水平。</p>
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
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
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
  background: #e6f7ff;
  border-left: 4px solid #1890ff;
  padding: 12px;
  border-radius: 4px;
  margin-top: 12px;
}
</style>
