import java.io.IOException;
import java.util.Scanner;

public class CliHomeOverviewer implements HomeOverviewerInterface, Runnable {
	private Logs logs;
	private HomeWebSocketServer webSocketServer;
	
	public volatile boolean cont;
	
	private CliHomeOverviewer(int webSocketServerPort, String logsFile) 
			throws IOException {
		
		System.out.println("CliHomeOverviewer constructor begin");
		
		cont = true;
		
		webSocketServer = new HomeWebSocketServer(this, webSocketServerPort);
		
		logs = new Logs(logsFile);
		
		System.out.println("CliHomeOverviewer constructor end");
	}
	
	@Override
	public void send(String message) {
		String parsedMessage = parseMessage(message);
		webSocketServer.send(parsedMessage);
		logs.log("[Sent]" + message);
		System.out.println("[Sending]" + message);
	}
	
	@Override
	public void receive(String message ) {
		System.out.println("[Message received]" + message);
		logs.log("[Received]" + message);
	}
	

	@Override
	public void run() {
		Scanner input = new Scanner(System.in);
		System.out.println("CliHomeOverviewer run started");
		webSocketServer.start();
		while (cont) {
			try {
				Thread.sleep(10000);
				send(input.nextLine());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		input.close();
	}
	
	@Override
	public String[] logs() {
		throw new UnsupportedOperationException("logs()");
	}
	
	@Override
	public void writeLog(String message) {
		System.out.println(message);
		logs.log(message);
	}
	
	private String parseMessage(String message) {
		String data = "{\r\n" + 
				"	            'header': {" + 
				"	                'sender_type': 'home'," + 
				"	                'private_key': '123Soleil'," + 
				"	            },\r\n" + 
				"	            'body': {" +
				"				    {" +
				"						'request': {" +
											"'"+message +"'"+
				"						}" + 
				"					}" +
				"				}" + 
				"	        }";
		return data;

	}
	
	public static void main(String[] args) throws IOException {
		System.out.println("CliHomeOverviewer main started");
		int serverPort = 8888;
		String logsFile = "cliLogs.txt";
		
		CliHomeOverviewer i = new CliHomeOverviewer(serverPort, logsFile);
		Thread iThread = new Thread(i);
		System.out.println("CliHomeOverviewer before starting thread");
		iThread.start();
		
		
	}
}
