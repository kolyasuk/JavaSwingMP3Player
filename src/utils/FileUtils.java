package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class FileUtils {

	// Delete old FileFilter and set new
	public static void addFileFilter(JFileChooser fileChooser, FileFilter filter) {
		fileChooser.removeChoosableFileFilter(fileChooser.getFileFilter());
		fileChooser.setFileFilter(filter);
		fileChooser.setSelectedFile(new File("")); // Delete last opened file
													// name from file chooser
	}

	// Get file name without extension
	public static String getFileNameWithoutExtension(String fileName) {
		File file = new File(fileName);
		int index = file.getName().lastIndexOf('.');
		if (index > 0 && index <= file.getName().length() - 2) {
			return file.getName().substring(0, index);
		}
		return "noname";
	}

	// Get file extension
	public static String getFileExtension(File file) {
		String ext = null;
		String s = file.getName();
		int i = s.lastIndexOf(".");

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}

		return ext;
	}

	public static void serialize(Object obj, String fileName) {
		try {
			FileOutputStream fow = new FileOutputStream(fileName);
			ObjectOutputStream oos = new ObjectOutputStream(fow);
			oos.writeObject(obj);
			oos.flush();
			oos.close();
			fow.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Object deserialise(String fileName) {
		try {
			FileInputStream fis = new FileInputStream(fileName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object obj = (Object) ois.readObject();
			fis.close();
			return obj;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean checkSongForDoubles(File file, DefaultListModel mp3ListModel) {
		boolean a = true;
		for (int i = 0; i < mp3ListModel.size(); i++) {
			if (getFileNameWithoutExtension(file.getName()).equals(mp3ListModel.get(i).toString())) {
				a = false;
				break;
			}
		}
		return a;
	}

	public static boolean checkSavedFileForDuplicate(JFileChooser fileChooser, File file) {
		String[] lst = fileChooser.getCurrentDirectory().list(); // Get list of
																	// files in
																	// directory
		String fileDescription = fileChooser.getFileFilter().getDescription(); // Get
																				// file
																				// description
		String fileExtension = fileDescription.substring(fileDescription.length() - 5, fileDescription.length() - 1); // Get
																														// file
																														// extension
		for (int i = 0; i < lst.length; i++) { // Check for duplicate
			if ((file.getName() + fileExtension).equals(lst[i])) {
				return true;
			}
		}
		return false;
	}
}
