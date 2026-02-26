import json
import sys
from typing import Dict, Any


class DataFetcherExecutor:
    def __init__(self):
        self.fetchers = {}

    def register_fetcher(self, name: str, fetcher_class):
        self.fetchers[name] = fetcher_class

    def execute(
        self, fetcher_name: str, method: str, params: Dict[str, Any] = None
    ) -> Dict[str, Any]:
        if fetcher_name not in self.fetchers:
            return {"success": False, "error": f"Unknown fetcher: {fetcher_name}"}

        try:
            fetcher = self.fetchers[fetcher_name]()
            if not hasattr(fetcher, method):
                return {
                    "success": False,
                    "error": f"Method {method} not found in {fetcher_name}",
                }

            func = getattr(fetcher, method)
            params = params or {}
            result = func(**params)

            return {
                "success": True,
                "fetcher": fetcher_name,
                "method": method,
                "result": result,
            }
        except Exception as e:
            return {
                "success": False,
                "fetcher": fetcher_name,
                "method": method,
                "error": str(e),
            }


def main():
    if len(sys.argv) < 3:
        print(
            json.dumps(
                {
                    "success": False,
                    "error": "Usage: python -m data_fetcher.executor <fetcher> <method> [params_json]",
                }
            )
        )
        sys.exit(1)

    fetcher_name = sys.argv[1]
    method = sys.argv[2]
    params = {}

    if len(sys.argv) > 3:
        try:
            params = json.loads(sys.argv[3])
        except json.JSONDecodeError as e:
            print(
                json.dumps(
                    {"success": False, "error": f"Invalid JSON params: {str(e)}"}
                )
            )
            sys.exit(1)

    from data_fetcher.macro_data import MacroDataFetcher

    executor = DataFetcherExecutor()
    executor.register_fetcher("macro", MacroDataFetcher)

    result = executor.execute(fetcher_name, method, params)
    print(json.dumps(result, ensure_ascii=False, indent=2))


if __name__ == "__main__":
    main()
