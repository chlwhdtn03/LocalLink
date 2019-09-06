package beat.chlwhdtn;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import beat.Main;
import javazoom.jl.player.Player;

public class Music extends Thread {
	private Player player;
	private boolean isrun;
	private Track track;
	private File file;
	private InputStream is;
	private BufferedInputStream bis;

	public Music(Track track, boolean isrun) {
		try {
			setName(track.title + " 재생 쓰레드");
			this.isrun = isrun;
			this.track = track;
			is = new FileInputStream(track.musicuri);
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
				is = new FileInputStream(track.musicuri);
				bis = new BufferedInputStream(this.is);
				player = new Player(this.bis);
			} while (isrun);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
