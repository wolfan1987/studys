package org.andrewliu.socket.tcptest;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * 网络编程之： 获取机器网卡/IP/端口信息
 * @author de
 *
 */
public class InetAddressExample_1 {

	
	public static void main(String[] args) {
		try{
			//得到网卡List
			Enumeration<NetworkInterface> interfaceList = NetworkInterface.getNetworkInterfaces();
			if(interfaceList == null){
				System.out.println("-- No iterface found --");
			}else{
				//迭代所有网卡
				while(interfaceList.hasMoreElements()){
					NetworkInterface iface = interfaceList.nextElement();
					System.out.println("Interface "+iface.getName() + " : ");
					//得到网卡的IP地址(IPv4,IPv6)
					Enumeration<InetAddress> addrList = iface.getInetAddresses();
					if(!addrList.hasMoreElements()){
						System.out.println("\t( No addresses from this interface)");
					}
					while(addrList.hasMoreElements()){
						InetAddress address = addrList.nextElement();
						//分别打印出是IPv4还是IPv6.
						System.out.println("\t Address"+((address instanceof  Inet4Address ? "(v4)" : (address instanceof  Inet6Address ? "(V6)" : "?"))));
						System.out.println(": "+address.getHostAddress());
					}
				}
			}
		}catch(SocketException se){
			System.out.println(" Error getting network interfaces:"+se.getMessage());
		}
		String host = "del-PC";
			try{
				System.out.println(host+" : ");
				//通过主机名，得到所有IP地址
				InetAddress[] addressList = InetAddress.getAllByName(host);  //
				
				for ( InetAddress address : addressList){
					System.out.println(" \t"+ address.getHostName()+" /"+address.getHostAddress());
				}
				
			}catch(UnknownHostException e){
				System.out.println("\t Unable to find address for "+host);
			}
	}
	
}


