package com.yinker.etl.pm.util;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.StringUtils;

@SuppressWarnings("restriction")
public class AESUtil {

	public static String iv = "1234567890666666";


	/**
     * AES加密明文
     * @param key       密钥（16位）
     * @param data      明文
     * @return
     * @throws Exception
     */
	public static String encrypt(String key, String data) throws Exception {
		try {

			if (StringUtils.isBlank(key) || StringUtils.isBlank(data)) { return ""; }
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			int blockSize = cipher.getBlockSize();

			byte[] dataBytes = data.getBytes("UTF-8");
			int plaintextLength = dataBytes.length;
			if (plaintextLength % blockSize != 0) {
				plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
			}

			byte[] plaintext = new byte[plaintextLength];
			System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

			SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
			IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

			cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
			byte[] encrypted = cipher.doFinal(plaintext);

			return new sun.misc.BASE64Encoder().encode(encrypted);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
     * AES解密密文
     * @param key       密钥（16位）
     * @param data      密文
     * @return
     * @throws Exception
     */
	public static String decrypt(String key, String data) throws Exception {
		try {
			if (StringUtils.isBlank(key) || StringUtils.isBlank(data)) { return ""; }
			byte[] encrypted1 = new sun.misc.BASE64Decoder().decodeBuffer(data);

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
			IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

			cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

			byte[] original = cipher.doFinal(encrypted1);
			String originalString = new String(original, "UTF-8");
			return originalString;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
