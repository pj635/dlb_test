package com.runoob.dlb;

import java.net.DatagramPacket;  
import java.net.InetAddress;  
import java.net.MulticastSocket;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.codec.binary.Hex;  
  

public class UdpGroupClient {    
      
    @SuppressWarnings("deprecation")
	public static void test() throws Exception{  
        InetAddress group = InetAddress.getByName("233.0.0.6");//组播地址  
        int port = 6000;  
        MulticastSocket socket = null;//创建组播套接字  
        try {  
        	socket = new MulticastSocket(port);  
        	socket.joinGroup(group);//加入连接  
            byte[] buffer = new byte[8192];  
            System.out.println("接收数据包启动！(启动时间: "+new Date()+")");  
            while(true){  
                // 建立一个指定缓冲区大小的数据包  
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);  
                socket.receive(dp);  
                byte[] encrypted = dp.getData();
                System.out.println("收到的组播数据（加密）（byte[]）：" + Arrays.toString(encrypted));  
                
                // 对收到的组播数据进行解密
                String keyHexStr = "399b0686376d80318b872e61ec1b9c9c";
//        		String ivHexStr="3dafba429d9eb430b422da802c9fac41";
                String ivHexStr="00000000000000000000000000000000";
        	    byte[] keyByte = null;
        	    byte[] ivByte = null;
        	    
        	    keyByte = Hex.decodeHex(keyHexStr.toCharArray());
            	ivByte = Hex.decodeHex(ivHexStr.toCharArray());
                
                // 解密
                String DeString = AesCTR.getInstance().decrypt(encrypted, keyByte, ivByte);
                System.out.println("解密后的字串是：" + DeString);
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }finally{  
            if(socket!=null){  
                try {  
                	socket.leaveGroup(group);  
                	socket.close();  
                } catch (Exception e2) {  
                	System.out.println("leave group or close socket error"); 
                }  
            }  
        }  
    }  
     
    public static void main(String[] args) throws Exception {  
        test();  
    } 
  
}  