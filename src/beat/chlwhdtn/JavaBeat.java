package beat.chlwhdtn;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.SystemColor;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.GeneralPath;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Mixer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.PopupMenuUI;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.BasicSliderUI;

import beat.Delay;
import beat.Main;
import beat.ScreenType;
import beat.Timer;
import beat.Volume;
import beat.WebManager;
import net.iharder.dnd.FileDrop;

public class JavaBeat extends JFrame {

	/* 유틸 */
	public static JavaBeat instance;
	int fps = 144;
	Delay delay = new Delay(fps);
	private ScreenType nowScreen = ScreenType.MainMenu;
	private Point mouseDownCompCoords = null;
	private WebManager wm;

	/* 리소스 */
	private Image screenImage; // JFrame을 초기화
	private Graphics screenGraphic; // JFrame을 꾸밀 수 있게 초기화
	private Image Background; // (정보창) 회색 배경
	private ImageIcon min_exit, min_enter;
	private ImageIcon close_exit, close_enter;

	/* 공통 요소 */
	private JButton btnX = new JButton();
	private JButton btnM = new JButton();
	
	/* Main 화면 요소 */
	private JList<String> connect_list = new JList<String>();
	private JScrollPane connect_scroll = new JScrollPane(connect_list);

	/* MainMenu 화면 요소 */
	private JButton exitButton = new JButton("나가기");
	private JButton musicButton = new JButton("시작하기");

	/* Music 유틸 */
	ArrayList<Track> tracklist = new ArrayList<Track>();
	private Music selectedMusic;
	private boolean lyricmode = false;
	private int nowSelected = 0;
	public Track selectedTrack;

	/* Music 화면 요소 */
	private JButton nextButton = new JButton(">>");
	private JButton backButton = new JButton("<<");
	private JButton showlyricButton = new JButton("가사");
	private JButton btnOption = new JButton();
	private JLabel titlelabel = new JLabel("JavaBeat");
	private JLabel artwork = new JLabel();
	private JLabel songinfo = new JLabel("MP3 파일을 이곳에 드래그하세요!");
	private JLabel playtime = new JLabel("0:00");
	private JLabel nowtime = new JLabel("0:00");
	private JTextArea lyric = new JTextArea();
	private JScrollPane lyricscroll = new JScrollPane(lyric);
	private JProgressBar timebar = new JProgressBar();
	private JButton playButton = new JButton("시작");
	private JButton endButton = new JButton("←");
	private JSlider volumebar = new JSlider();

	/* MusicGame 유틸 */
	public static Game game;
	private Thread gameThread;

