package beat.chlwhdtn;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import javazoom.jl.player.Player;

public class Music extends Thread {
	private Player player;
	private boolean isrun;
	private File file;
	private FileInputStream fis;
	private BufferedInputStream bis;

	public Music(String name, boolean isrun) {
		try {
			this.isrun = isrun;
			file = new File(beat.Main.class.getResource("../music/" + name).toURI());
			if (!file.isDirectory())
				file.mkdir();
			fis = new FileInputStream(this.file);
			bis = new BufferedInputStream(this.fis);
			player = new Player(this.bis);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getTime() {
		if (player == null)
			return 0;
		return player.getPosition();
	}

	public void close() {
		isrun = false;
		player.close();
		interrupt();
	}

	public void run() {
		try {
			do {
				player.play();
				fis = new FileInputStream(this.file);
				bis = new BufferedInputStream(this.fis);
				player = new Player(this.bis);
			} while (isrun);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
