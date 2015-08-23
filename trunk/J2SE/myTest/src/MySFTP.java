import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.TimeZone;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class MySFTP {

	/**
	 * 连接sftp服务器
	 * 
	 * @param host
	 *            主机
	 * @param port
	 *            端口
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return
	 */
	public ChannelSftp connect(String host, int port, String username, String password) {
		ChannelSftp sftp = null;
		try {
			JSch jsch = new JSch();
			jsch.getSession(username, host, port);
			Session sshSession = jsch.getSession(username, host, port);
			System.out.println("Session created.");
			sshSession.setPassword(password);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.connect();
			System.out.println("Session connected.");
			System.out.println("Opening Channel.");
			Channel channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
			System.out.println("Connected to " + host + ".");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sftp;
	}

	/**
	 * 上传文件
	 * 
	 * @param directory
	 *            上传的目录
	 * @param uploadFile
	 *            要上传的文件
	 * @param sftp
	 */
	public void upload(String directory, String uploadFile, ChannelSftp sftp) {
		try {
			sftp.cd(directory);
			File file = new File(uploadFile);
			sftp.put(new FileInputStream(file), file.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载文件
	 * 
	 * @param directory
	 *            下载目录
	 * @param downloadFile
	 *            下载的文件
	 * @param saveFile
	 *            存在本地的路径
	 * @param sftp
	 */
	public void download(String directory, String downloadFile, String saveFile, ChannelSftp sftp) {
		try {
			System.out.println("Start Download: " + downloadFile);
			sftp.cd(directory);
			File file = new File(saveFile);
			sftp.get(downloadFile, new FileOutputStream(file));
			System.out.println("Donload Complete...");
			sftp.exit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param directory
	 *            要删除文件所在目录
	 * @param deleteFile
	 *            要删除的文件
	 * @param sftp
	 */
	public void delete(String directory, String deleteFile, ChannelSftp sftp) {
		try {
			sftp.cd(directory);
			sftp.rm(deleteFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 列出目录下的文件
	 * 
	 * @param directory
	 *            要列出的目录
	 * @param sftp
	 * @return
	 * @throws SftpException
	 */
	public Vector<?> listFiles(String directory, ChannelSftp sftp) throws SftpException {
		return sftp.ls(directory);
	}

	public static void main(String[] args) {
		try {
			Thread.sleep(1000 * 60);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		boolean flag = args.length > 0 ? "1".equals(args[0]) : false;
		MySFTP sf = new MySFTP();
		String host = "217.70.134.18";
		int port = 22;
		String username = "root";
		String password = "c431a4425bc56080c868435c8d910f83";
		String directory = "/usr/27yule/SFS_PRO_1.6.6/Server/logs";
		String downloadFile = "wrapper_" + getDateTime(flag) + ".log";
		String saveFile = "D:\\logbackup\\" + downloadFile;
		ChannelSftp sftp = sf.connect(host, port, username, password);
		sf.download(directory, downloadFile, saveFile, sftp);
		sf.delete(directory, downloadFile, sftp);
		System.exit(0);
	}
	
	private static String getDateTime(boolean flag) {
		Calendar cale = Calendar.getInstance();
		cale.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		if (flag)
			cale.add(Calendar.DATE, -1);
		
		int year = cale.get(Calendar.YEAR);
		int month = cale.get(Calendar.MONTH) + 1;
		int date = cale.get(Calendar.DATE);
		
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumIntegerDigits(2);
		format.setMinimumIntegerDigits(2);
		format.setMaximumFractionDigits(0);
		format.setMaximumFractionDigits(0);
		format.setGroupingUsed(false);
		
		String d = year + format.format(month) + format.format(date);
		return d;
	}
}