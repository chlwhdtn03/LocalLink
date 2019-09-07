package beat.chlwhdtn;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

import beat.Main;
import javazoom.jl.player.Player;

public class Music extends Thread {
	private Player player;
	private boolean isrun;
	private Track track;
	private File file;
	private InputStream is;
	private long skipped = 0;
	private BufferedInputStream bis;
	private SourceDataLine dataLine;

	public Music(Track track, boolean isrun, int skip) {
		try {
			setName(track.title + " 재생 쓰레드");
			this.isrun = isrun;
			this.track = track;
			is = new FileInputStream(track.musicuri);
			bis = new BufferedInputStream(is);
            
			skipped = skip * 1000;
			bis.skip(skip * track.byteforsec);
			player = new Player(bis);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public long getTime() {
		if (player == null)
			return 0;
		return skipped + player.getPosition();
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
				skipped = 0;
				is = new FileInputStream(track.musicuri);
				bis = new BufferedInputStream(this.is);
				player = new Player(this.bis);
			} while (isrun);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
