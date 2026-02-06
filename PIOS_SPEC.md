# 0. 项目定位（升级版）

## 项目本质

PIOS 不是一个金融工具。
不是一个宏观分析系统。
不是一个科技资讯平台。

它是一个：

“个人级世界结构认知操作系统”。

目标是帮助使用者在信息爆炸时代：

- 建立长期世界模型
- 识别结构级变化
- 感知慢变量
- 进行连续推演
- 形成独立趋势判断能力

---

## 技术架构要求

### 项目结构

项目根目录下分为两个独立项目：

```
pios/
├── backend/          # 后端项目目录
│   ├── src/
│   ├── pom.xml       # Maven 配置文件
│   └── ...
├── frontend/         # 前端项目目录
│   ├── src/
│   ├── package.json  # npm 配置文件
│   └── ...
└── README.md
```

- **后端项目**：采用 Java 技术栈，位于 `backend/` 目录
- **前端项目**：采用 Vue 技术栈，位于 `frontend/` 目录

### 部署方式

两个项目可以：

- ✅ **分开部署**（推荐开发环境）
  - 后端独立运行，提供 RESTful API
  - 前端独立运行，通过 HTTP 请求调用后端 API
  - 支持跨域配置（CORS）
  
- ✅ **部署到一起**（推荐生产环境）
  - 前端构建后的静态文件由后端服务托管
  - 统一域名和端口，简化部署和运维

### 后端技术栈（Java）

#### 核心框架
- **Java 版本**：JDK 17 或更高版本
- **框架**：Spring Boot 3.x
- **构建工具**：Maven 3.8+
- **项目结构**：标准 Maven 多模块结构（可选）

#### Web 层
- **Web 框架**：Spring MVC
- **API 文档**：SpringDoc OpenAPI（Swagger 3.x）
- **参数校验**：Spring Validation（Hibernate Validator）
- **跨域处理**：Spring CORS 配置

#### 数据层
- **数据库**：MySQL 8.0+ 或 PostgreSQL 14+
- **ORM 框架**：MyBatis-Plus 3.5+ 或 Spring Data JPA
- **连接池**：HikariCP
- **数据库迁移**：Flyway 或 Liquibase

#### 缓存与消息
- **缓存**：Redis 7.0+（用于数据缓存和会话管理）
- **缓存框架**：Spring Cache + Redis
- **消息队列**：RabbitMQ 或 RocketMQ（可选，用于异步任务）

#### 安全与认证
- **安全框架**：Spring Security
- **JWT 认证**：JJWT 或 Spring Security JWT
- **密码加密**：BCrypt

#### 工具库
- **JSON 处理**：Jackson（Spring Boot 默认）
- **日志框架**：Logback（Spring Boot 默认）
- **工具类**：Hutool、Apache Commons
- **日期处理**：Java 8+ Time API

#### 任务调度
- **定时任务**：Spring Scheduled 或 Quartz
- **异步处理**：Spring @Async

#### 监控与运维
- **健康检查**：Spring Boot Actuator
- **应用监控**：Micrometer + Prometheus（可选）
- **日志收集**：ELK Stack（可选）

### 前端技术栈（Vue）

#### 核心框架
- **Vue 版本**：Vue 3.x（Composition API）
- **构建工具**：Vite 5.x
- **包管理工具**：pnpm（推荐）或 npm/yarn
- **Node 版本**：Node.js 18+ 或更高版本

#### UI 框架
- **UI 组件库**：Element Plus 或 Ant Design Vue
- **图标库**：Element Plus Icons 或 @ant-design/icons-vue
- **样式预处理**：Sass/SCSS 或 Less

#### 状态管理
- **状态管理**：Pinia（推荐）或 Vuex
- **持久化**：pinia-plugin-persistedstate

#### 路由与导航
- **路由框架**：Vue Router 4.x
- **路由守卫**：支持全局前置守卫、路由独享守卫

#### HTTP 请求
- **HTTP 客户端**：Axios
- **请求拦截器**：统一处理 token、错误码、loading 状态
- **响应拦截器**：统一处理响应数据、错误提示

#### 工具库
- **日期处理**：Day.js 或 date-fns
- **数据可视化**：ECharts 或 Apache ECharts（用于图表展示）
- **工具函数**：Lodash-es
- **类型检查**：TypeScript（可选，推荐使用）

