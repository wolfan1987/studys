package org.andrewliu.socket.tcptest;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * ������֮�� ��ȡ��������/IP/�˿���Ϣ
 * @author de
 *
 */
public class InetAddressExample_1 {

	
	public static void main(String[] args) {
		try{
			//�õ�����List
			Enumeration<NetworkInterface> interfaceList = NetworkInterface.getNetworkInterfaces();
			if(interfaceList == null){
				System.out.println("-- No iterface found --");
			}else{
				//������������
				while(interfaceList.hasMoreElements()){
					NetworkInterface iface = interfaceList.nextElement();
					System.out.println("Interface "+iface.getName() + " : ");
					//�õ�������IP��ַ(IPv4,IPv6)
					Enumeration<InetAddress> addrList = iface.getInetAddresses();
					if(!addrList.hasMoreElements()){
						System.out.println("\t( No addresses from this interface)");
					}
					while(addrList.hasMoreElements()){
						InetAddress address = addrList.nextElement();
						//�ֱ��ӡ����IPv4����IPv6.
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
				//ͨ�����������õ�����IP��ַ
				InetAddress[] addressList = InetAddress.getAllByName(host);  //
				
				for ( InetAddress address : addressList){
					System.out.println(" \t"+ address.getHostName()+" /"+address.getHostAddress());
				}
				
			}catch(UnknownHostException e){
				System.out.println("\t Unable to find address for "+host);
			}
	}
	
}


