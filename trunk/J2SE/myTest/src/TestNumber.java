

public class TestNumber {
	
	public static int getFatiguedNum(int num, int state) {
		double flag = state == 1 ? 0.5 : 1;
		flag = state == 2 ? 0 : flag;
		return (num == 1 && flag == 0.5) ? 1 : (int) (num * flag);
	}

	public static void main(String[] args) {
		System.out.println(getFatiguedNum(1, 1));
		
	}

}
