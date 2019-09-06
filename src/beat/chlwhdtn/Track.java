package beat.chlwhdtn;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import beat.Main;

public class Track {
	public String musicuri;
	public String title;
	public Image image;
	public String artist;
	public String album;
	public String lyric;
	public int duration;

	public Track(String musicsrc) {
		try {
			this.musicuri = musicsrc;
			InputStream in = new FileInputStream(musicsrc);
			BufferedInputStream bis = new BufferedInputStream(in);
			File tmp = File.createTempFile("tmp", ".mp3");
			tmp.deleteOnExit();
			IOUtils.copy(in, new FileOutputStream(tmp));
			in.close();
			bis.close();
			AudioFile audioFile = AudioFileIO.read(tmp);
			Tag tag = audioFile.getTag();
			this.title = tag.getFirst(FieldKey.TITLE);
			this.artist = tag.getFirst(FieldKey.ARTIST);
			this.album = tag.getFirst(FieldKey.ALBUM);
			this.lyric = tag.getFirst(FieldKey.LYRICS);
			this.duration = audioFile.getAudioHeader().getTrackLength();
			if(this.title.isEmpty() || this.title == null) {
				title = tmp.getName();
			}
			this.image = (Image) tag.getFirstArtwork().getImage();
			this.image = this.image.getScaledInstance(800, 800, Image.SCALE_SMOOTH);
		} catch (CannotReadException | IOException | TagException | ReadOnlyFileException
				| InvalidAudioFrameException e) {
			e.printStackTrace();
		}
	}
	
}
