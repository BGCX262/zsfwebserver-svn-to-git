
import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 协议体封装
 * @author zsf
 * 2011-7-21 上午09:18:58
 */
public class ProtocolParserUtil {
	
	private static final Log LOG = LogFactory.getLog(ProtocolParserUtil.class);
	private byte[] response = new byte[0];
	private int index = 0;

	public void appendByte(byte b) {
		response = DataFactory.addByteArray(response, new byte[] { b });
	}
	
	public void appendInt(int i) {
		response = DataFactory.addByteArray(response, DataFactory.getbyte(i));
	}
	
	public void appendDouble(double d) {
		response = DataFactory.addByteArray(response, DataFactory.doubleToXiaoTouByte(d));
	}
	
	public void appendLong(long l) {
		response = DataFactory.addByteArray(response, DataFactory.doubleToXiaoTouByte(l));
	}
	
	public void appendString(String str) {
		try {
			byte[] bytes = str.getBytes("utf-8");
			byte[] temp = new byte[bytes.length + 2];
			DataFactory.replace(temp, 0, DataFactory.getbyte(bytes.length));
			DataFactory.replace(temp, 0 + 2, DataFactory.get(bytes, 0, bytes.length));
			bytes = temp;
			response = DataFactory.addByteArray(response, bytes);
		} catch (UnsupportedEncodingException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	public void appendAll(byte[] b) {
		response = DataFactory.addByteArray(response, b);
	}
	
	public void appendAll(ProtocolParserUtil c) {
		response = DataFactory.addByteArray(response, c.response);
	}
	
	public void appendLength() {
		response = DataFactory.addLength(0, response);
	}
	
	public void replaceByte(byte b, int idx) {
		response[idx] = b;
	}
	
	public void clear() {
		response = new byte[0];
	}
	
	public void setResponse(byte[] re) {
		response = re;
	}
	
	public byte[] getResponse() {
		return response;
	}
	
	public byte[] getResponse(int start, int len) {
		return DataFactory.get(response, start, len);
	}
	
	public int readInt() {
		return DataFactory.getInt(DataFactory.get(response, (index += 4) - 4, 4));
	}
	
	public int readInt(int start) {
		return DataFactory.getInt(DataFactory.get(response, (index = 4 + start) - 4, 4));
	}
	
	public int readShortInt() {
		return DataFactory.getInt(DataFactory.get(response, (index += 2) - 2, 2));
	}
	
	public int readShortInt(int start) {
		return DataFactory.getInt(DataFactory.get(response, (index = 2 + start) - 2, 2));
	}
	
	public byte readByte() {
		return DataFactory.get(response, (index += 1) - 1, 1)[0];
	}
	
	public byte readByte(int start) {
		return DataFactory.get(response, (index = 1 + start) - 1, 1)[0];
	}
	
	public double readDouble() {
		long l = DataFactory.getLong(DataFactory.get(response, (index += 8) - 8, 8));
		return Double.longBitsToDouble(l);
	}
	
	public double readDouble(int start) {
		long l = DataFactory.getLong(DataFactory.get(response, (index = 8 + start) - 8, 8));
		return Double.longBitsToDouble(l);
	}
	
	public long readLong() {
		return DataFactory.doubleBytesToLong(DataFactory.get(response, (index += 8) - 8, 8));
	}
	
	public long readLong(int start) {
		return DataFactory.doubleBytesToLong(DataFactory.get(response, (index = 8 + start) - 8, 8));
	}
	
	public String readString() {
		int len = DataFactory.getInt(DataFactory.get(response, (index += 2) - 2, 2));
		try {
			return new String(DataFactory.get(response, (index += len) - len, len), "utf-8");
		} catch (UnsupportedEncodingException e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}
	
	public String readString(int start) {
		int len = DataFactory.getInt(DataFactory.get(response, (index = 2 + start) - 2, 2));
		try {
			return new String(DataFactory.get(response, (index += len) - len, len), "utf-8");
		} catch (UnsupportedEncodingException e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}
	
	public int length() {
		return response.length;
	}
	
	/**
	 * 获取实例
	 * @return
	 */
	public static ProtocolParserUtil getInstance() {
		return new ProtocolParserUtil();
	}
	
	/**
	 * 获取实例
	 * @return
	 */
	public static ProtocolParserUtil getInstance(byte[] response) {
		ProtocolParserUtil protocolParserUtil = new ProtocolParserUtil();
		protocolParserUtil.response = response;
		return protocolParserUtil;
	}
	
	protected ProtocolParserUtil() {}

	public String toString() {
		return DataFactory.getHexBytes(response);
	}
	
	public void trace() {
		DataFactory.traceHexBytes(response);
	}
	
	public static void main(String[] args) {
		ProtocolParserUtil cmd = getInstance();
		cmd.appendString("12332是不是1");
		cmd.appendString("12332是不是1");
		DataFactory.traceHexBytes(cmd.getResponse());
		System.out.println(cmd.readString());
		System.out.println(cmd.readString());
	}
}
