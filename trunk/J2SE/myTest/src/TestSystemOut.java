import java.io.OutputStream;
import java.io.PrintStream;


public class TestSystemOut {
	
	public final int num = 2;
	
	public static void main(String[] args) throws Exception {
		
		/*TestSystemOut out = new TestSystemOut();
		
		Field field = TestSystemOut.class.getDeclaredField("num");
		Field field2 = field.getClass().getDeclaredField("modifiers");
		field2.setAccessible(true);
		field2.setInt(field, 2);

		field.setAccessible(true);
		field.setInt(out, 100);

		System.out.println("java编译出来的值：" + out.num);
		System.out.println("修改后的值：" + field.get(out) + "，字段名" + field.getName());*/
		
		System.out.println(args[0]);
		
		System.setOut(new MyPrintStream(System.out));
		
		System.out.println("123");
		System.out.println("123");
		System.out.println("123");
		
		System.out.println(System.out);
		
	}

}

class MyPrintStream extends PrintStream {
	
	private StringBuffer sb = null;

	public MyPrintStream(OutputStream out) {
		super(out);
		
		sb = new StringBuffer();
	}

	@Override
	public void println(String x) {
		sb.append(x);
		sb.append("\r\n");
		super.println(x);
	}
	
	public String toString() {
		String rtnVal = sb.toString();
		sb.delete(0, sb.length());
		return rtnVal;
	}
	
}