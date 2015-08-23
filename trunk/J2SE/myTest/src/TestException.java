public class TestException {

	public static void main(String[] args) {
		run();
	}
	
	public static void run() throws MyException {
		try {
			throw new MyException("≤‚ ‘");
		} catch (MyException e) {
			e.printStackTrace();
			e.printStackTrace(System.err);
			
			for (StackTraceElement ste : e.getStackTrace()) {
				System.out.println(ste.getLineNumber());
			}
			throw (MyException) e.fillInStackTrace();
		}
	}
	
}

class MyException extends RuntimeException {
	private static final long serialVersionUID = 5304364383804831585L;

	public MyException() {
	}

	public MyException(String msg) {
		super(msg);
	}
}