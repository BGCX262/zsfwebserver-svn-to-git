import java.util.EnumMap;



public class TestEnumMap {
	
	public static void main(String[] args) {
		
		EnumMap<Chars, String> enums = new EnumMap<Chars, String>(Chars.class);
		
		enums.put(Chars.A, "123");
		enums.put(Chars.B, "456");
		enums.put(Chars.C, "789");
		
		System.out.println(enums.get(Chars.A));
		
	}

}

enum Chars {
	A, B, C
}