package support;

/**
 * <p>类说明：</p>
 * 		
 *    
 * <p>Copyright: Copyright (c) 2008</p>
 *    
 * @author mark
 * 2007-12-12
 *
 * @version 1.0
 */

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateBean {

	public DateBean() {
	}

	/**
	 * 功能说明： 根据时间获取相应的LONG值
	 * 
	 * @param vs_time
	 *            String 格式为：hhmmss。
	 * @return long
	 */
	public final static long getTime(String vs_time) {
		long ll_time = 0l;
		long ll_para = 0l;
		int li_hour = 0;
		int li_minite = 0;
		int li_second = 0;

		try {
			ll_para = (new Long(vs_time)).longValue();
			li_hour = (int) (ll_para / 10000);
			ll_para = (ll_para - li_hour * 10000);
			li_minite = (int) (ll_para / 100);
			li_second = (int) (ll_para - li_minite * 100);

			ll_time = (li_hour * 60 * 60 + li_minite * 60 + li_second) * 1000;

		} catch (Exception e) {

		}
		return ll_time;
	}

	/**
	 * 功能说明： 根据年月信息返回该月的总小时数。 未考虑润年的2月。以后修改。
	 * 
	 * @param vs_str
	 *            String：格式为："YYYYMM"
	 * @return int
	 */
	public final static int getHoursOfMonth(String vs_str) {
		int li_month = Integer.valueOf(vs_str);

		li_month = li_month % 100;

		switch (li_month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			return 24 * 31;
		case 2:
			return 24 * 28;
		case 4:
		case 6:
		case 9:
		case 11:
			return 24 * 30;
		}

		return 0;
	}

	/**
	 * 功能说明： 根据时间获取相应的LONG值
	 * 
	 * @param vs_time
	 *            String 格式为：hhmm。
	 * @return long
	 */
	public final static long getTimeofHHMM(String vs_hhmm) {
		vs_hhmm = vs_hhmm + "00";

		return getTime(vs_hhmm);
	}

	public final static String getFirstDayofMonth(String vs_date) {
		return vs_date.substring(0, 6) + "01";
	}

	public final static String getEndDayofMonth(String vs_date) {
		return vs_date.substring(0, 6) + "31";
	}

	/**
	 * 功能说明： 将“YYYYMMDD”格式的日期转换为“YYYY年MM月DD日”格式。
	 * 
	 * @param vs_date
	 *            String
	 * @return String
	 */
	public final static String getGbDate(String vs_date) {
		if (vs_date == null || vs_date.length() < 8) {
			return vs_date;
		}
		StringBuffer lo_date = new StringBuffer();

		lo_date.append(vs_date.substring(0, 4));
		lo_date.append("年");
		lo_date.append(vs_date.substring(4, 6));
		lo_date.append("月");
		lo_date.append(vs_date.substring(6, 8));
		lo_date.append("日");

		return lo_date.toString();
	}

	/**
	 * 功能说明： 将“YYYYMMDD”格式的日期转换为“YYYY-MM-DD”格式。
	 * 
	 * @param vs_date
	 *            String
	 * @return String
	 */
	public final static String getBytrDate(String vs_date) {
		if (vs_date == null || vs_date.length() < 8) {
			return vs_date;
		}
		StringBuffer lo_date = new StringBuffer();

		lo_date.append(vs_date.substring(0, 4));
		lo_date.append("-");
		lo_date.append(vs_date.substring(4, 6));
		lo_date.append("-");
		lo_date.append(vs_date.substring(6, 8));

		return lo_date.toString();
	}

	public final static String getGbToday() {
		String ls_Format = "yyyy年MM月dd日";
		java.util.Date ldt_Today = new java.util.Date();

		SimpleDateFormat lo_Formatter = new SimpleDateFormat(ls_Format);
		String ls_Today = lo_Formatter.format(ldt_Today);

		return ls_Today;
	}

	/**
	 * function: 将带时间的日期型变量转换成"YYYY年MM月DD日的格式" Parameters: vo_Date: Type--
	 * Timestamp Comment-- 带时间的日期型变量
	 * 
	 * Return: Type-- String Comment-- 转换后的日期型数据(格式为:YYYY年MM月DD日)
	 */

	public final static String getGbDate(Timestamp vo_Date) {
		String ls_Format = "yyyy年MM月dd日";

		java.util.Date ldt_Date = new java.util.Date(vo_Date.getTime());

		SimpleDateFormat lo_Formatter = new SimpleDateFormat(ls_Format);
		String ls_Date = lo_Formatter.format(ldt_Date);

		return ls_Date;
	}

	/**
	 * 功能说明： 将日期型数据转换成为“YYYYMMDD”格式。
	 * 
	 * @param vo_Date
	 *            Date
	 * @return String
	 */
	public final static String getDate(java.util.Date vo_Date) {
		String ls_Format = "yyyyMMdd";

		SimpleDateFormat lo_Formatter = new SimpleDateFormat(ls_Format);
		String ls_Date = lo_Formatter.format(vo_Date);

		return ls_Date;
	}

	public static String getDateTime(String dateTime, String patten) {
		if (dateTime==null || dateTime.isEmpty() || dateTime.length() != 8) {
			return "";
		}
		int year = Integer.parseInt(dateTime.substring(0, 4));
		int month = Integer.parseInt(dateTime.substring(4, 6)) - 1;
		int date = Integer.parseInt(dateTime.substring(6, 8));

		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, date);
		SimpleDateFormat formatter = new SimpleDateFormat(patten);
		return formatter.format(calendar.getTime());
	}

	/**
	 * 按YYYYMMDDHHMMSS格式返回当前系统日期
	 */
	public final static String getSysdateTime() {
		String ls_Format = "yyyyMMddHHmmss";

		java.util.Date ldt_Date = new java.util.Date();

		SimpleDateFormat lo_Formatter = new SimpleDateFormat(ls_Format);
		String ls_Date = lo_Formatter.format(ldt_Date);

		return ls_Date;
	}

	/**
	 * 按YYYYMMDD格式返回当前系统日期
	 */
	public final static String getSysdate() {
		String ls_Format = "yyyyMMdd";

		java.util.Date ldt_Date = new java.util.Date();

		SimpleDateFormat lo_Formatter = new SimpleDateFormat(ls_Format);
		String ls_Date = lo_Formatter.format(ldt_Date);

		return ls_Date;
	}

	/**
	 * 按patten格式返回当前系统日期
	 */
	public final static String getSysdate(String patten) {
		String ls_Format = patten;

		java.util.Date ldt_Date = new java.util.Date();

		SimpleDateFormat lo_Formatter = new SimpleDateFormat(ls_Format);
		String ls_Date = lo_Formatter.format(ldt_Date);

		return ls_Date;
	}

	/**
	 * 返回系统当前的时间
	 */
	public static Timestamp getToday() {
		return new Timestamp((new java.util.Date()).getTime());
	}

	/**
	 * 根据字符串返回对应的时间对象.
	 */
	public static Timestamp getDatetime(String vs_datetime) {
		// if(StingUtil.isEmpty(vs_datetime)||vs_datetime.length()!=14) return ;
		StringBuffer lo_date = new StringBuffer(200);

		lo_date.append(vs_datetime.substring(0, 4));
		lo_date.append("-");
		lo_date.append(vs_datetime.substring(4, 6));
		lo_date.append("-");
		lo_date.append(vs_datetime.substring(6, 8));
		lo_date.append(" ");

		lo_date.append(vs_datetime.substring(8, 10));
		lo_date.append(":");
		lo_date.append(vs_datetime.substring(10, 12));
		lo_date.append(":");
		if (vs_datetime.length() < 14)
			lo_date.append("00");
		else
			lo_date.append(vs_datetime.substring(12, 14));

		return Timestamp.valueOf(lo_date.toString());
	}

	public static String timestampToGbDate(String date) {
		String result = "";
		try {
			result = date.substring(0, 4) + date.substring(5, 7)
					+ date.substring(8, 10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getGbDate(result);
	}

	public static String timestampToGbDateWithTime(String date) {
		String result = "";
		try {
			result = date.substring(0, 4) + date.substring(5, 7)
					+ date.substring(8, 10);
			result = getGbDate(result);
			result = result + " " + date.substring(11, 13) + "点"
					+ date.substring(14, 16) + "分";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public final static String getGenDate(String vs_date) {
		StringBuffer lo_date = new StringBuffer();

		lo_date.append(vs_date.substring(0, 4));
		lo_date.append("-");
		lo_date.append(vs_date.substring(4, 6));
		lo_date.append("-");
		lo_date.append(vs_date.substring(6, 8));

		return lo_date.toString();
	}

	/**
	 * 功能说明： 将“YYYY-MM-DD”格式的日期转化为“YYYYMMDD”格式。
	 * 
	 * @param vs_date
	 *            String
	 * @return String
	 */
	public final static String getDateChar8(String vs_date) {
		if (vs_date != null)
			return vs_date.substring(0, 4) + vs_date.substring(5, 7)
					+ vs_date.substring(8, 10);
		else
			return null;
	}

	public final static String getCurrentQuarter() {
		String date = getSysdate().substring(4);
		int intDate = Integer.parseInt(date);

		if (intDate < 401) {
			return 1 + "";
		} else if (intDate < 701) {
			return 2 + "";
		} else if (intDate < 901) {
			return 3 + "";
		} else {
			return 4 + "";
		}
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public final static Date dateString2Date(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		return formatter.parse(date, new ParsePosition(0));
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public final static Date dateString2Date(String date, String patten) {
		SimpleDateFormat formatter = new SimpleDateFormat(patten);
		return formatter.parse(date, new ParsePosition(0));
	}

	/**
	 * 计算两个日期之间的天数
	 * 
	 * @param fDate
	 * @param oDate
	 * @return
	 */
	public final static int daysOfTwo(String fDate, String oDate) {
		// 首先定义一个calendar，必须使用getInstance()进行实例化
		Calendar aCalendar = Calendar.getInstance();
		// 里面野可以直接插入date类型
		aCalendar.setTime(parseDateTime(fDate, "yyyyMMdd"));
		// 计算此日期是一年中的哪一天
		int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
		aCalendar.setTime(parseDateTime(oDate, "yyyyMMdd"));

		int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
		// 求出两日期相隔天数
		int days = day2 - day1;

		Integer fdatayear = Integer.parseInt(fDate.substring(0, 4));
		Integer oDateyear = Integer.parseInt(oDate.substring(0, 4));
		if (fdatayear - oDateyear != 0) {
			int newdays = 0;
			int fdatayears = fdatayear;
			for (; fdatayears < oDateyear; fdatayears++) {
				if (fdatayears % 4 == 0)
					newdays++;

			}
			int alldays = (fdatayear - oDateyear) * 365;
			days = days - alldays + newdays;
		}
		return days;
	}

	/**
	 * 计算当天日期的INT数
	 * 
	 * @param fDate
	 * @param oDate
	 * @return
	 */
	public final static int daysOfOne(String fDate) {
		// 首先定义一个calendar，必须使用getInstance()进行实例化
		Calendar aCalendar = Calendar.getInstance();
		// 里面野可以直接插入date类型
		aCalendar.setTime(parseDateTime(fDate, "yyyyMMdd"));
		// 计算此日期是一年中的哪一天
		int day = aCalendar.get(Calendar.DAY_OF_YEAR);
		return day;
	}

	/**
	 * 将指定字符串转换成日期格式
	 * 
	 * @param String
	 *            param, 格式: "yyyy-MM-dd HH:mm:ss".
	 * @return Date
	 */
	public final static Date parseDateTime(String param, String patten) {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat(patten);
		try {
			date = sdf.parse(param);
		} catch (ParseException ex) {
			ex.printStackTrace();
			// System.out.println(ex);
		}
		return date;
	}

	/**
	 * 获取月最后一天
	 * 
	 * @return month
	 */
	@SuppressWarnings("static-access")
	public final static String endDayOfMonth(String month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(parseDateTime(DateBean.getNextMonth(month) + "01",
				"yyyyMMdd"));
		cal.add(cal.DAY_OF_MONTH, -1);
		return DateBean.getDate(cal.getTime());
	}

	/**
	 * 返回下个月
	 * 
	 * @param month
	 * @return
	 */
	public final static String getNextMonth(String month) {
		String nextSession = "";
		if (month == null || month.length() != 6) {
			return "";
		}
		int startYear = Integer.parseInt(month.substring(0, 4));
		int startMonth = Integer.parseInt(month.substring(4, 6));
		startMonth = startMonth + 1;
		if (startMonth > 12) {
			startYear = startYear + 1;
			startMonth = 1;
		}
		if (startMonth < 10) {
			nextSession = String.valueOf(startYear) + "0"
					+ String.valueOf(startMonth);
		} else {
			nextSession = String.valueOf(startYear)
					+ String.valueOf(startMonth);
		}
		return nextSession;
	}
        
	/**
	 * 返回下个月
	 * 
	 * @param month
	 * @return
	 */
	public final static String getNextSecond(String time) {
		return getAfterTimeBySecond(time,1);
	}

	/**
	 * 上一个月
	 * 
	 * @param startSession
	 * @return
	 */
	public static String getLastMonth(String startSession) {
		String lastSession = "";
		if (startSession == null || startSession.length() != 6) {
			System.out.println("月份值有问题");
			return "";
		}
		int startYear = Integer.parseInt(startSession.substring(0, 4));
		int startMonth = Integer.parseInt(startSession.substring(4, 6));
		startMonth = startMonth - 1;
		if (startMonth <= 0) {
			startYear = startYear - 1;
			startMonth = 12;
		}
		if (startMonth < 10) {
			lastSession = String.valueOf(startYear) + "0"
					+ String.valueOf(startMonth);
		} else {
			lastSession = String.valueOf(startYear)
					+ String.valueOf(startMonth);
		}
		return lastSession;
	}

	/**
	 * 查询当前日期前相隔天数的日期
	 * 
	 * @param days
	 *            int
	 * @return String
	 */
	@SuppressWarnings("static-access")
	public final static String getBeforDay(int days, String patten) {
		Calendar cal = Calendar.getInstance();
		cal.add(cal.DATE, -days);
		return getDateTime(cal.getTime(), patten);
	}

	/**
	 * 查询当前日期前相隔天数的日期
	 * 
	 * @param days
	 *            int
	 * @return String
	 */
	@SuppressWarnings("static-access")
	public final static String getBeforDay(String curDate, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateBean.dateString2Date(curDate));
		cal.add(cal.DATE, -days);
		return getDateTime(cal.getTime(), "yyyyMMdd");
	}

	/**
	 * 相隔秒数前的时间
	 * 
	 * @param days
	 *            int
	 * @return String
	 */
	@SuppressWarnings("static-access")
	public final static String getBeforTimeBySecond(String curTime, int second) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateBean.dateString2Date(curTime, "yyyyMMddHHmmss"));
		cal.add(cal.SECOND, -second);
		return getDateTime(cal.getTime(), "yyyyMMddHHmmss");
	}
       /**
	 * 相隔秒数后的时间
	 * 
	 * @param days
	 *            int
	 * @return String
	 */
	@SuppressWarnings("static-access")
	public final static String getAfterTimeBySecond(String curTime, int second) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateBean.dateString2Date(curTime, "yyyyMMddHHmmss"));
		cal.add(cal.SECOND, second);
		return getDateTime(cal.getTime(), "yyyyMMddHHmmss");
	}

	/**
	 * 多少分钟前的时间
	 * 
	 * @param curTime
	 * @param minute
	 * @return
	 */
	@SuppressWarnings("static-access")
	public final static String getBeforTimeByMinute(String curTime, int minute) {
		Calendar cal = Calendar.getInstance();
		if(curTime==null||curTime.isEmpty())
			return null;
		if(curTime.length()==8) curTime+="000000";
		if(curTime.length()==12) curTime+="00";
		if(curTime.length()!=14) return null;
		cal.setTime(DateBean.dateString2Date(curTime, "yyyyMMddHHmmss"));
		cal.add(cal.MINUTE, -minute);
		return getDateTime(cal.getTime(), "yyyyMMddHHmmss");
	}

	/**
	 * 返回指定时间的指定格式的字符串值
	 * 
	 * @param date
	 *            Date
	 * @param patten
	 *            String 如："yyyy-MM-dd HH:mm:ss"
	 * @return String
	 */
	public final static String getDateTime(Date date, String patten) {
		SimpleDateFormat sdf = new SimpleDateFormat(patten);
		return sdf.format(date);
	}

	/**
	 * 返回一个日期是星期几
	 * 
	 * @return String
	 */
	public final static String getDayOfWeek(String curDate) {
		// 首先定义一个calendar，必须使用getInstance()进行实例化
		Calendar aCalendar = Calendar.getInstance();
		// 里面野可以直接插入date类型
		aCalendar.setTime(parseDateTime(curDate, "yyyy-MM-dd"));
		// 计算此日期是一年中的哪一天
		int day1 = aCalendar.get(Calendar.DAY_OF_WEEK);
		String arr[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		return arr[day1 - 1];
	}

	/**
	 * 返回两个时间相隔的毫秒数
	 * 
	 * @param startdays
	 * @param enddays
	 * @return
	 */
	public final static long getIntervalTimes(String startdays, String enddays) {
		Date startday = DateBean.getDatetime(startdays);
		Date endday = DateBean.getDatetime(enddays);
		// 确保startday在endday之前
		if (startday.after(endday)) {
			Date cal = startday;
			startday = endday;
			endday = cal;
		}
		// 分别得到两个时间的毫秒数
		long sl = startday.getTime();
		long el = endday.getTime();

		long ei = el - sl;
		return ei;
		// return (int) (ei / (1000 * 60 * 30));
	}
	
	public final static double getIntervalMinute(String startdays, String enddays) throws Exception {
		if (startdays.isEmpty() || enddays.isEmpty()) {
			throw new Exception("时间有问题");
		}
		long ei = DateBean.getIntervalTimes(startdays, enddays);
		return (double) ((ei * 1.0) / (1000 * 60));
	}
	

	/**
	 * 返回两个时间相隔的小时数
	 * 
	 * @param startdays
	 * @param enddays
	 * @return
	 * @throws Exception 
	 */
	public final static double getIntervalHours(String startdays, String enddays) throws Exception {
		if (startdays.isEmpty() || enddays.isEmpty()) {
			throw new Exception("时间有问题");
		}
		// DebugOut.println(""+startdays+"/"+enddays);
		long ei = DateBean.getIntervalTimes(startdays, enddays);
		return (double) ((ei * 1.0) / (1000 * 60 * 60));
	}

	public static void main(String args[]) {

		System.out.println(DateBean.getBeforTimeBySecond("20091014103000", 60));
	}
	
	

	/**
	 * 
	*@author zengcj
	*功能：日期加减法
	*@param datetime
	*@param days
	*@return
	*@throws ParseException
	*@throws java.text.ParseException
	 */
	public static String getDateDeal(String datetime, int days) {
		String rDate = "";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		datetime = datetime.substring(0, 4)+"-"+datetime.substring(4, 6)+"-"+datetime.substring(6, 8);
		
		Date d = null;
		try {
			d = df.parse(datetime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		rDate = df.format(new Date(d.getTime() + days * 24 * 60 * 60 * 1000));
		rDate = rDate.replaceAll("-", "");
		return rDate;
	}
}
