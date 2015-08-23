
public class TestDoubleString {

	public static void main(String[] args) {
		System.out.println(System.getenv());

		float a = 1f;
		int b = 4007;
		int c = 3;
		System.out.println(String.format("1: {%1$s, %3$s, %2$s}", new Object[] { b, a, c }));
		
	}

}
