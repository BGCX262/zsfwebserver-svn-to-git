import javax.swing.filechooser.FileSystemView;


public class TestSystem {

	public static void main(String[] args) throws InterruptedException {
		FileSystemView fsv = FileSystemView.getFileSystemView();

		System.out.println(fsv.getHomeDirectory().getAbsolutePath());
	}

}
