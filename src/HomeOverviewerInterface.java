
public interface HomeOverviewerInterface{
	public void send(String message);
	
	public void receive(String message );
	
	public String[] logs();
	
	public void writeLog(String message);
}
