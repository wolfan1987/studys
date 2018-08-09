package com.anmf.daocommon;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;

/**
 * 条件查询
 * 
 * @author Administrator
 * 
 */
public class QuerySelf<ANMF> {
	public static final String AND = " and ";

	private String getValueAsString(Operater op) {
		if (op == Operater.不等于)
			return " != ";
		else if (op == Operater.大于)
			return " > ";
		else if (op == Operater.大于等于)
			return " >= ";
		else if (op == Operater.小于)
			return " < ";
		else if (op == Operater.小于等于)
			return " <= ";
		else if (op == Operater.等于)
			return " = ";
		else if (op == Operater.模糊)
			return " like ";
		else if (op == Operater.IS)
			return " is ";
		return null;
	}

	/**
	 * 拼接where子句
	 * 
	 * @param list
	 *            条件集合
	 * @return
	 */
	public String getWhere(List<Condition> list) {
		if (list == null || (list != null && list.size() == 0))
			return "";
		StringBuffer where = new StringBuffer();

		Set<String> set = new HashSet<String>();
		int id = 0;

		where.append(" where ");
		for (Condition c : list) {
			where.append(" a.").append(c.getProperty());
			if (c.getOp() == Operater.IS) {
				where.append(" is null ");
			} else {

				// 是否有重复的条件
				if (!set.contains(c.getProperty())) {
					set.add(c.getProperty());
					where.append(this.getValueAsString(c.getOp())).append(":")
							.append(
									c.getProperty().indexOf('.') == -1 ? c
											.getProperty() : c.getProperty()
											.replaceAll("\\.", "dot"));
				} else {
					where.append(this.getValueAsString(c.getOp())).append(":")
							.append(
									c.getProperty().indexOf('.') == -1 ? c
											.getProperty()
											+ id : c.getProperty().replaceAll(
											"\\.", "dot")
											+ id);
					id++;
				}
			}
			where.append(AND);
		}

		String w = where.toString();
		if (w.endsWith(AND)) {
			w = w.substring(0, w.length() - AND.length());
		}
		return w;
	}

	/**
	 * 拼接 排序语句
	 * 
	 * @param orderTerm
	 *            排序条件集
	 * @return
	 */
	private String getOrder(List<OrderTerm> orderTerm) {
		if (orderTerm == null || (orderTerm != null && orderTerm.size() == 0))
			return "";
		StringBuffer order = new StringBuffer();
		order.append(" order by ");
		for (OrderTerm o : orderTerm) {
			order.append(" a.").append(o.getName()).append(
					getOrderWord(o.getWise())).append(",");
		}

		String w = order.toString();
		if (w.endsWith(",")) {
			w = w.substring(0, w.length() - 1);
		}
		return w;
	}

	@SuppressWarnings("static-access")
	private String getOrderWord(OrderWise ow) {
		if (ow == ow.DESC) {
			return " desc ";
		} else
			return " asc ";
	}

	/**
	 * 得到完整的HQL语句
	 * 
	 * @param clazz
	 * @param where
	 * @return
	 */
	public String getFullHql(Class<ANMF> clazz, List<Condition> where,
			List<OrderTerm> orderTerm) {
		String w = this.getWhere(where);
		String order = this.getOrder(orderTerm);
		StringBuilder sb = new StringBuilder();
		sb.append("from ").append(clazz.getSimpleName()).append(" as a ")
				.append(w).append(order);

		return sb.toString();
	}

	/**
	 * 动态绑定参数
	 * 
	 * @param query
	 * @param where
	 * @return
	 */
	@SuppressWarnings("unused")
	public Query dynaBindParameter(Query query, List<Condition> where) {
		String key = null;

		Set<String> set = new HashSet<String>();
		int id = 0;
		for (Condition c : where) {
			key = c.getProperty();
			if (c.getValue() != null) {
				if (key.indexOf('.') != -1) {
					key = key.replaceAll("\\.", "dot");
				}

				// 是否有重复的条件
				if (set.contains(key)) {
					key += id;
					id++;
				} else {
					set.add(key);
				}

				if (c.getValue().getClass() == java.sql.Timestamp.class) {
					query.setTimestamp(key, (Timestamp) c.getValue());
				} else if (c.getValue().getClass() == java.sql.Date.class) {
					query.setDate(key, (Date) c.getValue());
				} else if (c.getValue().getClass() == java.sql.Time.class) {
					query.setTime(key, (Time) c.getValue());
				} else {
					if (c.getOp() == Operater.模糊) {
						query.setParameter(key, "%" + c.getValue() + "%");
					} else {
						query.setParameter(key, c.getValue());
					}
				}
			}
		}
		return query;
	}
}
