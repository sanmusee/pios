#!/bin/bash
# PIOS 自主工作执行框架
# 用于支持长时间连续任务执行

set -e

WORK_DIR="/Users/weideng/Projects/pri/pios"
LOG_DIR="$WORK_DIR/logs"
STATE_FILE="$WORK_DIR/.pios_state.json"
PROGRESS_FILE="$WORK_DIR/.pios_progress.md"

# 创建必要的目录
mkdir -p "$LOG_DIR"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 日志函数
log_info() {
    echo -e "${GREEN}[INFO]${NC} $(date '+%Y-%m-%d %H:%M:%S') - $1" | tee -a "$LOG_DIR/pios.log"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $(date '+%Y-%m-%d %H:%M:%S') - $1" | tee -a "$LOG_DIR/pios.log"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $(date '+%Y-%m-%d %H:%M:%S') - $1" | tee -a "$LOG_DIR/pios.log"
}

log_section() {
    echo -e "${BLUE}[SECTION]${NC} $(date '+%Y-%m-%d %H:%M:%S') - $1" | tee -a "$LOG_DIR/pios.log"
    echo -e "\n========================================" >> "$LOG_DIR/pios.log"
    echo -e "$1" >> "$LOG_DIR/pios.log"
    echo -e "========================================\n" >> "$LOG_DIR/pios.log"
}

# 初始化状态文件
init_state() {
    if [ ! -f "$STATE_FILE" ]; then
        cat > "$STATE_FILE" << 'EOF'
{
  "project": "PIOS",
  "version": "1.0.0",
  "start_time": "",
  "current_phase": "initialization",
  "completed_tasks": [],
  "current_task": null,
  "pending_tasks": [
    "backend_setup",
    "frontend_setup", 
    "database_setup",
    "layer1_implementation",
    "layer2_implementation",
    "layer3_implementation",
    "layer4_implementation",
    "layer5_implementation",
    "integration_testing",
    "deployment_config"
  ],
  "progress_percentage": 0,
  "last_updated": ""
}
EOF
        log_info "状态文件已初始化"
    fi
}

# 更新状态
update_state() {
    local key=$1
    local value=$2
    
    if command -v jq &> /dev/null; then
        jq ".$key = \"$value\"" "$STATE_FILE" > "$STATE_FILE.tmp" && mv "$STATE_FILE.tmp" "$STATE_FILE"
    else
        log_warn "jq 未安装，状态更新可能不完整"
    fi
    
    # 更新时间戳
    if command -v jq &> /dev/null; then
        jq ".last_updated = \"$(date -u +%Y-%m-%dT%H:%M:%SZ)\"" "$STATE_FILE" > "$STATE_FILE.tmp" && mv "$STATE_FILE.tmp" "$STATE_FILE"
    fi
}

# 完成任务
complete_task() {
    local task=$1
    
    if command -v jq &> /dev/null; then
        # 添加到已完成列表
        jq ".completed_tasks += [\"$task\"]" "$STATE_FILE" > "$STATE_FILE.tmp" && mv "$STATE_FILE.tmp" "$STATE_FILE"
        # 从未完成列表移除
        jq ".pending_tasks -= [\"$task\"]" "$STATE_FILE" > "$STATE_FILE.tmp" && mv "$STATE_FILE.tmp" "$STATE_FILE"
        # 更新进度百分比
        local total=$(jq '.completed_tasks | length + (.pending_tasks | length)' "$STATE_FILE")
        local completed=$(jq '.completed_tasks | length' "$STATE_FILE")
        local percentage=$((completed * 100 / total))
        jq ".progress_percentage = $percentage" "$STATE_FILE" > "$STATE_FILE.tmp" && mv "$STATE_FILE.tmp" "$STATE_FILE"
    fi
    
    log_info "任务完成: $task"
}

# 显示当前状态
show_status() {
    log_section "当前项目状态"
    
    if [ -f "$STATE_FILE" ]; then
        if command -v jq &> /dev/null; then
            echo -e "\n${BLUE}=== 项目进度 ===${NC}"
            jq -r '"项目: " + .project + " v" + .version' "$STATE_FILE"
            jq -r '"当前阶段: " + .current_phase' "$STATE_FILE"
            jq -r '"进度: " + (.progress_percentage | tostring) + "%"' "$STATE_FILE"
            
            echo -e "\n${GREEN}=== 已完成任务 ===${NC}"
            jq -r '.completed_tasks[] | "  ✓ " + .' "$STATE_FILE"
            
            echo -e "\n${YELLOW}=== 待完成任务 ===${NC}"
            jq -r '.pending_tasks[] | "  ○ " + .' "$STATE_FILE"
            
            if [ -n "$(jq -r '.current_task // empty' "$STATE_FILE")" ]; then
                echo -e "\n${BLUE}=== 当前执行任务 ===${NC}"
                jq -r '.current_task | "  → " + .' "$STATE_FILE"
            fi
        else
            cat "$STATE_FILE"
        fi
    else
        log_warn "状态文件不存在"
    fi
    
    echo -e "\n${BLUE}=== 日志文件 ===${NC}"
    ls -lh "$LOG_DIR" 2>/dev/null || echo "暂无日志文件"
}

# 检查依赖
check_dependencies() {
    log_section "检查依赖环境"
    
    local deps=("java" "mvn" "node" "npm")
    local missing=()
    
    for dep in "${deps[@]}"; do
        if command -v $dep &> /dev/null; then
            log_info "$dep 已安装"
        else
            log_warn "$dep 未安装"
            missing+=($dep)
        fi
    done
    
    if [ ${#missing[@]} -eq 0 ]; then
        log_info "所有依赖已满足"
        return 0
    else
        log_error "缺少依赖: ${missing[*]}"
        return 1
    fi
}

# 主执行函数
main() {
    case "${1:-status}" in
        init)
            init_state
            log_info "PIOS 项目初始化完成"
            ;;
        status)
            show_status
            ;;
        check)
            check_dependencies
            ;;
        start)
            init_state
            update_state "start_time" "$(date -u +%Y-%m-%dT%H:%M:%SZ)"
            update_state "current_phase" "development"
            log_info "PIOS 自主工作系统已启动"
            show_status
            ;;
        complete)
            if [ -z "$2" ]; then
                log_error "请指定任务名称: ./pios-work.sh complete <task_name>"
                exit 1
            fi
            complete_task "$2"
            ;;
        reset)
            rm -f "$STATE_FILE"
            log_info "状态已重置"
            init_state
            ;;
        *)
            echo "用法: $0 {init|status|check|start|complete <task>|reset}"
            exit 1
            ;;
    esac
}

main "$@"
