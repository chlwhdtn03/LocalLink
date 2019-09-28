package locallink.chlwhdtn;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

import locallink.Main;


public class Note extends Thread {
	private Image noteBasicImage = (new ImageIcon(Main.class.getResource("/images/noteBasic.png"))).getImage();
	private int x;
	private int y = 450 - (1000 / Main.SLEEP_TIME * Main.NOTE_SPEED) * Main.REACH_TIME ;

	public String noteType;

	public boolean proceeded = true;
	public boolean islong = false;

	public void close() {
		proceeded = false;
	}

	public Note(String noteType) {
		setName(noteType + " 노트");
		if (noteType.equals("S")) {
			x = 80;
		} else if (noteType.equals("D")) {
			x = 184;
		}
		if (noteType.equals("F")) {
			x = 288;
		}
		if (noteType.equals("J")) {
			x = 438;
		}
		if (noteType.equals("K")) {
			x = 542;
		}
		if (noteType.equals("L")) {
			x = 646;
		}
		this.noteType = noteType;
	}

	public void setLong(boolean bool) {
		this.islong = bool;
		if (bool) {
			BufferedImage bi = Main.toBufferedImage(noteBasicImage);
			Graphics2D g = bi.createGraphics();
			g.setColor(new Color(255, 255, 0));
			g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
			noteBasicImage = bi;
		} else {
			noteBasicImage = (new ImageIcon(Main.class.getResource("/images/noteBasic.png"))).getImage();
		}
	}

	public void screenDraw(Graphics g) {
		if (noteType.equals("long")) {
			g.drawImage(noteBasicImage, x, y, null);
			g.drawImage(noteBasicImage, x + 100, y, null);
		} else {
			g.drawImage(noteBasicImage, x, y, null);
		}
	}

	public void drop() {
		y += Main.NOTE_SPEED;
		if (y > 500) {
			if(LocalLink.game != null) {
				LocalLink.game.JudgeImage = LocalLink.game.MissImage;
				LocalLink.game.combo = 0;
			}
			close();
		}
	}

	public void run() {
		try {
			while (true) {
				drop();
				if (proceeded) {
					Thread.sleep(Main.SLEEP_TIME);
				} else {
					interrupt();
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String judge() {
		if (y >= 530) {
			close();
			return "MISS";
		}
		if (y >= 480) {
			close();
			return "GOOD";
		}
		if (y >= 400) {
			close();
			return "PERFECT";
		}
		if (y >= 380) {
			close();
			return "GOOD";
		}
		if (y >= 0) {
			close();
			return "EARLY";
		}
		return "MISS";
	}
}
