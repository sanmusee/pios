# PIOS Python 模块

用于数据获取、处理和分析的 Python 代码模块。

## 目录结构

```
python/
├── config/          # 配置文件
├── data_fetcher/    # 数据获取模块
├── utils/           # 工具函数
├── requirements.txt # Python 依赖
└── README.md        # 说明文档
```

## 安装依赖

```bash
cd python
pip install -r requirements.txt
```

## 使用方式

### 1. 直接运行 Python 脚本

```bash
cd python
python -m data_fetcher.macro_data
```

### 2. Java 调用 Python

通过 ProcessBuilder 或 Jython 调用 Python 脚本获取数据。

## 注意事项

- Python 版本要求: 3.8+
- 所有数据获取脚本应返回 JSON 格式便于 Java 解析
