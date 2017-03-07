package com.github.hualuomoli.validator;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import com.github.hualuomoli.validator.lang.InvalidParameterException;
import com.google.common.collect.Sets;

/**
 * 验证器工具
 * @author lbq
 *
 */
public class Validate {

	private static Validator validator;

	/**
	 * 验证实体类是否有效
	 * @param object 实体类
	 * @throws InvalidParameterException 不合法的数据
	 */
	public static void valid(Object object) throws InvalidParameterException {
		valid(object, Default.class);
	}

	/**
	 * 验证实体类是否有效
	 * @param object 实体类
	 * @param groups 验证规则
	 * @throws InvalidParameterException 不合法的数据
	 */
	public static void valid(Object object, Class<?>... groups) throws InvalidParameterException {

		if (object == null) {
			return;
		}

		if (groups == null || groups.length == 0) {
			return;
		}

		// 获取验证器
		Validator validator = getValidator();

		// 验证
		Set<ConstraintViolation<Object>> set = validator.validate(object, groups);
		if (set == null || set.size() == 0) {
			return;
		}

		// 设置错误信息
		Set<String> errors = Sets.newHashSet();
		for (ConstraintViolation<Object> constraintViolation : set) {
			errors.add(constraintViolation.getMessage());
		}
		throw new InvalidParameterException(errors);
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
