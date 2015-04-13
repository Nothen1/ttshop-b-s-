package pojo;

public class Page {
	
	private int allRows; //allRows
	private int totalPage; //allRows/maxResult+1
	private int currentPage;  //(firstResult/maxResult) +1 
	private int maxResult; //ÿҳ��ʾ������
	private int firstResult; //�ӵڼ��п�ʼ��ʾ = (currentpage-1)/maxresult
	
	public int getAllRows() {
		return allRows;
	}
	public void setAllRows(int allRows) {
		this.allRows = allRows;
	}
	public int getTotalPage() {
		if(allRows%maxResult!=0){
			this.totalPage = allRows/maxResult+1;
		}else{
			this.totalPage = allRows/maxResult;
		}
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getMaxResult() {
		return maxResult;
	}
	public void setMaxResult(int maxResult) {
		this.maxResult = maxResult;
	}
	public Page() {
		super();
	}
	public int getFirstResult() {
		firstResult = (currentPage-1)*maxResult;
		return firstResult;
	}
	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}
	public Page(int currentPage, int maxResult,int allrows) {
		super();
		this.currentPage = currentPage;
		this.maxResult = maxResult;
		this.firstResult = (currentPage-1)/maxResult;
		this.allRows = allrows;
		this.totalPage = allRows/maxResult+1;
	}
	
	
}
