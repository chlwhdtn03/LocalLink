package musicdata;

import locallink.chlwhdtn.Beat;

public interface BeatListener {
	
	public Beat[] getNotes(int startTime, int gap);

}
