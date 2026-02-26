<template>
  <el-dialog
    v-model="visible"
    title="财政与政策强度面板 - 使用指南"
    width="800px"
    :close-on-click-modal="true"
    class="guide-dialog"
  >
    <div class="guide-content">
      <div class="guide-intro">
        <p>财政 = 真实需求的启动器。在中国，财政政策往往比货币政策更能决定经济走向。</p>
      </div>

      <el-collapse v-model="activeNames">
        <el-collapse-item name="1" title="专项债发行进度">
          <div class="guide-section">
            <p>专项债是财政政策的核心抓手。3.8万亿额度通常在9月底前要完成80%以上发行。</p>
            <div class="insight-box">
              <strong>关键阈值：</strong><br/>
              • 6月底 > 50%：政策积极<br/>
              • 9月底 > 80%：正常进度<br/>
              • 低于60%：政策偏保守
            </div>
          </div>
        </el-collapse-item>

        <el-collapse-item name="2" title="财政支出结构">
          <div class="guide-section">
            <p>观察财政资金流向：基建占比提升=稳增长意愿强；民生占比高=兜底思维。</p>
          </div>
        </el-collapse-item>

        <el-collapse-item name="3" title="基建投资细分">
          <div class="guide-section">
            <p>交运、水利、能源三大领域。交运回升=经济活跃；水利发力=逆周期调节。</p>
          </div>
        </el-collapse-item>

        <el-collapse-item name="4" title="政策强度判定">
          <div class="guide-section">
            <div class="threshold-list">
              <div class="threshold-item">
                <span class="tag danger">强刺激</span>
                <span>专项债进度>80% + 财政支出>10%</span>
              </div>
              <div class="threshold-item">
                <span class="tag warning">积极</span>
                <span>专项债进度60-80% + 财政支出8-10%</span>
              </div>
              <div class="threshold-item">
                <span class="tag success">温和</span>
                <span>专项债进度40-60% + 财政支出5-8%</span>
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
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
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

.threshold-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.threshold-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px;
  background: #f5f7fa;
  border-radius: 6px;
}

.tag {
  padding: 4px 12px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 600;
  color: white;
}

.tag.danger { background: #f5222d; }
.tag.warning { background: #fa8c16; }
.tag.success { background: #52c41a; }
</style>
