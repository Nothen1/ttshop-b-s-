package pojo;

public class UploadStatus {
	
	private long bytesread; //���ϴ����ֽ��� ��λ byte B
	private long contentLength; //�����ļ����ֽ��� ��λ byte
	private Integer items; //�����ϴ��ڼ����ļ�
	private String filename; //�����ϴ����ļ���
	private long starttime;
	private Integer state; //��¼����ɶ��ٸ��ļ�
	private String message;
	private long totalbytes;  //���ֽ��� ����λbyte
	private Double precent;
	
	public long getBytesread() {
		return bytesread;
	}
	public void setBytesread(long bytesread) {
		this.bytesread = bytesread;
	}
	public long getContentLength() {
		return contentLength;
	}
	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}
	public Integer getItems() {
		return items;
	}
	public void setItems(Integer items) {
		this.items = items;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public long getStarttime() {
		return starttime;
	}
	public void setStarttime(long starttime) {
		this.starttime = starttime;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public long getTotalbytes() {
		return totalbytes;
	}
	public void setTotalbytes(long totalbytes) {
		this.totalbytes = totalbytes;
	}
	public Double getPrecent() {
		return precent;
	}
	public void setPrecent(Double precent) {
		this.precent = precent;
	}
	public UploadStatus(long bytesread, long contentLength, Integer items) {
		super();
		this.bytesread = bytesread;
		this.contentLength = contentLength;
		this.items = items;
		this.starttime = System.currentTimeMillis();
	}
	public UploadStatus() {
		super();
	}
	
	
}
