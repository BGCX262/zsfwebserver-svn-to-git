package com.ttt.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;

/**
 * @author ChangXiaoJun
 * @version v0.1 Time:7.April.2008
 */
public class DataFactory {
	
	public static final int lengthOffset = 0;

	/**
	 * add two byte array to a new byte array
	 * 
	 * @param front
	 *            front byte array
	 * @param rear
	 *            back byte array
	 * @return result of adding two byte array
	 */
	public static byte[] addByteArray(final byte[] front, final byte[] rear) {
		byte[] destination = new byte[front.length + rear.length];
		for (int i = 0; i < destination.length; i++) {
			if (i < front.length)
				destination[i] = front[i];
			else
				destination[i] = rear[i - front.length];
		}
		return destination;
	}

	/**
	 * add package length to index of offset,length contribute for two bytes
	 * 
	 * @param packageContent
	 */
	public static byte[] addLength(int offset, byte[] packageContent) {
		if (offset > packageContent.length - 1 || offset < 0) {
			offset = lengthOffset;
			// log.warn("The offset is negative or zero,used default value.");
		}
		byte[] temp = new byte[packageContent.length + 2];
		replace(temp, 0, get(packageContent, 0, offset));
		replace(temp, offset, getbyte(temp.length));
		replace(temp, offset + 2, get(packageContent, offset, packageContent.length - offset));
		return temp;
	}

	/**
	 * 
	 * @param add
	 *            package length to index of offset,length contribute for two
	 *            bytes
	 */
	public static void changeLength(int offset, byte[] packageContent) {
		if (offset > packageContent.length - 1 || offset < 0) {
			return;
		}
		int temp = packageContent.length;
		packageContent[offset + 1] = (byte) (temp >> 8 & 0xFF);
		packageContent[offset] = (byte) (temp & 0x00FF);
	}

	/**
	 * read 4 bytes to Integer
	 * 
	 * @param offset
	 * @param temp
	 * @return
	 */
	public static int getInt(int offset, byte[] temp) {
		if (temp == null || offset > temp.length - 1 || offset < 0) {
			return -1;
		}
		if (temp.length > offset + 3) {
			return (int) (((temp[offset + 3] & 0xff) << 24) | ((temp[offset + 2] & 0xff) << 16) | ((temp[offset + 1] & 0xff) << 8) | (temp[offset] & 0xff));
		} else {
			return -1;
		}
	}

	public static boolean compare(byte[] content1, byte[] content2) {
		if (content1 == content2) {
			return true;
		}
		if (content1.length != content2.length) {
			return false;
		} else {
			for (int i = 0; i < content2.length; i++) {
				if (content1[i] != content2[i])
					return false;
			}
			return true;
		}
	}

	/**
	 * add fill to message at the offset position
	 */
	public static byte[] addToMiddle(final byte[] mgs, int offset, final byte[] fill) {
		if (offset > mgs.length || offset < 0) {
			return null;
		}
		byte[] result = new byte[mgs.length + fill.length];
		for (int i = 0; i < result.length; i++) {
			if (i < offset) {
				result[i] = mgs[i];
			} else if (i < offset + fill.length) {
				result[i] = fill[i - offset];
			} else {
				result[i] = mgs[i - fill.length];
			}
		}
		return result;
	}

	/**
	 * change a part of array which was begin at offset and length equal fill's
	 * length if offset add fill's length > pack's length ,change from offset to
	 * pack's length
	 * 
	 * @param pack
	 * @param offset
	 * @param fill
	 * @return
	 * @return
	 */
	public static void replace(byte[] pack, int offset, final byte[] fill) {
		if (pack == null || offset > pack.length - 1 || offset < 0 || fill == null)
			return;
		int length = (fill.length + offset > pack.length) ? pack.length - offset : fill.length;
		for (int i = 0; i < length; i++) {
			pack[i + offset] = fill[i];
		}
	}

	/**
	 * return a new byte array which was made by a part of appointment array.
	 * 
	 * @param pack
	 * @param offset
	 * @param length
	 * @return A part of appointment array that started at index offset,
	 */
	public static byte[] get(final byte[] pack, final int offset, int length) {
		if (pack == null || offset > pack.length - 1 || offset < 0)
			return null;
		if (offset + length > pack.length)
			length = pack.length - offset;
		byte[] temp = new byte[length];
		for (int i = offset; i < offset + length; i++) {
			temp[i - offset] = pack[i];
		}
		return temp;
	}

	/**
	 * reserver to integer
	 * 
	 * @param pack
	 *            pack length is 4\2\1
	 * @return
	 */
	public static int getInt(final byte[] pack) {
		if (pack == null) {
			return -1;
		}
		int length = pack.length;
		if (length >= 4) {
			return (pack[3] & 0xff) << 24 | (pack[2] & 0xff) << 16 | (pack[1] & 0xff) << 8 | pack[0] & 0xff;
		} else if (length == 2) {
			return (pack[1] & 0xff) << 8 | pack[0] & 0xff;
		} else if (length == 1) {
			return pack[0] & 0xff;
		} else {
			throw new IllegalStateException("The parameter'length couldn't match to Integer");
		}
	}
	
	public static double getDouble(byte[] pack) {
		return Double.longBitsToDouble(getLong(pack));
	}

	/**
	 * read a datagram's length
	 * 
	 * @param pack
	 * @return
	 */
	public static int readLength(int offset, byte[] pack) {
		if (pack == null || offset > pack.length - 1 || offset < 0)
			return -1;
		if (pack.length >= offset + 2) {
			return (pack[offset + 1] & 0xff) << 8 | pack[offset] & 0xff;
		}
		return -1;

	}

