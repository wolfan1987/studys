package com.anmf.daocommon;
/**
 * �ṩ���� ϵͳ���ݷ�ҳ
 * 
 * @author Administrator
 * 
 */
public class Pagination {
	/**
	 * ��ǰ�ڼ�ҳ
	 */
	private int curPage;

	/**
	 * ÿһҳ��ʾ��������¼
	 */
	private int pageRows;

	/**
	 * ��ҳ��
	 */
	private int pages;

	/**
	 * �ܼ�¼����
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
