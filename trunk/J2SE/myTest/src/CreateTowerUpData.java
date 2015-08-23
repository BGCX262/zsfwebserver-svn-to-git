import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;



public class CreateTowerUpData {
	
	public static void main(String[] args) {
		//createBPBean();
		createBPPBean();
		//createTowerBean();
	}
	
	public static void createTowerBean() {
		int baseID = 40000;
		int sceneLevel = 30;
		int upLevel = 14;

		String[] names = new String[] {
				"箭塔", "飞弹", "大炮", "爆火", "塔王", "飞矛", "弹伤", "碎石", "重弹",
				"塔圣", "法伤", "奥弹", "蓝魔", "魔爆", "塔魂"
		};
		String[] types = new String[] {
				"n", "c", "m"
		};
		StringBuilder sb = new StringBuilder();
		int start = 0001;
		int limit = names.length * sceneLevel * upLevel;
		for (int i = start; i < start + limit; i++) {
			int baseLevel = ((i - 1) / sceneLevel / (names.length - 1)) + 1;
			int sLevel = (i - 1) % sceneLevel + 1;
			int level = (i - 1) / sceneLevel % upLevel + 1;
			int towerAttType = (i - 1) / (sceneLevel * upLevel * (names.length / types.length));
			
			sb.append("insert into towerbean values(");
			sb.append("\""+(i)+"\",");
			System.out.print("\t");
			
			sb.append("\""+(baseID + ((i - 1) / sceneLevel) + 1)+"\",");
			System.out.print("\t");

			sb.append("\""+(4)+"\",");
			System.out.print("\t");

			sb.append("\""+(sLevel)+"\",");
			System.out.print("\t");

			sb.append("\""+(level)+"\",");
			System.out.print("\t");

			sb.append("\""+(names[(i - 1) / sceneLevel / (names.length - 1)])+"\",");
			System.out.print("\t");

			sb.append("\""+(baseLevel)+"\",");
			System.out.print("\t");

			sb.append("\""+(1)+"\",");
			System.out.print("\t");

			sb.append("\""+("s_r1_b" + (baseLevel))+"\",");
			System.out.print("\t");

			sb.append("\""+("t_" + types[towerAttType] + "_" + ((((i - 1) / sceneLevel / (names.length - 1))) % (names.length / types.length) + 1))+"\",");
			System.out.print("\t");

			sb.append("\""+("t_" + types[towerAttType] + "_" + ((((i - 1) / sceneLevel / (names.length - 1))) % (names.length / types.length) + 1))+"\",");
			System.out.print("\t");

			sb.append("\""+(0)+"\",");
			System.out.print("\t");

			/* atk */
			sb.append("\""+(100)+"\",");
			System.out.print("\t");

			/* asd */
			sb.append("\""+(1)+"\",");
			System.out.print("\t");

			/* ar */
			sb.append("\""+(90)+"\",");
			System.out.print("\t");

			/* towerType */
			sb.append("\""+(3)+"\",");
			System.out.print("\t");

			/* attackNum */
			sb.append("\""+(1)+"\",");
			System.out.print("\t");

			/* shellType */
			sb.append("\""+(3)+"\",");
			System.out.print("\t");

			/* duration */
			sb.append("\""+(3)+"\",");
			System.out.print("\t");

			/* buildNeedRock */
			sb.append("\""+(sLevel == 1 ? 100 : 0)+"\",");
			System.out.print("\t");

			/* buildNeedMetal */
			sb.append("\""+(sLevel == 1 ? 100 : 0)+"\",");
			System.out.print("\t");

			/* buildNeedCrystal */
			sb.append("\""+(sLevel == 1 ? 100 : 0)+"\",");
			System.out.print("\t");

			/* buildNeedCoin */
			sb.append("\""+(sLevel == 1 ? 100 : 0)+"\",");
			System.out.print("\t");

			/* upIndexID */
			sb.append("\""+(sLevel == sceneLevel ? i : i + 1)+"\",");
			System.out.print("\t");

			/* upTime */
			sb.append("\""+(33000)+"\",");
			System.out.print("\t");

			/* upNeedRock */
			sb.append("\""+(30)+"\",");
			System.out.print("\t");

			/* upNeedMetal */
			sb.append("\""+(30)+"\",");
			System.out.print("\t");

			/* upNeedCrystal */
			sb.append("\""+(30)+"\",");
			System.out.print("\t");

			/* upNeedFriend */
			sb.append("\""+(0)+"\",");
			System.out.print("\t");

			/* buildTime */
			sb.append("\""+(25000)+"\",");
			System.out.print("\t");

			/* rate */
			sb.append("\""+(0)+"\",");
			System.out.print("\t");

			/* effect */
			sb.append("\""+(0)+"\",");
			System.out.print("\t");

			/* comment */
			sb.append("\""+("comment")+"\",");
			System.out.print("\t");

			/* towerAttType */
			sb.append("\""+(towerAttType + 1)+"\");\n");
			System.out.print("\t");
			
			System.out.println();
		}
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("d:/1.sql"));
			bw.write(sb.toString());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void createBPPBean() {

		String[] names = new String[] {
				"箭塔", "飞弹", "大炮", "爆火", "塔王", "飞矛", "弹伤", "碎石", "重弹",
				"塔圣", "法伤", "奥弹", "蓝魔", "魔爆", "塔魂"
		};
		int baseID = 20000;
		
		for (int i = 1; i <= 60; i++) {
			System.out.print(i);
			System.out.print("\t");
			
			System.out.print(baseID + i);
			System.out.print("\t");

			System.out.print(2);
			System.out.print("\t");

			System.out.print(1);
			System.out.print("\t");

			System.out.print(names[((i - 1) / 4)] + "" + (((i - 1) % 4) + 1));
			System.out.print("\t");

			System.out.print(((i - 1) / 4) + 1);
			System.out.print("\t");

			System.out.print(((i - 1) % 4) + 1);
			System.out.print("\t");

			System.out.print(1);
			System.out.print("\t");

			System.out.print(((i - 1) / 4) * 14 + 30001);
			System.out.print("\t");

			System.out.print("p_r1_b" + (((i - 1) / 4) + 1) + "_" + (((i - 1) % 4) + 1));
			System.out.print("\t");

			System.out.print("comment");
			System.out.print("\t");
			
			
			System.out.println();
		}
	}
	
