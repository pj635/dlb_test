package com.runoob.dlb;


import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
 
 
public class AesCBC {
//	private static String keyHexStr = "399b0686376d80318b872e61ec1b9c9c";
//    private static String ivHexStr="3dafba429d9eb430b422da802c9fac41";
	private static String keyHexStr = "31313131313131313131313131313131";
	private static String ivHexStr="31313131313131313131313131313131";
    private static byte[] keyByte = null;
    private static byte[] ivByte = null;
    
    private static AesCBC instance=null;
    
 
    public AesCBC() throws DecoderException{
    	keyByte = Hex.decodeHex(keyHexStr.toCharArray());
    	ivByte = Hex.decodeHex(ivHexStr.toCharArray());
    }
 
 
    public static AesCBC getInstance() throws DecoderException {
        if (instance==null)
            instance= new AesCBC();
        return instance;
    }
 

    // 加密
    public byte[] encrypt(String sSrc, byte[] keyByte, byte[] ivByte) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        System.out.println("加密算法是：" + cipher.getAlgorithm());
        SecretKeySpec skeySpec = new SecretKeySpec(keyByte, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivByte);//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        return encrypted;
    }
 
 
	// 解密
	public String decrypt(byte[] encrypted, byte[] keyByte, byte[] ivByte) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(keyByte, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec iv = new IvParameterSpec(ivByte);
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
		byte[] original = cipher.doFinal(encrypted);
		String originalString = new String(original, "utf-8");
		return originalString;
	}
 
    
    public static void main(String[] args) throws Exception {
        // 需要加密的字串
        String cSrc = "123456";
        System.out.println("加密前的字串是："+cSrc);
        // 加密
        byte[] encrypted = AesCBC.getInstance().encrypt(cSrc, keyByte, ivByte);
        System.out.println("加密后的结果是(byte[])："+ Arrays.toString(encrypted));
        System.out.println("加密后的结果是(base64)："+ Base64.getEncoder().encodeToString(encrypted));
        
        // 解密
        String DeString = AesCBC.getInstance().decrypt(encrypted, keyByte, ivByte);
        System.out.println("解密后的字串是：" + DeString);
    }
    
}







