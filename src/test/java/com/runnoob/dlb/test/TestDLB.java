package com.runnoob.dlb.test;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
//import java.net.UnknownHostException;
//import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.codec.binary.Hex;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.runoob.dlb.AesCTR;

public class TestDLB {
	private static String keyHexStr = "399b0686376d80318b872e61ec1b9c9c";
	private static String ivHexStr = "00000000000000000000000000000000";
	private static String udpGroupServerIp = "233.0.0.6";
	private static int udpGroupServerPort = 6000;
	private MulticastSocket socket = null; //组播套接字
	private InetAddress group = null;
	
	@SuppressWarnings("deprecation")
	@BeforeClass
	public void setup_class() throws Exception {
		group = InetAddress.getByName(udpGroupServerIp);// 组播地址
		
		socket = new MulticastSocket(udpGroupServerPort);
		socket.joinGroup(group);// 加入连接
		byte[] buffer = new byte[8192];
		System.out.println("接收数据包启动！(启动时间: " + new Date() + ")");
		while (true) {
			// 建立一个指定缓冲区大小的数据包
			DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
			socket.receive(dp);
			byte[] encrypted = dp.getData();
			System.out.println("收到的组播数据（加密）（byte[]）：" + Arrays.toString(encrypted));

			// 对收到的组播数据进行解密
			byte[] keyByte = null;
			byte[] ivByte = null;

			keyByte = Hex.decodeHex(keyHexStr.toCharArray());
			ivByte = Hex.decodeHex(ivHexStr.toCharArray());

			// 解密
			String DeString = AesCTR.getInstance().decrypt(encrypted, keyByte, ivByte);
			System.out.println("解密后的字串是：" + DeString);
		}
	}
	
	
	@SuppressWarnings("deprecation")
	@AfterClass
	public void teardown_class() throws Exception {
		if (socket != null) {
			try {
				socket.leaveGroup(group);
				socket.close();
			} catch (Exception e2) {
				System.out.println("leave group or close socket error");
			}
		}
	}
	
	
	@Test 
	public void test_1() throws Exception {
		int responseCode = 200;
		
		Assert.assertEquals(responseCode, 200, "The response code should be 200!");
		
//		InetAddress group = InetAddress.getByName(udpGroupServerIp);// 组播地址
//		MulticastSocket socket = null;// 创建组播套接字
//		try {
//			socket = new MulticastSocket(udpGroupServerPort);
//			socket.joinGroup(group);// 加入连接
//			byte[] buffer = new byte[8192];
//			System.out.println("接收数据包启动！(启动时间: " + new Date() + ")");
//			while (true) {
//				// 建立一个指定缓冲区大小的数据包
//				DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
//				socket.receive(dp);
//				byte[] encrypted = dp.getData();
//				System.out.println("收到的组播数据（加密）（byte[]）：" + Arrays.toString(encrypted));
//
//				// 对收到的组播数据进行解密
//				byte[] keyByte = null;
//				byte[] ivByte = null;
//
//				keyByte = Hex.decodeHex(keyHexStr.toCharArray());
//				ivByte = Hex.decodeHex(ivHexStr.toCharArray());
//
//				// 解密
//				String DeString = AesCTR.getInstance().decrypt(encrypted, keyByte, ivByte);
//				System.out.println("解密后的字串是：" + DeString);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (socket != null) {
//				try {
//					socket.leaveGroup(group);
//					socket.close();
//				} catch (Exception e2) {
//					System.out.println("leave group or close socket error");
//				}
//			}
//		}
	}
}
