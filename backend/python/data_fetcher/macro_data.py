import json
import sys
from typing import Dict, List, Any


class MacroDataFetcher:
    def __init__(self):
        self.data_sources = {
            "gdp": self.fetch_gdp_data,
            "cpi": self.fetch_cpi_data,
            "ppi": self.fetch_ppi_data,
            "real_estate": self.fetch_real_estate_data,
        }

    def fetch_gdp_data(
        self, start_date: str = None, end_date: str = None
    ) -> List[Dict[str, Any]]:
        try:
            import akshare as ak

            df = ak.macro_china_gdp()
            result = []
            for _, row in df.iterrows():
                result.append(
                    {
                        "period": str(row.get("季度", "")),
                        "value": float(row.get("国内生产总值-绝对值", 0))
                        if pd.notna(row.get("国内生产总值-绝对值"))
                        else None,
                        "yoy_growth": float(row.get("国内生产总值-同比增长", 0))
                        if pd.notna(row.get("国内生产总值-同比增长"))
                        else None,
                    }
                )
            return result
        except Exception as e:
            return {"error": str(e)}

    def fetch_cpi_data(
        self, start_date: str = None, end_date: str = None
    ) -> List[Dict[str, Any]]:
        try:
            import akshare as ak

            df = ak.macro_china_cpi()
            result = []
            for _, row in df.iterrows():
                result.append(
                    {
                        "period": str(row.get("月份", "")),
                        "national_yoy": float(row.get("全国-同比增长", 0))
                        if pd.notna(row.get("全国-同比增长"))
                        else None,
                        "national_mom": float(row.get("全国-环比增长", 0))
                        if pd.notna(row.get("全国-环比增长"))
                        else None,
                    }
                )
            return result
        except Exception as e:
            return {"error": str(e)}

    def fetch_ppi_data(
        self, start_date: str = None, end_date: str = None
    ) -> List[Dict[str, Any]]:
        try:
            import akshare as ak

            df = ak.macro_china_ppi()
            result = []
            for _, row in df.iterrows():
                result.append(
                    {
                        "period": str(row.get("月份", "")),
                        "ppi_yoy": float(row.get("PPI-同比增长", 0))
                        if pd.notna(row.get("PPI-同比增长"))
                        else None,
                        "ppi_mom": float(row.get("PPI-环比增长", 0))
                        if pd.notna(row.get("PPI-环比增长"))
                        else None,
                    }
                )
            return result
        except Exception as e:
            return {"error": str(e)}

    def fetch_real_estate_data(
        self, indicator: str = "sales_area"
    ) -> List[Dict[str, Any]]:
        try:
            import akshare as ak

            if indicator == "sales_area":
                df = ak.macro_china_real_estate_sale_area()
            elif indicator == "investment":
                df = ak.macro_china_real_estate_investment()
            else:
                return {"error": f"Unknown indicator: {indicator}"}

            result = []
            for _, row in df.iterrows():
                result.append(
                    {
                        "period": str(row.iloc[0]),
                        "value": float(row.iloc[1]) if pd.notna(row.iloc[1]) else None,
                        "yoy_growth": float(row.iloc[2])
                        if len(row) > 2 and pd.notna(row.iloc[2])
                        else None,
                    }
                )
            return result
        except Exception as e:
            return {"error": str(e)}

    def fetch_data(self, data_type: str, **kwargs) -> Dict[str, Any]:
        if data_type not in self.data_sources:
            return {"error": f"Unknown data type: {data_type}"}

        try:
            data = self.data_sources[data_type](**kwargs)
            return {
                "success": True,
                "data_type": data_type,
                "count": len(data) if isinstance(data, list) else 0,
                "data": data,
            }
        except Exception as e:
            return {"success": False, "data_type": data_type, "error": str(e)}


def main():
    if len(sys.argv) < 2:
        print(
            json.dumps(
                {
                    "success": False,
                    "error": "Usage: python -m data_fetcher.macro_data <data_type> [params]",
                }
            )
        )
        sys.exit(1)

    data_type = sys.argv[1]
    fetcher = MacroDataFetcher()
    result = fetcher.fetch_data(data_type)
    print(json.dumps(result, ensure_ascii=False, indent=2))


if __name__ == "__main__":
    import pandas as pd

    main()
