import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;


public final class TestDaka extends TimerTask {
    private String url;
    private String postData;//提交的数据
    private String filePath;//页面保存地址
    
    public TestDaka(String url,String postData,String filePath) {
        this.url=url;
        this.postData=postData;
        this.filePath=filePath;
    }
    
    public void toRequest() {
        try {
            URL url = new URL(this.url);
            HttpURLConnection httpUrl=(HttpURLConnection)url.openConnection();
            BufferedOutputStream hurlBufOus=null;
            httpUrl.setDoOutput(true);
            
            if(this.postData!=null&&!"".equals(this.postData)){
                httpUrl.setDoInput(true);
                hurlBufOus=new BufferedOutputStream(httpUrl.getOutputStream());
                hurlBufOus.write(this.postData.getBytes());
                hurlBufOus.flush();
            }
            System.out.println("httpUrl.getRequestMethod()="+httpUrl.getRequestMethod());
            System.out.println("httpUrl.getURL()="+httpUrl.getURL());
            BufferedInputStream hurlBufIns=new BufferedInputStream(httpUrl.getInputStream());
            BufferedOutputStream fileBufOus=new BufferedOutputStream(new FileOutputStream(this.filePath));
            int read=0;
            byte[] byteBuf=new byte[9412];
            while((read=hurlBufIns.read(byteBuf))!=-1){
                fileBufOus.write(byteBuf, 0, read);
            }
            
            if(hurlBufOus!=null)hurlBufOus.close();
            if(hurlBufIns!=null)hurlBufIns.close();
            if(fileBufOus!=null)fileBufOus.close();
            httpUrl.disconnect();
            
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public static void main(String[] args) {
    	Timer timer = new Timer();
    	String name = "27025";
    	String password = "115599";
    	String t = "上班";
    	
    	String url = "http://192.168.27.60:8080/COWA/TestWork";
    	String type = "%E4%B8%8A%E7%8F%AD";
		try {
			type = URLEncoder.encode(t, "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	String postData = "username=" + name + "&pwd=" + password + "&test=" + type;
    	String filePath = "D:\\down\\daka\\";
    	File path = new File(filePath);
    	
    	if (!path.exists()) {
    		path.mkdirs();
    	}
    	
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		
    	TestDaka td = new TestDaka(url, postData, filePath);
    	
    	while (true) {
    		int minute = (int) (Math.random() * 30.0 + 30.0);
    		int second = (int) (Math.random() * 60.0);
    		
    		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
    		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), 
    				8, minute, second);
    		c.add(Calendar.DATE, 1);
    		
    		String fileName = dateFormat.format(c.getTime()) + ".html";

    		td.cancel();
    		timer.purge();
    		td = new TestDaka(url, postData, filePath + fileName);
			timer.schedule(td, c.getTime());
			
			try {
				Thread.sleep(60 * 60 * 24 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void run() {
		toRequest();
	}

}