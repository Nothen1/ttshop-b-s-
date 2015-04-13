package tools;

import java.util.ArrayList;
import java.util.List;
import pojo.Customer;
import pojo.ProductInfo;
import pojo.SaleRecord;
import pojo.SaleRecordList;

public class JsonTool {
	
	private static JsonTool instance = null;
	
	private JsonTool(){
		super();
	}
	public static JsonTool getInstance(){
		if(instance==null){
			synchronized (JsonTool.class) {
				if(instance==null){
					instance = new JsonTool();
				}
			}
		}
		return instance;
	}
	
	/**
	 * ֱ�Ӵ���һ�����е�json�ۡ�����������
	 * @param list
	 * @return json����->[...]
	 */
	public String fromList(List<String> list){
		StringBuilder json = new StringBuilder();
		json.append("[");
		if(list!=null && list.size()>0){
			for (Object object : list) {
				json.append("\"");
				json.append(object.toString()); 
				json.append("\"");
				json.append(",");
			}
			json.setCharAt(json.length()-1, ']');
		}else{
			json.append("]");
		}
		return json.toString();
	}
	
	/**
	 * �������ߣ��б�title����һ��json�������title����list.get[0]��0�ݡ���,[��title����list.get[1]��0�ݡ���]��
	 * @param list string[]
	 * @param string...titles
	 * @return ��title����list.get[0]��0�ݡ���,[��title����list.get[1]��0�ݡ���]��
	 */
	public String formList(List<String[]> list,String...titles){
		StringBuilder json = new StringBuilder();
		json.append("[");
		if(list!=null && list.size()>0){
			for (int j = 0; j < list.size(); j++) {
				json.append("{");
				String[] strs = list.get(j);
				for (int i = 0; i < titles.length; i++) {
					json.append(titles[i]+":");
					json.append("\"");
					json.append(strs[i]);
					json.append("\"");
					if(i<titles.length-1){
						json.append(",");
					}
				}
				json.append("},");
			}
			json.setCharAt(json.length()-1, ']');
		}else{
			json.append("]");
		}
		return json.toString();
	}
	
	/**
	 * ����һ���ͻ��б� json ����
	 * @param customers
	 * @return json->customers[{value:"c-id",lable,"tel+name+c-id"}]
	 */
	public String createCustomerJson(List<Customer> customers){
		List<String[]> strings = new ArrayList<String[]>();
		for (Customer customer : customers) {
			String[] args = {customer.getCustomerId(),"Tel."+customer.getCustomerTel()+"/Name."+customer.getCustomerName()+"/Id."+customer.getCustomerId()}; //����customerName��Ϊ�˸���׼ȷ����customerID
			strings.add(args);
		}
		return "var customers =" +this.formList(strings, "value","label")+";";
	}
	
	/**
	 * ���� ���۵� json ����,ͨ��ҳ��,�˻���
	 * @param list saleRecords
	 * @return json-> invoices[{value:"invoiceno",lable:"invoiceno",customerid:"c-id","warehouse:"w-name"}]
	 */
	public String createSalerecordsJson(List<SaleRecord> saleRecords){
		List<String[]> strings = new ArrayList<String[]>();
		for (SaleRecord saleRecord : saleRecords) {
			String[] args = {saleRecord.getInvoiceno(),saleRecord.getInvoiceno(),saleRecord.getCustomerid(),saleRecord.getWarehouse()}; 
			strings.add(args);
		}
		return "var invoicenos =" +this.formList(strings, "value","label","customerid","warehouse")+";";
	}
	
	/**
	 * POS ����ר������� customers json ����, autocomplete ��
	 * @param customers
	 * @return json->customers[{value:"c-id",lable,"tel+name+c-id"}]
	 */
	public String createCustomerJsonForPos(List<Customer> customers){
		List<String[]> strings = new ArrayList<String[]>();
		for (Customer customer : customers) {
			String[] args = {customer.getCustomerId(),"Tel:"+customer.getCustomerTel()+"Name:"+customer.getCustomerName()+"Id:"+customer.getCustomerId()};
			strings.add(args);
		}
		return "var customers =" +this.formList(strings, "value","label")+";";
	}
	
	/**
	 * ��Ʒ json ����, ��ҳ����
	 * @param list<productInfo> infos
	 * @return json->infos[{value,lable,desc,price,mprice}...]
	 */
	public String createInfoJson(List<ProductInfo> productInfos){
		List<String[]> strings = new ArrayList<String[]>();
		for (ProductInfo info : productInfos) {
			String[] args = {info.getBarcode(),info.getBarcode()+"|"+info.getPdesc(),info.getPdesc(),info.getSprice().toString(),info.getMprice().toString()};
			strings.add(args);
		}
		return "var infos ="+this.formList(strings, "value","label","desc","price","mprice")+";";
	}
	
