package utils;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import gui.MP3Player;

public class SkinUtils{
	
	public static void changeSkin(Component component, LookAndFeel lookAndFeel) {
		try {
			UIManager.setLookAndFeel(lookAndFeel);
		} catch (UnsupportedLookAndFeelException ex) {
			Logger.getLogger(MP3Player.class.getName()).log(Level.SEVERE, null, ex);
		}
		SwingUtilities.updateComponentTreeUI(component);
	}

	public static void changeSkin(Component component, String lookAndFeel) {
		try {
			try {
				UIManager.setLookAndFeel(lookAndFeel);
			} catch (ClassNotFoundException e) {
				Logger.getLogger(SkinUtils.class.getName()).log(Level.SEVERE, null, e);
			} catch (InstantiationException e) {
				Logger.getLogger(SkinUtils.class.getName()).log(Level.SEVERE, null, e);
			} catch (IllegalAccessException e) {
				Logger.getLogger(SkinUtils.class.getName()).log(Level.SEVERE, null, e);
			}
		} catch (UnsupportedLookAndFeelException ex) {
			Logger.getLogger(MP3Player.class.getName()).log(Level.SEVERE, null, ex);
		}
		SwingUtilities.updateComponentTreeUI(component);
	}
	
}
