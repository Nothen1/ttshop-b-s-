package action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import pojo.ProductInfo;
import pojo.Pstock;
import pojo.StockChecking;
import pojo.User;
import pojo.Warehouse;
import tools.DateTool;
import tools.JsonTool;

public class CheckingAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2378620569588912346L;
	private User user;
	private List<Warehouse> warehouses;
	private StockChecking stockChecking;
	private List<StockChecking> stockCheckingList;
	private String resJson;
	private String barcode;
	private Integer quantity;
	private Warehouse warehouse;
	private Integer wid;
	private ProductInfo productInfo;
	private Pstock pstock;
	private String date;
	private InputStream inputStream;
	private Integer scid;
	/**
	 * ��ѯPRODUCTINFO����, �̵�ҳ����
	 * ���� warehouse Ϊ wid , barcode
	 * ��������������Ϣ�� info �� stock
	 * @throws Exception
	 */
	public void findProductInfo() throws Exception{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter printWriter = response.getWriter();
		if(wid==null){
			printWriter.write("error01:"+"ѡ���̵�Ĳֿ�!");
			printWriter.flush();
			printWriter.close();
			return;
		}
		warehouse = this.warehouseService.get(Warehouse.class, wid);
		if (warehouse==null || warehouse.getWname()==null) {
			printWriter.write("error01:"+"ѡ���̵�Ĳֿ�!");
			printWriter.flush();
			printWriter.close();
			return;
		}
		if(barcode==null || barcode.trim().equals("")){
			printWriter.write("error02:"+"û���ҵ��˲�Ʒ����!");
			printWriter.flush();
			printWriter.close();
			return;
		}
		ProductInfo productInfo = this.productinfoService.findByBaecode(barcode);
		if(productInfo==null){
			printWriter.write("error02:"+"û���ҵ��˲�Ʒ����!");
			printWriter.flush();
			printWriter.close();
			return;
		}
		pstock = this.pstockService.findbyBarcodeAndWarehouse(barcode, warehouse.getWname());
//		System.out.println(pstock);
		if(pstock==null){
			printWriter.write("error:"+"û���ҵ��˲�Ʒ���!");
			printWriter.flush();
			printWriter.close();
			return;
		}
		String json = JsonTool.getInstance().getInfoDetail(productInfo, "", pstock.getQuantity());
//		System.out.println(json);
		//String json = JsonTool.getInstance().getInfoDesc(productInfo);
		printWriter.write(json.trim());
		printWriter.flush();
		printWriter.close();
		return;
	}
	/**
	 * �����̵�ҳ
	 * @return
	 * @throws Exception
	 */
	public String toCheckingPage() throws Exception{
		warehouses = this.warehouseService.findByKeyword("");
		this.setReturnurl(request.getContextPath()+FILESEPARATOR+"warehousefunction.jsp");
		List<ProductInfo> infos = this.productinfoService.findByBarcodeOrDesc("",1);
		List<String[]> list1 = new ArrayList<String[]>();
		for (ProductInfo productInfo : infos) {
			String[] args = {productInfo.getBarcode(),productInfo.getBarcode()+"|"+productInfo.getPdesc(),productInfo.getPdesc()};
			list1.add(args);
		}
		resJson= "var infos = "+ JsonTool.getInstance().formList(list1,"value","label","desc")+";";
		return SUCCESS;
	}
	
	/**
	 * ����һ���̵���
	 * @return 
	 * @throws Exception
	 */
	public String addChecking() throws Exception{
		this.setReturnurl(request.getContextPath()+FILESEPARATOR+"checking"+FILESEPARATOR+"toCheckingPage.action");
		if(warehouse!=null && warehouse.getId()!=null){
			warehouse = this.warehouseService.get(Warehouse.class, warehouse.getId());
			if(barcode!=null){
				productInfo = this.productinfoService.findByBaecode(barcode);
				if(productInfo==null){
					this.setMessage("��Ʒ�����д���");
					return INPUT;
				}
				if(quantity==null){
					this.setMessage("�������̵�����");
					return INPUT;
				}
				User user = (User) request.getSession().getAttribute("user");
				pstock = this.pstockService.findbyBarcodeAndWarehouse(barcode, warehouse.getWname());
				stockChecking = new StockChecking(pstock,productInfo,quantity, warehouse);
				if(user!=null){
					stockChecking.setOperator(user.getUsername());
				}
				this.stockCheckingService.add(stockChecking);
				pstock.setQuantity(quantity); // ��������
				this.pstockService.update(pstock);
				return SUCCESS;
			}else{
				this.setMessage("��Ʒ���벻��Ϊ��!");
				return INPUT;
			}
		}else{
			this.setMessage("��Ʒ���벻��Ϊ��!");
			return INPUT;
		}
	}
	
	/**
	 * �����̵�
	 * @return
	 * @throws Exception
	 */
	public String cancelChecking() throws Exception{
		this.setReturnurl(request.getContextPath()+FILESEPARATOR+"checking"+FILESEPARATOR+"toCheckingPage.action");
		if(scid!=null && scid!=0){
			stockChecking = this.stockCheckingService.get(StockChecking.class, scid);
			stockChecking.setStatus(0);
			pstock = stockChecking.getPstock();
			pstock.setQuantity(stockChecking.getQuantity_before()); 
			this.stockCheckingService.update(stockChecking);
			this.pstockService.update(pstock);
			this.setMessage(SUCCMESSAGE);
			return SUCCESS;
		}else{
			return INPUT;
		}
	}
	
	/**
	 * �̵���ϸ , ���������
	 * @return
	 * @throws Exception
	 */
	public String listCheckingList() throws Exception{
		this.setReturnurl(request.getContextPath()+FILESEPARATOR+"warehousefunction.jsp");
		if(date==null || date.trim().equals("")){
			date = DateTool.getInstance().DateToPattern1(new Date());
		}
		warehouses = this.warehouseService.findByKeyword("");
		if(warehouse!=null && warehouse.getId()!=0){
			warehouse = this.warehouseService.get(Warehouse.class, warehouse.getId());
			stockCheckingList = this.stockCheckingService.findCheckingListByDateAndWarehouse(date,warehouse.getId());
		}else{
			stockCheckingList = this.stockCheckingService.findCheckingListByDate(date);
		}
		return SUCCESS;
	}
	
	/**
	 * ����һ���̵���
	 * @return
	 * @throws Exception
	 */
	public String createCheckingExcel() throws Exception{
		if(date==null || date.trim().equals("")){
			date = DateTool.getInstance().DateToPattern1(new Date());
		}
		warehouses = this.warehouseService.findByKeyword("");
		if(warehouse!=null && warehouse.getId()!=0){
			warehouse = this.warehouseService.get(Warehouse.class, warehouse.getId());
			stockCheckingList = this.stockCheckingService.findCheckingListByDateAndWarehouse(date,warehouse.getId());
		}else{
			stockCheckingList = this.stockCheckingService.findCheckingListByDate(date);
		}
		ByteOutputStream outputStream = new ByteOutputStream();
		WritableWorkbook workbook = Workbook.createWorkbook(outputStream);
		WritableSheet sheet = workbook.createSheet("�̵��¼��", 0);
		int row = 0;
		{
			Label label01 = new Label(0, row, "�ֿ�");
			sheet.addCell(label01);
			if(warehouse.getId()!=0){
				Label label02 = new Label(1, row, warehouse.getWnickname());
				sheet.addCell(label02);
			}else{
				Label label02 = new Label(1, row, "ȫ��");
				sheet.addCell(label02);
			}
		}
		row++;
		{
		Label label01 = new Label(0, row, "��");
		Label label02 = new Label(1, row, "��Ʒ����*");
		Label label03 = new Label(2, row, "��Ʒ�ͺ�*");
		Label label04 = new Label(3, row, "�̵�ǰ����");
		Label label05 = new Label(4, row, "�̵�����");
		Label label06 = new Label(5, row, "���");
		Label label07 = new Label(6, row, "�̵���");
		Label label08 = new Label(7, row, "ʱ��");
		sheet.addCell(label01);
		sheet.addCell(label02);
		sheet.addCell(label03);
		sheet.addCell(label04);
		sheet.addCell(label05);
		sheet.addCell(label06);
		sheet.addCell(label07);
		sheet.addCell(label08);
		}
		row++;
		for (int i = 0; i < stockCheckingList.size(); i++) {
			StockChecking checking = stockCheckingList.get(i);
			Label label01 = new Label(0, row, ""+(i+1));
			Label label02 = new Label(1, row, checking.getProductInfo().getBarcode());
			Label label03 = new Label(2, row, checking.getProductInfo().getPdesc());
			jxl.write.Number label04 = new jxl.write.Number(3, row, checking.getQuantity_before());
			jxl.write.Number label05 = new jxl.write.Number(4, row, checking.getQuantity_after());
			jxl.write.Number label06 = new jxl.write.Number(5, row, checking.getQuantity_after()-checking.getQuantity_before());
			Label label07 = new Label(6, row, checking.getOperator());
			Label label08 = new Label(7, row, checking.getUpdatetime());
			sheet.addCell(label01);
			sheet.addCell(label02);
			sheet.addCell(label03);
			sheet.addCell(label04);
			sheet.addCell(label05);
			sheet.addCell(label06);
			sheet.addCell(label07);
			sheet.addCell(label08);
			row++;
		}
		for (int i = 0; i < sheet.getColumns(); i++) {
			sheet.setColumnView(i, 30);
		}
		for (int i = 0; i < sheet.getRows(); i++) {
			sheet.setRowView(i, 300);
		}
		workbook.write();
		workbook.close();
		response.reset();
		response.setContentType("application/vn.ms-xls");
		response.setCharacterEncoding("utf-8");
		this.inputStream = new ByteArrayInputStream(outputStream.getBytes()); // �ؼ�,�� inputstream ���
		return SUCCESS;
	}
	
	public List<Warehouse> getWarehouses() {
		return warehouses;
	}
	public void setWarehouses(List<Warehouse> warehouses) {
		this.warehouses = warehouses;
	}
	public StockChecking getStockChecking() {
		return stockChecking;
	}
	public void setStockChecking(StockChecking stockChecking) {
		this.stockChecking = stockChecking;
	}
	public List<StockChecking> getStockCheckingList() {
		return stockCheckingList;
	}
	public void setStockCheckingList(List<StockChecking> stockCheckingList) {
		this.stockCheckingList = stockCheckingList;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getResJson() {
		return resJson;
	}
	public void setResJson(String resJson) {
		this.resJson = resJson;
	}

	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Warehouse getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ProductInfo getProductInfo() {
		return productInfo;
	}

	public void setProductInfo(ProductInfo productInfo) {
		this.productInfo = productInfo;
	}

	public Pstock getPstock() {
		return pstock;
	}

	public void setPstock(Pstock pstock) {
		this.pstock = pstock;
	}
	public Integer getWid() {
		return wid;
	}
	public void setWid(Integer wid) {
		this.wid = wid;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public Integer getScid() {
		return scid;
	}
	public void setScid(Integer scid) {
		this.scid = scid;
	}
	
	
}
