package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import utils.FileUtils;
import utils.Mp3PlayerFileFilter;
import utils.SkinUtils;

public class MP3Player extends JFrame {

	private static final String MP3_FILE_EXTENSION = "mp3";
	private static final String MP3_FILE_DESCRIPTION = "Mp3 files";
	private static final String PLAYLIST_FILE_EXTENSION = "pls";
	private static final String PLAYLIST_FILE_DESCRIPTION = "Playlist files";
	private static final String EMPTY_STRING = "";
	private static final String INPUT_SONG_NAME = "input song name";
	private static final String BORDER = "-----------------------------------\n";

	private JPanel contentPane;
	private JTextField txtSearch;
	private DefaultListModel mp3ListModel = new DefaultListModel<>();
	private Mp3PlayerFileFilter mp3FileFilter = new Mp3PlayerFileFilter(MP3_FILE_EXTENSION, MP3_FILE_DESCRIPTION);
	private Mp3PlayerFileFilter playListFileFilter = new Mp3PlayerFileFilter(PLAYLIST_FILE_EXTENSION, PLAYLIST_FILE_DESCRIPTION);

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
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
			}
		});
		setResizable(false);
		setTitle("SampleMP3Player");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 284, 406);
		setLocationRelativeTo(null);

		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg", "gif");
		fileChooser.setFileFilter(filter);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setMultiSelectionEnabled(true);

		JMenuBar jMenuBar1 = new JMenuBar();
		setJMenuBar(jMenuBar1);

		JMenu menuFile = new JMenu("File");
		jMenuBar1.add(menuFile);

		JMenuItem menuOpenPlaylist = new JMenuItem("Open playlist");

		menuOpenPlaylist.setIcon(new ImageIcon(MP3Player.class.getResource("/images/open-icon.png")));
		menuFile.add(menuOpenPlaylist);

		JMenuItem menuSavePlaylist = new JMenuItem("Save playlist");
		menuSavePlaylist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuSavePlaylistActionListener(fileChooser, e);
			}
		});
		menuSavePlaylist.setIcon(new ImageIcon(MP3Player.class.getResource("/images/save_16.png")));
		menuFile.add(menuSavePlaylist);

		JSeparator menuSeparator = new JSeparator();
		menuFile.add(menuSeparator);

		JMenuItem menuExit = new JMenuItem("Exit");
		menuExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MP3Player.this.dispose();
			}
		});
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
		txtSearch.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtSearch.getText().equals(INPUT_SONG_NAME)) {
					txtSearch.setText(EMPTY_STRING);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (txtSearch.getText().trim().equals(EMPTY_STRING)) {
					txtSearch.setText(INPUT_SONG_NAME);
				}
			}
		});
		txtSearch.setForeground(Color.GRAY);
		txtSearch.setFont(new Font("Tahoma", Font.ITALIC, 11));
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

		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(lstPlayList, popupMenu);

		JMenuItem mntmAddSong = new JMenuItem("Add song");
		mntmAddSong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnAddSongActionListener(fileChooser);
			}
		});
		mntmAddSong.setIcon(new ImageIcon(MP3Player.class.getResource("/images/plus_16.png")));
		popupMenu.add(mntmAddSong);

		JMenuItem mntmDeleteSong = new JMenuItem("Delete song");
		mntmDeleteSong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDeleteSongActionListener(lstPlayList);
			}
		});

		mntmDeleteSong.setIcon(new ImageIcon(MP3Player.class.getResource("/images/remove_icon.png")));
		popupMenu.add(mntmDeleteSong);

		JMenuItem mntmOpenPlaylist = new JMenuItem("Open playlist");
		mntmOpenPlaylist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuOpenPlaylistActionListener(fileChooser, lstPlayList);
			}
		});
		mntmOpenPlaylist.setIcon(new ImageIcon(MP3Player.class.getResource("/images/open-icon.png")));
		popupMenu.add(mntmOpenPlaylist);

		JMenuItem mntmSavePlaylist = new JMenuItem("Save playlist");
		mntmSavePlaylist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuSavePlaylistActionListener(fileChooser, e);
			}
		});
		mntmSavePlaylist.setIcon(new ImageIcon(MP3Player.class.getResource("/images/save_16.png")));
		popupMenu.add(mntmSavePlaylist);

		JButton btnDeleteSong = new JButton("");
		btnDeleteSong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDeleteSongActionListener(lstPlayList);
			}
		});
		btnDeleteSong.setToolTipText("Delete song");
		btnDeleteSong.setIcon(new ImageIcon(MP3Player.class.getResource("/images/remove_icon.png")));
		btnDeleteSong.setBounds(50, 11, 30, 23);
		panelMain.add(btnDeleteSong);

		JButton btnSelectPrev = new JButton("");
		btnSelectPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int prevIndex = lstPlayList.getSelectedIndex() - 1;
				if (prevIndex >= 0) { // if in list size
					lstPlayList.setSelectedIndex(prevIndex);
				}
				if (prevIndex < 0) { // if on top of list - select last item
					lstPlayList.setSelectedIndex(lstPlayList.getModel().getSize() - 1);
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
				int nextIndex = lstPlayList.getSelectedIndex() + 1;
				if (nextIndex <= lstPlayList.getModel().getSize() - 1) { // if
																			// in
																			// list
																			// size
					lstPlayList.setSelectedIndex(nextIndex);
				}
				if (nextIndex > lstPlayList.getModel().getSize() - 1) { // if on
																		// end
																		// of
																		// list
																		// -
																		// select
																		// first
																		// item
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

				if (indexPlayList.length > 0) {
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

		menuOpenPlaylist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuOpenPlaylistActionListener(fileChooser, lstPlayList);
			}
		});

		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String searchStr = txtSearch.getText();
				if (searchStr == null || searchStr.equals(EMPTY_STRING))
					return;
				ArrayList<Integer> mp3FindedIndexes = new ArrayList<>();

				for (int i = 0; i < mp3ListModel.size(); i++) {
					Mp3 mp3 = (Mp3) mp3ListModel.get(i);

					if (mp3.getName().toUpperCase().contains(searchStr.toUpperCase())) {
						mp3FindedIndexes.add(i);
					}
				}

				int[] selectIndexes = new int[mp3FindedIndexes.size()];

				if (selectIndexes.length == 0) {
					JOptionPane.showMessageDialog(MP3Player.this, "Don't find \"" + searchStr + "\"");
					txtSearch.requestFocus();
					txtSearch.selectAll();
					return;
				}

				for (int i = 0; i < selectIndexes.length; i++) {
					selectIndexes[i] = mp3FindedIndexes.get(i).intValue();
				}
				lstPlayList.setSelectedIndices(selectIndexes);
			}
		});

		btnAddSong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnAddSongActionListener(fileChooser);
			}
		});
	}

	private void btnAddSongActionListener(JFileChooser fileChooser) {
		FileUtils.addFileFilter(fileChooser, mp3FileFilter);
		int result = fileChooser.showOpenDialog(MP3Player.this);
		if (result == JFileChooser.APPROVE_OPTION) {
			String duplicateSongs = "";
			File[] selectedFiles = fileChooser.getSelectedFiles();
			for (File file : selectedFiles) {
				if (FileUtils.checkSongForDoubles(file, mp3ListModel)) {
					Mp3 mp3 = new Mp3(file.getName(), file.getPath());
					mp3ListModel.addElement(mp3);
				} else {
					duplicateSongs += file.getName() + "\n";
				}
			}
			if (!duplicateSongs.equals("")) {
				JOptionPane.showMessageDialog(MP3Player.this, "Song(s): \n" + BORDER + duplicateSongs + BORDER + "are duplicated and don't added to playlist");
			}
		}
	}

	private void btnDeleteSongActionListener(JList lstPlayList) {
		int[] indexPlayList = lstPlayList.getSelectedIndices();

		if (indexPlayList.length > 0) {
			ArrayList<Mp3> mp3ListForRemove = new ArrayList<>();
			for (int i = 0; i < indexPlayList.length; i++) {
				if (mp3ListModel.getElementAt(indexPlayList[i]) instanceof Mp3) {
					Mp3 mp3 = (Mp3) mp3ListModel.getElementAt(indexPlayList[i]);
					mp3ListForRemove.add(mp3);
				}
			}

			for (Mp3 mp3 : mp3ListForRemove) {
				mp3ListModel.removeElement(mp3);
			}

		}
	}

	private void menuOpenPlaylistActionListener(JFileChooser fileChooser, JList lstPlayList) {
		FileUtils.addFileFilter(fileChooser, playListFileFilter);
		int result = fileChooser.showOpenDialog(MP3Player.this);

		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedPlayList = fileChooser.getSelectedFile();
			DefaultListModel mp3ListModel = (DefaultListModel) FileUtils.deserialise(selectedPlayList.getPath());
			MP3Player.this.mp3ListModel = mp3ListModel;
			lstPlayList.setModel(mp3ListModel);
		}
	}

	private void menuSavePlaylistActionListener(JFileChooser fileChooser, ActionEvent e) {
		FileUtils.addFileFilter(fileChooser, playListFileFilter);
		int result = fileChooser.showSaveDialog(MP3Player.this);

		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			if (FileUtils.checkSavedFileForDuplicate(fileChooser, selectedFile) || selectedFile.exists()) { // if
																											// file
																											// exists
				int resultOverwrite = JOptionPane.showConfirmDialog(MP3Player.this, "File already exist", "Overwrite?", JOptionPane.YES_NO_CANCEL_OPTION);

				switch (resultOverwrite) {
				case JOptionPane.NO_OPTION: // if don't rewrite file open file
											// chooser again
					menuSavePlaylistActionListener(fileChooser, e);
					return;
				case JOptionPane.CANCEL_OPTION: // if close pane do nothing
					fileChooser.cancelSelection();
					return;
				}
				fileChooser.approveSelection();
			}

			String fileExtension = FileUtils.getFileExtension(selectedFile);

			String fileNameForSave = (fileExtension != null && fileExtension.equals(PLAYLIST_FILE_EXTENSION)) ? selectedFile.getPath() : selectedFile.getPath() + "." + PLAYLIST_FILE_EXTENSION;
			FileUtils.serialize(mp3ListModel, fileNameForSave);
		}
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});

	}
}
