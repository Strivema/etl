package com.yinker.etl.util;

import ognl.Ognl;
import ognl.OgnlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.common.utils.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * <b>类描述:</b>JavaBean常用转换操作工具类
 * <b>作者:</b>李得亮
 * <b>创建日期:</b>2016年12月09日 下午4:57:04
 * </pre>
 */
public class BeanUtil {

	private static final Logger LOG = LoggerFactory.getLogger(BeanUtil.class);

	/**
	 * 得到对象属性值
	 * 
	 * @param bean
	 *            对象
	 * @param fieldName
	 *            属性名称
	 * @return 属性值
	 */
	public static Object getFieldValue(Object bean, String fieldName) {
		try {
			return Ognl.getValue(fieldName, bean);
		} catch (OgnlException e) {
			LOG.error("不能获取对象[" + bean + "]的属性[" + fieldName + "]值:", e);
			throw new RuntimeException("不能获取对象[" + bean + "]的属性[" + fieldName + "]值:", e);
		}
	}

	/**
	 * 设置对象属性值
	 * 
	 * @param bean
	 *            对象
	 * @param fieldName
	 *            属性名称
	 * @param value
	 *            属性值
	 */
	public static void setFieldValue(Object bean, String fieldName, Object value) {
		try {
			Ognl.setValue(fieldName, bean, value);
		} catch (OgnlException e) {
			LOG.error("不能设置对象[" + bean + "]的属性[" + fieldName + "]值[" + value + "]:", e);
			throw new RuntimeException("不能设置对象[" + bean + "]的属性[" + fieldName + "]值[" + value + "]:", e);
		}
	}

	/**
	 * Map转换为对象
	 * 
	 * @param map
	 *            属性Map
	 * @param className
	 *            转化为的目标对象类名
	 * @return 目标对象
	 */
	public static Object convertToBean(Map<String, Object> map, String className) {
		try {
			if (map == null || map.isEmpty()) {
				return null;
			}

			// 结果集为HashMap
			if ("java.util.hashmap".equals(className.toLowerCase()) || "hashmap".equals(className.toLowerCase())) {
				return map;
			}

			Object bean = newInstance(className);
			BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String fieldName = property.getName();
				if (map.containsKey(fieldName)) {
					Object value = map.get(fieldName);
					if (value != null) {
						try {
							Method setter = property.getWriteMethod();
							if (setter == null) {
								continue;
							}
							setter.invoke(bean, value);
						} catch (Exception e) {
							LOG.error("设置属性异常:", e);
						}
					}
				}
			}
			return bean;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 对象转换为MAP,支持类的继承关系
	 * 
	 * @param bean
	 *            对象
	 * @return 属性Map
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> convertToMap(Object bean) {
		try {
			if (bean == null) {
				return null;
			}

			if (bean instanceof Map) {
				return (Map<String, Object>) bean;
			}

			Map<String, Object> map = new HashMap<String, Object>();
			BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String fieldName = property.getName();
				try {
					Method getter = property.getReadMethod();
					if (getter == null) {
						continue;
					}
					Object value = getter.invoke(bean);
					if (value != null) {
						map.put(fieldName, value);
					}
				} catch (Exception e) {
					LOG.error("获取属性异常:", e);
				}
			}

			return map;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 新建类实例
	 * 
	 * @param className
	 *            类名称
	 * @return 类实例对象
	 */
	public static Object newInstance(String className) {
		if (StringUtils.isEmpty(className)) {
			throw new RuntimeException("ClassName为空，无法实例化!");
		}
		try {
			Class<?> clazz = Class.forName(className);
			return clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 新建类实例
	 * 
	 * @param className
	 *            类名称
	 * @return 类实例对象
	 */
	public static Class<?> newClass(String className) {
		if (StringUtils.isEmpty(className)) {
			throw new RuntimeException("ClassName为空，无法实例化!");
		}
		try {
			Class<?> clazz = Class.forName(className);
			return clazz;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
