package de.landshut.haw.edu.control;


import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import de.landshut.haw.edu.model.Client;
import de.landshut.haw.edu.util.Constants;
import de.landshut.haw.edu.util.MetaDataProperties;
import de.landshut.haw.edu.util.ResultLine;
import de.landshut.haw.edu.util.TransmissionObject;


/** Description of ClientHandler <br>
 * Management of client sockets. 	
 * @author Michael Walter
 * @version 1.0 
 */
public class ClientHandler extends Thread {
	
	
	private ArrayList<Client> clients = null;
	
	private MetaDataProperties metadata;
	
	
	/**
	 * Constructor.
	 * @param metadata
	 */
	public ClientHandler(MetaDataProperties metadata) {
		System.out.println("ClientHandler created");
		
		this.metadata = metadata;
		
		clients = new ArrayList<Client>();
	}
	
	
	/**
	 * Send a TransmissionObject object to all connected clients.
	 * @param transmissionStatus Status of the object.
	 * @param data Content to send
	 */
	public void sendToAllClients(String transmissionStatus, String[] data) {
		
		TransmissionObject obj = new TransmissionObject(transmissionStatus, data);

		for( Client client: clients ) {
			
			client.send(obj);
			
		}
		
		obj = null;
	}
	
	
	/**
	 * Send a TransmissionObject object to all connected clients.
	 * @param transmissionStatus Status of the object.
	 * @param data Content to send
	 */
	public void sendToAllClients(String transmissionStatus, ArrayList<ResultLine> data)  {
		
		TransmissionObject obj = new TransmissionObject(transmissionStatus, data);
		
		for( Client client: clients ) {
			
			client.send(obj);
			
		}
		
		obj = null;
	}
	
	
	/**
	 * Remove closed clients from clients list.
	 */
	public void clearClosedClients() {

		for (Iterator<Client> iterator = clients.iterator(); iterator.hasNext(); ) {
			
			Client client = iterator.next();
		    
			//is one of the socket closed remove him from client list
			if (client.isClosed() == true) {
				
		    	iterator.remove();	
		    	
		    }
		}
	}
	
	
	
	/**
	 * Add a client to clients list.	
	 * @param socket Socket of client.
	 * @return <b>true</b> if adding Client was successful else <b>false</b>
	 */
	public boolean addClient(Socket socket) {
		
		Client client = new Client(socket);
		
		client.send( new TransmissionObject(Constants.METADATA_TRANSMISION, metadata.getMetadata()));
			
		return clients.add(client);
	}

	
	/**
	 * Close connection from all clients in clients list.
	 */
	public void closeClients() {
		
		for( Client client: clients ) {
			
			client.closeConnection();
			
		}

	}

	
}
