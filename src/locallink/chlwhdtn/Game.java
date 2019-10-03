package locallink.chlwhdtn;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import locallink.Main;
import musicdata.BeatListener;
import musicdata.Flower;
import musicdata.Justaway;
import musicdata.Sameday;
import musicdata.Sunrise;

public class Game extends Thread {

	private Image gameInfoImage = new ImageIcon(Main.class.getResource("/images/gameInfo.png")).getImage();
	private Image judgementLineImage = new ImageIcon(Main.class.getResource("/images/judgementLine.png")).getImage();
	private Image noteRouteLineImage = new ImageIcon(Main.class.getResource("/images/noteRouteLine.png")).getImage();
	private Image noteRouteSImage = new ImageIcon(Main.class.getResource("/images/noteRoute.png")).getImage();
	private Image noteRouteDImage = new ImageIcon(Main.class.getResource("/images/noteRoute.png")).getImage();
	private Image noteRouteFImage = new ImageIcon(Main.class.getResource("/images/noteRoute.png")).getImage();
	private Image noteRouteJImage = new ImageIcon(Main.class.getResource("/images/noteRoute.png")).getImage();
	private Image noteRouteKImage = new ImageIcon(Main.class.getResource("/images/noteRoute.png")).getImage();
	private Image noteRouteLImage = new ImageIcon(Main.class.getResource("/images/noteRoute.png")).getImage();
	private Image Background = new ImageIcon(Main.class.getResource("/images/background.png")).getImage();

	public Image MissImage = new ImageIcon(Main.class.getResource("/images/Miss.png")).getImage();
	public Image EarlyImage = new ImageIcon(Main.class.getResource("/images/Early.png")).getImage();
	public Image GoodImage = new ImageIcon(Main.class.getResource("/images/Good.png")).getImage();
	public Image PerfectImage = new ImageIcon(Main.class.getResource("/images/Perfect.png")).getImage();
	public Image JudgeImage;
	
	public int combo; 
	private String title;
	private String artist;
	private Image artwork;
	public Music gameMusic;

	ArrayList<Note> notelist = new ArrayList<Note>();

	public Game(Track track) {
		setName(track.title + " 메인 쓰레드");
		this.title = track.title;
		this.combo = 0;
		this.artist = track.artist;
		this.artwork = track.image;
		this.gameMusic = new Music(track, false, 0, false);
	}

	public void screenDraw(Graphics2D g) {
		g.setBackground(Color.BLACK);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(artwork, 0, -100, null);
		g.drawImage(noteRouteSImage, 80, 0, null);
		g.drawImage(noteRouteDImage, 184, 0, null);
		g.drawImage(noteRouteFImage, 288, 0, null);
		g.drawImage(noteRouteJImage, 438, 0, null);
		g.drawImage(noteRouteKImage, 542, 0, null);
		g.drawImage(noteRouteLImage, 646, 0, null);
		g.drawImage(noteRouteLineImage, 76, 30, null);
		g.drawImage(noteRouteLineImage, 180, 30, null);
		g.drawImage(noteRouteLineImage, 284, 30, null);
		g.drawImage(noteRouteLineImage, 388, 30, null);
		g.drawImage(noteRouteLineImage, 434, 30, null);
		g.drawImage(noteRouteLineImage, 538, 30, null);
		g.drawImage(noteRouteLineImage, 642, 30, null);
		g.drawImage(noteRouteLineImage, 746, 30, null);
		g.drawImage(gameInfoImage, 0, 550, null);
		g.drawImage(judgementLineImage, 0, 450, null);
		for (int i = 0; i < notelist.size(); i++) {
			Note note = notelist.get(i);
			if (!note.proceeded) {
				notelist.remove(i);
				i--;
			} else {
				note.screenDraw(g);
			}
		}
		g.drawImage(Background, 0, 0, 800, 30, null);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setFont(new Font("맑은 고딕", 0, 20));
		g.setColor(Color.white);
		g.drawString("JavaBeat", 350, 22);
		g.setColor(Color.DARK_GRAY);
		g.setFont(new Font("맑은 고딕", Font.BOLD, 30));
		g.drawString("S", 120, 480);
		g.drawString("D", 224, 480);
		g.drawString("F", 328, 480);
		g.drawString("J", 478, 480);
		g.drawString("K", 582, 480);
		g.drawString("L", 686, 480);
		g.setFont(new Font("맑은 고딕", Font.PLAIN, 30));
		g.setColor(Color.white);
		g.drawString(artist + " - " + title, 20, 588);
		g.drawImage(JudgeImage, 320, 100, null);
		g.setFont(new Font("맑은 고딕", Font.BOLD, 30));
		g.drawString("x" + combo, 350, 250);
	}

