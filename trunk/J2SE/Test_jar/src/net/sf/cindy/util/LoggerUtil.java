package net.sf.cindy.util;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author XiaoJun
 *
 */
final public class LoggerUtil {
    
    /** ��ŵ��ļ��� **/
    private static String fileName = "flowControl";
    
    /**
     * �õ�Ҫ��¼����־��·�����ļ�����
     * @return
     */
    private static String getLogName() {
        StringBuffer logPath = new StringBuffer();
        logPath.append("logs");
        logPath.append("/"+fileName);
        File file = new File(logPath.toString());
        if (!file.exists())
            file.mkdir();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        logPath.append("/"+simpleDateFormat.format(new Date()) + "%-u.%g.log");
        return logPath.toString();
    }
    
    /**
     * �õ�Ҫ��¼����־��·�����ļ�����
     * @return
     */
    private static String getPathPrefix() {
        StringBuffer logPath = new StringBuffer();
        logPath.append("logs");
        logPath.append("/"+fileName);
        File file = new File(logPath.toString());
        if (!file.exists())
            file.mkdir();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        logPath.append("/"+simpleDateFormat.format(new Date()));
        return logPath.toString();
    }
    
    /**
     * ����Logger���������־�ļ�·��
     * @param logger 
     * @throws SecurityException
     * @throws IOException
     */
    public static void setLogingProperties(Logger logger) {
        setLogingProperties(logger,Level.ALL);
    }
    
    /**
     * ����Logger���������־�ļ�·��
     * @param logger
     * @param level ����־�ļ������level�������ϵ���Ϣ
     * @throws SecurityException
     * @throws IOException
     */
    public static void setLogingProperties(Logger logger,Level level,String logName) {
    	if (logName == null) {
    		setLogingProperties(logger,level);
		} else {
			 FileHandler fileHandler;
		        try {
		        	logName = (getPathPrefix())+"-" + logName + "-%u.%g" + ".log";
					fileHandler = new FileHandler(logName,524288,5,true);
		            logger.setLevel(level);
		            fileHandler.setFormatter(new SimpleFormatter());
		            if(logger.getHandlers().length==0)
		            	logger.addHandler(fileHandler);//��־����ļ�
		        } catch (SecurityException e) {
		            logger.log(Level.SEVERE, "��ȫ�Դ���", e);
		        } catch (IOException e) {
		            logger.log(Level.SEVERE,"��ȡ�ļ���־����", e);
		        }
		}
    }
    
    /**
     * ����Logger���������־�ļ�·��
     * @param logger
     * @param level ����־�ļ������level�������ϵ���Ϣ
     * @throws SecurityException
     * @throws IOException
     */
    public static void setLogingProperties(Logger logger,Level level) {
        FileHandler fileHandler;
        try {
            fileHandler = new FileHandler(getLogName(),524288,5,true);
            logger.setLevel(level);
            fileHandler.setFormatter(new SimpleFormatter());
            if(logger.getHandlers().length==0)
            	logger.addHandler(fileHandler);//��־����ļ�
        } catch (SecurityException e) {
            logger.log(Level.SEVERE, "��ȫ�Դ���", e);
        } catch (IOException e) {
            logger.log(Level.SEVERE,"��ȡ�ļ���־����", e);
        }
    }
    
}