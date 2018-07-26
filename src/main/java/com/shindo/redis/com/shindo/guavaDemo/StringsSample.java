package com.shindo.redis.com.shindo.guavaDemo;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;

/**
 * Guava工具包中String相关方法学习demo
 */
public class StringsSample {

    public static void main(String[] args) {
        //使用com.google.common.base.Strings类的isNullOrEmpty(input)方法判断字符串是否为空
        String input = "";
        boolean isNullOrEmpth = Strings.isNullOrEmpty(input);
        System.out.println(isNullOrEmpth);//true

        //获得两个字符串相同的前缀或者后缀
        String a = "com.sn.shindo.hello";
        String b = "com.sn.shindo.hi";
        String commonPrefix = Strings.commonPrefix(a, b);
        System.out.println(commonPrefix);//com.sn.shindo.h

        String c = "com.jd.marry";
        String d = "com.sn.marry";
        String commonSuffix = Strings.commonSuffix(c, d);
        System.out.println(commonSuffix);//.marry

        //Strings的padStart和padEnd方法来补全字符串
        int minLength = 4;
        String padEndResult = Strings.padEnd("123", minLength, '0');
        System.out.println(padEndResult);//1230

        String padStartResult = Strings.padStart("1", 2, '0');
        System.out.println(padStartResult);//01

        //使用Splitter类来拆分字符串
        Splitter.onPattern("[,，]{1,}");


    }

}
