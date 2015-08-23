import java.util.Observable;
import java.util.Observer;


/**
 * 测试捕捉修改变量事件
 * @author zsf
 */
public class TestDynamicModify implements Observer {

	public static void main(String[] args) {
		DynamicBean bean = new DynamicBean();
		bean.addObserver(new TestDynamicModify());
		bean.setName("123");
		bean.setName("abc");
	}

	public void update(Observable o, Object arg) {
		if (arg.toString().indexOf("=") != -1) {
			String[] split = arg.toString().split("=");
			arg = split[0] + "变量值修改为：" + split[1];
		}
		System.out.println(o.getClass().getName() + "类中的" + arg);
	}

}

/**
 * 测试bean
 * @author zsf
 */
class DynamicBean extends Observable {
	
	private String name = null;

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
		setChanged();
		notifyObservers("name=" + name);
	}
	
}