package beat.chlwhdtn;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;

import beat.Main;
import javazoom.jl.player.Player;

public class Music extends Thread {
	private Player player;
	private boolean isrun;
	private String name;
	private File file;
	private InputStream is;
	private BufferedInputStream bis;

	public Music(String name, boolean isrun) {
		try {
			setName(name + " 재생 쓰레드");
			this.isrun = isrun;
			this.name = name;
			is = Main.class.getResourceAsStream("/music/"+name);
			bis = new BufferedInputStream(is);
			player = new Player(bis);

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
				is = Main.class.getResourceAsStream("/music/"+name);
				bis = new BufferedInputStream(this.is);
				player = new Player(this.bis);
			} while (isrun);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
