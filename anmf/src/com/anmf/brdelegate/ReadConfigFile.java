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
 * 读取配置文件中的信息
 * 
 * @author Administrator
 * 
 */
public class ReadConfigFile {
	public static Map<String, String> readConfig() throws BRDelegateException {
		/**
		 * 用于存放从配置文件中读取出来的dao command delegate对象字符串
		 */
		Map<String, String> mapObject = new HashMap<String, String>();
		/**
		 * 下面是用jdom对配置文件进行解析读取,然后存入HashMap中
		 */
		SAXBuilder builder = new SAXBuilder();
		/**
		 * 获得配置文件的存放位置
		 */
		String strPath = ReadConfigFile.class.getResource("/").getPath()
				+ "anmf-config.xml";
		try {
			Document document = builder.build(new File(strPath));
			/**
			 * 获得根元素
			 */
			Element rootElement = document.getRootElement();
			/**
			 * 通过根元素得到所有的子元素,如dao command delegate
			 */
			List<Element> elementChilder = rootElement.getChildren();
			for (Element e : elementChilder) {
				/**
				 * 获得子元素的子元素
				 */
				List<Element> elementList = e.getChildren();
				for (Element ec : elementList) {
					/**
					 * 将所有子元素存入Map
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
