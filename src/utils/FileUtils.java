package utils;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class FileUtils {
	
	//Delete old FileFilter and set new
	public static void addFileFilter(JFileChooser fileChooser, FileFilter filter) {
		fileChooser.removeChoosableFileFilter(fileChooser.getFileFilter());
		fileChooser.setFileFilter(filter);
		fileChooser.setSelectedFile(new File("")); //Delete last opened file name from file chooser
	}
	
	//Get file name without extension
	public static String getFileNameWithoutExtension(String fileName) {
		File file = new File(fileName);
		int index = file.getName().lastIndexOf('.');
		if(index > 0 && index <= file.getName().length()-2) {
			return file.getName().substring(0, index);
		}
		return "noname";
	}
	
	//Get file extension
	public static String getFileExtension(File file) {
		String ext = null;
		String s = file.getName();
		int i = s.lastIndexOf(".");
		
		if(i>0 && i<s.length()-1) {
			ext = s.substring(i+1).toLowerCase();
		}
		
		return ext;
	}
}
