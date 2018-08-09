package com.anmf.daocommon;
/**
 * 提供用于 系统数据分页
 * 
 * @author Administrator
 * 
 */
public class Pagination {
	/**
	 * 当前第几页
	 */
	private int curPage;

	/**
	 * 每一页显示多少条记录
	 */
	private int pageRows;

	/**
	 * 总页数
	 */
	private int pages;

	/**
	 * 总记录条数
	 */
	private int rowes;

	public Pagination() {
		super();
	}

	public Pagination(int curPage, int pageRows) {
		super();
		this.curPage = curPage;
		this.pageRows = pageRows;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getPageRows() {
		return pageRows;
	}

	public void setPageRows(int pageRows) {
		this.pageRows = pageRows;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public int getRowes() {
		return rowes;
	}

	public void setRowes(int rowes) {
		this.rowes = rowes;
	}

}
