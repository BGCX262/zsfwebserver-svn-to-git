import java.util.ArrayList;
import java.util.List;
import java.util.Vector;



public class TestVector {
	
	public static void main(String[] args) {
		
		//List<Entry> list = new ArrayList();
		Vector<Entry> list = new Vector();
		
		for (int i = 0; i < 10; i++) {
			list.add(new Entry("name" + i, "pwd" + 1));
		}
		
		for (int i = 0; i < list.size(); i++) {
			Entry e = (Entry) list.get(i);
			String[] strArr = new String[2];
			strArr[0] = e.getName();
			strArr[1] = e.getPwd();
			System.out.println(strArr[0] + "\t" + strArr[1]);
		}
		
	}

}

class Entry {
	
	private String name;
	private String pwd;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPwd() {
		return pwd;
	}
	
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public Entry(String name, String pwd) {
		super();
		this.name = name;
		this.pwd = pwd;
	}
}
