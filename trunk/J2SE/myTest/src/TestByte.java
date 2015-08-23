public class TestByte {

	public static void main(String[] args) {
		/*
		 * int n = Integer.MAX_VALUE; int m = 257;
		 * 
		 * System.out.println(Integer.toBinaryString(n));
		 * System.out.println(Integer.toBinaryString(m));
		 * System.out.println(Integer.toHexString(n));
		 * System.out.println(Integer.toHexString(m));
		 * 
		 * System.out.println("------------------------------------" + 0xff);
		 * 
		 * System.out.println(Integer.toBinaryString(n & 0xff));
		 * System.out.println(Integer.toBinaryString(m & 0xff));
		 * DataFactory.traceHexBytes(new byte[] { (byte) (n >> 24), (byte) (n >>
		 * 16), (byte) (n >> 8), (byte) n }); DataFactory.traceHexBytes(new
		 * byte[] { (byte) (m >> 24), (byte) (m >> 16), (byte) (m >> 8), (byte)
		 * m });
		 * 
		 * DataFactory.traceHexBytes(DataFactory.doubleToXiaoTouByte(Double.
		 * MAX_VALUE)); System.out.println(Long.MAX_VALUE);
		 * System.out.println(DataFactory
		 * .getLong(DataFactory.doubleToXiaoTouByte(new
		 * Double(Long.MAX_VALUE))));
		 * 
		 * int i = 1; int j = i << 4;
		 * 
		 * if (j < 0) System.out.println("less than 0"); else
		 * System.out.println("more than 0");
		 * 
		 * System.out.println(Double.doubleToLongBits(Double.MAX_VALUE));
		 * System.
		 * out.println(Double.valueOf(Double.doubleToLongBits(Double.MAX_VALUE))
		 * + ""); System.out.println(Double.MAX_VALUE);
		 * 
		 * System.out.println("===================================");
		 * 
		 * System.out.println(Double.MAX_VALUE);
		 * DataFactory.traceHexBytes(DataFactory
		 * .doubleToXiaoTouByte(Double.MAX_VALUE));
		 * System.out.println(byteToDouble
		 * (DataFactory.doubleToXiaoTouByte(Double.MAX_VALUE)));
		 * 
		 * System.out.println(Double.doubleToLongBits(Double.MAX_VALUE));
		 * System.out.println(Long.MAX_VALUE);
		 * 
		 * System.out.println(Double.longBitsToDouble(Long.MAX_VALUE));
		 * DataFactory
		 * .traceHexBytes(DataFactory.longOrIntegerToBytes(Long.MAX_VALUE));
		 * 
		 * System.out.println(Float.floatToIntBits(Float.MAX_VALUE));
		 * System.out.println(Integer.MAX_VALUE);
		 * 
		 * System.out.println(Byte.MIN_VALUE + "," + Byte.MAX_VALUE);
		 */

		/*System.out.println(DataConvertUtil.bytes2Int(DataConvertUtil.int2Bytes(Integer.MAX_VALUE)) == Integer.MAX_VALUE);
		System.out.println(DataConvertUtil.bytes2Short(DataConvertUtil.short2Bytes(Short.MAX_VALUE)) == Short.MAX_VALUE);
		System.out.println(DataConvertUtil.bytes2Long(DataConvertUtil.long2Bytes(Long.MAX_VALUE)) == Long.MAX_VALUE);
		System.out.println(DataConvertUtil.bytes2Double(DataConvertUtil.double2Bytes(Double.MAX_VALUE)) == Double.MAX_VALUE);
		System.out.println(DataConvertUtil.bytes2Float(DataConvertUtil.float2Bytes(Float.MAX_VALUE)) == Float.MAX_VALUE);

		System.out.println(DataConvertUtil.bytes2Int(DataConvertUtil.int2Bytes(Integer.MIN_VALUE)) == Integer.MIN_VALUE);
		System.out.println(DataConvertUtil.bytes2Short(DataConvertUtil.short2Bytes(Short.MIN_VALUE)) == Short.MIN_VALUE);
		System.out.println(DataConvertUtil.bytes2Long(DataConvertUtil.long2Bytes(Long.MIN_VALUE)) == Long.MIN_VALUE);
		System.out.println(DataConvertUtil.bytes2Double(DataConvertUtil.double2Bytes(Double.MIN_VALUE)) == Double.MIN_VALUE);
		System.out.println(DataConvertUtil.bytes2Float(DataConvertUtil.float2Bytes(Float.MIN_VALUE)) == Float.MIN_VALUE);

		DataConvertUtil.printBytes(DataConvertUtil.int2Bytes(Integer.MAX_VALUE));
		DataConvertUtil.printBytes(DataConvertUtil.short2Bytes(Short.MAX_VALUE));
		DataConvertUtil.printBytes(DataConvertUtil.long2Bytes(Long.MAX_VALUE));
		DataConvertUtil.printBytes(DataConvertUtil.double2Bytes(Double.MAX_VALUE));
		DataConvertUtil.printBytes(DataConvertUtil.float2Bytes(Float.MAX_VALUE));

		DataConvertUtil.printBytes(DataConvertUtil.int2Bytes(Integer.MIN_VALUE));
		DataConvertUtil.printBytes(DataConvertUtil.short2Bytes(Short.MIN_VALUE));
		DataConvertUtil.printBytes(DataConvertUtil.long2Bytes(Long.MIN_VALUE));
		DataConvertUtil.printBytes(DataConvertUtil.double2Bytes(Double.MIN_VALUE));
		DataConvertUtil.printBytes(DataConvertUtil.float2Bytes(Float.MIN_VALUE));

		System.out.println(new byte[] { (byte) 0xff }[0]);

		new TestByte();
		
		final byte[] lock = new byte[0];
		
		synchronized (lock) {
			try {
				lock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}*/
		
	}

	@Override
	protected void finalize() throws Throwable {
		System.out.println("over");
		
		super.finalize();
	}

	public static double byteToDouble(byte[] b) {
		long l;
		l = b[0];
		l &= 0xffl;
		l |= ((long) b[1] << 8);
		l &= 0xffffl;
		l |= ((long) b[2] << 16);
		l &= 0xffffffl;
		l |= ((long) b[3] << 24);
		l &= 0xffffffffl;
		l |= ((long) b[4] << 32);
		l &= 0xffffffffffl;
		l |= ((long) b[5] << 40);
		l &= 0xffffffffffffl;
		l |= ((long) b[6] << 48);
		l &= 0xffffffffffffffl;
		l |= ((long) b[7] << 56);

		return Double.longBitsToDouble(l);
	}

}