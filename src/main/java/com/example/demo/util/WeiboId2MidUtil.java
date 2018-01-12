package com.example.demo.util;

/**
 * 微博mid和微博page_url转化
 * 新浪微博page_url规则
 * url格式  weibo.com/userid/xxxxxxxx
 * 后面的部分是可以通过mid转化得到
 */
public class WeiboId2MidUtil {

    public static void main(String[] args) {
        System.out.println(Uid2Mid("FzqJImC0K"));
        System.out.println(Mid2Uid("4172120529866324"));//Mid2Id
    }


    private static String[] str62keys = {"0", "1", "2", "3", "4", "5", "6",
            "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w",
            "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
            "X", "Y", "Z"};

    public static String IntToEnode62(Integer int10) {
        String s62 = "";
        int r = 0;
        while (int10 != 0) {
            r = int10 % 62;
            s62 = str62keys[r] + s62;
            int10 = (int) Math.floor(int10 / 62.0);
        }
        return s62;
    }

    //62进制转成10进制
    public static String Str62toInt(String str62) {
        long i64 = 0;
        for (int i = 0; i < str62.length(); i++) {
            long Vi = (long) Math.pow(62, (str62.length() - i - 1));
            String t = str62.substring(i, i + 1);

            i64 += Vi * findindex(t);
        }
        // System.out.println(i64);
        return Long.toString(i64);
    }

    public static int findindex(String t) {
        int index = 0;
        for (int i = 0; i < str62keys.length; i++) {
            if (str62keys[i].equals(t)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public static String Uid2Mid(String mid) {
        String id = "";
        for (int i = mid.length() - 4; i > -4; i = i - 4) //从最后往前以4字节为一组读取URL字符
        {
            int offset1 = i < 0 ? 0 : i;
            int len = i < 0 ? mid.length() % 4 : 4;

            String str = mid.substring(offset1, offset1 + len);
            // System.out.println(offset1+" "+len+" "+str);

            str = Str62toInt(str);

            if (offset1 > 0) //若不是第一组，则不足7位补0
            {
                while (str.length() < 7) {
                    str = "0" + str;
                }
            }
            id = str + id;
        }

        return id;
    }


    public static String Mid2Uid(String str10) {
        String mid = "";
        int count = 1;
        for (int i = str10.length() - 7; i > -7; i = i - 7) // 从最后往前以7字节为一组读取字符
        {
            int offset = i < 0 ? 0 : i;
            int len = i < 0 ? str10.length() % 7 : 7;
            String temp = str10.substring(offset, offset + len);
            String url = IntToEnode62(Integer.valueOf(temp));
            if (count != 3) {//z xghm uXym 生成的链接从右往左的前2组，4位一组，不足4位的补0
                for (int j = 0; j < 4 - url.length(); j++) {
                    url = "0" + url;
                }
            }
            mid = url + mid;
            count++;
        }
        return mid;
    }
}
