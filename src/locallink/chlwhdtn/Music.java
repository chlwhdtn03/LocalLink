package locallink.chlwhdtn;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

import org.jaudiotagger.audio.exceptions.NoWritePermissionsException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class Music extends Thread {
	private Player player;
	private boolean isrun;
	boolean lock = false;
	Object lockobject = new Object();
	private Track track;
	private File file;
	private InputStream is;
	private long skipped = 0;
	private BufferedInputStream bis;
	private SourceDataLine dataLine;

	public Music(Track track, boolean isrun, int skip, boolean lock) {
		try {
			setName(track.title + " 재생 쓰레드");
			this.lock = lock;
			this.isrun = isrun;
			this.track = track;
			is = new FileInputStream(track.musicuri);
			is.skip((skip) * track.byteforsec);
			skipped = ((track.bytelength - is.available()) / track.byteforsec) * 1000;
			bis = new BufferedInputStream(is);
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

	public boolean getLock() {
		return lock;
	}

	public void setLock(boolean bool) {
		if (bool) {
			suspend();
			lock = true;
		} else {
			synchronized (lockobject) {
				resume();
				lock = false;
				lockobject.notifyAll();
			}
		}
	}

	public void close() {
		try {
			isrun = false;
			if (lock == true)
				resume();
			player.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	boolean Finished = false;

	public void run() {
		try {
			do {
				synchronized (lockobject) {
					while (lock)
						try {
							lockobject.wait();
						} catch (InterruptedException e) {
							break;
						}
				}
				player.play();
				skipped = 0;
				if (isrun == false) {
					if (player.isComplete()) {
						close();
						LocalLink.instance.selectRight();
						break;
					}
				}
				is = new FileInputStream(track.musicuri);
				bis = new BufferedInputStream(this.is);
				player = new Player(this.bis);

			} while (isrun);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
