/*
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.tang.core.tools;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * Business modules' record query class use defined criteria. The query base on
 * criteria divide three parts, include blur query, section query and exact
 * match query, it dependent on mybatis plus framework and make it more
 * convenient. The query condition is dynamic defined in
 * {@link CriteriaResource}
 * 
 * <p>
 * Blur query use SQL's fuzzy query to achieve data's blur search.
 * 
 * <pre>
 * select * from test t where t.name like '%test%';
 * </pre>
 * 
 * <p>
 * Section query means query datas in specific condition range,for example the
 * date's begin and end.
 * 
 * <pre>
 * select * from test t where t.date>='2018-10-15 08:16:30' and t.date<='2018-10-15 20:16:30';
 * </pre>
 * 
 * <p>
 * Exact match query is query accurate what you input content from database.
 * 
 * <pre>
 * select * from test t where t.code='BH1234';
 * </pre>
 * 
 * @author tang
 * @since 2018
 * @see CriteriaResource
 */
public class CriteriaBuilder implements CriteriaResource {

	/**
	 * 查询字段转换映射
	 */
	public static final Map<String, String> fieldMap = new HashMap<>();
	static {
		fieldMap.put("apply", "apply_time");
		fieldMap.put("audit", "audit_time");
		fieldMap.put("numRange", "num");
	}

	/**
	 * Field type defined map.
	 */
	public static final Map<String, String> typeMap = new HashMap<>();
	static {
		typeMap.put("num", "int");
		typeMap.put("apply_time", "datetime");
		typeMap.put("buy_date", "date");
	}

