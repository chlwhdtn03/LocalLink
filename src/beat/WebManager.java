package beat;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
	private final List<ServerWebSocket> clients  = new  ArrayList<>();
	public WebManager() {
		server = Vertx.vertx().createHttpServer(new HttpServerOptions().setPort(10426)).requestHandler(req -> {

			try(InputStream in = getClass().getResourceAsStream("/web/index.html")) {
				byte[] data = new byte[1024];
				int size;
				Buffer buffer = Buffer.factory.buffer();
				while((size = in.read(data)) != -1) {
					buffer.appendBytes(data, 0, size);
				}
				req.response().end(buffer);
			} catch(Exception e) {
				req.response().setStatusCode(404).end();
			}
		}).websocketHandler(ws -> {
			ws.closeHandler( v -> {
				if(clients.contains(ws)) {
					clients.remove(ws);
					System.out.println("접속해제");
				}
			});
			
			ws.frameHandler(frame -> {
			
				String order = frame.textData();
				System.out.println(order);
				if(order.equals("connect")) {
					clients.add(ws);
				}
				if(order.equals("nextsong")) {
					JavaBeat.instance.selectRight();
				} else if (order.equals("backsong")) {
					JavaBeat.instance.selectLeft();
				}
			});
		}).listen(10426, result -> {
			if(result.succeeded()) {
				System.out.println("개방 성공");
			} else {
				System.out.println("개방 실패");
			}
		});
		
	}

	public void sendTrack(Track track) {
		JsonObject title = new JsonObject();
		title.addProperty("type", "title");
		title.addProperty("data", track.title);
		JsonObject album = new JsonObject();
		album.addProperty("type", "album");
		album.addProperty("data", track.album);
		JsonObject signer = new JsonObject();
		signer.addProperty("type", "singer");
		signer.addProperty("data", track.artist);
		new ArrayList<>(clients).forEach((client) -> {
			client.writeFinalTextFrame(title.toString());
			client.writeFinalTextFrame(album.toString());
			client.writeFinalTextFrame(signer.toString());
		}); 
	}
}
