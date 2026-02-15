<template>
  <el-dialog
    v-model="visible"
    title="全球定价与大类资产锚点 - 使用指南"
    width="850px"
    :close-on-click-modal="true"
    class="guide-dialog"
  >
    <div class="guide-content">
      <div class="guide-intro">
        <p>这四个模块共同构成了资金流向的因果链，帮助你理解全球流动性如何影响A股定价。</p>
        <div class="flow-chain">
          <div class="chain-item">
            <div class="chain-tag primary">因</div>
            <div class="chain-title">全球定价锚</div>
          </div>
          <el-icon><ArrowRight /></el-icon>
          <div class="chain-item">
            <div class="chain-tag success">果</div>
            <div class="chain-title">汇率外资</div>
          </div>
          <el-icon><ArrowRight /></el-icon>
          <div class="chain-item">
            <div class="chain-tag warning">择时</div>
            <div class="chain-title">风险溢价</div>
          </div>
          <el-icon><ArrowRight /></el-icon>
          <div class="chain-item">
            <div class="chain-tag danger">前瞻</div>
            <div class="chain-title">实体需求</div>
          </div>
        </div>
      </div>

      <el-collapse v-model="activeNames">
        <el-collapse-item name="1">
          <template #title>
            <div class="collapse-title">
              <span class="number-badge primary">1</span>
              <span>全球定价之锚（因）</span>
            </div>
          </template>
          
          <div class="guide-section">
            <p class="section-intro"><strong>10年美债 vs 10年国债</strong> - 全球资产定价的终极参考点</p>            
            
            <div class="insight-box">
              <div class="insight-title">核心逻辑</div>
              <p>当美债收益率上行，而国债收益率平稳甚至下行时，中美利差倒挂加剧。这会直接驱动人民币贬值压力和北向资金流出。</p>
            </div>
            
            <div class="insight-box warning">
              <div class="insight-title">交易信号</div>
              <p>如果你看到10年美债狂飙，A股的成长股（高估值股）通常会有杀估值的压力。这是全球流动性收紧的先行指标。</p>
            </div>
            
            <div class="threshold-list">
              <div class="threshold-item">
                <span class="threshold-tag danger">利差 < -150bp</span>
                <span>外资流出压力加大</span>
              </div>
              <div class="threshold-item">
                <span class="threshold-tag warning">利差 -100bp ~ -150bp</span>
                <span>中性偏紧</span>
              </div>
              <div class="threshold-item">
                <span class="threshold-tag success">利差 > -100bp</span>
                <span>外资回流窗口</span>
              </div>
            </div>
          </div>
        </el-collapse-item>

        <el-collapse-item name="2">
          <template #title>
            <div class="collapse-title">
              <span class="number-badge success">2</span>
              <span>汇率与外资情绪（果）</span>
            </div>
          </template>
          
          <div class="guide-section">
            <p class="section-intro"><strong>USD/CNH vs 北向资金</strong> - 聪明钱的真实动作</p>
            
            <div class="insight-box">
              <div class="insight-title">核心逻辑</div>
              <p>外资（北向）对汇率极度敏感。人民币贬值，外资为了避险往往会流出；人民币升值，外资则倾向于流入。</p>
            </div>
            
            <div class="insight-box success">
              <div class="insight-title">反转信号</div>
              <p>当汇率企稳回升且北向资金开始连续5日净流入时，往往是宏观情绪反转的先兆。这是左侧布局的重要窗口。</p>
            </div>
            
            <div class="threshold-list">
              <div class="threshold-item">
                <span class="threshold-tag danger">CNH > 7.3</span>
                <span>外资流出加速</span>
              </div>
              <div class="threshold-item">
                <span class="threshold-tag warning">CNH 7.1-7.3</span>
                <span>观望区间</span>
              </div>
              <div class="threshold-item">
                <span class="threshold-tag success">CNH < 7.1</span>
                <span>外资回流</span>
              </div>
            </div>
          </div>
        </el-collapse-item>

        <el-collapse-item name="3">
          <template #title>
            <div class="collapse-title">
              <span class="number-badge warning">3</span>
              <span>风险补偿与估值水位（择时）</span>
            </div>
          </template>
          
          <div class="guide-section">
            <p class="section-intro"><strong>ERP = 1/PE - 10Y国债收益率</strong> - 判定当前位置是便宜还是贵</p>
            
            <div class="insight-box">
              <div class="insight-title">核心逻辑</div>
              <p>ERP衡量的是"买股票"比"买债券"多出来的吸引力。ERP越高，股票的性价比越高。</p>
            </div>
            
            <div class="insight-box success">
              <div class="insight-title">极端机会</div>
              <p>当ERP处于历史+2倍标准差以上时，说明股票极度便宜，即使宏观数据还没转好，长线配置的胜率已经极高了。</p>
            </div>
            
            <div class="threshold-list">
              <div class="threshold-item">
                <span class="threshold-tag success">ERP > +2σ</span>
                <span>极度低估，长线布局</span>
              </div>
              <div class="threshold-item">
                <span class="threshold-tag warning">ERP +1σ ~ +2σ</span>
                <span>偏便宜，逐步建仓</span>
              </div>
              <div class="threshold-item">
                <span class="threshold-tag danger">ERP < 均值</span>
                <span>偏贵，谨慎追高</span>
              </div>
            </div>
          </div>
        </el-collapse-item>

        <el-collapse-item name="4">
          <template #title>
            <div class="collapse-title">
              <span class="number-badge danger">4</span>
              <span>实体需求前瞻（前瞻）</span>
            </div>
          </template>
          
          <div class="guide-section">
            <p class="section-intro"><strong>LME铜 vs 南华商品指数</strong> - 铜博士的复苏信号</p>
            
            <div class="insight-box">
              <div class="insight-title">核心逻辑</div>
              <p>铜被称为"铜博士"，因为其应用极其广泛。铜价通常领先于PPI指数1-2个月，是实体工业需求的先行指标。</p>
            </div>
            
            <div class="insight-box success">
              <div class="insight-title">复苏确认</div>
              <p>如果铜价和商品指数联袂上行，说明实体工业的真实需求（不仅仅是数据上的）正在复苏。这是PPI发布前的提前信号。</p>
            </div>
            
            <div class="threshold-list">
              <div class="threshold-item">
                <span class="threshold-tag success">铜价↑ + 商品↑</span>
                <span>实体需求复苏</span>
              </div>
              <div class="threshold-item">
                <span class="threshold-tag warning">铜价↑ + 商品→</span>
                <span>结构性机会</span>
              </div>
              <div class="threshold-item">
                <span class="threshold-tag danger">铜价↓ + 商品↓</span>
                <span>需求疲软</span>
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

