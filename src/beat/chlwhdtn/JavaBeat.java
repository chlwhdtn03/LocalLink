package beat.chlwhdtn;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

public class JavaBeat extends JFrame {
	private Image screenImage;
	private Graphics screenGraphic;
	private Image Background;
	private JButton exitButton = new JButton("나가기");
	private JButton startButton = new JButton("시작하기");
	private JButton nextButton = new JButton(">>"); 
	private JButton backButton = new JButton("<<");

	ArrayList<Track> tracklist = new ArrayList<Track>();
	public static Game game;
	private Music selectedMusic;
	private int nowSelected = 0;

	private JButton playButton = new JButton("시작");
	private JButton endButton = new JButton("←");
	
	private boolean isMainScreen = false;
	private boolean isGameScreen = false;


	public JavaBeat() {

		tracklist.add(new Track("해야", "해야.mp3"));
		tracklist.add(new Track("Flower", "플라워.mp3"));
		tracklist.add(new Track("지나고도 같은 오늘", "지나고도같은오늘.mp3"));

		setTitle("JavaBeat ");
		setSize(800, 600);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(3);
		setVisible(true);
		setLayout(null);
		setBackground(new Color(255, 255, 255));
		setFocusable(true);
		requestFocusInWindow();

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
				selectTrack(0);
				startButton.setVisible(false);
				exitButton.setVisible(false);
				nextButton.setVisible(true);
				backButton.setVisible(true);
				playButton.setVisible(true);
				isMainScreen = true;
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

		backButton.setBounds(30, 200, 100, 50);
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
				JavaBeat.game = new Game(tracklist.get(nowSelected).title, tracklist.get(nowSelected).gamemusic);
				JavaBeat.game.start();
				gameStart(nowSelected);
				endButton.setVisible(true);
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
				JavaBeat.game.close();
				selectTrack(0);
				startButton.setVisible(false);
				exitButton.setVisible(false);
			nextButton.setVisible(true);
				backButton.setVisible(true);
				endButton.setVisible(false);
				playButton.setVisible(true);
				isMainScreen = true;
				isGameScreen = false;
				selectTrack(nowSelected);
			}
		});
		add(endButton);

		Background = (new ImageIcon(beat.Main.class.getResource("../images/main.png"))).getImage();
	}

	public void paint(Graphics g) {
		screenImage = createImage(800, 600);
		screenGraphic = screenImage.getGraphics();
		screenDraw((Graphics2D) screenGraphic);
		g.drawImage(screenImage, 0, 0, null);
	}


	public void screenDraw(Graphics2D g) {
		paintComponents(g);
		if (isMainScreen) {
			g.drawImage(Background, 0, 0, null);
		}
		if (isGameScreen) {
			game.screenDraw(g);
		}
		try {
			Thread.sleep(5L);
		} catch (Exception e) {
			e.printStackTrace();
		}
		repaint();
	}

	public void selectTrack(int nowSelected) {
		if (selectedMusic != null) {
			selectedMusic.close();
		}
		selectedMusic = new Music(((Track) tracklist.get(nowSelected)).gamemusic, true);
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
