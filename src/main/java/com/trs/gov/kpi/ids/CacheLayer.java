package com.trs.gov.kpi.ids;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户对远程Cache进行本地缓存，避免每次都从远程缓存中获取； <br>
 * 主要用在一次连续的操作中，如：一个请求的全过程，对相同的key获取，将仅获取一次远程缓存，而后都从本地缓存中获取。
 * 
 * @author huxiejin
 *
 */
public class CacheLayer {

	private CacheLayer(){}

	private static final Logger logger = Logger.getLogger(CacheLayer.class);

	/**
	 * 远程cache的本地缓存,Map的结构为：<br>
	 * <key, Map>,其中的key为redis的set，get，lset，lrange等需要传入的key信息<br>
	 * Map的结构为： <CacheKey,
	 * CacheValue>，其中的CacheKey可参考类结构的定义{@link CacheKey}，CacheValue为Redis的返回结果<br>
	 * 之所以不直接以CacheKey作为一级Map的key，是因为在缓存失效时，避免在十几万key信息中进行遍历，影响性能。
	 */
	private static ThreadLocal<Map> cacheThreadLocal = new ThreadLocal<Map>() {
		@Override
		public Map initialValue() {
			return new HashMap();
		}
	};

	public static void set(CacheKey cacheKey, Object cacheValue) {
		Map cacheMap = (Map) cacheThreadLocal.get().get(cacheKey.getKey());

		if (cacheMap == null) {
			cacheMap = new HashMap();
			cacheThreadLocal.get().put(cacheKey.getKey(), cacheMap);
		}

		cacheMap.put(cacheKey, cacheValue);

		if (logger.isDebugEnabled()) {
			logger.debug("set cacheKey:" + cacheKey + ";cacheValue:" + cacheValue);
		}
	}

	public static Object get(CacheKey cacheKey) {
		Map cacheMap = (Map) cacheThreadLocal.get().get(cacheKey.getKey());

		if (cacheMap == null) {
			return null;
		}

		Object cacheValue = cacheMap.get(cacheKey);

		if (logger.isDebugEnabled()) {
			logger.debug("get cacheKey:" + cacheKey + ";cacheValue:" + cacheValue);
		}
		return cacheValue;
	}

	public static void delete(CacheKey cacheKey) {
		Map cacheMap = (Map) cacheThreadLocal.get().get(cacheKey.getKey());

		if (cacheMap == null) {
			return;
		}

		cacheMap.remove(cacheKey);

		if (logger.isDebugEnabled()) {
			logger.debug("delete cacheKey:" + cacheKey);
		}
	}

	/**
	 * 将属于key类型的所有key都失效，如：lset将导致lindex，lexists,lrange等key的cache失效
	 * 
	 * @param key
	 */
	public static void disable(Object key) {
		cacheThreadLocal.get().remove(key);
	}

	public static void clear() {
		cacheThreadLocal.remove();

		if (logger.isDebugEnabled()) {
			logger.debug("clear cache Layer");
		}
	}

	/**
	 * 用于作为CacheLay的key信息
	 *
	 * @author huxiejin
	 *
	 */
	class CacheKey {
		// 方法的名称，如：get，lindex，lrange
		private String methodName;

		// 方法的参数信息，第一个参数一般都是key
		private Object[] arguments;

		public CacheKey(String methodName, Object... arguments) {
			this.methodName = methodName;
			this.arguments = arguments;
		}

		/**
		 * 返还真是的key信息
		 *
		 */
		public Object getKey() {
			return arguments[0];
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof CacheKey)) {
				return false;
			}

			CacheKey another = (CacheKey) obj;
			if (!methodName.equals(another.methodName)) {
				return false;
			}

			for (int i = 0; i < arguments.length; i++) {
				if (!arguments[i].equals(another.arguments[i])) {
					return false;
				}
			}
			return true;
		}

		@Override
		public int hashCode() {
			int hashCode = methodName.hashCode();

			for (int i = 0; i < arguments.length; i++) {
				hashCode = hashCode | arguments[i].hashCode();
			}

			return hashCode;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder(methodName);
			sb.append("(");
			for (int i = 0; i < arguments.length; i++) {
				sb.append(arguments[i]);
				if (i < (arguments.length - 1)) {
					sb.append(",");
				}
			}
			sb.append(")");

			return sb.toString();
		}
	}
}