#### 代码规范
- **代码格式化**：Prettier
- **代码检查**：ESLint
- **Git 提交规范**：Commitlint + Husky（可选）

### 项目目录结构规范

#### 后端目录结构（推荐）

```
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/pios/
│   │   │       ├── PiosApplication.java
│   │   │       ├── config/          # 配置类
│   │   │       ├── controller/      # 控制器层
│   │   │       ├── service/         # 服务层
│   │   │       │   └── impl/       # 服务实现
│   │   │       ├── mapper/          # MyBatis Mapper
│   │   │       ├── entity/          # 实体类
│   │   │       ├── dto/             # 数据传输对象
│   │   │       ├── vo/              # 视图对象
│   │   │       ├── common/          # 公共类
│   │   │       │   ├── result/      # 统一响应结果
│   │   │       │   ├── exception/   # 异常处理
│   │   │       │   └── util/        # 工具类
│   │   │       └── security/        # 安全相关
│   │   └── resources/
│   │       ├── mapper/              # MyBatis XML
│   │       ├── db/migration/        # 数据库迁移脚本
│   │       ├── application.yml      # 配置文件
│   │       └── application-dev.yml  # 开发环境配置
│   └── test/                        # 测试代码
├── pom.xml
└── README.md
```

#### 前端目录结构（推荐）

```
frontend/
├── src/
│   ├── api/              # API 接口定义
│   ├── assets/           # 静态资源
│   │   ├── images/
│   │   └── styles/
│   ├── components/        # 公共组件
│   ├── views/            # 页面组件
│   ├── router/           # 路由配置
│   ├── store/            # 状态管理（Pinia）
│   ├── utils/            # 工具函数
│   ├── composables/      # 组合式函数
│   ├── types/            # TypeScript 类型定义（如使用 TS）
│   ├── App.vue
│   └── main.js
├── public/               # 公共静态文件
├── vite.config.js        # Vite 配置
├── package.json
└── README.md
```

### API 设计规范

#### RESTful API 规范
- **URL 命名**：使用名词复数形式，如 `/api/v1/info-signals`
- **HTTP 方法**：
  - `GET`：查询资源
  - `POST`：创建资源
  - `PUT`：更新资源（完整更新）
  - `PATCH`：更新资源（部分更新）
  - `DELETE`：删除资源
- **版本控制**：URL 路径中包含版本号，如 `/api/v1/`

