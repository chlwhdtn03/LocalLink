package locallink;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.JsonObject;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.ServerWebSocket;
import locallink.Account.AccountManager;
import locallink.chlwhdtn.LocalLink;
import locallink.chlwhdtn.Track;

public class WebManager {

	@SuppressWarnings("unused")
	private HttpServer server;
	public final List<ServerWebSocket> clients = new ArrayList<>();
	public boolean isOpen = false;
	public HashMap<String, File> FileIDList = new HashMap<String, File>();

	public WebManager() {
		server = Vertx.vertx().createHttpServer(new HttpServerOptions().setPort(SettingManager.Port)).requestHandler(req -> {

			try (InputStream in = getClass()
					.getResourceAsStream("/web" + (req.path().equals("/") ? "/index.html" : req.path()))) {
				byte[] data = new byte[1024];
				int size;
				Buffer buffer = Buffer.factory.buffer();
				while ((size = in.read(data)) != -1) {
					buffer.appendBytes(data, 0, size);
				}
				req.response().end(buffer);
			} catch (Exception e) {
				req.response().setStatusCode(404).end();
			}
		}).websocketHandler(ws -> {
			ws.closeHandler(v -> {
				if (clients.contains(ws)) {
					clients.remove(ws);
					System.out.println(ws.remoteAddress() + "에서 접속이 해제되었습니다.");
				}
			});

			ws.frameHandler(frame -> {

				String order = frame.textData();
				String[] cmd = order.split(" ");
				System.out.println(ws.remoteAddress() + "에게 " + order + " 수신");
				if (order.equals("connect")) {
					// 접속 완료 됬을 경우
					clients.add(ws);
					sendTrack(LocalLink.instance.selectedTrack);
				}
				if (cmd[0].equals("nextsong")) {
					LocalLink.instance.selectRight();
				} else if (cmd[0].equals("backsong")) {
					LocalLink.instance.selectLeft();
				} else if (cmd[0].equals("login")) {
					JsonObject login = new JsonObject();
					login.addProperty("type", "login");
					if(AccountManager.login(cmd[1], cmd[2])) {
						login.addProperty("data", "allow");
						login.addProperty("name", AccountManager.getAccount(cmd[1]).name);
					} else {
						login.addProperty("data", "deny");
					}
					ws.writeFinalTextFrame(login.toString());
				} else if(cmd[0].equals("signup")) {
					
					JsonObject signup = new JsonObject();
					signup.addProperty("type", "signup");
					if(AccountManager.containsID(cmd[1])) {
						signup.addProperty("data", "iderr");
					} else {
						try {
							String name = "";
							for(int i = 2; i < cmd.length-1; i++) {
								name += " " + cmd[i];								
							}
							
							AccountManager.addAccount(cmd[1], cmd[cmd.length-1], name.trim());
							signup.addProperty("data", "allow");
						} catch(Exception e) {
							signup.addProperty("data", "deny");
						}
					}
					ws.writeFinalTextFrame(signup.toString());
				} else if(cmd[0].equals("transfer")) {
					if(cmd[1].equals("head")) {
						// : transfer head <ID> <description>
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-kkmmss-");
						File savetmp;
						do {
							savetmp = new File("Download/" + sdf.format(new Date()) + new Random().nextInt(10) + "/");
						} while(savetmp.isDirectory());
						savetmp.mkdirs();
						File headerFile = new File(savetmp.getAbsolutePath() + "/header.txt");
						try {
							FileIDList.put(cmd[2], savetmp);
							BufferedWriter bw = new BufferedWriter(new FileWriter(headerFile));
							bw.write(cmd[2] + "\n");
							bw.flush();
							bw.write(ws.remoteAddress().toString() + "\n");
							bw.flush();
							bw.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if(FileIDList.containsKey(cmd[1])) {
						try {
							String base64 = cmd[2].split(",")[1];
							byte[] Bytes = DatatypeConverter.parseBase64Binary(base64);
							Path destinationFile = Paths.get(FileIDList.get(cmd[1]).getPath(), "test.png");
							Files.write(destinationFile, Bytes);
//							Charset charset = Charset.forName("UTF-8");
//							ByteBuffer bb = charset.encode(cmd[2]);
//							FileChannel fc = new FileOutputStream("test.png").getChannel();
//							fc.write(bb);
//							fc.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
		}).listen(SettingManager.Port, result -> {
			if (result.succeeded()) {
				System.out.println(SettingManager.Port + " 포트로 개방 성공");
				isOpen = true;
			} else {
				System.out.println(SettingManager.Port + " 포트로 개방 실패");
			}
		});

	}

	public void sendTrack(Track track) {
		if (clients.isEmpty())
			return;
		Thread thread = new Thread(() -> {
			JsonObject title = new JsonObject();
			title.addProperty("type", "title");
			try {
				title.addProperty("data", track.title);
			} catch (NullPointerException e) {
				title.addProperty("data", "재생중인 항목 없음");
			}

			JsonObject album = new JsonObject();
			album.addProperty("type", "album");
			try {
				album.addProperty("data", track.album);
			} catch (NullPointerException e) {
				album.addProperty("data", " ");
			}

			JsonObject signer = new JsonObject();
			signer.addProperty("type", "singer");
			try {
				signer.addProperty("data", track.artist);
			} catch (NullPointerException e) {
				signer.addProperty("data", " ");
			}

			JsonObject image = new JsonObject();
			try {
				image.addProperty("type", "artwork");
				image.addProperty("data", encodeToString(track.image));
			} catch (NullPointerException e) {
				image.addProperty("type", "artwork");
				image.addProperty("data", " ");
			}

			JsonObject lyric = new JsonObject();
			lyric.addProperty("type", "lyric");
			try {
				lyric.addProperty("data", track.lyric);
			} catch (NullPointerException e) {
				lyric.addProperty("data", " ");
			}

			clients.forEach((client) -> {
				client.writeFinalTextFrame(title.toString());
				client.writeFinalTextFrame(album.toString());
				client.writeFinalTextFrame(signer.toString());
				client.writeFinalTextFrame(lyric.toString());
				client.writeFinalTextFrame(image.toString());
			});
		});
		thread.setName("웹 데이터 전송");
		thread.start();
	}

	public String encodeToString(Image img) {
		BufferedImage image;
		if (img instanceof BufferedImage) {
			image = (BufferedImage) img;
		}

		// Create a buffered image with transparency
		image = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		// Draw the image on to the buffered image
		Graphics2D bGr = image.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
		String imageString = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			ImageIO.write(image, "png", bos);
			byte[] imageBytes = bos.toByteArray();

			Base64 base64 = new Base64();
			imageString = base64.encodeToString(imageBytes);

			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageString;
	}

}
