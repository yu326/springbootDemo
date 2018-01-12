package com.example.demo.Test;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by koreyoshi on 2017/12/20.
 */
public class TestRegex {
    @org.junit.Test
    public void test() {

        String context = "世界第一的公主殿下Miku";
        ArrayList arrayList = new ArrayList();
        arrayList.add("公2主");
        arrayList.add("miku");
        String regex = String.join("|", arrayList);
        String re = ".*(" + regex + ").*";
        Pattern p = Pattern.compile(re,Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(context);
        boolean bl = matcher.find();
        System.out.println(bl);
    }
}