	public void pressS() {
		judge("S");
		noteRouteSImage = new ImageIcon(Main.class.getResource("/images/noteRoutePressed.png")).getImage();
	}

	public void releaseS() {
		noteRouteSImage = new ImageIcon(Main.class.getResource("/images/noteRoute.png")).getImage();
	}

	public void pressD() {
		judge("D");
		noteRouteDImage = new ImageIcon(Main.class.getResource("/images/noteRoutePressed.png")).getImage();
	}

	public void releaseD() {
		noteRouteDImage = new ImageIcon(Main.class.getResource("/images/noteRoute.png")).getImage();
	}

	public void pressF() {
		judge("F");
		noteRouteFImage = new ImageIcon(Main.class.getResource("/images/noteRoutePressed.png")).getImage();
	}

	public void releaseF() {
		noteRouteFImage = new ImageIcon(Main.class.getResource("/images/noteRoute.png")).getImage();
	}

	public void pressJ() {
		judge("J");
		noteRouteJImage = new ImageIcon(Main.class.getResource("/images/noteRoutePressed.png")).getImage();
	}

	public void releaseJ() {
		noteRouteJImage = new ImageIcon(Main.class.getResource("/images/noteRoute.png")).getImage();
	}

	public void pressK() {
		judge("K");
		noteRouteKImage = new ImageIcon(Main.class.getResource("/images/noteRoutePressed.png")).getImage();
	}

	public void releaseK() {
		noteRouteKImage = new ImageIcon(Main.class.getResource("/images/noteRoute.png")).getImage();
	}

	public void pressL() {
		judge("L");
		noteRouteLImage = new ImageIcon(Main.class.getResource("/images/noteRoutePressed.png")).getImage();
	}

	public void releaseL() {
		noteRouteLImage = new ImageIcon(Main.class.getResource("/images/noteRoute.png")).getImage();
	}

	@Override
	public void run() {
		dropNotes();
	}

	public void close() {
		notelist.clear();
		gameMusic.close();
		interrupt();
	}

	public void dropNotes() {
	
		BeatListener bl = new Sunrise();
		if (title.equals("해야")) {
			bl = new Sunrise();
		} else if (title.equals("플라워")) {
			bl = new Flower();
		} else if (title.equals("지나고도같은오늘")) {
			bl = new Sameday();
		} else if(title.equals("그냥냅둬")) {
			bl = new Justaway();
		}
		Beat[] beats = bl.getNotes(Main.REACH_TIME * 1000, 100);
		int i = 0;
		gameMusic.start();
		while (i < beats.length && !isInterrupted()) {
			boolean dropped = false;
			if (beats[i].time <= gameMusic.getTime()) {
				Note note = new Note(beats[i].name);
				note.start();
				notelist.add(note);
				i++;
				dropped=true;
			}
			if (!dropped) {
				try {
					Thread.sleep(5);
				} catch (Exception e) {
					interrupt();
					e.printStackTrace();
					return;
				}
			}
		}
		try {
			gameMusic.join();
			close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void judge(String input) {
		for (int i = 0; i < notelist.size(); i++) {
			Note note = notelist.get(i);
			if (input.equals(note.noteType)) {
				String result = note.judge();
				switch (result) {
				case "GOOD":
					JudgeImage = GoodImage;
					combo++;
					break;
				case "PERFECT":
					JudgeImage = PerfectImage;
					combo++;
					break;
				case "MISS":
					JudgeImage = MissImage;
					combo = 0;
					break;
				case "EARLY":
					JudgeImage = EarlyImage;
					combo = 0;
					break;
				}
				break;
			}
		}
	}

}
