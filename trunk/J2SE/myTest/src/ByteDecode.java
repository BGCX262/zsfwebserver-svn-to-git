

public class ByteDecode {
	
	public static void main(String[] args) {
		String str = "25000000280000000000000000000000444004000000347500000100000000008055c011ffffffc80000000000000001000000119802000000d362362473420100000000000056c013ffffffc80000000000000001000000119802000000d362362473420100000000004056c0cdffffffc80000000000000001000000119802000000d362362473420100000000008056c0d8ffffffc80000000000000001000000119802000000d36236247342";
		
		byte[] b = new byte[str.length() / 2];
		for (int i = 0; i < str.length() / 2; i++) {
			b[i] = (byte)Integer.parseInt(new String(new char[] {str.charAt(i * 2), str.charAt(i * 2 + 1)}), 16);
		}
		
		DataFactory.traceHexBytes(b);
		ProtocolParserUtil p = ProtocolParserUtil.getInstance(b);
		
		System.out.println(p.readByte(10));
		int count = p.readInt(18);
		p.readInt();
		System.out.println(count);
		for (int i = 0; i < count; i++) {
			System.out.println("---------------------------------");
			System.out.println(p.readByte());
			System.out.println(p.readLong());
			System.out.println(p.readInt());
			System.out.println(p.readInt());
			System.out.println(p.readInt());
			int c = p.readInt();
			System.out.println(c);
			for (int j = 0; j < c; j++) {
				System.out.println(p.readInt());
				System.out.println(p.readDouble());
			}
			System.out.println("---------------------------------");
		}
		
	}

}
