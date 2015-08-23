import java.util.Random;


public class TestRandom {
	
	public static void main(String[] args) {
		int[] c = new int[5];
		for (int i = 0; i < 100; i++) {
			c[new Random().nextInt(1)]++;
		}
		
		for (int i : c) {
			System.out.println(i);
		}
	}

}
