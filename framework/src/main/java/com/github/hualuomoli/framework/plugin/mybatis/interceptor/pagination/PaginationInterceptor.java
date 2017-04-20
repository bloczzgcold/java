package com.github.hualuomoli.framework.plugin.mybatis.interceptor.pagination;

import java.sql.Connection;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.github.hualuomoli.framework.plugin.mybatis.interceptor.BaseInterceptor;
import com.github.hualuomoli.tool.util.ReflectionUtils;

/**
 * 数据库分页插件，只拦截查询语句.
 */
@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }) })
public class PaginationInterceptor extends BaseInterceptor {

	private static final long serialVersionUID = 2276302599945811333L;

	// 分页查询条件
	private static final ThreadLocal<Integer> LOCAL_PAGE_NUMBER = new ThreadLocal<Integer>();
	private static final ThreadLocal<Integer> LOCAL_PAGE_SIZE = new ThreadLocal<Integer>();
	// 排序条件
	private static final ThreadLocal<String> LOCAL_ORDER_BY = new ThreadLocal<String>();
	// 总数据量
	private static final ThreadLocal<Integer> LOCAL_COUNT = new ThreadLocal<Integer>();

	@Override
	public Object intercept(Invocation invocation) throws Throwable {

		final MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];

		Object parameter = invocation.getArgs()[1];
		BoundSql boundSql = mappedStatement.getBoundSql(parameter);

		// 获取分页参数对象
		Integer pageNumber = getPageNumber();
		Integer pageSize = getPageSize();
		// 排序
		String orderBy = getOrderBy();
		// 执行查询前清除数量
		clearCount();

		if (pageNumber != null) {
			// 分页查询

			String originalSql = boundSql.getSql().trim();

			// 得到总记录数
			Executor exec = (Executor) invocation.getTarget();
			Connection conn = exec.getTransaction().getConnection();
			int count = PaginationSQLHelper.getCount(originalSql, conn, mappedStatement, parameter, boundSql, logger);

			// 设置查询的数量
			setCount(count);

			if (logger.isDebugEnabled()) {
				logger.debug("count={}", count);
			}

			// 数量为零
			if (count <= 0) {
				return new ArrayList<Object>();
			}

			// 查询数据
			// 分页查询 本地化对象 修改数据库注意修改实现
			String pageSql = PaginationSQLHelper.generatePageSql(originalSql, pageNumber, pageSize, orderBy, dialect);
			invocation.getArgs()[2] = new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
			BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), pageSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
			// 解决MyBatis 分页foreach 参数失效 start
			if (ReflectionUtils.getFieldValue(boundSql, "metaParameters") != null) {
				MetaObject mo = (MetaObject) ReflectionUtils.getFieldValue(boundSql, "metaParameters");
				ReflectionUtils.setFieldValue(newBoundSql, "metaParameters", mo);
			}
			// 解决MyBatis 分页foreach 参数失效 end
			MappedStatement newMs = PaginationSQLHelper.createStatement(mappedStatement, new BoundSqlSqlSource(newBoundSql));

			invocation.getArgs()[0] = newMs;

		} else if (StringUtils.isNotBlank(orderBy)) {
			// 查询列表排序

			String originalSql = boundSql.getSql().trim();

			// 查询数据
			String orderSql = PaginationSQLHelper.generateOrderSql(originalSql, orderBy, dialect);
			invocation.getArgs()[2] = new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
			BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), orderSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
			// 解决MyBatis 分页foreach 参数失效 start
			if (ReflectionUtils.getFieldValue(boundSql, "metaParameters") != null) {
				MetaObject mo = (MetaObject) ReflectionUtils.getFieldValue(boundSql, "metaParameters");
				ReflectionUtils.setFieldValue(newBoundSql, "metaParameters", mo);
			}
			// 解决MyBatis 分页foreach 参数失效 end
			MappedStatement newMs = PaginationSQLHelper.createStatement(mappedStatement, new BoundSqlSqlSource(newBoundSql));

			invocation.getArgs()[0] = newMs;

		}

		// 清除本地排序
		clearOrderBy();
		// 清除分页信息
		clearPagination();

		// end
		return invocation.proceed();
	}

	// 获取排序
	private static String getOrderBy() {
		return LOCAL_ORDER_BY.get();
	}

	// 设置排序
	public static void setOrderBy(String orderBy) {
		LOCAL_ORDER_BY.set(orderBy);
	}

	// 清除排序
	private static void clearOrderBy() {
		LOCAL_ORDER_BY.remove();
	}

	// 获取分页
	private static Integer getPageNumber() {
		return LOCAL_PAGE_NUMBER.get();
	}

	private static Integer getPageSize() {
		return LOCAL_PAGE_SIZE.get();
	}

	// 设置分页
	public static void setPagination(Integer pageNumber, Integer pageSize) {
		LOCAL_PAGE_NUMBER.set(pageNumber);
		LOCAL_PAGE_SIZE.set(pageSize);
	}

	// 设置分页
	public static void setPagination(Integer pageNumber, Integer pageSize, String orderBy) {
		LOCAL_PAGE_NUMBER.set(pageNumber);
		LOCAL_PAGE_SIZE.set(pageSize);
		LOCAL_ORDER_BY.set(orderBy);
	}

	// 清除分页
	private static void clearPagination() {
		LOCAL_PAGE_NUMBER.remove();
		LOCAL_PAGE_SIZE.remove();
	}

	// 获取数量
	public static Integer getCount() {
		return LOCAL_COUNT.get();
	}

	// 设置数量
	private static void setCount(Integer count) {
		LOCAL_COUNT.set(count);
	}

	// 清除数量
	private static void clearCount() {
		LOCAL_COUNT.remove();
	}

}
