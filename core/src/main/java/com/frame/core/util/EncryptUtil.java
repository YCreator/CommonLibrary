package com.frame.core.util;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密/解密工具
 * Created by yzd on 2016/6/23.
 */
public class EncryptUtil {

    /**
     * SHA1加密工具
     **/
    @SuppressLint("DefaultLocale")
    public static String encryptSHA(@NonNull String in) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(in.getBytes("UTF-8"));
            byte[] result = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                int i = b & 0xff;
                if (i < 0xf) {
                    sb.append(0);
                }
                sb.append(Integer.toHexString(i));
            }
            System.out.println(sb.toString().toUpperCase());
            return sb.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * md5加密
     *
     * @param in
     * @return
     */
    public static String encryptMD5(@NonNull String in) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(in.getBytes());
            byte[] a = digest.digest();
            int len = a.length;
            StringBuilder sb = new StringBuilder(len << 1);
            for (byte b : a) {
                sb.append(Character.forDigit((b & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(b & 0x0f, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 散列哈希加密算法
     *
     * @param key
     * @param str
     * @return
     */
    public static String encryptHmacSha1(@NonNull String key, @NonNull String str) {
        try {
            Mac hmacSha1;
            SecretKeySpec macKey;
            byte[] keyBytes = key.getBytes();
            byte[] text = str.getBytes();
            try {
                hmacSha1 = Mac.getInstance("HmacSHA1");
                macKey = new SecretKeySpec(keyBytes, "HmacSHA1");
            } catch (NoSuchAlgorithmException nsae) {
                hmacSha1 = Mac.getInstance("HMAC-SHA-1");
                macKey = new SecretKeySpec(keyBytes, "HMAC-SHA-1");
            }
            hmacSha1.init(macKey);
            byte[] rawHmac = hmacSha1.doFinal(text);
            StringBuilder sb = new StringBuilder();
            for (byte b : rawHmac) {
                sb.append(byteToHexString(b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException nae) {
            nae.printStackTrace();
            return null;
        } catch (InvalidKeyException ike) {
            ike.printStackTrace();
            return null;
        }
    }

    /**
     * DES加密
     * @param key
     * @param str
     * @return
     */
    public static String encryptDES(String key, String str) {
        try {
            Key k = toKey(Base64.decode(key, Base64.DEFAULT));
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, k);
            return new String(cipher.doFinal(str.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * DES解密
     * @param key
     * @param str
     * @return
     * @throws Exception
     */
    public static String decryptDES(String key, String str)  {
        try {
            Key k = toKey(Base64.decode(key, Base64.DEFAULT));
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, k);
            return new String(cipher.doFinal(str.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**

     * 文件加密
     * @param key           key
     * @param sourceFile    源文件
     * @param destFileName  加密后的文件名
     * @throws Exception
     */
    public static void encryptFileByDES(String key, File sourceFile, String destFileName) throws Exception {
        if (sourceFile.exists() && sourceFile.isFile()) {
            String destFilePath = String.format("%s/%s", sourceFile.getParent(), destFileName);
            File destFile = new File(destFilePath);
            if (destFile.createNewFile()) {
                InputStream in = new FileInputStream(sourceFile);
                OutputStream out = new FileOutputStream(destFile);
                Key k = toKey(Base64.decode(key, Base64.DEFAULT));
                Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, k);
                CipherInputStream cin = new CipherInputStream(in, cipher);
                byte[] cache = new byte[1024];
                int nRead;
                while ((nRead = cin.read(cache)) != -1) {
                    out.write(cache, 0, nRead);
                    out.flush();
                }
                out.close();
                cin.close();
                in.close();
            }
        }
    }

    /**
     * 文件解密
     * @param key           key
     * @param sourceFile    源文件
     * @param destFileName  解密后的文件名
     * @throws Exception
     */
    public static void decryptFileByDES(String key, File sourceFile, String destFileName) throws Exception {
        if (sourceFile.exists() && sourceFile.isFile()) {
            String destFilePath = String.format("%s/%s", sourceFile.getParent(), destFileName);
            File destFile = new File(destFilePath);
            if ( destFile.createNewFile()) {
                InputStream in = new FileInputStream(sourceFile);
                OutputStream out = new FileOutputStream(destFile);
                Key k = toKey(Base64.decode(key, Base64.DEFAULT));
                Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, k);
                CipherOutputStream cout = new CipherOutputStream(out, cipher);
                byte[] cache = new byte[1024];
                int nRead;
                while ((nRead = in.read(cache)) != -1) {
                    cout.write(cache, 0, nRead);
                    cout.flush();
                }
                cout.close();
                out.close();
                in.close();
            }
        }
    }

    /**
    * 生成密钥
    * @param seed 密钥种子
    * @return
    * @throws Exception
    */
    @SuppressLint("TrulyRandom")
    public static String getSecretKey(String seed) throws Exception {
        SecureRandom secureRandom;
        if (seed != null && !"".equals(seed))
            secureRandom = new SecureRandom(seed.getBytes());
        else
            secureRandom = new SecureRandom();
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES/CBC/PKCS5Padding");
        keyGenerator.init(secureRandom);
        SecretKey secretKey = keyGenerator.generateKey();
        return new String(Base64.encode(secretKey.getEncoded(),Base64.DEFAULT));
    }

    /**
     * 转换为十六进制
     * @param ib
     * @return
     */
    private static String byteToHexString(byte ib) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] ob = new char[2];
        ob[0] = Digit[(ib >>> 4) & 0X0f];
        ob[1] = Digit[ib & 0X0F];
        return new String(ob);
    }

    /**
     * 转换密钥
     * @param key
     * @return
     * @throws Exception
     */
    private static Key toKey(byte[] key) throws Exception {
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        return keyFactory.generateSecret(dks);
    }
}
