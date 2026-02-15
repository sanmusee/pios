import logging
import sys
from datetime import datetime


def setup_logger(
    name: str = "pios_python", level: int = logging.INFO
) -> logging.Logger:
    logger = logging.getLogger(name)
    logger.setLevel(level)

    if not logger.handlers:
        handler = logging.StreamHandler(sys.stdout)
        handler.setLevel(level)

        formatter = logging.Formatter(
            "%(asctime)s - %(name)s - %(levelname)s - %(message)s",
            datefmt="%Y-%m-%d %H:%M:%S",
        )
        handler.setFormatter(formatter)
        logger.addHandler(handler)

    return logger


def format_period(period_str: str) -> str:
    try:
        if "-" in period_str and len(period_str.split("-")) == 2:
            year, month = period_str.split("-")
            return f"{year}-{month.zfill(2)}-01"
        return period_str
    except:
        return period_str


def safe_float(value, default: float = 0.0) -> float:
    try:
        return float(value) if value is not None else default
    except (ValueError, TypeError):
        return default