.flow-chain {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  flex-wrap: wrap;
}

.chain-item {
  text-align: center;
  padding: 12px 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.chain-tag {
  display: inline-block;
  padding: 2px 10px;
  border-radius: 10px;
  font-size: 12px;
  font-weight: 600;
  color: white;
  margin-bottom: 6px;
}

.chain-tag.primary { background: #409eff; }
.chain-tag.success { background: #67c23a; }
.chain-tag.warning { background: #e6a23c; }
.chain-tag.danger { background: #f56c6c; }

.chain-title {
  font-weight: 600;
  font-size: 14px;
  color: #303133;
}

.flow-chain .el-icon {
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
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 14px;
  color: white;
}

.number-badge.primary { background: #409eff; }
.number-badge.success { background: #67c23a; }
.number-badge.warning { background: #e6a23c; }
.number-badge.danger { background: #f56c6c; }

.guide-section {
  padding: 10px 0;
}

.section-intro {
  margin: 0 0 16px 0;
  padding: 12px;
  background: #e6f7ff;
  border-left: 4px solid #1890ff;
  border-radius: 4px;
  color: #606266;
  line-height: 1.6;
}

.insight-box {
  padding: 12px;
  background: #f5f7fa;
  border-radius: 6px;
  margin-bottom: 12px;
  border-left: 3px solid #409eff;
}

.insight-box.warning {
  background: #fff7e6;
  border-left-color: #fa8c16;
}

.insight-box.success {
  background: #f6ffed;
  border-left-color: #52c41a;
}

.insight-title {
  font-weight: 600;
  font-size: 14px;
  color: #303133;
  margin-bottom: 6px;
}

.insight-box p {
  margin: 0;
  font-size: 13px;
  color: #606266;
  line-height: 1.6;
}

.threshold-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-top: 16px;
}

.threshold-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 12px;
  background: #f5f7fa;
  border-radius: 6px;
  font-size: 14px;
  color: #606266;
}

.threshold-tag {
  padding: 4px 10px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 600;
  white-space: nowrap;
}

.threshold-tag.success {
  background: #f6ffed;
  color: #52c41a;
}

.threshold-tag.warning {
  background: #fff7e6;
  color: #fa8c16;
}

.threshold-tag.danger {
  background: #fff1f0;
  color: #f5222d;
}

:deep(.el-collapse-item__header) {
  padding: 16px;
  font-size: 15px;
}

:deep(.el-collapse-item__content) {
  padding: 0 16px 16px 56px;
}
</style>
