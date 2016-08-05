package de.landshut.haw.edu.control;

import de.landshut.haw.edu.util.Constants;
import de.landshut.haw.edu.util.ServerProperties;

/**
 * Sends data from server to all clients.
 * @author Michael
 */
public class DataHandler extends Thread {
	
	
	private DatabaseHandler dbHandler;
	
	private ClientHandler cHandler;
	
	private boolean activeTransmission;
	
	private ServerProperties props;
	
	
	
	public DataHandler(DatabaseHandler dbHandler, ClientHandler cHandler, ServerProperties props) {
		System.out.println("DataHandler");
		
		this.dbHandler = dbHandler;
		
		this.cHandler = cHandler;
		
		this.props = props;
		
		activeTransmission = true;
	}

	
	
	/**
	 * Initialize sending with a START_TRANSMISSION to all clients.<br>
	 * Then get data from database and send it to all clients.<br>
	 * Signal end of transmission with END_TRANSMISSION.
	 */
	public void dataTransmision() {
		start();
	}
	
	
	
	
	@Override
	public void run() {
		
		final int SECONDS_POW = 1000;
		
		int countBeforePrintln = 0;

		int count = 0;
		
		String[] data;
		
//		ArrayList<ResultLine> data;
		
		long oldTimestamp = dbHandler.getMinTimestamp();

		long endTimestamp = dbHandler.getMaxTimestamp();

		long newTimestamp;
		
//		long elapsedTime;
		long elapsedTime = Long.parseLong(props.getProperty("elapsedTime"));
		
		long snapshotSystemTime;
				
		cHandler.sendToAllClients(Constants.START_TRANSMISSION, dbHandler.getSQLHeaderData());
		
		snapshotSystemTime = System.nanoTime();
		
		if(props.getProperty("countBeforePrintln").matches((Constants.REG_EXP))){
			countBeforePrintln = Integer.parseInt(props.getProperty("countBeforePrintln"));
		}
		
		do {

			cHandler.clearClosedClients();
			
//			elapsedTime = (System.nanoTime() - snapshotSystemTime) * SECONDS_POW;
			
			snapshotSystemTime = System.nanoTime() ;
			
			newTimestamp =  oldTimestamp + elapsedTime;
			
			data = dbHandler.getSQLDataStringArray(oldTimestamp, newTimestamp);

		
			oldTimestamp = newTimestamp;

			if(newTimestamp > endTimestamp) {
				
				activeTransmission = false;
				
			} else if(data != null && data.length > 0) { // only send if data contains actual content
				
				count++;
				
				cHandler.sendToAllClients(Constants.ACTIVE_TRANSMISSION, data);
				
			}
			
			if(count == countBeforePrintln && countBeforePrintln > 0) {
				
				System.out.println(count + " data objects send.");
				
				count = 0;
				
			}
			
			
		} while(activeTransmission);
		
		// send closing data object
		System.out.println("Sending '" + Constants.END_TRANSMISSION + "' signal to clients.");
		
		cHandler.sendToAllClients(Constants.END_TRANSMISSION, "".split(""));
		
		
		// close client sockets
		System.out.print("Attempting to close clients...");
		
		cHandler.closeClients();
		
		System.out.println("closed");
		
		
		// close connection to database
		System.out.print("Attempting to close database connection... ");
		
		dbHandler.closeConnection();
		
		System.out.println("closed.");
		
		System.out.println("Enter keyword to shutdown server.");
		
	}

	

	public synchronized void setActiveTransmission(boolean activeTransmission) {
		this.activeTransmission = activeTransmission;
	}


	
	
}
