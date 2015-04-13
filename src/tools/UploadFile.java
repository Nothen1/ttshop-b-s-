package tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.struts2.ServletActionContext;

public class UploadFile {
	
	private static UploadFile instance;
	
	private UploadFile(){
		super();
	}
	
	public static UploadFile getInstance() {
		if(instance==null){
			synchronized (UploadFile.class) {
				if(instance==null){
					instance = new UploadFile();
				}
			}
		}
		return instance;
	}
	
	public String generateSavepath(String floder){
		String path = null;
		path = ServletActionContext.getServletContext().getRealPath(floder);
		return path;
	}
	
	public String generateFilename(String type){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHMMSS");
		Random random = new Random();
		String suijishu = random.nextInt()+"";
		suijishu = suijishu.substring(suijishu.length()-4, suijishu.length());
		String name = dateFormat.format(new Date());
		String filename = name+suijishu+type;
		return filename;
	}
	
	public void upload(File file,String filename,String dir) throws Exception{
		//String filename = this.generateFilename(type);
		if(!file.exists()){
			throw new Exception("�Ҳ����ϴ��ļ�!");
		}
		File saveFile = new File(dir,filename);
		
		InputStream ins = null;
		OutputStream ous = null;
		
		ins = new FileInputStream(file); //��ȡ���� file �ļ��ֽ�
		file = null;
		ous = new FileOutputStream(saveFile); //������� savefile �ļ��ֽ�
		
		byte[] bs = new byte[2048];
		int len = 0;
		while ((len=ins.read(bs))!=-1) {
			ous.write(bs,0,len);
		}
		if(ins!=null){
			ins.close();
		}
		if(ous!=null){
			ous.close();
		}
	}

}
