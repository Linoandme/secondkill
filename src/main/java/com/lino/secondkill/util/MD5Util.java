package com.lino.secondkill.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    public static String md5(String str){
        return DigestUtils.md5Hex(str);
    }
    private static String salt = "1h2bu6kd0a9";
    //第一次md5
    public static String inputPassToFormPass(String inputPass){
        String str = ""+salt.charAt(2)+salt.charAt(4)+inputPass+salt.charAt(6)+salt.charAt(7);
        return md5(str);
    }
    //数据库md5
    public static String formPassToDbPass(String formPass,String saltDB){
        String str = ""+saltDB.charAt(2)+saltDB.charAt(4)+formPass+saltDB.charAt(6)+saltDB.charAt(7);
        return md5(str);
    }

    public static String inputPassToDbPass(String input,String saltDB){
        String formPass = inputPassToFormPass(input);
        return   formPassToDbPass(formPass,saltDB);
    }

   public static void main(String[] args){
        System.out.println(inputPassToFormPass("123456"));
        System.out.println(formPassToDbPass("123456","2k1i123ljo234"));
        System.out.println(inputPassToDbPass("123456","2k1i123ljo234"));
    }
    //e48c081ec55b585f391c28cffca56752
}
