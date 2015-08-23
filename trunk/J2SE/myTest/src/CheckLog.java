import java.io.BufferedReader;
import java.io.FileReader;


public class CheckLog {
	
	public static void main(String[] args) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\D-io\\Desktop\\wrapper_20100806.log"));
		
		String str;
		double pou = 0.0d;
		double[] rs = new double[10];
		double eror = 0.0d;
		while ((str = reader.readLine()) != null) {
			if (str.indexOf("玩家扣除了") != -1) {
				int begin = str.indexOf("玩家扣除了") + 6;
				int end = str.indexOf(" 手续费");
				pou = Double.valueOf(str.substring(begin, end)).doubleValue();
				
				//System.out.println(pou);
			}

			if (str.indexOf("游戏结束，游戏结果：") != -1) {
				int i = 0;
				while ((str = reader.readLine()).indexOf("]: €") != -1) {
					int begin = str.indexOf("€") + 1;
					rs[i] = Double.valueOf(str.substring(begin)).doubleValue();
					i++;
				}
				
				double sum = 0.0d;
				for (int j = 0; j < rs.length; j++) {
					if (rs[j] != 0) {
						//System.out.print(rs[j] + " + ");
						sum += rs[j];
					}
				}
				
				sum = DoubleFormat.getResult(sum);
				if (sum != 0 && pou != 0) {
					//System.out.println(DoubleFormat.getResult(sum + pou));
					System.out.println(sum + " - - - - - - - - " + pou);
					sum = DoubleFormat.getResult(sum + pou);
					
					if (sum != 0) {
						eror = DoubleFormat.getResult(eror + sum);
						System.out.println(str + " ---------------------- " + sum);
					}
				}
				
				//System.out.println(" = " + DoubleFormat.getResult(sum));
				
				rs = new double[10];
			}
		}
		System.out.println(eror);
		
		reader.close();
	}

}
