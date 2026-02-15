import akshare as ak
import json


def fetch_macro_gdp():
    print("正在获取 GDP 数据...")
    df = ak.macro_china_gdp()
    print(f"获取到 {len(df)} 条记录")
    print("\n前 5 条数据:")
    print(df.head().to_string())
    return df


def fetch_macro_cpi():
    print("正在获取 CPI 数据...")
    df = ak.macro_china_cpi()
    print(f"获取到 {len(df)} 条记录")
    print("\n前 5 条数据:")
    print(df.head().to_string())
    return df


def fetch_real_estate_sales():
    print("正在获取房地产销售面积数据...")
    df = ak.macro_china_real_estate_sale_area()
    print(f"获取到 {len(df)} 条记录")
    print("\n前 5 条数据:")
    print(df.head().to_string())
    return df


if __name__ == "__main__":
    
    macro_china_gdp_yearly_df = ak.macro_china_gdp_yearly()
    print(macro_china_gdp_yearly_df)
    
    print("=" * 60)
    print("AKShare 数据获取示例")
    print("=" * 60)

    print("\n" + "=" * 60)
    # fetch_macro_gdp()

    print("\n" + "=" * 60)
    # fetch_macro_cpi()

    print("\n" + "=" * 60)
    # fetch_real_estate_sales()

    print("\n" + "=" * 60)
    print("数据获取完成")
