package com.example.demo.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by koreyoshi on 2017/12/15.
 */
public class ArrayHandleUtils {
    /**
     * 实现php中的array_chunk 函数，把一个list切割成指定长度的多个list
     *
     * @param arrs  带切割的数组
     * @param limit 数组的长度
     * @return 返回切割后的list
     */
    public static List<Object> arrayChunk(List<Object> arrs, int limit) {
        if (arrs.size() < limit) {
            return arrs;
        }
        ArrayList newIdsArr = new ArrayList();
        ArrayList childNewIdsArr = new ArrayList();
        for (int i = 0; i < arrs.size(); i++) {
            if (childNewIdsArr.size() == limit) {
                newIdsArr.add(childNewIdsArr);
                childNewIdsArr.clear();
            }
            childNewIdsArr.add(arrs.get(i));
        }
        newIdsArr.add(childNewIdsArr);
        return newIdsArr;
    }
}
