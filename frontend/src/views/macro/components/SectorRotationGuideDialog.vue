<template>
  <el-dialog
    v-model="visible"
    title="行业景气度与风格轮动 - 使用指南"
    width="800px"
    :close-on-click-modal="true"
    class="guide-dialog"
  >
    <div class="guide-content">
      <div class="guide-intro">
        <p>宏观定仓位，风格定节奏，行业定品种。在宏观环境确定后，通过风格轮动和行业景气度寻找最优配置。</p>
      </div>

      <el-collapse v-model="activeNames">
        <el-collapse-item name="1" title="模块一：市场风格指数">
          <div class="guide-section">
            <p>价值(沪深300) vs 成长(创业板指) 相对强弱。风格切换往往领先行业轮动1-2个月。</p>
            <div class="insight-box">
              <strong>风格判断逻辑：</strong><br/>
              • 价值占优：利率上行、通胀预期、盈利确定性溢价 → 拥抱蓝筹<br/>
              • 成长占优：流动性宽松、风险偏好提升、主题炒作 → 布局小票
            </div>
            <div class="insight-box warning">
              <strong>风格切换信号：</strong><br/>
              • 风格比值突破20日均线 + 成交量放大 = 风格切换确认<br/>
              • 风格比值与指数背离 = 警惕风格回归
            </div>
          </div>
        </el-collapse-item>

        <el-collapse-item name="2" title="模块二：行业拥挤度监控">
          <div class="guide-section">
            <p>行业成交额占全市场比重。逆向策略：寻找底部无人问津的行业，回避顶部人声鼎沸的行业。</p>
            <div class="insight-box">
              <strong>拥挤度分级：</strong><br/>
              • 绿色：成交占比 < 历史均值50% → 底部区域，关注机会<br/>
              • 橙色：成交占比 50%-85% → 正常区间，趋势延续<br/>
              • 红色：成交占比 > 85% → 极度拥挤，警惕回调
            </div>
            <div class="insight-box warning">
              <strong>逆向策略要点：</strong><br/>
              • 底部行业 + 基本面改善预期 = 左侧布局机会<br/>
              • 拥挤行业 + 盈利下调 = 逃顶信号
            </div>
          </div>
        </el-collapse-item>

        <el-collapse-item name="3" title="模块三：盈利预期斜率">
          <div class="guide-section">
            <p>Forward EPS 变化率。预期下修导致估值压制，预期上修带来估值扩张。</p>
            <div class="insight-box">
              <strong>预期变化解读：</strong><br/>
              • EPS变化率 > +2%：分析师密集上调，盈利改善确认<br/>
              • EPS变化率 -2% ~ +2%：预期平稳，估值中枢震荡<br/>
              • EPS变化率 < -2%：分析师下调，警惕戴维斯双杀
            </div>
            <div class="insight-box warning">
              <strong>预期与股价关系：</strong><br/>
              • 股价跌 + 预期上调 = 错杀机会（买入）<br/>
              • 股价涨 + 预期下调 = 透支风险（卖出）
            </div>
          </div>
        </el-collapse-item>

        <el-collapse-item name="4" title="模块四：个股结构与广度">
          <div class="guide-section">
            <p>站上均线个股百分比。反映市场整体健康度，用于判断指数与个股的背离。</p>
            <div class="insight-box">
              <strong>市场广度信号：</strong><br/>
              • MA20占比 < 20%：极度冰点，跌无可跌，反弹在即<br/>
              • MA20占比 20%-80%：正常区间，结构分化<br/>
              • MA20占比 > 80%：过热风险，注意回调
            </div>
            <div class="insight-box warning">
              <strong>背离分析：</strong><br/>
              • 指数新高 + 广度下降 = 顶部背离（危险）<br/>
              • 指数新低 + 广度回升 = 底部背离（机会）
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
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
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