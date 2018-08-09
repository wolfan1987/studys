package com.anmf.daocommon;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.anmf.exception.CommonException;

/**
 * 日期转换类
 * 
 * @author Administrator
 * 
 */
public class DateConversion {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

	private static SimpleDateFormat sdf1 = new SimpleDateFormat(
			"yyyy年MM月dd日 HH:mm:ss");

	private static SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm:ss");

	/**
	 * 由(Date)类型日期 转换为 String 类型
	 * 
	 * @param date
	 *            Date类型日期
	 * @return String类型日期
	 */
	public static String DateToString(Date date) {
		return sdf.format(date);
	}

	/**
	 * 由(String)类型日期 转换为 Date 类型
	 * 
	 * @param str
	 *            String类型日期
	 * @return Date类型日期
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
	 * 由 Timestamp 类型 转换为 String 类型
	 * 
	 * @param timestamp
	 *            Timestamp类型日期时间
	 * @return String类型日期时间
	 */
	public static String StringToTimestamp(Timestamp timestamp) {
		if (timestamp != null)
			return sdf1.format(new Date(timestamp.getTime()));
		return null;
	}

	/**
	 * 
	 * 由String 类型 转换为 Timestamp 类型
	 * 
	 * @param source
	 *            String类型日期时间
	 * @return Timestamp类型日期时间
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
	 * 从字符串到日期
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
	 * 从日期到字符串
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
