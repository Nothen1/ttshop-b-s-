package tools;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import javax.imageio.ImageIO;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class PictureTool {
	
	private static PictureTool instance;
	
	private PictureTool(){
		super();
	}
	
	public static PictureTool getInstance(){
		if(instance==null){
			synchronized (PictureTool.class) {
				if(instance==null){
					instance = new PictureTool();
				}
			}
		}
		return instance;
	}
	
	/**
	 * ���ص���file,ѹ������ļ�
	 * @param file
	 * @param width
	 * @param height
	 * @return
	 * @throws Exception
	 */
	public File resize(File inputfile,int width,int height,boolean proportion,String outputdir,String filename) throws Exception{
		System.gc();
		if(!inputfile.exists()){
			return null;
		}
		Image image = ImageIO.read(inputfile);
		if(image.getWidth(null)==-1){
			//System.out.println("����ͼƬ��ʽ");
			return null;
		}
		if(width>image.getWidth(null)){
			width = image.getWidth(null);
		}
		if(proportion==true){
			double rate = (double)image.getWidth(null)/(double)width;
	        height = (int) (image.getHeight(null)/rate);
		}
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        bufferedImage.getGraphics().drawImage(image,0, 0,width,height,null);
        File file2 = new File(outputdir,filename);
        FileOutputStream fileOutputStream = new FileOutputStream(file2);
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fileOutputStream);
        JPEGEncodeParam encodeParam = JPEGCodec.getDefaultJPEGEncodeParam(bufferedImage);
        /*ѹ������*/
        encodeParam.setQuality(0.9f, true);
        /*����ѹ����������*/
        encoder.encode(bufferedImage,encodeParam);
        inputfile = null;
        image = null;
        file2 = null;
        bufferedImage = null;
        encodeParam = null;
        encoder = null;
        fileOutputStream.close();
        Runtime.getRuntime().gc();
        System.out.println("ȫ�����պ�:"+Runtime.getRuntime().totalMemory()/1024/1024+"M");
		return file2;
	}
	
	/**
	 * �ϴ�ͼƬ,��ѹ��
	 * @param file
	 * @param width
	 * @param height
	 * @return
	 * @throws Exception
	 */
	public File uploadImage(File inputfile,String outputdir,String filename) throws Exception{
		Runtime.getRuntime().gc();
		if(!inputfile.exists()){
			return null;
		}
		int width = 600;
		int height = 600;
		Image image = ImageIO.read(inputfile);
		if(image.getWidth(null)==-1){
			return null;
		}
		//ͼƬ�������Ϊԭͼ
		width = image.getWidth(null);
		double rate = (double)image.getWidth(null)/(double)width;
		height = (int) (image.getHeight(null)/rate);
		
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		bufferedImage.getGraphics().drawImage(image,0, 0,width,height,null);
		File file2 = new File(outputdir,filename);
		FileOutputStream fileOutputStream = new FileOutputStream(file2);
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fileOutputStream);
		JPEGEncodeParam encodeParam = JPEGCodec.getDefaultJPEGEncodeParam(bufferedImage);
		/*ѹ������*/
		encodeParam.setQuality(0.9f, true);
		/*����ѹ����������*/
		encoder.encode(bufferedImage,encodeParam);
		fileOutputStream.close();
		return file2;
	}
	
	/**
	 * �ϴ�ͼƬ,����ѹ��ͼ��ԭͼ
	 * @param file
	 * @param width
	 * @param height
	 * @return
	 * @throws Exception
	 */
	public void creteImages(File inputfile,String outputdir,String smalfiledir,String filename) throws Exception{
		if(!inputfile.exists()){
			return;
		}
		int width = 800;//800�����õ�,��ʼ����
		int height = 800;//
		System.out.println("total_memory=" + Runtime.getRuntime().totalMemory()/1024/1024 + "M");    
		System.out.println("max_memory=" + Runtime.getRuntime().maxMemory()/1024/1024 + "M");
		Image image = ImageIO.read(inputfile);
		this.resize(inputfile, 500, 500, true, smalfiledir, filename);
		inputfile = null; //ǿ�ƻ���
		if(image.getWidth(null)==-1){
			return;
		}
		//ͼƬ�������Ϊԭͼ
		width = image.getWidth(null);
		double rate = (double)image.getWidth(null)/(double)width;
		height = (int) (image.getHeight(null)/rate);
		
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		bufferedImage.getGraphics().drawImage(image,0, 0,width,height,null);
		image = null;
		File file2 = new File(outputdir,filename);
		FileOutputStream fileOutputStream = new FileOutputStream(file2);
		file2 = null;
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fileOutputStream);
		JPEGEncodeParam encodeParam = JPEGCodec.getDefaultJPEGEncodeParam(bufferedImage);
		/*ѹ������*/
		encodeParam.setQuality(0.8f, true);
		/*����ѹ����������*/
		encoder.encode(bufferedImage,encodeParam);
		bufferedImage=null;
		fileOutputStream.close();
		Runtime.getRuntime().gc();
		System.out.println("�ɹ�");
		System.out.println("total_memory=" + Runtime.getRuntime().totalMemory()/1024/1024 + "M");   
		return;
	}
	
	
}
