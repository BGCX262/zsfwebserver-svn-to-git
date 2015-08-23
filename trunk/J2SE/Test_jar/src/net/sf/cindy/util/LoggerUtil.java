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
    
    /** 存放的文件夹 **/
    private static String fileName = "flowControl";
    
    /**
     * 得到要记录的日志的路径及文件名称
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
     * 得到要记录的日志的路径及文件名称
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
     * 配置Logger对象输出日志文件路径
     * @param logger 
     * @throws SecurityException
     * @throws IOException
     */
    public static void setLogingProperties(Logger logger) {
        setLogingProperties(logger,Level.ALL);
    }
    
    /**
     * 配置Logger对象输出日志文件路径
     * @param logger
     * @param level 在日志文件中输出level级别以上的信息
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
		            	logger.addHandler(fileHandler);//日志输出文件
		        } catch (SecurityException e) {
		            logger.log(Level.SEVERE, "安全性错误", e);
		        } catch (IOException e) {
		            logger.log(Level.SEVERE,"读取文件日志错误", e);
		        }
		}
    }
    
    /**
     * 配置Logger对象输出日志文件路径
     * @param logger
     * @param level 在日志文件中输出level级别以上的信息
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
            	logger.addHandler(fileHandler);//日志输出文件
        } catch (SecurityException e) {
            logger.log(Level.SEVERE, "安全性错误", e);
        } catch (IOException e) {
            logger.log(Level.SEVERE,"读取文件日志错误", e);
        }
    }
    
}