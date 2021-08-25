package com.bjsxt;


import org.apache.shiro.codec.Base64;

public class Test {
    public static void main(String[] args) {
        String s = Base64.encodeToString("12345678".getBytes());
        System.out.println(s);
        String s1 = Base64.decodeToString(s);
        System.out.println(s1);
    }
}
