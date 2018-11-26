package com.whc.util.uuid;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 生成UUID业务类
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年09月26 下午2:12
 */
public class UUIDGenerator {

    /**
     * 生成UUID公用业务方法
     */
    public static String createKey() {
        return UUID.randomUUID().toString();
    }

    /**
     * 创建32位的UUID
     */
    public static String create32Key() {
        return UUID.randomUUID().toString().replaceAll("\\-", "");
    }

    /**
     * 创建指定数量的随机字符串
     *
     * @param length
     * @return
     */
    public static int createRandom(int length) {
        String retStr = "";
        String strTableFirst = "123456789";
        String strTable = "1234567890";

        for (int i = 0; i < length; i++) {
            if (i == 0) {
                double dblR = Math.random() * 9;
                int intR = (int) Math.floor(dblR);
                retStr += strTableFirst.charAt(intR);
            } else {
                double dblR = Math.random() * 10;
                int intR = (int) Math.floor(dblR);
                retStr += strTable.charAt(intR);
            }
        }
        return Integer.parseInt(retStr);
    }

    public static int getPassWordLevel(String password) {
        int passwordType = 0;
        boolean isDigit = false; // 定义一个boolean值, 用来表示是否包含数字
        boolean isLetter = false;// 定义一个boolean值, 用来表示是否包含字母
        boolean isWord = false;     // 定义一个boolean值, 用来表示是否包含特殊字符
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(password);
        isWord = m.find();
        if (password.length() >= 6 && password.length() <= 20) {
            for (int i = 0; i < password.length(); i++) {
                if (Character.isDigit(password.charAt(i))) { // 用char包装类中的判断数字的方法判断每一个字符
                    isDigit = true;
                    continue;
                }
                if (Character.isLetter(password.charAt(i))) { // 用char包装类中的判断字母的方法判断每一个字符
                    isLetter = true;
                    continue;
                }
            }
            if (isDigit && isLetter == false) {
                passwordType = 1;
            }
            if (isDigit == false && isLetter == true) {
                passwordType = 1;
            }
            if ((isDigit == true && isLetter == true) || (isDigit == true && isWord == true) ||
                    (isLetter == true && isWord == true)) {
                passwordType = 2;
            }
            if (isDigit == true && isLetter == true && isWord == true) {
                passwordType = 3;
            }
        } else {
            passwordType = 0;
        }
        return passwordType;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(create32Key());
        }
    }
}