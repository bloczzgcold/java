package com.github.hualuomoli.validate.util;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import com.google.common.collect.Sets;

/**
 * 验证器工具
 * @author lbq
 *
 */
public class ValidatorUtils {

	private static Validator validator;

	/**
	 * 验证实体类是否有效
	 * @param object 实体类
	 * @return 如果有效返回null,否则返回错误信息
	 */
	public static Set<String> valid(Object object) {
		return valid(object, Default.class);
	}

	/**
	 * 验证实体类是否有效
	 * @param object 实体类
	 * @param groups 验证规则
	 * @return 如果有效返回null,否则返回错误信息
	 */
	public static Set<String> valid(Object object, Class<?>... groups) {

		if (object == null) {
			return null;
		}

		if (groups == null || groups.length == 0) {
			return null;
		}

		// 获取验证器
		Validator validator = getValidator();

		// 验证
		Set<ConstraintViolation<Object>> set = validator.validate(object, groups);
		if (set == null || set.size() == 0) {
			return null;
		}

		// 设置错误信息
		Set<String> errors = Sets.newHashSet();
		for (ConstraintViolation<Object> constraintViolation : set) {
			errors.add(constraintViolation.getMessage());
		}
		return errors;
	}

	/**
	 * 获取验证器
	 * @return 验证器
	 */
	private static Validator getValidator() {
		if (validator == null) {
			validator = Validation.buildDefaultValidatorFactory().getValidator();
		}
		return validator;
	}

}
