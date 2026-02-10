# PIOS - Personal Intelligence Operating System

个人级世界结构认知操作系统

## 项目结构

```
pios/
├── backend/          # Spring Boot 后端
├── frontend/         # Vue 3 前端
├── logs/            # 执行日志
├── build.sh         # 构建脚本
├── start-backend.sh # 启动后端
├── start-frontend.sh # 启动前端
└── pios-work.sh     # 工作管理脚本
```

## 快速开始

### 1. 构建项目

```bash
./build.sh
```

### 2. 启动后端

```bash
./start-backend.sh
```

后端将在 http://localhost:8080/api 运行

### 3. 启动前端（新终端）

```bash
./start-frontend.sh
```

前端将在 http://localhost:3000 运行

## 系统架构

PIOS 采用五层能力模型：

1. **信息采集层 (Raw Layer)** - 自动采集关键情报
2. **信号分级层 (Filter Layer)** - 判断信息影响层级
3. **关联分析层 (Correlation Layer)** - 构建因果链条
4. **推演层 (Inference Layer)** - 进行三层推演
5. **汇报层 (Report Layer)** - 生成结构简报

## 技术栈

- **后端**: Spring Boot 2.7, Java 8, MyBatis-Plus, MySQL
- **前端**: Vue 3, Vite, Element Plus, Axios
- **数据库**: MySQL 8.0, Redis

## API 文档

启动后端后访问: http://localhost:8080/api/swagger-ui.html

## 开发状态

- ✅ 项目初始化
- ✅ 后端核心实现
- ✅ 前端界面实现
- ✅ API 集成
- ✅ 构建打包
