package beat;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import beat.chlwhdtn.JavaBeat;
import beat.chlwhdtn.Track;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.ServerWebSocket;

public class WebManager {

	private HttpServer server;
	private final List<ServerWebSocket> clients = new ArrayList<>();
	public int port = 10426;
	public boolean isOpen = false;

	public WebManager() {
		server = Vertx.vertx().createHttpServer(new HttpServerOptions().setPort(port)).requestHandler(req -> {

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
				System.out.println(ws.remoteAddress() + "에게 " + order + " 수신");
				if (order.equals("connect")) {
					// 접속 완료 됬을 경우 // 접속 완료 됬을 경우 // 접속 완료 됬을 경우
					clients.add(ws);
					sendTrack(JavaBeat.instance.selectedTrack);
				}
				if (order.equals("nextsong")) {
					JavaBeat.instance.selectRight();
				} else if (order.equals("backsong")) {
					JavaBeat.instance.selectLeft();
				}
			});
		}).listen(port, result -> {
			if (result.succeeded()) {
				System.out.println(port + " 포트로 개방 성공");
				isOpen = true;
			} else {
				System.out.println(port + " 포트로 개방 실패");
			}
		});

	}

	public void sendTrack(Track track) {
		JsonObject title = new JsonObject();
		title.addProperty("type", "title");
		try {
			title.addProperty("data", track.title);
		} catch(NullPointerException e) {
			title.addProperty("data", "재생중인 항목 없음");		
		}
		
		JsonObject album = new JsonObject();
		album.addProperty("type", "album");
		try {
			album.addProperty("data", track.album);
		} catch(NullPointerException e) {
			album.addProperty("data", " ");		
		}
		
		JsonObject signer = new JsonObject();
		signer.addProperty("type", "singer");
		try {
			signer.addProperty("data", track.artist);
		} catch(NullPointerException e) {
			signer.addProperty("data", " ");	
		}
		
		JsonObject image = new JsonObject();
		try {
			image.addProperty("type", "artwork");
			image.addProperty("data", encodeToString(track.image));
		} catch(NullPointerException e) {
			image.addProperty("type", "null");
			image.addProperty("data", " ");			
		}
		
		new ArrayList<>(clients).forEach((client) -> {
			client.writeFinalTextFrame(title.toString());
			client.writeFinalTextFrame(album.toString());
			client.writeFinalTextFrame(signer.toString());
			client.writeFinalTextFrame(image.toString());
		});
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
