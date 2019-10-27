package locallink;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SettingManager {
	
	public static boolean Web_Enable = true; // 웹기능 사용 여부.
	public static int Port = 80; // 웹기능 사용 여부를 사용할 경우, 사용할 포트.
	public static boolean Access_Player = false; // 웹에서 로그인하고 권한을 부여받은 계정으로만 플레이어를 제어합니다.
	public static boolean Access_FileTransfer = false; // 웹에서 로그인하고 권한을 부여받은 계정으로만 파일을 전송합니다.

	private static File dir = new File(System.getenv("appdata") + "/LocalLink/");
	public static File file = new File(System.getenv("appdata") + "/LocalLink/Settings.txt");
	
	public static void saveConfig() {
		try {
			if(file.isDirectory() == false)
				dir.mkdirs();
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			write(bw, "##데이터형을 꼭 맞춰주세요.");
			write(bw, "##프로그램의 설정 메뉴에서 관리하는걸 권장합니다.");
			write(bw, "##띄어쓰기를 한번 해준 다음 값을 입력하세요.");
			write(bw, "##해당 파일을 삭제하면 설정값이 초기화됩니다.");
			write(bw, "WebService(Boolean) " + Boolean.toString(Web_Enable));
			write(bw, "Port(Integer) " + Integer.toString(Port));
			write(bw, "Access_Player(Boolean) " + Boolean.toString(Access_Player));
			write(bw, "Access_FileTransfer(Boolean) " + Boolean.toString(Access_FileTransfer));
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
	
	public static void loadConfig() {
		try {
			if(file.exists() == false)
				saveConfig();
			BufferedReader br = new BufferedReader(new FileReader(file));
			System.out.println("Setting 파일을 불러옵니다.");
			System.out.println(" -- " + file.getName() + " -- ");
			String s;
			while(true) {
				if((s =br.readLine()).startsWith("#")) {
					continue;
				}
				Web_Enable = Boolean.parseBoolean(s.split(" ")[1]); // 4번째줄 위 IF문에 readLine 안에 해당 값이 있으니 s 사용 
				Port = Integer.parseInt(br.readLine().split(" ")[1]); // 이제부터 readLine 해주면 됨
				Access_Player = Boolean.parseBoolean(br.readLine().split(" ")[1]); 
				Access_FileTransfer = Boolean.parseBoolean(br.readLine().split(" ")[1]); 
				if(br.ready()) {
					System.out.println("오래된 파일입니다. 기존 설정을 유지시키고 파일을 재생성합니다.");
					saveConfig();
					loadConfig();
				}
				break;
			}
			br.close(); 
			System.out.println(SettingToString());
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	private static String SettingToString() {
		String result = "";
		String s;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while(br.ready()) {
				if((s =br.readLine()).startsWith("#")) {
					continue;
				}
				result += s + "\n"; // 4번째줄 위 IF문에 readLine 안에 해당 값이 있으니 s 사용
			}
			br.close();
			return result.trim();
		} catch (IOException e) {
			return result.trim();
		}
	}
	
}
