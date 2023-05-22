package com.jkr.utils;


import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * AES加密解密类
 * @author znw
 */
@Component
public class AesEncryptUtil {

    private static final String KEY = "qwertyuiopasdf12";


    /**
     * method_name:desEncrypt
     * create_user:ZNW
     * create_date:2020/7/23
     * create_time:11:20
     * describe:解密方法
     * param:[data]
     * return:java.lang.String
     */
    public static String desEncrypt(String data){
        try {
            byte[] encrypted1 = new Base64().decode(data);

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(KEY.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original).trim();
            return originalString;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}
