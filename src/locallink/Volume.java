package locallink;


import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javazoom.jl.player.JavaSoundAudioDevice;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Port;
import javax.sound.sampled.Port.Info;

public class Volume extends JavaSoundAudioDevice {

	public static void setVolume(float volume) {
		Info source = Port.Info.SPEAKER;
		if (AudioSystem.isLineSupported(source)) {
			try {
				Port outline = (Port) AudioSystem.getLine(source);
				outline.open();
				FloatControl volumeControl = (FloatControl) outline.getControl(FloatControl.Type.VOLUME);
				volumeControl.setValue(volume);
				outline.close();
			} catch (LineUnavailableException ex) {
				System.err.println("오디오 장치 찾을 수 없음");
				ex.printStackTrace();
			}
		}
	}
	public static int getVolume() {
		Info source = Port.Info.SPEAKER;
		if (AudioSystem.isLineSupported(source)) {
			try {
				Port outline = (Port) AudioSystem.getLine(source);
				outline.open();
				FloatControl volumeControl = (FloatControl) outline.getControl(FloatControl.Type.VOLUME);
				int result = (int) (volumeControl.getValue() * 100);
				outline.close();
				return result;
			} catch (LineUnavailableException ex) {
				System.err.println("오디오 장치 찾을 수 없음");
				ex.printStackTrace();
			}
		}
		return 0;
	}

}