package locallink.chlwhdtn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayListManager {
	// 이곳에서 파일배열은 편하게 입력하면 됨
	// 1번 재생목록 쓰고싶으면 넘겨주는값 그대로 1 넘기면 됨
	// 배열식으로 숫자 쓰면 안됨 
	private static File dir = new File(System.getenv("appdata") + "/LocalLink/Music/");
	private static File[] file = { 
			new File(System.getenv("appdata") + "/LocalLink/Music/P1.txt"),
			new File(System.getenv("appdata") + "/LocalLink/Music/P2.txt"),
			new File(System.getenv("appdata") + "/LocalLink/Music/P3.txt")
	};
	public static void save() {
		try {

			if(LocalLink.instance.tracklist.size() == 0) {
				System.out.println("재생목록이 비어있습니다.");
				return;
			}
			
			List<String> trackurl = loadTrackURL(1);
			for(int i = 0; i < trackurl.size(); i++) {
				if(trackurl.get(i).equals(LocalLink.instance.tracklist.get(i).musicuri) == false)
					break;
				if(i == trackurl.size() - 1) {
					System.out.println("트랙정보가 바뀌지 않았습니다. 저장하지 않습니다.");
					return;
				}
			}
			
			if(file[2].exists())
				file[2].delete();
			if(file[1].exists())
				file[1].renameTo(file[2]);
			if(file[0].exists()) {
				file[0].renameTo(file[1]);
			} else {
				dir.mkdirs();
			}
			
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(file[0]));
			for(Track music : LocalLink.instance.tracklist) {
				write(bw, music.musicuri);
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void write(BufferedWriter bw, String msg) {
		try {
			bw.write(msg + "\n");
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<Track> loadTrackList(int num) {
		try {
			if(file[num-1].exists() == false)
				return new ArrayList<Track>();
			List<Track> tracklist = new ArrayList<Track>();
			BufferedReader br = new BufferedReader(new FileReader(file[num-1]));
			System.out.println("플레이리스트 파일을 불러옵니다.");
			String s;
			while(br.ready()) {
				if((s =br.readLine()).startsWith("#")) {
					continue;
				}
				tracklist.add(new Track(s));
			}
			br.close();
			return tracklist;
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<Track>();
		}	
	}
	
	public static List<Track> loadHeader() {
		List<Track> tracklist = new ArrayList<Track>();
		try {
			BufferedReader br1 = new BufferedReader(new FileReader(file[0]));
			tracklist.add(new Track(br1.readLine()));
			br1.close();			
			if(file[1].exists() == false)
				return tracklist;
			BufferedReader br2 = new BufferedReader(new FileReader(file[1]));
			tracklist.add(new Track(br2.readLine()));
			br2.close();			
			if(file[2].exists() == false)
				return tracklist;
			BufferedReader br3 = new BufferedReader(new FileReader(file[2]));
			tracklist.add(new Track(br3.readLine()));
			br3.close();			
			return tracklist;
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<Track>();
		}	
	}
	
	public static List<String> loadTrackURL(int num) {
		try {
			if(file[num-1].exists() == false)
				return new ArrayList<String>();
			List<String> tracklist = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new FileReader(file[num-1]));
			System.out.println("플레이리스트 문자열 파일을 불러옵니다.");
			String s;
			while(br.ready()) {
				if((s =br.readLine()).startsWith("#")) {
					continue;
				}
				tracklist.add(s);
			}
			br.close();
			return tracklist;
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}	
	}
}