	/**
	 * Query records by specific criteria which have three situation. This function
	 * needs the criteria must not null, if one or more condition have no value,
	 * then should use empty array instead of null value.
	 * 
	 * @param ew
	 *            entity wrapper object
	 * @param base
	 *            entity object
	 * @param blurFields
	 *            blur criteria fields
	 * @param sectionFields
	 *            section criteria fields
	 * @param equalFields
	 *            accurate criteria fields
	 */
	public static <T> void queryCriteriaNotNull(QueryWrapper<T> ew, Object base, String[] blurFields,
			String[] sectionFields, String[] equalFields) {
		if (ew != null && base != null && blurFields != null && sectionFields != null && equalFields != null) {
			Field[] fields = CommonUtils.getFields(base, true);
			List<String> blurList = Arrays.asList(blurFields);
			List<String> sectionList = Arrays.asList(sectionFields);
			List<String> equalList = Arrays.asList(equalFields);

			try {
				if (fields != null) {
					for (Field field : fields) {
						String fieldName = field.getName();
						if (guarantee(fieldName)) {
							Object value = CommonUtils.getFieldValue(base, fieldName);
							// 根据不同查询类型进行不同包装
							if (value != null && !"".equals(value)) {
								if (blurList.contains(fieldName)) {
									dealBlurSearch(ew, fieldName, value.toString());
								} else if (sectionList.contains(fieldName)) {
									dealSectionSearch(ew, fieldName, value.toString());
								} else if (equalList.contains(fieldName)) {
									dealEqualSearch(ew, fieldName, value);
								}
							}
						}
					}
				}
			} catch (SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Query records by specific criteria which have three situation. This function
	 * allow use null value instead if, have not value with one or more condition
	 * value.
	 * 
	 * @param ew
	 *            entity wrapper object
	 * @param base
	 *            entity object
	 * @param blurFields
	 *            blur criteria fields
	 * @param sectionFields
	 *            section criteria fields
	 * @param equalFields
	 *            accurate criteria fields
	 */
	public static <T> void queryCriteria(QueryWrapper<T> ew, Object base, String[] blurFields, String[] sectionFields,
			String[] equalFields) {
		if (ew != null && base != null) {
			Field[] fields = CommonUtils.getFields(base, true);
			List<String> blurList = null;
			List<String> sectionList = null;
			List<String> equalList = null;

			if (blurFields != null)
				blurList = Arrays.asList(blurFields);
			if (sectionFields != null)
				sectionList = Arrays.asList(sectionFields);
			if (equalFields != null)
				equalList = Arrays.asList(equalFields);

			try {
				if (fields != null) {
					for (Field field : fields) {
						String fieldName = field.getName();
						if (guarantee(fieldName)) {
							Object value = CommonUtils.getFieldValue(base, fieldName);
							// 根据不同查询类型进行不同包装
							if (value != null && !"".equals(value)) {
								if (blurList != null && blurList.contains(fieldName)) {
									dealBlurSearch(ew, fieldName, value.toString());
								} else if (sectionList != null && sectionList.contains(fieldName)) {
									dealSectionSearch(ew, fieldName, value.toString());
								} else if (equalList != null && equalList.contains(fieldName)) {
									dealEqualSearch(ew, fieldName, value);
								}
							}
						}
					}
				}
			} catch (SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 区间值查询
	 * 
	 * @param ew
	 * @param key
	 * @param value
	 */
	public static <T> void dealSectionSearch(QueryWrapper<T> ew, String key, String value) {
		if (ew != null && key != null && value != null) {
			String begin = null;
			String end = null;
			String k = null;
			String[] values = value.split(",");
			if (values != null && values.length == 2) {

				begin = values[0];
				end = values[1];
				k = fieldMap.get(key);
				if (k == null)
					k = camel2Underscore(key);
				String type = typeMap.get(k);

				// 指明数据类型的字段查询
				if (type != null) {
					switch (type) {
					case "float":
						ew.ge(k, Float.parseFloat(begin));
						ew.le(k, Float.parseFloat(end));
						break;

					case "int":
						ew.ge(k, Integer.parseInt(begin));
						ew.le(k, Integer.parseInt(end));
						break;

					case "datetime":
						// 如果是时间比较，需要转换类型
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						try {
							ew.ge(k, sdf.parse(begin + " 00:00:00"));
							ew.le(k, sdf.parse(end + " 23:59:59"));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						break;

					case "date":
						// 如果是日期比较，需要转换类型
						SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
						try {
							ew.ge(k, sdf2.parse(begin));
							ew.le(k, sdf2.parse(end));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						break;

					default:
						break;
					}
				} else {
					// 默认情况下查询的是日期字符串格式，长度为10位，如：2018-01-01
					if (begin != null && begin.length() > 10)
						begin = begin.substring(0, 10);
					if (end != null && end.length() > 10)
						end = end.substring(0, 10);
					ew.ge(k, begin);
					ew.le(k, end);
				}
			}
		}
	}

	/**
	 * 模糊查询
	 * 
	 * @param ew
	 * @param key
	 * @param value
	 */
	public static <T> void dealBlurSearch(QueryWrapper<T> ew, String key, String value) {
		if (ew != null && key != null && value != null) {
			key = camel2Underscore(key);
			ew.like(key, value.trim());
		}
	}

	/**
	 * 相等查询条件
	 * 
	 * @param ew
	 *            实体包装类
	 * @param key
	 *            键
	 * @param value
	 *            值
	 */
	public static <T> void dealEqualSearch(QueryWrapper<T> ew, String key, Object value) {
		if (ew != null && key != null && value != null) {
			key = camel2Underscore(key);
			String v = fieldMap.get(key);

			if (!minusOne(value)/* value.equals("-1") */) {
				// 匹配多个字段
				if (v != null && v.indexOf(",") != -1) {
					// ew.andNew();
					// String[] ors = v.split(",");
					// for (int i = 0, len = ors.length; i < len; i++) {
					// ew.or(ors[i].trim(), value);
					// }
				} else
					ew.eq(key, value);
			}
			// 通常情况下，-1是下拉框的“全部”选项的值，所以此时是不做过滤的。
			// 但也存在搜索的值确认是-1的情况，这种情况在搜索的时候需要说明一下就可以，
			// 说明的方法就是配置一下标记值
			else {
				if (v != null && v.equals("true"))
					ew.eq(key, value);
			}
		}
	}

	/**
	 * 判断是否是-1
	 * 
	 * @param value
	 * @return
	 */
	private static boolean minusOne(Object value) {
		boolean flag = false;
		if (value instanceof String) {
			if (((String) value).trim().equals("-1"))
				flag = true;
		} else if (value instanceof Integer) {
			if ((Integer) value == -1)
				flag = true;
		}
		return flag;
	}

	/**
	 * 验证字段
	 * 
	 * @param fieldName
	 * @return
	 */
	private static boolean guarantee(String fieldName) {
		List<String> notIncludeList = Arrays.asList(NOT_INCLUDE_FIELDS);
		return CommonUtils.notEmpty(fieldName) && !notIncludeList.contains(fieldName);
	}

	/**
	 * 驼峰转下划线
	 * 
	 * @param key
	 * @return
	 */
	public static String camel2Underscore(String key) {
		return key.replaceAll("[A-Z]", "_$0").toLowerCase();
	}

	/**
	 * 驼峰转下划线(效率高)
	 * 
	 * @param str
	 * @return
	 */
	public static String humpToLine(String str) {
		Pattern humpPattern = Pattern.compile("[A-Z]");
		Matcher matcher = humpPattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 下划线转驼峰
	 * 
	 * @param str
	 * @return
	 */
	public static String lineToHump(String str) {
		Pattern linePattern = Pattern.compile("_(\\w)");
		str = str.toLowerCase();
		Matcher matcher = linePattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

}
