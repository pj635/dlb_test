package com.runoob.dlb;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.Arrays;
import java.util.Base64;

public class AesCTR {
	
	private static String ivHexStr = "31313131313131313131313131313131";
	private static String keyHexStr = "31313131313131313131313131313131";
//	private static String keyHexStr = "399b0686376d80318b872e61ec1b9c9c";
//  private static String ivHexStr="3dafba429d9eb430b422da802c9fac41";
	
	private static byte[] keyByte = null;
	private static byte[] ivByte = null;
	
	private static AesCTR instance=null;
	
	public AesCTR() throws DecoderException{
    	keyByte = Hex.decodeHex(keyHexStr.toCharArray());
    	ivByte = Hex.decodeHex(ivHexStr.toCharArray());
    }
	
	public static AesCTR getInstance() throws DecoderException {
        if (instance==null)
            instance= new AesCTR();
        return instance;
    }
	
	// 加密
    public byte[] encrypt(String sSrc, byte[] keyByte, byte[] ivByte) throws Exception {
    	Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");	
        SecretKeySpec skeySpec = new SecretKeySpec(keyByte, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivByte);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        return encrypted;
    }
	
	// 解密
	public String decrypt(byte[] encrypted, byte[] keyByte, byte[] ivByte) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(keyByte, "AES");
		Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
		System.out.println("加密算法是：" + cipher.getAlgorithm());
		IvParameterSpec iv = new IvParameterSpec(ivByte);
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
		byte[] original = cipher.doFinal(encrypted);
		String originalString = new String(original, "utf-8");
		return originalString;
	}
    
	public static void main(String[] args) throws Exception {
		// 需要加密的字串
        String cSrc = "123456";
        System.out.println("加密前的字串是："+ cSrc);
        // 加密
        byte[] encrypted = AesCTR.getInstance().encrypt(cSrc, keyByte, ivByte);
        System.out.println("加密后的结果是(byte[])："+ Arrays.toString(encrypted));
        System.out.println("加密后的结果是(base64)："+ Base64.getEncoder().encodeToString(encrypted));
        
        // 解密
        String DeString = AesCTR.getInstance().decrypt(encrypted, keyByte, ivByte);
        System.out.println("解密后的字串是：" + DeString);
	}

}
