package org.andrewliu.javanet.security.ssl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

public class DigitalSignatureCertficate {
	public static void main(String[] args) {
		CertificateFactory oneCertificateFactory = null;
		FileInputStream oneFileInputStream = null;
		
		try {
			/**
			 * 生成一个实现指定证书类型的CertificateFactory数字证书工厂类的对象实例，本示例为
			 * X.509格式的数字证书
			 * X.509的CertificateFactory返回的证书必须是java.security.cert.X509Certificate的实例
			 * 而CRL的CertificateFactory返回的证书则必须是java.security.cert.X509CRL的实例
			 */
			oneCertificateFactory = CertificateFactory.getInstance("X.509");
			/**
			 * 将数字证书文件(下面的E:\mykeystores\server.cer)转换为输入流的对象实例
			 */
			oneFileInputStream = new FileInputStream("F:\\PosWorkspace\\netsecurity\\mykeystores\\server.cer");
			
			Certificate oneCertificate = null;
			String oneCertificateContentString = null;
			/**
			 * 根据数字证书工厂和目标数字证书的输入流对象实例创建出一个数字证书的对象实例
			 */
			oneCertificate = oneCertificateFactory.generateCertificate(oneFileInputStream);
			/**
			 * 输出目标数字证书内的相关信息
			 *以下语句可变为：oneCertificateContentString = ((java.security.cert.X509Certificate)oneCertificate).toString(); 
			 *因为本示例的数字证书其实为X.509格式的数字证书
			 */
			oneCertificateContentString = oneCertificate.toString();
			System.out.println(oneCertificateContentString);
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
