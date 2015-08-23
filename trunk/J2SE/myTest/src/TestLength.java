

public class TestLength {
	
	public static void main(String[] args) {
		
		String str = Integer.MAX_VALUE + "";
		byte[] getbyte = DataFactory.getbyte(Integer.MAX_VALUE);
		DataFactory.traceHexBytes(getbyte);
		DataFactory.traceHexBytes(str.getBytes());
		System.out.println(getbyte.length);
		
		
	}

}
