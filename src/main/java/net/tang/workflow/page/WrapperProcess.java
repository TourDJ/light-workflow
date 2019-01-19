package net.tang.workflow.page;

import java.util.ArrayList;
import java.util.Collection;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * mybatis plus 包装实体操作类
 * @author service1
 *
 */
public class WrapperProcess {

	private WrapperProcess() {
		
	}
	
	/**
	 * 设置有效状态
	 * @param qw 实体包装对象
	 * @param userId 用户id
	 */
	public static <T> void ensureStatus(QueryWrapper<T> qw) {
		if(qw != null) {
			qw.eq("status", 1);
		}
	}
	
	/**
	 * 设置有效状态
	 * @param qw 实体包装对象
	 * @param userId 用户id
	 */
	public static <T> void ensureStatus(QueryWrapper<T> qw, Integer userId) {
		if(qw != null && userId != null) {
			qw.eq("create_user", userId);
		}
	}
	
	/**
	 * 设置有效状态
	 * @param qw 实体包装对象
	 * @param userId 用户id
	 * @param flow 是否为流程对象。true：是，false：不是。
	 */
	public static <T> void ensureStatus(QueryWrapper<T> qw, Integer userId, boolean flow) {
		ensureStatus(qw, userId);
		if(!flow)
			ensureStatus(qw);
	}

	/**
	 * 设置排序属性
	 * @param qw 实体对象
	 * @param desc 是否是倒序
	 * @param args 参数
	 */
	public static <T> void sortRecords(QueryWrapper<T> qw, boolean desc, String... args) {
		Collection<String> collection = new ArrayList<>();
		if(args != null) {
			for (String string : args) {
				if(string != null)
					collection.add(string);
			}
		}
		
		if(collection.size() == 0)
			collection.add("update_time");
		
		String[] orders = new String[collection.size()];
		orders = collection.toArray(orders);
		if(qw != null) {
			if(desc)
				qw.orderByDesc(orders);
			else
				qw.orderByAsc(orders);
		}
	}
	
	/**
	 * 增加查询条件
	 * @param qw
	 * @param field
	 * @param value
	 * @param equal
	 */
	public static <T> void addQueryArgs(QueryWrapper<T> qw, String field, Object value, boolean equal) {
		if(value != null && !"".equals(value)) {
			if(equal) {
				qw.eq(field, value);
			} else
				qw.like(field, String.valueOf(value));
		}
	}

}
