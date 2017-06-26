package com.baseandroid.utils;


import android.util.Base64;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptUtil {

    private static final String AES_KEY = "Q1anBa0*ShengHu0";
    private static final String AES_IV = "L+\\~f4,Ir)b$=pkf";
    private static Queue<Cipher> eciphers = new ConcurrentLinkedQueue<>();
    private static Queue<Cipher> dciphers = new ConcurrentLinkedQueue<>();

    public static String decryptBase64(String base64Aes) throws Exception {
        byte[] decodedBytes = Base64.decode(base64Aes.getBytes("UTF-8"), Base64.DEFAULT);
        Cipher dcipher = null;
        try {
            dcipher = getCipher(false);
            return new String(dcipher.doFinal(decodedBytes), "UTF-8");
        } finally {
            returnCipher(false, dcipher);
        }
    }

    private static Cipher getCipher(boolean encrypt) throws Exception {
        Queue<Cipher> pool = encrypt ? eciphers : dciphers;
        Cipher cipher = pool.poll();

        if (cipher == null) {
            byte[] keyBytes = padWithZeros(AES_KEY.getBytes("UTF-8"), 32);
            byte[] ivBytes = padWithZeros(AES_IV.getBytes("UTF-8"), 16);
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec KeySpec = new SecretKeySpec(keyBytes, "AES");
            cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, KeySpec, new IvParameterSpec(ivBytes));
        }

        return cipher;
    }

    private static void returnCipher(boolean encrypt, Cipher cipher) {
        Queue<Cipher> pool = encrypt ? eciphers : dciphers;

        if (cipher != null) {
            pool.offer(cipher);
        }

        while (pool.size() > 3) {
            pool.poll();
        }
    }

    private static byte[] padWithZeros(byte[] input, int lenNeed) {
        int rest = input.length % lenNeed;
        if (rest > 0) {
            byte[] result = new byte[input.length + (lenNeed - rest)];
            System.arraycopy(input, 0, result, 0, input.length);
            return result;
        }
        return input;
    }

}
