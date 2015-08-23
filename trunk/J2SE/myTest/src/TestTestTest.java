

public class TestTestTest {
	
	public static void main(String[] args) {
		
		/*int begin = 40381;
		
		for (int i = begin; i < begin + 40; i++) {
			
			for (int j = 0; j < 10; j++) {
				System.out.println(i);
			}
		
		}*/
		int rate = 3;
		
		for (int i = 1; i <= 10; i++) {
			
			for (int j = 0; j < 40; j++) {
				
				System.out.print("s_r"+rate+"_b" + i);
				System.out.print("\t");
				//t_r1_b1_1
				System.out.print("t_r"+rate+"_b" + i + "_3");
				System.out.print("\t");
				System.out.print("t_r"+rate+"_b" + i + "_3");
				
				System.out.println();
				
			}
			
		}
		
	}

}
