<template>
  <el-dialog
    v-model="visible"
    title="宏观经济链路看板使用指南"
    width="900px"
    :close-on-click-modal="true"
    class="guide-dialog"
  >
    <div class="guide-content">
      <div class="guide-intro">
        <p>这个看板的四个模块，正好对应宏观经济的四个阶段：</p>
        <div class="flow-diagram">
          <span class="flow-item">政策底</span>
          <el-icon><ArrowRight /></el-icon>
          <span class="flow-item">情绪底</span>
          <el-icon><ArrowRight /></el-icon>
          <span class="flow-item">市场底</span>
          <el-icon><ArrowRight /></el-icon>
          <span class="flow-item">经济底</span>
        </div>
      </div>

      <el-collapse v-model="activeNames">
        <el-collapse-item name="1">
          <template #title>
            <div class="collapse-title">
              <span class="number-badge">1</span>
              <span>寻找"水往哪里流"：源头 → 传导</span>
            </div>
          </template>
          
          <div class="guide-section">
            <div class="section-tag phenomenon">现象</div>
            <p>如果 M2 很高，但 M1-M2 柱状图一直向下，且中长期贷款（红色柱子）占比很小。</p>
            
            <div class="section-tag meaning">深层内涵</div>
            <p>说明"水"只流到了银行端，没流进企业口袋，更没进股市。这时候指数（紫色虚线）的上涨通常是无力的，属于"存量博弈"。</p>
            
            <div class="section-tag signal">转机信号</div>
            <p>当 M1-M2 的负向柱子开始缩短，甚至翻红向上，那就是"水"开始激活的时刻。</p>
          </div>
        </el-collapse-item>

        <el-collapse-item name="2">
          <template #title>
            <div class="collapse-title">
              <span class="number-badge">2</span>
              <span>判定"景气真伪"：传导 → 结果</span>
            </div>
          </template>
          
          <div class="guide-section">
            <div class="section-tag phenomenon">现象</div>
            <p>如果社融数据暴涨，但随后几个月的 PPI（蓝线）依然在 -2.5% 徘徊，甚至继续下跌。</p>
            
            <div class="section-tag meaning">深层内涵</div>
            <p>说明信用扩张是"虚假繁荣"，可能是地方债在发力，或者企业在"借新还旧"，实体工业的实际需求并没有回来。</p>
            
            <div class="section-tag signal">转机信号</div>
            <p>PPI 触底反弹，与 CPI 的负向剪刀差收窄，说明制造业企业的利润空间正在修复。</p>
          </div>
        </el-collapse-item>

        <el-collapse-item name="3">
          <template #title>
            <div class="collapse-title">
              <span class="number-badge">3</span>
              <span>捕捉"预期差"：KPI 卡片 → 市场反应</span>
            </div>
          </template>
          
          <div class="guide-section">
            <div class="section-tag phenomenon">现象</div>
            <p>数据发布当日，数值虽好（如 PMI 50.1），但远低于预期（如预期 51.0）。</p>
            
            <div class="section-tag meaning">深层内涵</div>
            <p>市场会把这种"好数据"当作"利好出尽"或"不及预期"来杀估值。</p>
            
            <div class="section-tag signal">转机信号</div>
            <p>当数据本身一般（如 PMI 49.5），但显著高于预期（如预期 48.0），这往往是情绪大级别反弹的起点。</p>
          </div>
        </el-collapse-item>

        <el-collapse-item name="4">
          <template #title>
            <div class="collapse-title">
              <span class="number-badge">4</span>
              <span>观察"政策底"：结果 → 反馈</span>
            </div>
          </template>
          
          <div class="guide-section">
            <div class="section-tag phenomenon">现象</div>
            <p>结果区的 CPI/PPI 持续低迷，而反馈区的 LPR 阶梯加速下行。</p>
            
            <div class="section-tag meaning">深层内涵</div>
            <p>这就是典型的"政策底"。政策端已经开始疯狂喊你起床了。</p>
            
            <div class="section-tag signal">核心逻辑</div>
            <p>宏观逻辑通常是 "政策底 → 情绪底 → 市场底 → 经济底"。你这个看板的四个模块，正好覆盖了这四个阶段。</p>
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
import { ArrowRight } from '@element-plus/icons-vue'

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
  background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
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

.flow-diagram {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  flex-wrap: wrap;
}

.flow-item {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 8px 20px;
  border-radius: 20px;
  font-weight: 600;
  font-size: 14px;
}

.flow-diagram .el-icon {
  color: #909399;
  font-size: 20px;
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

.guide-section {
  padding: 10px 0;
}

.guide-section p {
  margin: 8px 0 16px 0;
  line-height: 1.6;
  color: #606266;
}

.section-tag {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 600;
  margin-bottom: 8px;
}

.section-tag.phenomenon {
  background: #e6f7ff;
  color: #1890ff;
  border: 1px solid #91d5ff;
}

.section-tag.meaning {
  background: #fff7e6;
  color: #fa8c16;
  border: 1px solid #ffd591;
}

.section-tag.signal {
  background: #f6ffed;
  color: #52c41a;
  border: 1px solid #b7eb8f;
}

:deep(.el-collapse-item__header) {
  padding: 16px;
  font-size: 15px;
}

:deep(.el-collapse-item__content) {
  padding: 0 16px 16px 56px;
}
</style>
