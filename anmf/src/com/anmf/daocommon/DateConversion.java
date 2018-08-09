package com.anmf.daocommon;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.anmf.exception.CommonException;

/**
 * ����ת����
 * 
 * @author Administrator
 * 
 */
public class DateConversion {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd��");

	private static SimpleDateFormat sdf1 = new SimpleDateFormat(
			"yyyy��MM��dd�� HH:mm:ss");

	private static SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm:ss");

	/**
	 * ��(Date)�������� ת��Ϊ String ����
	 * 
	 * @param date
	 *            Date��������
	 * @return String��������
	 */
	public static String DateToString(Date date) {
		return sdf.format(date);
	}

	/**
	 * ��(String)�������� ת��Ϊ Date ����
	 * 
	 * @param str
	 *            String��������
	 * @return Date��������
	 * @throws CommonException
	 */
	public static Date StringToDate(String str) throws CommonException {
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			throw new CommonException(e);
		}
	}

	/**
	 * �� Timestamp ���� ת��Ϊ String ����
	 * 
	 * @param timestamp
	 *            Timestamp��������ʱ��
	 * @return String��������ʱ��
	 */
	public static String StringToTimestamp(Timestamp timestamp) {
		if (timestamp != null)
			return sdf1.format(new Date(timestamp.getTime()));
		return null;
	}

	/**
	 * 
	 * ��String ���� ת��Ϊ Timestamp ����
	 * 
	 * @param source
	 *            String��������ʱ��
	 * @return Timestamp��������ʱ��
	 * @throws CommonException
	 */
	public static Timestamp TimestampToString(String source)
			throws CommonException {
		try {
			if (source == null || source.equals(""))
				return null;
			return new Timestamp(sdf1.parse(source).getTime());
		} catch (ParseException e) {
			throw new CommonException(e);
		}
	}

	/**
	 * ���ַ���������
	 * 
	 * @param str
	 * @return
	 */
	public static Time stringToTime(String str) {
		String[] s = str.split(":");
		int h = Integer.parseInt(s[0]);
		int m = Integer.parseInt(s[1]);
		int ss = Integer.parseInt(s[2]);
		Time time = new Time(h, m, ss);
		return time;
	}

	/**
	 * �����ڵ��ַ���
	 * 
	 * @param time
	 * @return
	 */
	public static String timeToString(Time time) {
		if (time != null) {
			return sdf2.format(time);
		}
		return null;
	}
}
