package com.app.finder.common.util;

import java.util.Date;
import java.util.Locale;

import org.ocpsoft.prettytime.PrettyTime;

public class PrettyTimeUtils {

	private static final Locale locale = new Locale("zh");

	/**
	 * 美化时间 如显示为 刚刚
	 * 
	 * @return
	 */
	public static final String rightNow() {
		PrettyTime p = new PrettyTime(locale);
		return p.format(new Date());

	}

	/**
	 * 12 分钟 后
	 * 3 天 后
	 * 3 个月 后
	 * @param millisecond
	 *            1000 * 60 * 12
	 *            1000 * 60 * 60 * 3
	 *            1000 * 60 * 60 * 24 * 3
	 *            1000 * 60 * 60 * 24 * 7 * 3
	 *            2629743830L * 3L
	 *            
	 * @return
	 */
	public static final String timeFromNow(long millisecond) {
		PrettyTime p = new PrettyTime(new Date(0), locale);
		return p.format(new Date(millisecond));
	}
	
	public static final String timeFromNow(Date date) {
		PrettyTime p = new PrettyTime(new Date(0), locale);
		return p.format(date);
	}

	/**
	 * 片刻之前
	 * 12 分钟 前
	 * @param millisecond 
	 * 				6000
	 * 				1000 * 60 * 12
	 * @return
	 */
	public static final String timeAgo(long millisecond) {
		PrettyTime p = new PrettyTime(new Date(millisecond),locale);
		return p.format(new Date(0));
	}
	public static final String timeAgo(Date date) {
		PrettyTime p = new PrettyTime(date,locale);
		return p.format(new Date(0));
	}
	
	public static void main(String[] args) {
		long millis = System.currentTimeMillis();//1407723738109 1407723893765
		System.out.println(millis);
		long l1 = 1407724044890L;
		long l2 = 1407723738109L;
		long l = l1 - l2;
		System.out.println(timeAgo(new Date(l)));
		
	}
}
