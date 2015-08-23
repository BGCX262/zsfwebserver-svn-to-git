
public class TestDLL {
	
	static {
		System.loadLibrary("123");
	}
	
	public native static void _临时子程序();
	
	public static void main(String[] args) {
		//System.out.println(System.getProperty("java.library.path"));
		_临时子程序();
	}

}
