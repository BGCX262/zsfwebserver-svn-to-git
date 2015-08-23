
public class TestEnum {

	public static void main(String[] args) {
		for (MyEnum e : MyEnum.values()) {
			System.out.println(e + ": " + e.getValue());
			
		}
		
		System.out.println(MyEnum.FIRST.getValue());
	}
	
}

enum MyEnum {
	FIRST("the first"),
	SECOND("the second");
	
	private String value = null;
	
	private MyEnum(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
