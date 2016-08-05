package de.landshut.haw.edu.control;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import de.landshut.haw.edu.util.Constants;
import de.landshut.haw.edu.util.ErrorCodes;
import de.landshut.haw.edu.util.ServerProperties;

/**
 * Create server socket and accepts new clients while in ACCEPT state.
 * @author Michael
 */
public class ConnectionHandler extends Thread{
	
	
	private ServerSocket serverSocket;
	
	private ClientHandler clientHandler;
	
	private boolean acceptClients;

	
	
	public ConnectionHandler(ClientHandler clientHandler) {
		System.out.println("ConnectionHandler created");
			
		this.clientHandler = clientHandler;
	}
	
	
	
	/**
	 * Create server socket with given parameters.
	 * @param props
	 */
	public void createConnection(ServerProperties props) {
		
		int port;
			
		try {
			
			port = Integer.parseInt(props.getProperty("port"));
			
		} catch (NumberFormatException e) {
			
            port = Constants.DEFAULT_PORT;
        }
	
		try {
            serverSocket = new ServerSocket(port);
            
        } catch (IOException e) {
        	
            System.err.println("Could not listen on port: " + port );
            
            System.exit(ErrorCodes.SERVER_CLOSE_ERR);
            
        }
		
		// start accepting clients
		acceptClients();
	}

	
	
	/**
	 * start accepting client
	 */
	private void acceptClients() {
		
		acceptClients = true;
		
		this.start();
	}
	
	
	
	/** Description of run()
	 *	Thread to accept clients as long as acceptClients is true.
	 */
	@Override
	public void run() {
		
		while(acceptClients) {
			
			Socket clientSocket;
			
			try {
				
				clientSocket = serverSocket.accept();
				
				if(acceptClients) {
					
					clientHandler.addClient(clientSocket);
					
				}
				
			} catch (SocketException e) {
				
				System.err.println("Socket execption. Ignorable error.");
				
			} catch (IOException e) {
				
				System.err.println("I/O error occured while waiting for a connection");
				
				System.exit(ErrorCodes.IO_ERR);
				
			}  
        }
		
		System.out.println("Stop accepting clients");
		
	}
		
	
	
	/**
	 * Stop accepting new client requests.
	 */
	public void stopAcceptClients() {
		
		acceptClients = false;
		
	}
	
	
	
	/**
	 * Explicit close of server socket.
	 */
	public void closeServerSocket() {
		
		if(! serverSocket.isClosed()) {
			
			try {
				
				serverSocket.close();
				
			} catch (IOException e) {
				
				System.err.println("Can't close server socket");
				
				System.exit(ErrorCodes.SOCKET_CLOSE_ERR);
				
			}  
		}
	}
}