#### 统一响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": 1234567890
}
```

- **成功响应**：`code` 为 200
- **错误响应**：`code` 为非 200，`message` 包含错误信息
- **分页响应**：`data` 中包含 `list`、`total`、`page`、`size` 等字段

#### 错误码规范
- `200`：成功
- `400`：请求参数错误
- `401`：未授权
- `403`：禁止访问
- `404`：资源不存在
- `500`：服务器内部错误
- 自定义业务错误码：`1000+`

### 数据库设计规范

#### 命名规范
- **表名**：小写字母 + 下划线，如 `info_signal`、`correlation_chain`
- **字段名**：小写字母 + 下划线，如 `created_at`、`updated_at`
- **索引命名**：`idx_表名_字段名`，如 `idx_info_signal_level`

#### 必需字段
每张表应包含：
- `id`：主键（BIGINT，自增）
- `created_at`：创建时间（DATETIME）
- `updated_at`：更新时间（DATETIME）
- `deleted_at`：删除时间（DATETIME，软删除，可选）

### 开发环境要求

#### 后端开发环境
- JDK 17+
- Maven 3.8+
- MySQL 8.0+ 或 PostgreSQL 14+
- Redis 7.0+
- IDE：IntelliJ IDEA（推荐）或 Eclipse

#### 前端开发环境
- Node.js 18+
- pnpm/npm/yarn
- IDE：VS Code（推荐）或 WebStorm

#### 版本控制
- Git
- 分支策略：Git Flow 或 GitHub Flow
- 提交信息：遵循 Conventional Commits 规范

### 部署要求

#### 后端部署
- **运行环境**：JDK 17+ 运行环境
- **打包方式**：Maven 打包为 JAR 或 WAR
- **运行方式**：`java -jar` 或部署到 Tomcat/Jetty
- **配置文件**：支持多环境配置（dev/test/prod）
- **日志目录**：配置日志文件输出路径

#### 前端部署
- **构建命令**：`pnpm build` 或 `npm run build`
- **构建产物**：`dist/` 目录
- **部署方式**：
  - 独立部署：Nginx 托管静态文件
  - 集成部署：后端服务托管静态文件

#### 生产环境建议
- **反向代理**：Nginx
- **HTTPS**：配置 SSL 证书
- **域名**：配置独立域名或子域名
- **监控**：应用性能监控（APM）
- **日志**：集中式日志管理

---

## 1. 要解决的核心痛点

当前时代存在以下认知困境：

1. 信息过载  
2. 重要信息被淹没  
3. 结构变化难以察觉  
4. 无法持续连续推演  
5. 趋势判断缺乏积累  
6. 慢变量变化难以感知  

本系统不提供：

❌ 新闻瀑布流  
❌ 行情终端  
❌ 资讯推送  
❌ 高频数据监控  

本系统提供：

✅ 结构级世界理解  
✅ 因果链条分析  
✅ 连续推演训练  
✅ 趋势级判断支持  

---

# 2. 系统能力分层模型（核心）

PIOS 必须具备五个能力层级。

该结构是系统的核心设计原则，未来拓展必须遵循此结构。

---

## 第一层：信息抓取层（Raw Layer）

目标：

自动采集影响“资产定价与社会结构变化”的关键情报。

信息来源包括但不限于：

宏观数据：
- CPI
- M2
- PMI
- 社融
- 利率变化

资本市场：
- A股 / 港股 / 美股指数
- 国债收益率
- 大宗商品
- 黄金 / 白银 / 有色金属

政策与制度：
- 央行表态
- 财政政策
- 地缘政治动态

产业与科技：
- AI 重大突破
- 开源模型进展
- 科技公司资本开支
- 产业链变化

企业层面：
- 重要公司财报
- 重大并购事件

重要原则：

不是拉取全部信息。
而是拉取“可能影响结构变化”的信息。

---

## 第二层：重要性判断层（Filter Layer）

目标：

判断信息的影响层级。

系统必须将所有信号分为：

Level 1：情绪级波动（短期噪音）  
Level 2：中期趋势变化  
Level 3：结构级变化  

系统必须回答：

这条信息属于哪个层级？

例如：

“美联储加息 25bp”
可能属于 Level 1 或 2。

“全球去美元化加速”
可能属于 Level 3。

系统必须具备“变量分级能力”。

---

## 第三层：关联分析层（Correlation Layer）

这是系统的核心差异能力。

目标：

构建变量之间的因果链条。

示例：

- 美债收益率上涨
- 黄金未下跌
- A股走弱
- 港股科技走强

系统必须自动生成：

可能原因 A  
可能原因 B  
历史对比阶段  
当前所处周期阶段  

输出必须为：

因果链结构，而不是简单解释。

---

## 第四层：推演层（Inference Layer）

目标：

构建三层推演能力。

系统每日必须输出一个结构推演：

1. 这意味着什么？
2. 接下来可能产生哪些连锁影响？
3. 最终谁受益？谁受损？

并标记：

时间尺度（短 / 中 / 长）
置信度等级（低 / 中 / 高）

推演的目标不是预测。
而是训练结构思维。

---

## 第五层：汇报层（Report Layer）

输出形式必须为结构简报，而非新闻堆积。

每日生成：

《今日结构变化简报》

必须包含：

1. 今日关键变量变化
2. 与历史阶段对比
3. 潜在趋势拐点判断
4. 资产影响矩阵
5. 风险提示
6. 当前结构阶段一句话总结

字数控制：
≤ 800 字

---

# 3. 系统节奏原则

系统更新节奏必须稳定。

每日：
结构变化更新 + 推演

每周：
趋势总结（预留）

每月：
阶段判断（预留）

禁止实时波动驱动更新。

---

# 4. 未来拓展原则

在未来版本中，可以扩展：

- 多变量关联网络图
- 慢变量检测模块
- 推演回溯验证模块
- 认知误差分析系统
- 多用户支持

但必须遵循五层能力结构。