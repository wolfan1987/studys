package org.andrewliu.javanet.security.ssl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import java.security.cert.Certificate;
/**
 * 从数字证书库文件中直接读取指定别名所对应的数字证书
 * @author de
 *
 */
public class DigitalSignatureDemo {

	public static void main(String[] args) {
		/**
		 * 数字证书库的访问密码
		 */
		
		String passWord = "123456";
		FileInputStream oneFileInputStream  = null;
		//指向密钥库文件所在的文件目录
		try {
			oneFileInputStream = new FileInputStream("F:\\PosWorkspace\\netsecurity\\mykeystores\\server.keystore");
			/**
			 * 定义一个证书项目的KeyStore对象，从而实现从KeyStore文件中提取私钥，证书
			 */
			KeyStore oneKeyStore = null;
			/**
			 * JKS 代表采用Java中的keytools证书工具所创建出的证书私钥格式，根据所创建的数字证书格式化
			 * 获得对应的KeyStore对象实例
			 */
			oneKeyStore = KeyStore.getInstance("JKS");
			/**
			 * 从给定输入流中加载此KeyStore，并指定相关的密码
			 */
			oneKeyStore.load(oneFileInputStream,passWord.toCharArray());
			/**
			 * Certificate 为管理各种身份证书的抽象类，而它的子类X509Certificat为X.509证书的抽象
			 * 类，X509Certificat类提供了一种访问X509证书所有属性的标准方式
			 */
			Certificate oneCertificate = null;
			/**
			 * 下面的"server_jks_test" 为指定的数字证书的别名，因此返回与给定别名相关联的数字
			 * 证书对象
			 * 如果给定的别名标识是通过调用setCertificateEntry方法创建的信息条目，或者是通过调用
			 * TrustedCertificateEntry为参数的setEntry方法创建的信息条目，则返回包含在访条件中的
			 * 可信的数字证书；但如果给定的别名标识是通过调用setkeyEntry方法创建的信息条目，或者
			 * 是通过调用 以PrivateKeyEntry为参数的setEntry方法而创建的信息条目，则返回该信息
			 * 条目中证书链中第一个信息条目元素。
			 */
			oneCertificate = oneKeyStore.getCertificate("liuDAServer");
			
			System.out.println("数字证书中的完整信息：\n"+oneCertificate.toString());
			System.out.println("证书中获取公钥：\n"+oneCertificate.getPublicKey());
			System.out.println("证书的类型：\n"+oneCertificate.getType());
			System.out.println("证书的哈希码值:\n"+oneCertificate.hashCode());
			System.out.println("证书的编码形式：\n"+oneCertificate.getEncoded());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
}
