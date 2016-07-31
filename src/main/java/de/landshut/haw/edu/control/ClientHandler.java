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
	
	
	public ClientHandler(MetaDataProperties metadata) {
		System.out.println("ClientHandler created");
		
		this.metadata = metadata;
		
		clients = new ArrayList<Client>();
	}
	
	
	/**
	 * Send string data to all clients.
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
	 * Send string data to all clients.
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
	 * Remove all clients whose socket is closed.
	 * @return number of deleted clients
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
	 * Add client to list	
	 * @param socket
	 * @return True if adding Client was succesful else false
	 */
	public boolean addClient(Socket socket) {
		
		Client client = new Client(socket);
		client.send( new TransmissionObject(Constants.METADATA_TRANSMISION, metadata.getMetadata()));
			
		return clients.add(client);
	}

	/**
	 * Close all connection from clients in list.
	 */
	public void closeClients() {
		
		for( Client client: clients ) {
			client.closeConnection();
		}
		
	}

	
}
