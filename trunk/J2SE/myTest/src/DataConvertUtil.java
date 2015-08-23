/**
 * data convert util (Little-Endian)
 * 
 * @author zsf
 */
public class DataConvertUtil {

	/**
	 * byte array convert to int
	 * 
	 * @param bytes
	 * @return
	 * @throws RuntimeException
	 */
	public static int bytes2Int(byte[] bytes) throws RuntimeException {
		if (bytes == null)
			throw new DataConvertException("byte array can not convert to int");

		if (bytes.length >= 4)
			return (bytes[0] & 0xff | (bytes[1] & 0xff) << 8 | (bytes[2] & 0xff) << 16 | (bytes[3] & 0xff) << 24);
		if (bytes.length == 2)
			return (bytes[0] & 0xff | (bytes[1] & 0xff) << 8);
		if (bytes.length == 1)
			return bytes[0] & 0xff;

		throw new DataConvertException("byte array can not convert to int");

	}

	/**
	 * int convert to byte array
	 * 
	 * @param i
	 * @return
	 */
	public static byte[] int2Bytes(int i) {
		return new byte[] { (byte) i, (byte) (i >> 8), (byte) (i >> 16), (byte) (i >> 24) };
	}

	/**
	 * byte array convert to short
	 * 
	 * @param bytes
	 * @return
	 */
	public static short bytes2Short(byte[] bytes) {
		if (bytes == null)
			throw new DataConvertException("byte array can not convert to short");

		if (bytes.length == 2)
			return (short) (bytes[0] & 0xff | (bytes[1] & 0xff) << 8);
		if (bytes.length == 1)
			return (short) (bytes[0] & 0xff);

		throw new DataConvertException("byte array can not convert to int");
	}

	/**
	 * short convert to byte array
	 * 
	 * @param i
	 * @return
	 */
	public static byte[] short2Bytes(short i) {
		return new byte[] { (byte) i, (byte) (i >> 8) };
	}

	/**
	 * byte array convert to long
	 * 
	 * @param bytes
	 * @return
	 * @throws RuntimeException
	 */
	public static long bytes2Long(byte[] bytes) {
		if (bytes == null)
			throw new DataConvertException("byte array can not convert to long");

		if (bytes.length >= 8)
			return (bytes[0] & 0xff | (long)(bytes[1] & 0xff) << 8 | (long)(bytes[2] & 0xff) << 16 | 
					(long)(bytes[3] & 0xff) << 24 | (long)(bytes[4] & 0xff) << 32 | (long)(bytes[5] & 0xff) << 40 | 
					(long)(bytes[6] & 0xff) << 48 | (long)(bytes[7] & 0xff) << 56);
		if (bytes.length == 4)
			return (bytes[0] & 0xff | (bytes[1] & 0xff) << 8 | (bytes[2] & 0xff) << 16 | (bytes[3] & 0xff) << 24);
		if (bytes.length == 2)
			return (bytes[0] & 0xff | (bytes[1] & 0xff) << 8);
		if (bytes.length == 1)
			return bytes[0] & 0xff;

		throw new DataConvertException("byte array can not convert to long");
	}

	/**
	 * long convert to byte array
	 * 
	 * @param i
	 * @return
	 */
	public static byte[] long2Bytes(long i) {
		return new byte[] { (byte) i, (byte) (i >> 8), (byte) (i >> 16), 
				(byte) (i >> 24), (byte) (i >> 32), (byte) (i >> 40), 
				(byte) (i >> 48), (byte) (i >> 56) };
	}

	/**
	 * byte array convert to double
	 * 
	 * @param bytes
	 * @return
	 * @throws RuntimeException
	 */
	public static double bytes2Double(byte[] bytes) {
		return Double.longBitsToDouble(bytes2Long(bytes));
	}
	
	/**
	 * double convert to byte array
	 * 
	 * @param i
	 * @return
	 */
	public static byte[] double2Bytes(double d) {
		long l = Double.doubleToLongBits(d);
		return long2Bytes(l);
	}
	
	/**
	 * byte array convert to float
	 * 
	 * @param bytes
	 * @return
	 * @throws RuntimeException
	 */
	public static float bytes2Float(byte[] bytes) {
		return Float.intBitsToFloat(bytes2Int(bytes));
	}
	
	/**
	 * float convert to byte array
	 * 
	 * @param i
	 * @return
	 */
	public static byte[] float2Bytes(float f) {
		return int2Bytes(Float.floatToIntBits(f));
	}
	
	/**
	 * print byte array
	 * @param bytes
	 */
	public static void printBytes(byte[] bytes) {
		System.out.println(toString(bytes) + "\tlength=" + bytes.length);
	}
	
	/**
	 * get zhe byte array by String
	 * @param bytes
	 * @return
	 */
	public static String toString(byte[] bytes) {
		StringBuffer sb = new StringBuffer(100);
		for (int i = 0, n = bytes.length; i < n; i++) {
			String s = Integer.toHexString((bytes[i] & 0xFF));
			if (s.length() == 1) {
				s = "0" + s;
			}
			sb.append(s);
		}
		return sb.toString();
		
	}

}

/**
 * convert exception
 * @author zsf
 */
class DataConvertException extends RuntimeException {

	private static final long serialVersionUID = 8999603986343687660L;
	
	public DataConvertException() { super(); }
	
	public DataConvertException(String msg) { super(msg); }
	
}
