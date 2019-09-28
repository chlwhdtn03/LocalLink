package locallink;


public class Timer {

	public static String getTime(int sec) {
		int minute = 0, second = sec;
		String str_min, str_sec;
		for(;;) {
			if(second >= 60) {
				second -= 60;
				minute++;
			} else {
				break;
			}			
		}
		str_min = Integer.toString(minute);
		str_sec = second < 10 ? "0" + Integer.toString(second) : Integer.toString(second);
		return str_min + ":" + str_sec;
	}
	
}
