package net.tang.core.tools;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 通用工具类
 * 
 * @author service1
 *
 */
public class CommonUtils {

	private CommonUtils() {
	}

	/**
	 * 字符串转换数字
	 * 
	 * @param id
	 * @return
	 */
	public static Integer parseInt(String id) {
		Integer uId = 0;

		if (id == null || "".equals(id))
			return -1;

		try {
			uId = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			// e.printStackTrace();
			uId = -1;
		}

		return uId;
	}

	/**
	 * 解析前台传来的数据。格式: 1,2,3
	 * 
	 * @param param
	 * @return
	 */
	public static Integer[] parseParmas(String param) {
		Objects.requireNonNull(param);
		
		Integer[] idList = null;

		try {
			String id = URLDecoder.decode(param, "utf-8");
			Objects.requireNonNull(id);
			
			String[] ids = id.split(",");
			int len = ids.length;
			idList = new Integer[len];
			for (int i = 0; i < len; i++) {
				idList[i] = Integer.parseInt(ids[i].trim());
			}
		} catch (NumberFormatException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return idList;
	}

	/**
	 * 非空转换
	 * 
	 * @param value
	 * @return
	 */
	public static String noEmpty(Object value) {
		return Optional.ofNullable(value)
				.map(v -> v.toString())
				.orElse("");
	}

	/**
	 * LocalDateTime 转换为 Date
	 * 
	 * @param localDateTime
	 * @return
	 */
	public static Date LocalDateTimeToDate(LocalDateTime localDateTime) {
		ZoneId zone = ZoneId.systemDefault();
		Instant instant = localDateTime.atZone(zone).toInstant();
		return Date.from(instant);
	}

	/**
	 * Date 转换为 LocalDateTime
	 * 
	 * @param date
	 * @return
	 */
	public static LocalDateTime DateToLocalDateTime(Date date) {
		Instant instant = date.toInstant();
		ZoneId zone = ZoneId.systemDefault();
		return LocalDateTime.ofInstant(instant, zone);
	}

	/**
	 * 
	 * @param dateTime
	 * @return
	 */
	public static String parseDateStr(LocalDateTime dateTime) {
		Objects.requireNonNull(dateTime);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = LocalDateTimeToDate(dateTime);
		return sdf.format(date);
	}

	/**
	 * 解析字符串格式的时间
	 * @param date
	 * @return
	 */
	public static Map<String, Object> parseDateStr(String date) {
		Objects.requireNonNull(date);
		
		Map<String, Object> dateMap = new HashMap<>();
		String[] values = date.split(",");
		if (values != null && values.length == 2) {
			String begin = values[0].trim();
			if (begin != null && begin.length() >= 10)
				begin = begin.substring(0, 10);
			dateMap.put("begin", begin);
			
			String end = values[1].trim();
			if (end != null && end.length() >= 10)
				end = end.substring(0, 10);
			dateMap.put("end", end);
		}
		return dateMap;
	}

	/**
	 * 解析逗号分隔的日期
	 * 
	 * @param date
	 * @param isDate
	 * @return
	 */
	public static Map<String, Object> parseDate(String date, boolean isDate) {
		Map<String, Object> dateMap = new HashMap<>();
		if(date != null) {
			String[] values = date.split(",");
			if (values != null && values.length == 2) {
				String begin = values[0].trim();
				String end = values[1].trim();
				if (isDate) {
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						Date beginDate = sdf.parse(values[0]);
						Date endDate = sdf.parse(values[1]);
						//结束日期加一天
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(endDate);
						calendar.add(Calendar.DATE, 1);
						endDate = calendar.getTime();
						dateMap.put("begin", DateToLocalDateTime(beginDate));
						dateMap.put("end", DateToLocalDateTime(endDate));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else {
					dateMap.put("begin", begin);
					dateMap.put("end", end);
				}
			}
		}
		return dateMap;
	}

	/**
	 * 获取对象的所有属性
	 * 
	 * @param obj
	 * @return
	 */
	public static Field[] getFields(Object obj) {
		return getFields(obj, false);
	}

	/**
	 * 获取对象的所有属性，包括从父类继承的属性
	 * 
	 * @param obj
	 * @param parent
	 * @return
	 */
	public static Field[] getFields(Object obj, boolean parent) {
		Objects.requireNonNull(obj);
		
		List<Field> fieldList = new ArrayList<>();
		Class<? extends Object> clazz = obj.getClass();

		while (clazz != null) {
			fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
			clazz = parent ? clazz.getSuperclass() : null;
		}

		Field[] fields = new Field[fieldList.size()];
		return fieldList.toArray(fields);
	}

	/**
	 * 获取通用对象字段的值
	 * 
	 * @param object
	 * @param fieldName
	 * @return
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object getFieldValue(Object object, String fieldName)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Objects.requireNonNull(object);
		
		Object value = null;
		String getName = capitalize(fieldName);
		if (notEmpty(getName)) {
			Method m = object.getClass().getMethod("get" + getName);
			value = m.invoke(object);
		}
		return value;
	}

	/**
	 * 验证字符串是否为空
	 * 
	 * @param value
	 * @return
	 */
	public static boolean notEmpty(String value) {
		return value != null && !"".equals(value);
	}

	/**
	 * 首字母转换为大写
	 * 
	 * @param fieldName
	 * @return
	 */
	public static String capitalize(String fieldName) {
		String getName = "";
		if (notEmpty(fieldName)) {
			getName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		}

		return getName;
	}

	/**
	 * 从字符串开始处抽取指定长度的字符
	 * 
	 * @param content
	 * @param len
	 * @return
	 */
	public static String extract(String content, int len) {
		return Optional.ofNullable(content)
				.filter(c -> c.length() > len)
				.map(c -> {
					StringBuffer buffer = new StringBuffer();
					buffer.append(c.substring(0, len)).append("...");
					return buffer.toString();
				}).orElse(content);
	}

	/**
	 * 字符串数据转为数字数组
	 * @param str
	 * @return
	 */
	public static Integer[] str2IntArray(String str) {
		String[] strs = str.split(",");
		Integer[] original = new Integer[strs.length];
		for (int i = 0, len = strs.length; i < len; i++) {
			original[i] = parseInt(strs[i]); 
		}
		return original;
	}
	
	/**
	 * 计算占比
	 * @param num
	 * @param total
	 * @return
	 */
	public static Float calcPercent(int num, int total) {
		Float rate = (num * 10000.0f) / total;
		return ((new BigDecimal(rate).setScale(0, BigDecimal.ROUND_HALF_UP)).floatValue()) / 10000;
	}

	/**
	 * Check list not null
	 * @param list
	 * @return
	 */
	public static boolean validList(List<?> list) {
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

}
