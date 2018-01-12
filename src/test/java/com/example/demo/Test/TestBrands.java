package com.example.demo.Test;

import org.bson.Document;

import java.util.*;

/**
 * Created by koreyoshi on 2018/1/9.
 */
public class TestBrands {
    /**
     * 修改 -- 品牌标签字段修改
     */
    @org.junit.Test
    public void test() {
        //构造数据
        Document data = new Document();
        List<String> brandSet = new ArrayList<>();
        brandSet.add("西门子");
        brandSet.add("ABB");
        brandSet.add("yu");
        data.put("brand_artificial", brandSet);

        //  test~~
        if (data.containsKey("brand_artificial") && data.get("brand_artificial") != null) {
            List<String> brandsList = (List) data.get("brand_artificial");
            if (brandsList.size() > 1) {
                String brandNmae = null;
                List<Map<String, Object>> handleBrandList = new ArrayList<>();
                for (String brand : brandsList) {
                    Map<String, Object> brandMap = new HashMap<>(2);
                    brandMap.put("main_brand", brand);
                    List<String> innerBrandList = new ArrayList<>();
                    for (String innerBrand : brandsList) {
                        if (!brand.equals(innerBrand)) {
                            innerBrandList.add(innerBrand);
                        }
                    }
                    brandMap.put("mentioned_brands", innerBrandList);
                    handleBrandList.add(brandMap);
                }
                data.put("brand_and_mentioned_brands", handleBrandList);
            }

        }
        System.out.println(data);


    }
}