	public JavaBeat() {

		wm = new WebManager();
		
		instance = this;
		delay.setSyncDelay(true);

		close_exit = new ImageIcon(Main.class.getResource("/Close_Exit.png"));
		close_enter = new ImageIcon(Main.class.getResource("/Close_Enter.png"));

		min_exit = new ImageIcon(Main.class.getResource("/Min_Exit.png"));
		min_enter = new ImageIcon(Main.class.getResource("/Min_Enter.png"));

		setUndecorated(true); // 테두리 상단창 삭제
		setTitle("JavaBeat");
		setSize(Main.WIDTH, Main.HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setBackground(new Color(0, 0, 0, 0));
		setLayout(null);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mouseDownCompCoords = e.getPoint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				mouseDownCompCoords = null;
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				Point currCoords = e.getLocationOnScreen();
				setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
			}
		});

		initGeneral();
		initMain();
		initMusic();
		Background = new ImageIcon(Main.class.getResource("/images/background.png")).getImage();

		new FileDrop(this, new FileDrop.Listener() {
			public void filesDropped(java.io.File[] files) {
				if (nowScreen.equals(ScreenType.Music)) {
					for (File file : files) {
						if (file.isDirectory()) {
							filesDropped(file.listFiles());
							continue;
						}
						if (file.getName().endsWith(".mp3"))
							tracklist.add(new Track(file.getAbsolutePath()));
					}
					if (tracklist.isEmpty() == false)
						selectTrack(0);
				}
			}
		});

	}

	// 게임 시작 쓰레드 생성
	public void startGame() {
		gameThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					game = new Game(tracklist.get(nowSelected));
					game.start();
					setFocusable(true);
					gameStart(nowSelected);
					game.join();
					endGame();
				} catch (InterruptedException e) {
					game.interrupt();
					e.printStackTrace();
				}
			}
		});
		gameThread.setName("게임 종료 대기 쓰레드");
		gameThread.start();
	}

	public void endGame() {
		gameThread.interrupt();
		JavaBeat.game.close();
		game = null;
		nowScreen = ScreenType.Music;
		gotoMusic();
		lyricmode = false;
		checklyric(false);
	}

	// 게임 시작
	public void gameStart(int nowSelected) {
		if (selectedMusic != null)
			selectedMusic.close();
		nowScreen = ScreenType.MusicGame;
		nextButton.setVisible(false);
		backButton.setVisible(false);
		playButton.setVisible(false);
		showlyricButton.setVisible(false);
		lyricscroll.setVisible(false);
		endButton.setVisible(true);
		songinfo.setVisible(false);
		playtime.setVisible(false);
		artwork.setVisible(false);
		timebar.setVisible(false);
		nowtime.setVisible(false);
		btnOption.setVisible(false);
		showlyricButton.setVisible(false);
	}
	
	public void gotoMusic() {
		connect_scroll.setVisible(false);
		musicButton.setVisible(false);
		exitButton.setVisible(false);
		endButton.setVisible(false);
		nextButton.setVisible(true);
		backButton.setVisible(true);
		playButton.setVisible(true);
		playtime.setVisible(true);
		songinfo.setVisible(true);
		artwork.setVisible(true);
		showlyricButton.setVisible(true);
		timebar.setVisible(true);
		nowtime.setVisible(true);
		btnOption.setVisible(true);
		nowScreen = ScreenType.Music;
	}

	public void checklyric(boolean imagenull) {
		if (lyricmode) {
			if (imagenull) {
				return;
			}
		}
		if (imagenull) {
			lyricmode = true;
		}
		artwork.setVisible(!lyricmode);
		lyricscroll.setVisible(lyricmode);
		if (imagenull) {
			lyricmode = false;
		}
	}
	
	public void paint(Graphics g) {
		screenImage = createImage(Main.WIDTH, Main.HEIGHT);
		screenGraphic = screenImage.getGraphics();
		screenDraw((Graphics2D) screenGraphic);
		g.drawImage(screenImage, 0, 0, null);
		try {
			delay.autoCompute();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void screenDraw(Graphics2D g) {
		if(nowScreen.equals(ScreenType.MainMenu)) {
			Vector<String> users = new Vector<String>();
			wm.clients.forEach(web -> {
				users.add(web.remoteAddress().toString());
			});
			connect_list.setListData(users);
		}
		if (nowScreen.equals(ScreenType.Music)) {
			if (tracklist.isEmpty() == false) {
				g.drawImage(tracklist.get(nowSelected).image, 0, -100, null);
			}
			g.drawImage(Background, 0, 0, 800, 30, null);
			g.drawImage(Background, 150, 70, 500, 500, null);
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g.setFont(new Font("맑은 고딕", 0, 20));
			g.setColor(Color.white);

			if (selectedMusic != null) {
				nowtime.setText(Timer.getTime((int) (selectedMusic.getTime() / 1000)));
				timebar.setValue((int) (selectedMusic.getTime() / 1000));
			}
		}
		if (nowScreen.equals(ScreenType.MusicGame)) {
			game.screenDraw(g);
			try {
				Thread.sleep(5L);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		g.setColor(Color.white);
		g.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.drawString(Long.toString(Main.MEMORY_USAGE / 1024 / 1024) + " MB", 650, 24);
		paintComponents(g);
		this.repaint();
	}

	public void selectTrack(int nowSelected) {
		if (selectedMusic != null) {
			selectedMusic.close();
		}
		try {
			wm.sendTrack(tracklist.get(nowSelected));
		} catch (Exception e) {
			return;
		}

		setTitle("▶ " + tracklist.get(nowSelected).title);
		titlelabel.setText("▶ " + tracklist.get(nowSelected).title);

		songinfo.setText("<html><center>" + tracklist.get(nowSelected).artist + " - " + tracklist.get(nowSelected).title
				+ "<br>" + tracklist.get(nowSelected).album + "</center></html>");

		timebar.setValue(0);
		timebar.setMaximum(tracklist.get(nowSelected).duration);
		playtime.setText(Timer.getTime(tracklist.get(nowSelected).duration));
		if (tracklist.get(nowSelected).image == null) {
			artwork.setIcon(null);
			checklyric(true);
		} else {
			artwork.setIcon(
					new ImageIcon(tracklist.get(nowSelected).image.getScaledInstance(250, 250, Image.SCALE_SMOOTH),
							tracklist.get(nowSelected).title));
			checklyric(false);
		}
		lyric.setText(tracklist.get(nowSelected).lyric);
		if (tracklist.get(nowSelected).title.toLowerCase().contains("(inst.)")) {
			lyric.setText("Instrumental 음원입니다");
		}
		lyric.setCaretPosition(0);
		selectedTrack = tracklist.get(nowSelected);
		selectedMusic = new Music(tracklist.get(nowSelected), true, 0);
		selectedMusic.start();
	}

	public void selectLeft() {
		if (nowSelected == 0) {
			nowSelected = tracklist.size() - 1;
		} else {
			nowSelected--;
		}
		selectTrack(nowSelected);
	}

	public void selectRight() {
		if (nowSelected == tracklist.size() - 1) {
			nowSelected = 0;
		} else {
			nowSelected++;
		}
		selectTrack(nowSelected);
	}

	public void initGeneral() {
		btnX.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnX.setBorder(null);
		btnX.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnX.setIcon(close_enter);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btnX.setIcon(close_exit);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				System.exit(0);
			}
		});
		btnX.setOpaque(false);
		btnX.setContentAreaFilled(false);
		btnX.setBorderPainted(false);
		btnX.setIcon(close_exit);
		btnX.setBounds(770, 0, 30, 30);
		add(btnX);

		btnM.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnM.setBorder(null);
		btnM.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnM.setIcon(min_enter);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btnM.setIcon(min_exit);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				setState(JFrame.ICONIFIED);
			}
		});
		btnM.setOpaque(false);
		btnM.setContentAreaFilled(false);
		btnM.setBorderPainted(false);
		btnM.setIcon(min_exit);
		btnM.setBounds(740, 0, 30, 30);

		add(btnM);

		addKeyListener(new GameKeyListener());
	}
	
	public void initMain() {
		musicButton.setBounds(30, 200, 100, 50);
		musicButton.setForeground(Color.WHITE);
		musicButton.setFont(new Font("맑은 고딕", 0, 16));
		musicButton.setBorderPainted(false);
		musicButton.setContentAreaFilled(false);
		musicButton.setFocusPainted(false);
		musicButton.setOpaque(true);
		musicButton.setBackground(SystemColor.textHighlight);
		musicButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gotoMusic();
			}
		});
		add(musicButton);

		exitButton.setBounds(30, 300, 100, 50);
		exitButton.setForeground(Color.WHITE);
		exitButton.setFont(new Font("맑은 고딕", 0, 16));
		exitButton.setBorderPainted(false);
		exitButton.setContentAreaFilled(false);
		exitButton.setFocusPainted(false);
		exitButton.setOpaque(true);
		exitButton.setBackground(SystemColor.textHighlight);
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		add(exitButton);
		
		connect_scroll.setBounds(175, 175, 450, 250);
		connect_scroll.setBackground(new Color(0, 0, 0, 0));
		connect_scroll.setBorder(null);
		connect_scroll.getVerticalScrollBar().setUnitIncrement(7);
		connect_scroll.getVerticalScrollBar().setBackground(new Color(0, 0, 0, 100));
		connect_scroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {

			@Override
			protected JButton createDecreaseButton(int orientation) {
				return createZeroButton();
			}

			@Override
			protected JButton createIncreaseButton(int orientation) {
				return createZeroButton();
			}

			@Override
			protected void configureScrollBarColors() {
				this.thumbColor = new Color(200, 200, 200, 50);
				this.minimumThumbSize = new Dimension(0, 50);
				this.maximumThumbSize = new Dimension(0, 50);
				this.thumbDarkShadowColor = new Color(200, 200, 200);
			}

			private JButton createZeroButton() {
				JButton jbutton = new JButton();
				jbutton.setPreferredSize(new Dimension(0, 0));
				jbutton.setMinimumSize(new Dimension(0, 0));
				jbutton.setMaximumSize(new Dimension(0, 0));
				return jbutton;
			}

		});
		connect_list.setBackground(new Color(0, 0, 0, 0));
		connect_list.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		connect_list.setForeground(Color.white);
		add(connect_scroll);
	}
	
	public void initMusic() {
		JPopupMenu menu = new JPopupMenu();
		menu.setBorderPainted(false);
		menu.setBackground(new Color(0, 0, 0, 0));

		volumebar.setOrientation(SwingConstants.VERTICAL);
		volumebar.setFocusable(false);
		volumebar.setBackground(new Color(0, 0, 0, 0));
		volumebar.setValue(Volume.getVolume());
		volumebar.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				float volume = (float) (volumebar.getValue() * 0.01);
				Volume.setVolume(volume);

			}
		});
		volumebar.setUI(new BasicSliderUI(volumebar) {
			@Override
			public void paintThumb(Graphics g) {
				g.setColor(new Color(200, 200, 200));
				g.fillRect(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);
			}

			@Override
			public void paintTrack(Graphics g) {
				g.setColor(new Color(50, 50, 50, 150));
				g.fillRect(trackRect.x, trackRect.y, trackRect.width, trackRect.height);
			}
		});
		menu.add(volumebar);

		btnOption.setBounds(150, 545, 50, 25);
		btnOption.setForeground(Color.WHITE);
		btnOption.setFont(new Font("맑은 고딕", 0, 16));
		btnOption.setContentAreaFilled(false);
		btnOption.setFocusPainted(false);
		btnOption.setOpaque(true);
		btnOption.setBorderPainted(true);
		btnOption.setBorder(new LineBorder(Color.white));
		btnOption.setBackground(new Color(0, 0, 0, 0));
		btnOption.setVisible(false);
		btnOption.setText("V");
		btnOption.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(menu.getHeight());
				menu.show(btnOption, -1 - volumebar.getPreferredSize().width,
						-5 + btnOption.getHeight() - volumebar.getPreferredSize().height);
			}
		});
		add(btnOption);

		nextButton.setBounds(500, 500, 100, 50);
		nextButton.setForeground(Color.WHITE);
		nextButton.setFont(new Font("맑은 고딕", 0, 16));
		nextButton.setContentAreaFilled(false);
		nextButton.setFocusPainted(false);
		nextButton.setOpaque(true);
		nextButton.setBorderPainted(false);
		nextButton.setBackground(new Color(0, 0, 0, 100));
		nextButton.setVisible(false);
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectRight();
			}
		});
		add(nextButton);

		backButton.setBounds(200, 500, 100, 50);
		backButton.setForeground(Color.WHITE);
		backButton.setFont(new Font("맑은 고딕", 0, 16));
		backButton.setContentAreaFilled(false);
		backButton.setFocusPainted(false);
		backButton.setOpaque(true);
		backButton.setBorderPainted(false);
		backButton.setBackground(new Color(0, 0, 0, 100));
		backButton.setVisible(false);
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectLeft();
			}
		});
		add(backButton);

		playButton.setBounds(350, 500, 100, 50);
		playButton.setForeground(Color.WHITE);
		playButton.setFont(new Font("맑은 고딕", 0, 16));
		playButton.setContentAreaFilled(false);
		playButton.setFocusPainted(false);
		playButton.setOpaque(true);
		playButton.setBorderPainted(false);
		playButton.setBackground(new Color(255, 255, 255, 100));
		playButton.setVisible(false);
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startGame();
			}
		});
		add(playButton);

		endButton.setBounds(0, 0, 76, 50);
		endButton.setVerticalTextPosition(1);
		endButton.setForeground(Color.WHITE);
		endButton.setFont(new Font("맑은 고딕", 0, 16));
		endButton.setBorderPainted(false);
		endButton.setContentAreaFilled(false);
		endButton.setFocusPainted(false);
		endButton.setOpaque(true);
		endButton.setVisible(false);
		endButton.setBackground(SystemColor.RED);
		endButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				endGame();
			}
		});
		add(endButton);

		titlelabel.setBounds(150, 5, 500, 20);
		titlelabel.setHorizontalAlignment(SwingConstants.CENTER);
		titlelabel.setForeground(Color.WHITE);
		titlelabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		titlelabel.setVisible(true);
		add(titlelabel);

		showlyricButton.setBounds(600, 70, 50, 25);
		showlyricButton.setForeground(Color.WHITE);
		showlyricButton.setFont(new Font("맑은 고딕", 0, 16));
		showlyricButton.setContentAreaFilled(false);
		showlyricButton.setFocusPainted(false);
		showlyricButton.setOpaque(true);
		showlyricButton.setBorderPainted(true);
		showlyricButton.setBackground(new Color(0, 0, 0, 0));
		showlyricButton.setBorder(new LineBorder(Color.white));
		showlyricButton.setVisible(false);
		showlyricButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lyricmode = !lyricmode;
				checklyric(false);
			}
		});
		add(showlyricButton);

		songinfo.setBounds(150, 100, 500, 100);
		songinfo.setForeground(Color.WHITE);
		songinfo.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		songinfo.setVerticalAlignment(SwingConstants.TOP);
		songinfo.setHorizontalAlignment(SwingConstants.CENTER);
		songinfo.setVisible(false);
		add(songinfo);

		nowtime.setBounds(200, 455, 50, 20);
		nowtime.setForeground(Color.WHITE);
		nowtime.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		nowtime.setHorizontalAlignment(JLabel.CENTER);
		nowtime.setVerticalAlignment(JLabel.CENTER);
		nowtime.setVisible(false);
		add(nowtime);

		playtime.setBounds(550, 455, 50, 20);
		playtime.setForeground(Color.WHITE);
		playtime.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		playtime.setHorizontalAlignment(JLabel.CENTER);
		playtime.setVerticalAlignment(JLabel.CENTER);
		playtime.setVisible(false);
		add(playtime);

		timebar.setBounds(260, 457, 280, 20);
		timebar.setOpaque(true);
		timebar.setBorderPainted(false);
		timebar.setBackground(new Color(0, 0, 0, 100));
		timebar.setForeground(new Color(200, 200, 255, 100));
		timebar.setVisible(false);
		timebar.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int mouseX = e.getX();
				int progressBarVal = (int) Math
						.round(((double) mouseX / (double) timebar.getWidth()) * timebar.getMaximum());
				timebar.setValue(progressBarVal);
				if (selectedMusic != null)
					selectedMusic.close();
				selectedMusic = new Music(tracklist.get(nowSelected), true, progressBarVal);
				selectedMusic.start();
			}
		});
		add(timebar);

		artwork.setBounds(275, 175, 250, 250);
		artwork.setForeground(Color.WHITE);
		artwork.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		artwork.setVisible(false);
		add(artwork);

		lyricscroll.setBounds(175, 175, 450, 250);
		lyricscroll.setVisible(false);
		lyricscroll.setBackground(new Color(0, 0, 0, 0));

		lyricscroll.setBorder(null);
		lyricscroll.getVerticalScrollBar().setUnitIncrement(7);
		lyricscroll.getVerticalScrollBar().setBackground(new Color(0, 0, 0, 100));
		lyricscroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {

			@Override
			protected JButton createDecreaseButton(int orientation) {
				return createZeroButton();
			}

			@Override
			protected JButton createIncreaseButton(int orientation) {
				return createZeroButton();
			}

			@Override
			protected void configureScrollBarColors() {
				this.thumbColor = new Color(200, 200, 200, 50);
				this.minimumThumbSize = new Dimension(0, 50);
				this.maximumThumbSize = new Dimension(0, 50);
				this.thumbDarkShadowColor = new Color(200, 200, 200);
			}

			private JButton createZeroButton() {
				JButton jbutton = new JButton();
				jbutton.setPreferredSize(new Dimension(0, 0));
				jbutton.setMinimumSize(new Dimension(0, 0));
				jbutton.setMaximumSize(new Dimension(0, 0));
				return jbutton;
			}

		});
		lyric.setBackground(new Color(0, 0, 0, 0));
		lyric.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		lyric.setForeground(Color.white);
		lyric.setEditable(false);
		lyric.setSelectionColor(new Color(0, 0, 0, 125));
		lyric.setSelectedTextColor(Color.WHITE);
		add(lyricscroll);
	}
}
