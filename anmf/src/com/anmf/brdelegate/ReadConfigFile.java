package com.anmf.brdelegate;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.anmf.exception.BRDelegateException;

/**
 * ��ȡ�����ļ��е���Ϣ
 * 
 * @author Administrator
 * 
 */
public class ReadConfigFile {
	public static Map<String, String> readConfig() throws BRDelegateException {
		/**
		 * ���ڴ�Ŵ������ļ��ж�ȡ������dao command delegate�����ַ���
		 */
		Map<String, String> mapObject = new HashMap<String, String>();
		/**
		 * ��������jdom�������ļ����н�����ȡ,Ȼ�����HashMap��
		 */
		SAXBuilder builder = new SAXBuilder();
		/**
		 * ��������ļ��Ĵ��λ��
		 */
		String strPath = ReadConfigFile.class.getResource("/").getPath()
				+ "anmf-config.xml";
		try {
			Document document = builder.build(new File(strPath));
			/**
			 * ��ø�Ԫ��
			 */
			Element rootElement = document.getRootElement();
			/**
			 * ͨ����Ԫ�صõ����е���Ԫ��,��dao command delegate
			 */
			List<Element> elementChilder = rootElement.getChildren();
			for (Element e : elementChilder) {
				/**
				 * �����Ԫ�ص���Ԫ��
				 */
				List<Element> elementList = e.getChildren();
				for (Element ec : elementList) {
					/**
					 * ��������Ԫ�ش���Map
					 */
					mapObject.put(ec.getName(), ec.getText());
				}
			}
			return mapObject;
		} catch (JDOMException e) {
			throw new BRDelegateException(e);
		} catch (IOException e) {
			throw new BRDelegateException(e);
		}
	}

}
