import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.InflaterOutputStream;
import java.util.zip.ZipInputStream;


public class Decompress {
	public static byte[]arrayConcat(byte[] a1, byte[] a2){
		byte[] b = new byte[a1.length + a2.length];
		for(int i = 0; i < a1.length + a2.length; i++){
			if(i < a1.length)
				b[i] = a1[i];
			else
				b[i] = a2[i - a1.length];
		}
		return b;
	}
	
	public static byte[] getBytes(byte[] data, int offset, int len){
		return Arrays.copyOfRange(data, offset, offset + len);
	}
	
	public static byte[] simpleDecrypt(byte[] data){
		System.out.println("data.size:" + data.length);
		int size = data.length / 4;
		byte[] c = getBytes(data, 0, size);
		byte[] b = getBytes(data, size, size);
		byte[] d = getBytes(data, 2 * size, data.length - 3 * size);
		byte[] a = getBytes(data, data.length - size, size);
		byte[] re = arrayConcat(a, b);
		re = arrayConcat(re, c);
		re = arrayConcat(re, d);
		return re;
	}
	
	public static byte[] decompress(byte[] data) throws IOException{
		System.out.println("data.size:" + data.length);
		byte[] temp = new byte[65536 * 8];
		Inflater  decompresser = new Inflater (false);
		decompresser.setInput(data);
		int n = 0;
		try {
			n = decompresser.inflate(temp);
			decompresser.end();
		} catch (DataFormatException e) {
			e.printStackTrace();
		}
		return Arrays.copyOf(temp, n);
	}
	
	public static byte[] unZip(byte[] data) {
		byte[] b = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			ZipInputStream zip = new ZipInputStream(bis);
			while (zip.getNextEntry() != null) {
				byte[] buf = new byte[1024];
				int num = -1;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				while ((num = zip.read(buf, 0, buf.length)) != -1) {
					baos.write(buf, 0, num);
				}
				b = baos.toByteArray();
				baos.flush();
				baos.close();
			}
			zip.close();
			bis.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return b;
	}
	

	public static byte[] decompressData(byte[] data) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			InflaterOutputStream zos = new InflaterOutputStream(bos);
			zos.write(data);
			zos.close();
			return bos.toByteArray();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		try {
			FileInputStream fi = new FileInputStream(new File(args[0]/*"d:\\f1_efc5.jpg"*/
					/*"d:\\f1_5228.jpg"*/));
			byte[] b =  new byte[fi.available()];
			fi.read(b);
			String str = new String(decompress(simpleDecrypt(b)), "UTF-8");
			System.out.println(str);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
