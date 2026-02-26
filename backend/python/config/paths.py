import os
import sys

PYTHON_MODULE_PATH = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

if PYTHON_MODULE_PATH not in sys.path:
    sys.path.insert(0, PYTHON_MODULE_PATH)
