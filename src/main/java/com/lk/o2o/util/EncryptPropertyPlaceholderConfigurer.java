package com.lk.o2o.util;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	//需要加密的字段数组
	private String[] encryptPropNames = { "jdbc.username", "jdbc.password" };

	/**
	 * 对关键的属性进行转换
	 * @param propertyName 字段名
	 * @param propertyValue 字段值
	 * @return 解密后的字段值
	 */
	@Override
	protected String convertProperty(String propertyName, String propertyValue) {
		if (isEncryptProp(propertyName)) {
			//对以加密的字段进行解密操作
			String decryptValue = DESUtils.getDecryptString(propertyValue);
			return decryptValue;
		} else {
			return propertyValue;
		}
	}

	//判断该字段是否加密
	private boolean isEncryptProp(String propertyName) {
		for (String encryptpropertyName : encryptPropNames) {
			if (encryptpropertyName.equals(propertyName))
				return true;
		}
		return false;
	}
}
