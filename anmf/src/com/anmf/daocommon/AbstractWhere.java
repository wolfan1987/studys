package com.anmf.daocommon;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * 
 * @author Administrator
 * 
 */
public class AbstractWhere {
	/**
	 * 
	 * @param listOrder
	 *            排序条件
	 * @param listWhere
	 *            where 条件
	 * @param req
	 *            请求参数
	 * @param id
	 *            主键字段
	 */
	private List<OrderTerm> listOrder = new ArrayList<OrderTerm>();

	private List<Condition> listWhere = new ArrayList<Condition>();

	public List<OrderTerm> getListOrder() {
		return listOrder;
	}

	public void setListOrder(List<OrderTerm> listOrder) {
		this.listOrder = listOrder;
	}

	public List<Condition> getListWhere() {
		return listWhere;
	}

	public void setListWhere(List<Condition> listWhere) {
		this.listWhere = listWhere;
	}

	public void getWhereandOrder(HttpServletRequest req, String id) {
		String page1 = req.getParameter("page");
		// 条数
		int pages = 10;
		if (page1 != null) {
			pages = Integer.parseInt(page1);
		} else {
			page1 = req.getParameter("page");
		}
		String strCurrpage = req.getParameter("currpage");
		int currpages = 1;
		if (strCurrpage != null) {
			currpages = Integer.parseInt(strCurrpage);
		}

		Pagination page = new Pagination();
		page.setCurPage(currpages);
		page.setPageRows(pages);
		req.setAttribute("page", page);
		Enumeration<String> st = req.getParameterNames();
		Condition con = null;
		boolean booOrder = true;
		OrderTerm order = new OrderTerm();
		String paixu = req.getParameter("paixu");
		if (st != null) {
			while (st.hasMoreElements()) {
				String name1 = st.nextElement();
				if (!name1.equals("actionname") && !name1.equals("currpage")
						&& !name1.equals("page")) {
					String value1 = req.getParameter(name1);

					con = new Condition();
					con.setOp(Operater.模糊);
					con.setValue(value1);
					con.setProperty(name1);

					if (booOrder == true) {
						order.setName(name1);
						if (paixu == null || paixu.equals("1")) {
							order.setWise(OrderWise.ASC);
						} else {
							order.setWise(OrderWise.DESC);
						}
					}
					booOrder = false;
					listWhere.add(con);
				}
			}
			if (booOrder == true) {
				order.setName(id);
				order.setWise(OrderWise.ASC);

			}
			if (listWhere != null && listWhere.size() > 0) {
				req.setAttribute("condition", listWhere.get(0));
			} else {
				req.setAttribute("condition", null);
			}
		}
		listOrder.add(order);
	}

	/**
	 * 用来封装Condition条件
	 * 
	 * @param op
	 * @param para
	 * @param value
	 * @return
	 */
	public static Condition getWhere(Operater op, String para, Object value) {
		Condition con = new Condition();
		con.setOp(op);
		con.setProperty(para);
		con.setValue(value);
		return con;
	}

}
