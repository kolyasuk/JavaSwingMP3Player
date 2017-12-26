package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import utils.FileUtils;
import utils.Mp3PlayerFileFilter;
import utils.SkinUtils;

import javax.swing.JMenuBar;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;

import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

public class MP3Player extends JFrame {

	private JPanel contentPane;
	private JTextField txtSearch;
	private DefaultListModel mp3ListModel = new DefaultListModel<>();

	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MP3Player frame = new MP3Player();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		

	}


	/**
	 * Create the frame.
	 */
	public MP3Player() {
		setResizable(false);
		setTitle("SampleMP3Player");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 284, 406);
		setLocationRelativeTo(null);
		
		 JFileChooser fileChooser = new JFileChooser();
		 FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "JPG & GIF Images", "jpg", "gif");
		 fileChooser.setFileFilter(filter);
		 fileChooser.setAcceptAllFileFilterUsed(false);
		 fileChooser.setMultiSelectionEnabled(true);
		 
		 Mp3PlayerFileFilter fileFilter = new Mp3PlayerFileFilter("mp3", "");
		
		JMenuBar jMenuBar1 = new JMenuBar();
		setJMenuBar(jMenuBar1);
		
		JMenu menuFile = new JMenu("File");
		jMenuBar1.add(menuFile);
		
		JMenuItem menuOpenPlaylist = new JMenuItem("Open playlist");
		menuOpenPlaylist.setIcon(new ImageIcon(MP3Player.class.getResource("/images/open-icon.png")));
		menuFile.add(menuOpenPlaylist);
		
		JMenuItem menuSavePlaylist = new JMenuItem("Save playlist");
		menuSavePlaylist.setIcon(new ImageIcon(MP3Player.class.getResource("/images/save_16.png")));
		menuFile.add(menuSavePlaylist);
		
		JSeparator menuSeparator = new JSeparator();
		menuFile.add(menuSeparator);
		
		JMenuItem menuExit = new JMenuItem("Exit");
		menuExit.setIcon(new ImageIcon(MP3Player.class.getResource("/images/exit.png")));
		menuFile.add(menuExit);
		
		JMenu menuPrefs = new JMenu("Service");
		jMenuBar1.add(menuPrefs);
		
		JMenu menuChangeSkin = new JMenu("Change skin");
		menuChangeSkin.setIcon(new ImageIcon(MP3Player.class.getResource("/images/gear_16.png")));
		menuPrefs.add(menuChangeSkin);
		
		JMenuItem menuSkin1 = new JMenuItem("Skin 1");
		menuSkin1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SkinUtils.changeSkin(MP3Player.this, UIManager.getSystemLookAndFeelClassName());
			}
		});
		menuChangeSkin.add(menuSkin1);
		
		JMenuItem menuSkin2 = new JMenuItem("Skin 2");
		menuSkin2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SkinUtils.changeSkin(MP3Player.this, new NimbusLookAndFeel());
			}
		});
		menuChangeSkin.add(menuSkin2);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panelSearch = new JPanel();
		panelSearch.setBounds(0, 0, 279, 37);
		contentPane.add(panelSearch);
		panelSearch.setLayout(null);
		
		txtSearch = new JTextField();
		txtSearch.setToolTipText("Input song to search");
		txtSearch.setBounds(10, 8, 228, 23);
		panelSearch.add(txtSearch);
		txtSearch.setColumns(10);
		
		JButton btnSearch = new JButton("");
		btnSearch.setToolTipText("Search song");
		btnSearch.setIcon(new ImageIcon(MP3Player.class.getResource("/images/search_16.png")));
		btnSearch.setBounds(248, 8, 21, 23);
		panelSearch.add(btnSearch);
		
		JPanel panelMain = new JPanel();
		panelMain.setToolTipText("Mute");
		panelMain.setBounds(0, 47, 279, 311);
		contentPane.add(panelMain);
		panelMain.setLayout(null);
		
		JButton btnAddSong = new JButton("");
		btnAddSong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileUtils.addFileFilter(fileChooser, fileFilter);
				int result = fileChooser.showOpenDialog(MP3Player.this);
				if(result == JFileChooser.APPROVE_OPTION) {
					File[] selectedFiles = fileChooser.getSelectedFiles();
					
					for(File file:selectedFiles) {
						Mp3 mp3 = new Mp3(file.getName(), file.getPath());
						mp3ListModel.addElement(mp3);
					}
				}
			}
		});
		
		
		btnAddSong.setToolTipText("Add song");
		btnAddSong.setIcon(new ImageIcon(MP3Player.class.getResource("/images/plus_16.png")));
		btnAddSong.setBounds(10, 11, 30, 23);
		panelMain.add(btnAddSong);
		
		JScrollPane jScrollPane2 = new JScrollPane();
		jScrollPane2.setBounds(10, 46, 259, 180);
		panelMain.add(jScrollPane2);
		
		JList lstPlayList = new JList();
		lstPlayList.setModel(mp3ListModel);
		jScrollPane2.setViewportView(lstPlayList);
		
		JButton btnDeleteSong = new JButton("");
		btnDeleteSong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] indexPlayList=lstPlayList.getSelectedIndices();
				
				if(indexPlayList.length > 0) {
					ArrayList<Mp3> mp3ListForRemove = new ArrayList<>();
					for(int i=0; i<indexPlayList.length;i++) {
						Mp3 mp3 = (Mp3) mp3ListModel.getElementAt(indexPlayList[i]);
						mp3ListForRemove.add(mp3);
					}
					
					for(Mp3 mp3 : mp3ListForRemove) {
						mp3ListModel.removeElement(mp3);
					}
					
				}
			}
		});
		btnDeleteSong.setToolTipText("Delete song");
		btnDeleteSong.setIcon(new ImageIcon(MP3Player.class.getResource("/images/remove_icon.png")));
		btnDeleteSong.setBounds(50, 11, 30, 23);
		panelMain.add(btnDeleteSong);
		
		JButton btnSelectPrev = new JButton("");
		btnSelectPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int prevIndex = lstPlayList.getSelectedIndex()-1;
				if(prevIndex>=0) { //if in list size 
					lstPlayList.setSelectedIndex(prevIndex);
				}
				if(prevIndex<0) { //if on top of list - select last item
					lstPlayList.setSelectedIndex(lstPlayList.getModel().getSize()-1);
				}

			}
		});
		btnSelectPrev.setToolTipText("Select prev song");
		btnSelectPrev.setIcon(new ImageIcon(MP3Player.class.getResource("/images/arrow-up-icon.png")));
		btnSelectPrev.setBounds(239, 11, 30, 23);
		panelMain.add(btnSelectPrev);
		
		JButton btnSelectNext = new JButton("");
		btnSelectNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int nextIndex = lstPlayList.getSelectedIndex()+1;
				if(nextIndex<=lstPlayList.getModel().getSize()-1) { //if in list size 
					lstPlayList.setSelectedIndex(nextIndex);
				}
				if(nextIndex>lstPlayList.getModel().getSize()-1) { //if on end of list - select first item
					lstPlayList.setSelectedIndex(0);
				}
			}
		});
		btnSelectNext.setToolTipText("Select next song");
		btnSelectNext.setIcon(new ImageIcon(MP3Player.class.getResource("/images/arrow-down-icon.png")));
		btnSelectNext.setBounds(199, 11, 30, 23);
		panelMain.add(btnSelectNext);
		

		
		JSlider slideVolume = new JSlider();

		slideVolume.setValue(100);
		slideVolume.setBounds(42, 237, 227, 22);
		panelMain.add(slideVolume);
		
		JButton btnPrevSong = new JButton("");
		btnPrevSong.setToolTipText("Prev song");
		btnPrevSong.setIcon(new ImageIcon(MP3Player.class.getResource("/images/prev-icon.png")));
		btnPrevSong.setBounds(39, 270, 30, 30);
		panelMain.add(btnPrevSong);
		
		JButton btnPlaySong = new JButton("");
		btnPlaySong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] indexPlayList = lstPlayList.getSelectedIndices();
				
				if(indexPlayList.length>0) {
					Mp3 mp3 = (Mp3) mp3ListModel.getElementAt(indexPlayList[0]);
					System.out.println(mp3.getPath());
				}
			}
		});
		btnPlaySong.setToolTipText("Play");
		btnPlaySong.setIcon(new ImageIcon(MP3Player.class.getResource("/images/Play.png")));
		btnPlaySong.setBounds(79, 270, 30, 30);
		panelMain.add(btnPlaySong);
		
		JButton btnPauseSong = new JButton("");
		btnPauseSong.setToolTipText("Pause");
		btnPauseSong.setIcon(new ImageIcon(MP3Player.class.getResource("/images/Pause-icon.png")));
		btnPauseSong.setBounds(119, 270, 30, 30);
		panelMain.add(btnPauseSong);
		
		JButton btnStopSong = new JButton("");
		btnStopSong.setToolTipText("Stop");
		btnStopSong.setIcon(new ImageIcon(MP3Player.class.getResource("/images/stop-red-icon.png")));
		btnStopSong.setBounds(159, 270, 30, 30);
		panelMain.add(btnStopSong);
		
		JButton btnNextSong = new JButton("");
		btnNextSong.setToolTipText("Next song");
		btnNextSong.setIcon(new ImageIcon(MP3Player.class.getResource("/images/next-icon.png")));
		btnNextSong.setBounds(199, 270, 30, 30);
		panelMain.add(btnNextSong);
		
		JToggleButton tglbtnVolume = new JToggleButton("");
		tglbtnVolume.setSelectedIcon(new ImageIcon(MP3Player.class.getResource("/images/mute.png")));
		tglbtnVolume.setIcon(new ImageIcon(MP3Player.class.getResource("/images/speaker.png")));
		tglbtnVolume.setBounds(10, 237, 23, 23);
		panelMain.add(tglbtnVolume);
		
		 
	}
}