	/**
	 * ��  productinfo ����
	 * @param info
	 * @return json{barcode,desc}
	 */
	public String getInfoDesc(ProductInfo info){
		String json ="{";
		json += "barcode:\""+info.getBarcode()+"\",";
		json += "desc:\""+info.getPdesc()+"\",";
		json +="}";
		return json;
	}
	
	/**
	 * Ϊ ajax �ṩ productinfo ����,���� stock �� imei ��Ϣ
	 * @param info
	 * @param imei
	 * @param pstock
	 * @return json{barcode,desc,imei,price,minprice,stock}
	 */
	public String getInfoDetail(ProductInfo info,String imei,Integer pstock){
		String json ="{";
		json += "barcode:\""+info.getBarcode()+"\",";
		json += "desc:\""+info.getPdesc()+"\",";
		json += "imei:\""+imei+"\",";
		json += "price:"+info.getSprice()+",";
		json += "minprice:"+info.getMprice()+",";
		json += "stock:"+pstock;
		json +="}";
		return json;
	}
	
	public String getsaleRecordJson(SaleRecord saleRecord){
		String json ="{";
		json += "invoiceno:\""+saleRecord.getInvoiceno()+"\",";
		json += "customerid:\""+saleRecord.getCustomerid()+"\",";
		json += "warehouse:\""+saleRecord.getWarehouse()+"\",";
		json += "quantity:"+saleRecord.getTotalquantity()+",";
		json += "amount:"+saleRecord.getTotalamount()+",";
		json +="}";
		return json;
	}
	
	public String createSalerecord(List<SaleRecord> saleRecords){
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (saleRecords!=null && saleRecords.size()>0) {
			for (int i = 0; i < saleRecords.size(); i++) {
				SaleRecord saleRecord = saleRecords.get(i);
				json.append("{");
					//invoiceno
					json.append("invoiceno"+":");
					json.append("\"");
					json.append(saleRecord.getInvoiceno());
					json.append("\"");
					json.append(",");
					json.append("customer"+":");
					json.append("\"");
					//System.out.println(saleRecord.getCustomerid());
					json.append(saleRecord.getCustomerid());
					json.append("\"");
					json.append(",");
					//totalquantity
					json.append("totalquantity"+":");
					json.append("\"");
					json.append(saleRecord.getTotalquantity());
					json.append("\"");
					json.append(",");
					//totalamount
					json.append("totalamount"+":");
					json.append("\"");
					json.append(saleRecord.getTotalamount());
					json.append("\"");
					json.append(",");
					json.append("date"+":");
					json.append("\"");
					json.append(saleRecord.getCreatetime());
					json.append("\"");
					if(i!=saleRecords.size()-1){
						json.append("},");
					}else{
						json.append("}");
					}
			}
		}
		json.append("]");
		return json.toString();
	}
	
	public String createSalerecordInfo(List<SaleRecordList> saleRecordLists){
		List<String[]> lists= new ArrayList<String[]>();
		if(!saleRecordLists.isEmpty()){
			for (SaleRecordList saleRecordList : saleRecordLists) {
				String[] args = {saleRecordList.getBarcode(),saleRecordList.getPdesc(),saleRecordList.getQuantity().toString(),saleRecordList.getPrice().toString()};
				lists.add(args);
			}
		}
		return this.formList(lists, "barcode","pdesc","quantity","price");
	}
	
	/**
	 * ajax ���� productinfo
	 * @param saleRecords
	 * @return
	 */
	public String createWebshopProduct(List<ProductInfo> productInfos){
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (!productInfos.isEmpty()) {
			for (int i = 0; i < productInfos.size(); i++) {
				ProductInfo productInfo = productInfos.get(i);
				json.append("{");
					json.append("id"+":");
					json.append("\"");
					json.append(productInfo.getId());
					json.append("\"");
					json.append(",");
					json.append("barcode"+":");
					json.append("\"");
					json.append(productInfo.getBarcode());
					json.append("\"");
					json.append(",");
					json.append("pdesc"+":");
					json.append("\"");
					json.append(productInfo.getPdesc());
					json.append("\"");
					json.append(",");
					json.append("sprice"+":");
					json.append("\"");
					json.append(productInfo.getSprice());
					json.append("\"");
					json.append(",");
					json.append("introduction"+":");
					json.append("\"");
					json.append(productInfo.getIntroduction());
					json.append("\"");
					json.append(",");
					json.append("small"+":");
					json.append("\"");
					json.append(productInfo.getImage().getSmall());
					json.append("\"");
					if(i!=productInfos.size()-1){
						json.append("},");
					}else{
						json.append("}");
					}
			}
		}
		json.append("]");
		return json.toString();
	}
	
}
