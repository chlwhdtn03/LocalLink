package beat.chlwhdtn;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Note extends Thread {
	private Image noteBasicImage = (new ImageIcon(beat.Main.class.getResource("../images/noteBasic.png"))).getImage();
	private int x;
	private int y = -150;

	public String noteType;

	public boolean proceeded = true;

	public void close() {
		proceeded = false;
	}

	public Note(String noteType) {
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

	public void screenDraw(Graphics g) {
		if (noteType.equals("long")) {
			g.drawImage(noteBasicImage, x, y, null);
			g.drawImage(noteBasicImage, x + 100, y, null);
		} else {
			g.drawImage(noteBasicImage, x, y, null);
		}
	}

	public void drop() {
		y += 3;
		if (y > 500) {
			JavaBeat.game.judge = "MISS!";
			JavaBeat.game.combo = 0;
			close();
		}
	}

	public void run() {
		try {
			while (true) {
				drop();
				if (proceeded) {
					Thread.sleep(10L);
					continue;
				}
				break;
			}
			interrupt();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String judge() {
		if (y >= 530) {
			close();
			return "MISS!";
		}
		if (y >= 480) {
			close();
			return "GOOD!";
		}
		if (y >= 400) {
			close();
			return "PERFECT!";
		}
		if (y >= 380) {
			close();
			return "GOOD!";
		}
		if (y >= 0) {
			close();
			return "EARLY!";
		}
		return "MISS!";
	}
}
