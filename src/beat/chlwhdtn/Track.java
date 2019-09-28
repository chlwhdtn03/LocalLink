package beat.chlwhdtn;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.tritonus.share.sampled.file.TAudioFileFormat;

import beat.Main;

public class Track {
	public String musicuri;
	public String title;
	public Image image;
	public String artist;
	public String album;
	public String lyric;
	public int duration;
	public long byteforsec;

	public Track(String musicsrc) {
		try {
			this.musicuri = musicsrc;
			AudioFile audioFile = AudioFileIO.read(new File(musicsrc));
			Tag tag = audioFile.getTag();
			this.title = tag.getFirst(FieldKey.TITLE);
			this.artist = tag.getFirst(FieldKey.ARTIST);
			this.album = tag.getFirst(FieldKey.ALBUM);
			this.lyric = tag.getFirst(FieldKey.LYRICS);
			this.duration = audioFile.getAudioHeader().getTrackLength();

			AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(new File(musicsrc));
			byteforsec = fileFormat.getByteLength() / duration;
			if (this.title.isEmpty() || this.title == null) {
				title = new File(musicsrc).getName();
			}
			try {
				this.image = (Image) tag.getFirstArtwork().getImage();
				this.image = this.image.getScaledInstance(800, 800, Image.SCALE_SMOOTH);
			} catch (NullPointerException e) {
				this.image = null;
			}
		} catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException
				| UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
	}

}