	/**
	 * deal with cut datagram rear
	 * 
	 * @param message
	 * @return rear array
	 */
	public static byte[] cutRear(final int length, final byte[] message) {
		return get(message, 0, length);
	}

	/**
	 * 
	 * @param number
	 * @return
	 */
	public static byte[] getbyte(int number) {
		byte[] ret = { (byte) (number), (byte) (number >> 8), (byte) (number >> 16), (byte) (number >> 24) };
		return ret;
	}

	/**
	 * Print bytes format hex
	 * 
	 * @param bytes
	 */
	public static synchronized void traceHexBytes(byte[] bytes) {
		for (int i = 0, n = bytes.length; i < n; i++) {
			String s = Integer.toHexString((bytes[i] & 0xFF));
			if (s.length() == 1) {
				s = "0" + s;
			}
			System.out.print(s);
		}
		System.out.print("\tlength=" + bytes.length);
		System.out.println();
	}

	public static String getHexBytes(byte[] bytes) {
		String result = new String();
		for (int i = 0, n = bytes.length; i < n; i++) {
			String s = Integer.toHexString((bytes[i] & 0xFF));
			if (s.length() == 1) {
				s = "0" + s;
			}
			result += s;
		}
		return result;
	}

	public static byte[] flushRelease(byte[] receiveArray) {
		if (receiveArray.length > 4 && receiveArray[0] == 60 && receiveArray[1] == 112 && receiveArray[2] == 111 && receiveArray[3] == 108) {
			/* Flush policy */
			return "<?xml version=\"1.0\"?><cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"*\" /></cross-domain-policy>\0\0\0\0\0\0\0\0\0\0\0".getBytes();
		}
		return null;
	}

	public static byte[] doubleToByte(double d) throws IOException {
		double l = d;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		dos.writeDouble(l);
		byte b[] = baos.toByteArray();
		return b;
	}

	public static byte[] doubleToXiaoTouByte(double d) {
		byte[] b = new byte[8];
		long l = Double.doubleToLongBits(d);
		for (int i = 0; i < b.length; i++) {
			b[i] = new Long(l).byteValue();
			l = l >> 8;

		}
		return b;
	}

	public static long doubleBytesToLong(byte[] b) {
		double longBitsToDouble = Double.longBitsToDouble(getLong(b));
		return new Double(longBitsToDouble).longValue();
	}

	public static byte[] getCurrentTime() {
		long time = System.currentTimeMillis();
		return doubleToXiaoTouByte(new Double(time));
	}

	/**
	 * Integer or Long conversion to byte array
	 * 
	 * @param indentity
	 * @return
	 */
	public static byte[] longOrIntegerToBytes(Object indentity) {
		if (indentity instanceof Integer) {
			int tempIndentity = (Integer) indentity;
			return getbyte(tempIndentity);
		} else if (indentity instanceof Long) {
			long tempIndentity = (Long) indentity;
			byte[] ret = { (byte) (tempIndentity), (byte) (tempIndentity >> 8), (byte) (tempIndentity >> 16), (byte) (tempIndentity >> 24), (byte) (tempIndentity >> 32), (byte) (tempIndentity >> 40),
					(byte) (tempIndentity >> 48), (byte) (tempIndentity >> 56) };
			return ret;
		}
		return null;
	}

	/**
	 * byte array converion to long,length is 8/4/2/1
	 * 
	 * @param indentity
	 * @return
	 */
	public static long getLong(byte[] indentity) {
		if (indentity == null) {
			return -1;
		}
		int length = indentity.length;
		if (length == 8) {
			long[] longByte = new long[8];
			for (int i = 0; i < indentity.length; i++) {
				longByte[i] = indentity[i];
			}
			/*
			 * Data & 0xff is to clear up sign bit then to move bits
			 * operate,move bits operate result is defined by operator type
			 */
			return (longByte[7] & 0xff) << 56 | (longByte[6] & 0xff) << 48 | (longByte[5] & 0xff) << 40 | (longByte[4] & 0xff) << 32 | (longByte[3] & 0xff) << 24 | (longByte[2] & 0xff) << 16
					| (longByte[1] & 0xff) << 8 | longByte[0] & 0xff;
		}
		if (length == 4) {
			return (indentity[3] & 0xff) << 24 | (indentity[2] & 0xff) << 16 | (indentity[1] & 0xff) << 8 | indentity[0] & 0xff;
		}
		if (length == 2) {
			return (indentity[1] & 0xff) << 8 | indentity[0] & 0xff;
		}
		if (length == 1) {
			return indentity[0] & 0xff;
		}
		return -1;
	}

	public static byte[] addNoContainLength(int i, byte[] gift) {
		if (i < 0) {
			throw new IllegalArgumentException(" " + i);
		} else {
			byte[] length = DataFactory.get(DataFactory.getbyte(gift.length), 0, 2);
			gift = DataFactory.addToMiddle(gift, 0, length);
		}
		return gift;
	}

	public static Object castArray(Object o) {
		return castArray(o, null);
	}

	public static Object castArray(Object o, Class<?> componentType) {
		if (o != null) {
			Class<?> varClass = o.getClass();
			if (varClass.isArray()) {
				if (componentType == null) {
					Object[] array = (Object[]) o;
					if (array.length >= 1) {
						componentType = array[0].getClass();
					}
				}
				if (componentType != null) {
					int length = Array.getLength(o);
					Object newArray = Array.newInstance(componentType, length);
					System.arraycopy(o, 0, newArray, 0, length);
					return newArray;
				}
			}
		}
		return o;
	}

}
