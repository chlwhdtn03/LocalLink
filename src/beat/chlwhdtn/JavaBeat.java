package beat.chlwhdtn;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import beat.Delay;
import beat.Main;

public class JavaBeat extends JFrame {
	private Image screenImage;
	private Graphics screenGraphic;
	private Image Background;
	private JButton exitButton = new JButton("나가기");
	private JButton startButton = new JButton("시작하기");
	private JButton nextButton = new JButton(">>");
	private JButton backButton = new JButton("<<");
	

	private JButton btnX = new JButton();
	private JButton btnM = new JButton();

	private ImageIcon min_exit, min_enter;
	private ImageIcon close_exit, close_enter;
	private Point mouseDownCompCoords = null;

	private JLabel artwork = new JLabel("이미지");
	private JLabel songinfo = new JLabel("곡이름");

	ArrayList<Track> tracklist = new ArrayList<Track>();
	public static Game game;
	private Music selectedMusic;
	private int nowSelected = 0;

	private JButton playButton = new JButton("시작");
	private JButton endButton = new JButton("←");

	private boolean isMainScreen = false;
	private boolean isGameScreen = false;
	
	int fps = 144;
	Delay delay = new Delay(fps);

	public JavaBeat() {
		
		delay.setSyncDelay(true);
		
		close_exit = new ImageIcon(Main.class.getResource("/Close_Exit.png"));
		close_enter = new ImageIcon(Main.class.getResource("/Close_Enter.png"));

		min_exit = new ImageIcon(Main.class.getResource("/Min_Exit.png"));
		min_enter = new ImageIcon(Main.class.getResource("/Min_Enter.png"));

		tracklist.add(new Track("지나고도같은오늘", "지나고도같은오늘.mp3"));
		tracklist.add(new Track("해야", "해야.mp3"));
		tracklist.add(new Track("플라워", "플라워.mp3"));

		setUndecorated(true);
		setTitle("JavaBeat");
		setSize(Main.WIDTH, Main.HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(3);
		setVisible(true);
		setBackground(new Color(0, 0, 0, 0));
		setLayout(null);
		setFocusable(true);
		requestFocusInWindow();

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

		startButton.setBounds(30, 200, 100, 50);
		startButton.setForeground(Color.WHITE);
		startButton.setFont(new Font("맑은 고딕", 0, 16));
		startButton.setBorderPainted(false);
		startButton.setContentAreaFilled(false);
		startButton.setFocusPainted(false);
		startButton.setOpaque(true);
		startButton.setBackground(SystemColor.textHighlight);
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gotoMenu();
			}
		});
		add(startButton);

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

		nextButton.setBounds(660, 200, 100, 50);
		nextButton.setForeground(Color.WHITE);
		nextButton.setFont(new Font("맑은 고딕", 0, 16));
		nextButton.setBorderPainted(false);
		nextButton.setContentAreaFilled(false);
		nextButton.setFocusPainted(false);
		nextButton.setOpaque(true);
		nextButton.setVisible(false);
		nextButton.setBackground(SystemColor.textHighlight);
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectRight();
			}
		});
		add(nextButton);

		backButton.setBounds(40, 200, 100, 50);
		backButton.setForeground(Color.WHITE);
		backButton.setFont(new Font("맑은 고딕", 0, 16));
		backButton.setBorderPainted(false);
		backButton.setContentAreaFilled(false);
		backButton.setFocusPainted(false);
		backButton.setOpaque(true);
		backButton.setVisible(false);
		backButton.setBackground(SystemColor.textHighlight);
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectLeft();
			}
		});
		add(backButton);

		playButton.setBounds(350, 500, 100, 50);
		playButton.setForeground(Color.WHITE);
		playButton.setFont(new Font("맑은 고딕", 0, 16));
		playButton.setBorderPainted(false);
		playButton.setContentAreaFilled(false);
		playButton.setFocusPainted(false);
		playButton.setOpaque(true);
		playButton.setVisible(false);
		playButton.setBackground(SystemColor.textHighlight);
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

		songinfo.setBounds(150, 100, 500, 50);
		songinfo.setForeground(Color.WHITE);
		songinfo.setFont(new Font("맑은 고딕",  Font.BOLD, 20));
		songinfo.setVisible(false);
		songinfo.setHorizontalAlignment(JLabel.CENTER);
		songinfo.setVerticalAlignment(JLabel.CENTER);
		add(songinfo);

		artwork.setBounds(275, 200, 250, 250);
		artwork.setForeground(Color.WHITE);
		artwork.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		artwork.setVisible(false);
		add(artwork);

		Background = (new ImageIcon(Main.class.getResource("../images/background.png"))).getImage();
	}

	public void startGame() {
		JavaBeat.game = new Game(tracklist.get(nowSelected));
		JavaBeat.game.start();
		gameStart(nowSelected);
		endButton.setVisible(true);
		songinfo.setVisible(false);
		artwork.setVisible(false);
	}

	public void endGame() {
		isMainScreen = true;
		startButton.setVisible(false);
		exitButton.setVisible(false);
		nextButton.setVisible(true);
		backButton.setVisible(true);
		endButton.setVisible(false);
		playButton.setVisible(true);
		songinfo.setVisible(true);
		artwork.setVisible(true);
		selectTrack(nowSelected);
		isGameScreen = false;
		JavaBeat.game.close();

	}

	public void gotoMenu() {
		selectTrack(0);
		startButton.setVisible(false);
		exitButton.setVisible(false);
		nextButton.setVisible(true);
		backButton.setVisible(true);
		playButton.setVisible(true);
		songinfo.setVisible(true);
		artwork.setVisible(true);
		isMainScreen = true;
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
		if (isMainScreen) {
			g.drawImage(tracklist.get(nowSelected).image, 0, -100,null);
			g.drawImage(Background,0,0,800,30,null);
			g.drawImage(Background, 150, 70, 500,500	, null);
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g.setFont(new Font("맑은 고딕", 0, 20));
			g.setColor(Color.white);
			g.drawString("JavaBeat", 350, 22);
		}
		if (isGameScreen) {
			game.screenDraw(g);
		}
		try {
			Thread.sleep(5L);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paintComponents(g);
		this.repaint();
	}

	public void selectTrack(int nowSelected) {
		if (selectedMusic != null) {
			selectedMusic.close();
		}
		songinfo.setText("<html><center>" + tracklist.get(nowSelected).artist + " - " + tracklist.get(nowSelected).title
				+ "<br>" + tracklist.get(nowSelected).album + "</center></html>");
		artwork.setIcon(new ImageIcon(tracklist.get(nowSelected).image.getScaledInstance(250, 250, Image.SCALE_SMOOTH),
				tracklist.get(nowSelected).title));
		selectedMusic = new Music(tracklist.get(nowSelected).musicuri, true);
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

	public void gameStart(int nowSelected) {
		if (selectedMusic != null)
			selectedMusic.close();
		isGameScreen = true;
		isMainScreen = false;
		nextButton.setVisible(false);
		backButton.setVisible(false);
		playButton.setVisible(false);
	}
}
