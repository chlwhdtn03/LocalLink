package notedata;

import beat.chlwhdtn.Beat;

public interface BeatListener {
	
	public Beat[] getNotes(int startTime, int gap);

}
