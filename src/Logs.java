import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

public class Logs {
	String pathToFile;
	
	private CopyOnWriteArrayList<String> list;
	
	public Logs(String filePath) {
		pathToFile = filePath;
		list = new CopyOnWriteArrayList<String>();
	}
	
	public synchronized void log(String message) {
		Date dNow = new Date();
		String now = new SimpleDateFormat ("yyyy.MM.dd - HH:mm:ss").format(dNow);
		String logEntry = String.format("[%s]%s", now, message);
		appendFile(logEntry);
		list.add(logEntry);		
	}
	
	public synchronized String[] getRecentLogs() {
		int size = list.size();
		return list.toArray(new String[size]);
	}
	
	private synchronized void appendFile(String msg) {
		try {
			Path path = Paths.get(pathToFile);
			if (!Files.exists(path)) {
				Files.createFile(path);
			}
			msg = "\n" + msg;
			Files.write(path, msg.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
