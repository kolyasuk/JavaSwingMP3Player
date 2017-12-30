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
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import objects.Mp3;
import objects.Mp3Player;
import utils.FileUtils;
import utils.Mp3PlayerFileFilter;
import utils.SkinUtils;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MP3PlayerGUI extends JFrame implements BasicPlayerListener {

	private static final String MP3_FILE_EXTENSION = "mp3";
	private static final String MP3_FILE_DESCRIPTION = "Mp3 files";
	private static final String PLAYLIST_FILE_EXTENSION = "pls";
	private static final String PLAYLIST_FILE_DESCRIPTION = "Playlist files";
	private static final String EMPTY_STRING = "";
	private static final String INPUT_SONG_NAME = "input song name";
	private static final String BORDER = "--------------------------------------------------\n";

	private JPanel contentPane;
	private JTextField txtSearch;
	private DefaultListModel mp3ListModel = new DefaultListModel<>();
	private Mp3PlayerFileFilter mp3FileFilter = new Mp3PlayerFileFilter(MP3_FILE_EXTENSION, MP3_FILE_DESCRIPTION);
	private Mp3PlayerFileFilter playListFileFilter = new Mp3PlayerFileFilter(PLAYLIST_FILE_EXTENSION, PLAYLIST_FILE_DESCRIPTION);
	private Mp3Player player = new Mp3Player(this);
	private JTextField txtVolumeStatus;
	private JList lstPlayList;
	private JSlider slideVolume;
	private JLabel lblPlayingSong;
	private JSlider sliderProgress;
	private int sliderValue;
	private String currentSong;
	private int[] indexPlayList;
	private long duration;
	private int bytesLen;
	private long secondsAmount;
	private double posValue = 0.0;
    private boolean movingFromJump = false;
    private boolean moveAutomatic = false;
	
	/**
	 * Launch the application.
	 */

	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MP3PlayerGUI frame = new MP3PlayerGUI();
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
	public MP3PlayerGUI() {
		setResizable(false);
		setTitle("SampleMP3Player");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 284, 392);
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

		menuOpenPlaylist.setIcon(new ImageIcon(MP3PlayerGUI.class.getResource("/images/open-icon.png")));
		menuFile.add(menuOpenPlaylist);

		JMenuItem menuSavePlaylist = new JMenuItem("Save playlist");
		
		menuSavePlaylist.setIcon(new ImageIcon(MP3PlayerGUI.class.getResource("/images/save_16.png")));
		menuFile.add(menuSavePlaylist);

		JSeparator menuSeparator = new JSeparator();
		menuFile.add(menuSeparator);

		JMenuItem menuExit = new JMenuItem("Exit");
		menuExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menuExit.setIcon(new ImageIcon(MP3PlayerGUI.class.getResource("/images/exit.png")));
		menuFile.add(menuExit);

		JMenu menuPrefs = new JMenu("Service");
		jMenuBar1.add(menuPrefs);

		JMenu menuChangeSkin = new JMenu("Change skin");
		menuChangeSkin.setIcon(new ImageIcon(MP3PlayerGUI.class.getResource("/images/gear_16.png")));
		menuPrefs.add(menuChangeSkin);

		JMenuItem menuSkin1 = new JMenuItem("Skin 1");
		menuSkin1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SkinUtils.changeSkin(MP3PlayerGUI.this, UIManager.getSystemLookAndFeelClassName());
			}
		});
		menuChangeSkin.add(menuSkin1);

		JMenuItem menuSkin2 = new JMenuItem("Skin 2");
		menuSkin2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SkinUtils.changeSkin(MP3PlayerGUI.this, new NimbusLookAndFeel());
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

		txtSearch.setForeground(Color.GRAY);
		txtSearch.setFont(new Font("Tahoma", Font.ITALIC, 11));
		txtSearch.setToolTipText("Input song to search");
		txtSearch.setBounds(10, 8, 228, 23);
		panelSearch.add(txtSearch);
		txtSearch.setColumns(10);

		JButton btnSearch = new JButton("");

		btnSearch.setToolTipText("Search song");
		btnSearch.setIcon(new ImageIcon(MP3PlayerGUI.class.getResource("/images/search_16.png")));
		btnSearch.setBounds(248, 8, 21, 23);
		panelSearch.add(btnSearch);

		JPanel panelMain = new JPanel();
		panelMain.setToolTipText("Mute");
		panelMain.setBounds(0, 47, 279, 299);
		contentPane.add(panelMain);
		panelMain.setLayout(null);

		JButton btnAddSong = new JButton("");

		btnAddSong.setToolTipText("Add song");
		btnAddSong.setIcon(new ImageIcon(MP3PlayerGUI.class.getResource("/images/plus_16.png")));
		btnAddSong.setBounds(10, 9, 30, 23);
		panelMain.add(btnAddSong);

		JScrollPane jScrollPane2 = new JScrollPane();
		jScrollPane2.setBounds(10, 46, 259, 180);
		panelMain.add(jScrollPane2);

		lstPlayList = new JList();

		
		lstPlayList.setModel(mp3ListModel);
		jScrollPane2.setViewportView(lstPlayList);

		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(lstPlayList, popupMenu);

		JMenuItem mntmAddSong = new JMenuItem("Add song");

		mntmAddSong.setIcon(new ImageIcon(MP3PlayerGUI.class.getResource("/images/plus_16.png")));
		popupMenu.add(mntmAddSong);

		JMenuItem mntmDeleteSong = new JMenuItem("Delete song");


		mntmDeleteSong.setIcon(new ImageIcon(MP3PlayerGUI.class.getResource("/images/remove_icon.png")));
		popupMenu.add(mntmDeleteSong);

		JMenuItem mntmOpenPlaylist = new JMenuItem("Open playlist");

		mntmOpenPlaylist.setIcon(new ImageIcon(MP3PlayerGUI.class.getResource("/images/open-icon.png")));
		popupMenu.add(mntmOpenPlaylist);

		JMenuItem mntmClearPlaylist = new JMenuItem("Clear playlist");

		mntmClearPlaylist.setIcon(new ImageIcon(MP3PlayerGUI.class.getResource("/images/exit.png")));
		popupMenu.add(mntmClearPlaylist);
		
		JMenuItem mntmPlay = new JMenuItem("Play");

		mntmPlay.setIcon(new ImageIcon(MP3PlayerGUI.class.getResource("/images/Play.png")));
		popupMenu.add(mntmPlay);
		
		JMenuItem mntmPause = new JMenuItem("Pause");

		mntmPause.setIcon(new ImageIcon(MP3PlayerGUI.class.getResource("/images/Pause-icon.png")));
		popupMenu.add(mntmPause);
		
		JMenuItem mntmStop = new JMenuItem("Stop");

		mntmStop.setIcon(new ImageIcon(MP3PlayerGUI.class.getResource("/images/stop-red-icon.png")));
		popupMenu.add(mntmStop);

		JButton btnDeleteSong = new JButton("");
		btnDeleteSong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDeleteSongActionPerformer(lstPlayList);
			}
		});
		btnDeleteSong.setToolTipText("Delete song");
		btnDeleteSong.setIcon(new ImageIcon(MP3PlayerGUI.class.getResource("/images/remove_icon.png")));
		btnDeleteSong.setBounds(50, 9, 30, 23);
		panelMain.add(btnDeleteSong);

		JButton btnSelectPrev = new JButton("");

		btnSelectPrev.setToolTipText("Select prev song");
		btnSelectPrev.setIcon(new ImageIcon(MP3PlayerGUI.class.getResource("/images/arrow-up-icon.png")));
		btnSelectPrev.setBounds(239, 9, 30, 23);
		panelMain.add(btnSelectPrev);

		JButton btnSelectNext = new JButton("");

		btnSelectNext.setToolTipText("Select next song");
		btnSelectNext.setIcon(new ImageIcon(MP3PlayerGUI.class.getResource("/images/arrow-down-icon.png")));
		btnSelectNext.setBounds(199, 9, 30, 23);
		panelMain.add(btnSelectNext);

		slideVolume = new JSlider();
		slideVolume.setVisible(false);
		slideVolume.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				slideVolume.setVisible(false);
				txtVolumeStatus.setVisible(false);
			}
		});

		txtVolumeStatus = new JTextField();
		txtVolumeStatus.setVisible(false);
		txtVolumeStatus.setHorizontalAlignment(SwingConstants.CENTER);
		txtVolumeStatus.setBounds(145, 260, 30, 23);
		txtVolumeStatus.setText(slideVolume.getValue()+"");
		panelMain.add(txtVolumeStatus);
		txtVolumeStatus.setColumns(3);
		slideVolume.setPaintLabels(true);
		slideVolume.setAlignmentY(Component.TOP_ALIGNMENT);
		slideVolume.setBorder(new LineBorder(new Color(0, 102, 255), 2, true));
		slideVolume.setSnapToTicks(true);
		slideVolume.setMinorTickSpacing(5);
		slideVolume.setBounds(171, 260, 98, 23);
		panelMain.add(slideVolume);
		sliderValue = slideVolume.getValue();

		JButton btnPrevSong = new JButton("");
		btnPrevSong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSelectPrevSongActionPerformer(lstPlayList);
				btnPlaySongActionPerformer(lstPlayList, slideVolume);
			}
		});
		btnPrevSong.setToolTipText("Prev song");
		btnPrevSong.setIcon(new ImageIcon(MP3PlayerGUI.class.getResource("/images/prev-icon.png")));
		btnPrevSong.setBounds(88, 256, 30, 30);
		panelMain.add(btnPrevSong);

		JButton btnStopSong = new JButton("");

		btnStopSong.setToolTipText("Stop");
		btnStopSong.setIcon(new ImageIcon(MP3PlayerGUI.class.getResource("/images/stop-red-icon.png")));
		btnStopSong.setBounds(57, 260, 23, 23);
		panelMain.add(btnStopSong);

		JButton btnNextSong = new JButton("");
		btnNextSong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSelectNextSongActionPerformer(lstPlayList);
				btnPlaySongActionPerformer(lstPlayList, slideVolume);
			}
		});
		btnNextSong.setToolTipText("Next song");
		btnNextSong.setIcon(new ImageIcon(MP3PlayerGUI.class.getResource("/images/next-icon.png")));
		btnNextSong.setBounds(155, 256, 30, 30);
		panelMain.add(btnNextSong);

		JToggleButton tglbtnVolume = new JToggleButton("");

		tglbtnVolume.setSelectedIcon(new ImageIcon(MP3PlayerGUI.class.getResource("/images/mute.png")));
		tglbtnVolume.setIcon(new ImageIcon(MP3PlayerGUI.class.getResource("/images/speaker.png")));
		tglbtnVolume.setBounds(195, 260, 23, 23);
		panelMain.add(tglbtnVolume);

		JToggleButton togglePlayPauseButton = new JToggleButton("");

		togglePlayPauseButton.setSelectedIcon(new ImageIcon(MP3PlayerGUI.class.getResource("/images/Pause-icon.png")));
		togglePlayPauseButton.setIcon(new ImageIcon(MP3PlayerGUI.class.getResource("/images/Play.png")));
		togglePlayPauseButton.setBounds(122, 256, 30, 30);
		panelMain.add(togglePlayPauseButton);

		JLabel lblShowVolume = new JLabel("+");
		lblShowVolume.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				slideVolume.setVisible(true);
				txtVolumeStatus.setVisible(true);
			}
		});
		lblShowVolume.setHorizontalAlignment(SwingConstants.CENTER);
		lblShowVolume.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblShowVolume.setBounds(221, 260, 20, 18);
		panelMain.add(lblShowVolume);
		
		txtSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode()==KeyEvent.VK_ENTER) {
					btnSearchActionPerformer(lstPlayList);
				}
			}
		});

		lblPlayingSong = new JLabel("Playing song");
		lblPlayingSong.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (indexPlayList != null) {
					lstPlayList.setSelectedIndex(indexPlayList[0]);
					togglePlayPauseButton.setSelected(true);
				}
			}
		});
		
		
		lblPlayingSong.setBounds(0, 31, 279, 14);
		panelMain.add(lblPlayingSong);
		lblPlayingSong.setHorizontalAlignment(SwingConstants.CENTER);
		
		sliderProgress = new JSlider();
		sliderProgress.setMaximum(1000);
		sliderProgress.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if(sliderProgress.getValueIsAdjusting() ==false) {
					if(moveAutomatic == true) {
						moveAutomatic =false;
						posValue = sliderProgress.getValue() * 1.0 / 1000;
						processSeek(posValue);
					}
				}else {
					moveAutomatic = true;
					movingFromJump = true;
				}
			}
		});
		sliderProgress.setValue(0);
		sliderProgress.setBounds(10, 229, 259, 23);
		panelMain.add(sliderProgress);
		
		menuOpenPlaylist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuOpenPlaylistActionPerformer(fileChooser, lstPlayList);
			}
		});
		
		menuSavePlaylist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuSavePlaylistActionPerformer(fileChooser, e);
			}
		});
		
		mntmAddSong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnAddSongActionPerformer(fileChooser);
			}
		});
		
		mntmDeleteSong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDeleteSongActionPerformer(lstPlayList);
			}
		});
		
		mntmOpenPlaylist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuOpenPlaylistActionPerformer(fileChooser, lstPlayList);
			}
		});
		
		mntmClearPlaylist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mp3ListModel.clear();
			}
		});
		
		mntmPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnPlaySongActionPerformer(lstPlayList, slideVolume);
			}
		});
		
		
		mntmPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.pause();
			}
		});
		
		mntmStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.stop();
			}
		});
		
		togglePlayPauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!togglePlayPauseButton.isSelected())
					player.pause();
				else {
					btnPlaySongActionPerformer(lstPlayList, slideVolume);
				}

			}
		});

		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSearchActionPerformer(lstPlayList);
			}
		});

		btnAddSong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnAddSongActionPerformer(fileChooser);
			}
		});

		tglbtnVolume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tglbtnVolume.isSelected()) {
					slideVolume.setValue(0);
				} else
					slideVolume.setValue(sliderValue);
			}
		});

		slideVolume.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (slideVolume.getValue() == 0)
					tglbtnVolume.setSelected(true);
				else {
					tglbtnVolume.setSelected(false);
					sliderValue = slideVolume.getValue();
				}
				player.setVolume(slideVolume.getValue(), slideVolume.getMaximum());
				txtVolumeStatus.setText(slideVolume.getValue() + "");
			}
		});

		btnSelectNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSelectNextSongActionPerformer(lstPlayList);
			}
		});

		btnSelectPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSelectPrevSongActionPerformer(lstPlayList);
			}
		});

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

		lstPlayList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
					int[] indexPlayList = lstPlayList.getSelectedIndices();

					if (indexPlayList.length > 0) {
						Mp3 mp3 = (Mp3) mp3ListModel.getElementAt(indexPlayList[0]);
						player.play(mp3.getPath());
						player.setVolume(sliderValue, slideVolume.getMaximum());
					}
				}
			}
		});

		btnStopSong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.stop();
				togglePlayPauseButton.setSelected(false);
				currentSong = "";
			}
		});
		
		lstPlayList.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode()==KeyEvent.VK_ENTER) {
					if(!togglePlayPauseButton.isSelected()) {
						btnPlaySongActionPerformer(lstPlayList, slideVolume);
						togglePlayPauseButton.setSelected(true);
					}else {
						player.pause();
						togglePlayPauseButton.setSelected(false);
					}
				}
				
			}
		});
		
		lstPlayList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				int[] indexPlayList = lstPlayList.getSelectedIndices();
				if (indexPlayList.length > 0) {
					Mp3 mp3 = (Mp3) mp3ListModel.getElementAt(indexPlayList[0]);
					if (mp3.getName().equals(currentSong)) {
						togglePlayPauseButton.setSelected(true);
					} else
						togglePlayPauseButton.setSelected(false);
				}
				
			}
		});
	}


	private void btnAddSongActionPerformer(JFileChooser fileChooser) {
		FileUtils.addFileFilter(fileChooser, mp3FileFilter);
		int result = fileChooser.showOpenDialog(MP3PlayerGUI.this);
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
				JOptionPane.showMessageDialog(MP3PlayerGUI.this, "Song(s): \n" + BORDER + duplicateSongs + BORDER + "are duplicated and don't added to playlist");
			}
		}
	}
	
	private void btnSearchActionPerformer(JList lstPlayList) {
		String searchStr = txtSearch.getText();
		if (searchStr == null || searchStr.equals(EMPTY_STRING) || searchStr.equals(INPUT_SONG_NAME))
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
			JOptionPane.showMessageDialog(MP3PlayerGUI.this, "Don't find \"" + searchStr + "\"");
			txtSearch.requestFocus();
			txtSearch.selectAll();
			return;
		}

		for (int i = 0; i < selectIndexes.length; i++) {
			selectIndexes[i] = mp3FindedIndexes.get(i).intValue();
		}
		lstPlayList.setSelectedIndices(selectIndexes);
		
	}

	private void btnDeleteSongActionPerformer(JList lstPlayList) {
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

	private void menuOpenPlaylistActionPerformer(JFileChooser fileChooser, JList lstPlayList) {
		FileUtils.addFileFilter(fileChooser, playListFileFilter);
		int result = fileChooser.showOpenDialog(MP3PlayerGUI.this);

		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedPlayList = fileChooser.getSelectedFile();
			DefaultListModel mp3ListModel = (DefaultListModel) FileUtils.deserialise(selectedPlayList.getPath());
			MP3PlayerGUI.this.mp3ListModel = mp3ListModel;
			lstPlayList.setModel(mp3ListModel);
		}
	}

	private void menuSavePlaylistActionPerformer(JFileChooser fileChooser, ActionEvent e) {
		FileUtils.addFileFilter(fileChooser, playListFileFilter);
		int result = fileChooser.showSaveDialog(MP3PlayerGUI.this);

		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			if (FileUtils.checkSavedFileForDuplicate(fileChooser, selectedFile) || selectedFile.exists()) { // if
																											// file
																											// exists
				int resultOverwrite = JOptionPane.showConfirmDialog(MP3PlayerGUI.this, "File already exist", "Overwrite?", JOptionPane.YES_NO_CANCEL_OPTION);

				switch (resultOverwrite) {
				case JOptionPane.NO_OPTION: // if don't rewrite file open file
											// chooser again
					menuSavePlaylistActionPerformer(fileChooser, e);
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

	private void btnSelectNextSongActionPerformer(JList lstPlayList) {
		int nextIndex = lstPlayList.getSelectedIndex() + 1;
		// if in list size
		if (nextIndex <= lstPlayList.getModel().getSize() - 1) {
			lstPlayList.setSelectedIndex(nextIndex);
		}
		// if on end of list - select first item
		if (nextIndex > lstPlayList.getModel().getSize() - 1) {
			lstPlayList.setSelectedIndex(0);
		}
	}

	private void btnSelectPrevSongActionPerformer(JList lstPlayList) {
		int prevIndex = lstPlayList.getSelectedIndex() - 1;
		if (prevIndex >= 0) { // if in list size
			lstPlayList.setSelectedIndex(prevIndex);
		}
		if (prevIndex < 0) { // if on top of list - select last item
			lstPlayList.setSelectedIndex(lstPlayList.getModel().getSize() - 1);
		}
	}

	private void btnPlaySongActionPerformer(JList lstPlayList, JSlider slideVolume) {
		indexPlayList = lstPlayList.getSelectedIndices();

		if (indexPlayList.length > 0) {
			Mp3 mp3 = (Mp3) mp3ListModel.getElementAt(indexPlayList[0]);
			currentSong = mp3.getName();
			player.play(mp3.getPath());
			player.setVolume(sliderValue, slideVolume.getMaximum());
		}
	}
	
	private boolean selectNextSong(JList lstPlayList) {
		int nextIndex = lstPlayList.getSelectedIndex() + 1;
		// if in list size
		if (nextIndex <= lstPlayList.getModel().getSize() - 1) {
			lstPlayList.setSelectedIndex(nextIndex);
			return true;
		}
		return false;
	}
	
    private void processSeek(double bytes) {
        try {
            long skipBytes = (long) Math.round(((Integer) bytesLen).intValue() * bytes);
            player.jump(skipBytes);
        } catch (Exception e) {
            e.printStackTrace();
            movingFromJump = false;
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

	@Override
	public void opened(Object o, Map map) {
		duration = (long) Math.round((((Long) map.get("duration")).longValue())/1000000);
		bytesLen = (int) Math.round(((Integer) map.get("mp3.length.bytes")).intValue());
		
		String songName = map.get("title") != null ? map.get("title").toString() : FileUtils.getFileNameWithoutExtension(new File(o.toString()).getName());
		if(songName.length()>35) {
			songName = songName.substring(0, 35) + "...";
		}
		lblPlayingSong.setText(songName);
	}

	@Override
	public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties) {
		float progress = -1.0f;
		
		if((bytesread>0) && (duration>0)) {
			progress = bytesread * 1.0f /bytesLen *1.0f;
		}
		
		secondsAmount = (long) (duration * progress);
		
		if(duration != 0) {
			if(movingFromJump == false) {
				sliderProgress.setValue(((int) Math.round(secondsAmount * 1000 /duration)));
			}
		}
		
	}

	@Override
	public void setController(BasicController arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stateUpdated(BasicPlayerEvent bpe) {
		int state = bpe.getCode();
		
		if(state == BasicPlayerEvent.PLAYING) {
			movingFromJump = false;
		}else if(state == BasicPlayerEvent.SEEKING) {
			movingFromJump = true;
		}else if(state == BasicPlayerEvent.EOM) {
			if(selectNextSong(lstPlayList)) {
				btnPlaySongActionPerformer(lstPlayList, slideVolume);
			}
		}
	}
}