	public static void createBPBean() {

		String[] names = new String[] {
				"箭塔", "飞弹", "大炮", "爆火", "塔王", "飞矛", "弹伤", "碎石", "重弹",
				"塔圣", "法伤", "奥弹", "蓝魔", "魔爆", "塔魂"
		};
		double[] baseRate = new double[] {
				1, 1, 1, 1, 0.3, 0.2, 0.1, 0.05, 0.001, 0.1, 0.1, 0.1, 0.1, 0.1
		};
		double[] extraRate = new double[] {
				1, 1, 1, 1, 0.5, 0.3, 0.15, 0.15, 0.1, 0.09, 0.09, 0.09, 0.09, 0.09
		};
		int[] bId = new int[] {
				10001, 10001, 10001, 10001, 10001, 10001, 10001, 10001, 10001, 100036, 100037, 100038, 100039, 100039
		};
		int[] bnums = new int[] {
				1, 1, 2, 2, 3, 3, 4, 4, 5, 1, 1, 1, 1, 1
		};
		int[] nums = new int[] {
				0, 0, 0, 0, 1, 1, 2, 2, 3, 10, 10, 10, 10, 10
		};
		int baseID = 30000;
		
		for (int i = 1; i <= 210; i++) {
			int level = ((i - 1) % 14) + 1;
			System.out.print(i);
			System.out.print("\t");
			
			System.out.print(baseID + i);
			System.out.print("\t");
			
			System.out.print(3);
			System.out.print("\t");
			
			System.out.print(names[((i - 1) / 14)]);
			System.out.print("\t");
			
			System.out.print(level);
			System.out.print("\t");
			
			System.out.print(((i - 1) / 14) + 1);
			System.out.print("\t");
			
			System.out.print("m_r1_b" + (((i - 1) / 14) + 1));
			System.out.print("\t");
			
			System.out.print(1);
			System.out.print("\t");
			
			System.out.print(40000 + i);
			System.out.print("\t");
			
			System.out.print(baseRate[(i - 1) % 14]);
			System.out.print("\t");
			
			System.out.print(extraRate[(i - 1) % 14]);
			System.out.print("\t");
			
			System.out.print("comment");
			System.out.print("\t");
			
			System.out.print(level <= 5 || level >= 10 ? (baseID + i) : (baseID + i) - level + 5);
			System.out.print("\t");
			
			/* 升级成功ID */
			System.out.print(i % 14 == 0 ? baseID + i : baseID + i + 1);
			System.out.print("\t");
			
			System.out.print(bId[(i - 1) % 14]);
			System.out.print("\t");
			
			System.out.print(bnums[(i - 1) % 14]);
			System.out.print("\t");
			
			System.out.print(nums[(i - 1) % 14]);
			System.out.print("\t");
			
			System.out.print(level < 5 ? 0 : 10002 + ((i - 1) / 14) / 5);
			System.out.print("\t");
			
			System.out.println();
		}
	
	}

}
