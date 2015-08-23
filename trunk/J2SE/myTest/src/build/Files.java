package build;

import java.io.File;
import java.util.HashMap;


public class Files extends HashMap<String, SimpleFile> {

	private static final long serialVersionUID = -755415227755972968L;
	
	public void put(SimpleFile file) {
		put(file.file.getName(), file);
	}

	public void remove(SimpleFile file) {
		remove(file.file.getName());
	}
	
	public SimpleFile get(String name) {
		return super.get(name);
	}
	
	public void reset(File[] files) {
		clear();
		for (int i = 0; i < files.length; i++) {
			put(new SimpleFile(files[i]));
		}
	}
	
	public void init(File[] files) {
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			SimpleFile getter = get(file.getName());
			if (getter == null) {
				put(new SimpleFile(file));
			} else if (file.lastModified() != getter.file.lastModified() ||
					file.length() != getter.file.length()) {
				getter.change = true;
			}
		}
	}

}

class SimpleFile {
	
	public File file;
	public boolean change;
	
	public SimpleFile(File file) {
		this.file = file;
		change = true;
	}
	
}