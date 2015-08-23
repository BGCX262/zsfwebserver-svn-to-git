package testException;

public class TestException {

	public static void main(String[] args) {

		System.out.println("join;;;;;;;");
		
		Handler.hand();
		
		System.out.println("success;;;;;;;");
		
	}
	
}

class Handler {
	
	public static void hand() {
		System.out.println("Handler.hand()");
		Exc.exc();
		System.out.println("Handler.hand()_over");
	}
	
}

class Exc {
	
	public static void exc() {
		throw new RuntimeException();
	}
	
}