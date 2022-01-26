package dlb_test;

import java.io.File;
import java.io.FileInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.yaml.snakeyaml.Yaml;

import com.alibaba.fastjson.JSONObject;

import dlb.AesCTR;

public class TestDLB {
	private static String udpGroupServerIp = "233.0.0.6";
	private static int udpGroupServerPort = 6000;
	private MulticastSocket socket = null; // 组播套接字
	private InetAddress group = null;

	@SuppressWarnings("deprecation")
	@BeforeClass
	public void setup_class() throws Exception {	
		Yaml yaml = new Yaml(); // 初始化Yaml解析器
		File f = new File("config.yaml");
		Map<?, ?> result = (Map<?, ?>) yaml.load(new FileInputStream(f));

		System.out.println(result.getClass());
		System.out.println(result);

		String keyHexStr = (String) ((Map<?, ?>) result.get("udpGroup")).get("keyHexStr");
		String ivHexStr = (String) ((Map<?, ?>) result.get("udpGroup")).get("ivHexStr");
		System.out.println("keyHexStr:" + keyHexStr);
		System.out.println("ivHexStr:" + ivHexStr);

		group = InetAddress.getByName(udpGroupServerIp); // 组播地址
		socket = new MulticastSocket(udpGroupServerPort);
		socket.joinGroup(group);// 加入连接
		byte[] buffer = new byte[110];
		System.out.println("接收数据包启动！(启动时间: " + new Date() + ")");

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
		
		// yaml反序列化
		yaml = new Yaml();
		Map<String, Object> map = (Map<String, Object>) yaml.load(DeString);
		JSONObject obj = new JSONObject(map);
		System.out.println(obj.toString());
		System.out.println(obj.get("params").getClass());
		
	}

	@SuppressWarnings("deprecation")
	@AfterClass
	public void teardown_class() throws Exception {
		if (socket != null) {
			socket.leaveGroup(group);
			socket.close();
		}
	}

	@Test
	public void test_1() throws Exception {
		int responseCode = 200;

		Assert.assertEquals(responseCode, 200, "The response code should be 200!");

	}
}
