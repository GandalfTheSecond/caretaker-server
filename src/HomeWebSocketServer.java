import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

// home/{componentName}/turn/{action}
// home/lightLivingRoom/turn/on
// home/livingRoomFireplace/turn/on
// home/livingRoomFireplace/turn/on

public class HomeWebSocketServer extends WebSocketServer{
	HomeOverviewerInterface overviewer;

	public HomeWebSocketServer(HomeOverviewerInterface overviewer, int port) throws UnknownHostException {
		super(new InetSocketAddress(port));
		this.overviewer = overviewer;
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		overviewer.writeLog(conn.toString() + " connected to the WebSocket server.");
		conn.send(""); //This method sends a message to the new client
	}


	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		overviewer.writeLog(conn.toString() + " disconnected from the the WebSocket server.");
	}

	private void handleMessage(String message) {
		overviewer.receive(message);
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		handleMessage(message);
	}

	@Override
	public void onMessage(WebSocket conn, ByteBuffer message) {
		String m = StandardCharsets.UTF_8.decode(message).toString();
		handleMessage(m);
	}


	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
		if (conn != null) {
			// some errors like port binding failed may not be assignable to a specific websocket
		}
	}

	@Override
	public void onStart() {
		overviewer.writeLog("HomeWebSocketServer started!");
		setConnectionLostTimeout(0);
		setConnectionLostTimeout(100);
	}

	public void send(String message) {
		broadcast(message);
	}
}
