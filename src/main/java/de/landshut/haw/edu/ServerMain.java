package de.landshut.haw.edu;

import java.util.Scanner;

import de.landshut.haw.edu.control.*;
import de.landshut.haw.edu.util.Constants;
import de.landshut.haw.edu.util.MetaDataProperties;
import de.landshut.haw.edu.util.ServerProperties;

public class ServerMain {

	public static Scanner CONSOLE_IN = new Scanner(System.in);
	
	
	public static void main(String[] args) throws InterruptedException {
		
		if(args.length < Constants.REQUIRED_ARGUMENTS) {
			System.out.println("Missing argument(s). Please add missing argument(s)");
			
		} else {

			InputHandler inHandler = new InputHandler();
			
			// load config data
			ServerProperties properties = new ServerProperties();
			
			properties.loadProperties();
			

			// load meta data
			MetaDataProperties metadata = new MetaDataProperties();
			
			metadata.loadMetaData(args[0]);
			
			
			// establish connection to database
			DatabaseHandler dbHandler = new DatabaseHandler();
			
			dbHandler.init(properties);
			
			
			// management of clients
			ClientHandler cHandler = new ClientHandler(metadata);
					
			
			// create server socket and wait for clients
			ConnectionHandler conHandler = new ConnectionHandler(cHandler);
			
			conHandler.createConnection(properties);
			
			
			// wait until signal to start is given
			inHandler.waitForStartSignal();
			
		
			// start data transmission 
			conHandler.stopAcceptClients();
			
			DataHandler dataHandler = new DataHandler(dbHandler, cHandler, properties);
			
			dataHandler.dataTransmision();
			
			
			// transmitting data finished
			conHandler.closeServerSocket();
			
			Thread.sleep(100);
			
			// user can abort server 
			inHandler.waitForCloseServerSignal();
			
			dataHandler.setActiveTransmission(false);	
			
			CONSOLE_IN.close();
		}
		
	}

}
