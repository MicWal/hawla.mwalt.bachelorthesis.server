package de.landshut.haw.edu.model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import de.landshut.haw.edu.util.ErrorCodes;
import de.landshut.haw.edu.util.TransmissionObject;

/**
 * Client handles the output stream of the socket and 
 * send data through the stream.
 * @author Michael
 */
public class Client {

    private Socket socket = null;
        
    private ObjectOutputStream  outObj;
    
    private boolean closed;
      
    
    public Client(Socket socket) {
        System.out.println("Client created");
        
        this.socket = socket;
        
        closed = false;
        
        createStream();
    }

    
    /**
     * Open stream to client socket. 
     */
	private void createStream() {
		
		try {	
			outObj = new ObjectOutputStream(socket.getOutputStream());
			
		} catch (IOException e) {
			System.err.println("Input/Output stream error");
			System.exit(ErrorCodes.STREAM_ERR);
		}
	}

	
    /**
     * Send string data to client socket
     * @param data
     */
    public void send(TransmissionObject data) {
    	
    	try {
//  		System.out.println("send object " + data.getTransmission_status() );
			outObj.writeObject(data);
			outObj.reset();
			
			data.unreferenceArrayList();;
			data = null;
			
		} catch (IOException e) {
			System.err.println("Error when sending object to client. Closing connection.");
			closeConnection();
		}
    }
    
    
    /** Description of closeConnection()
	 * Close streams and then the socket.
	 */
    public void closeConnection() {
    	closed = true;
    	
    	try {
			outObj.close();
			
		} catch (IOException e1) {
			System.err.println("Closing object stream failed. Client seems to be crashed.");
		}
    		  	
	  	if( !socket.isClosed()) {
	  		try {
	  			socket.close();
	  			
			} catch (IOException e) {
				System.err.println("Closing socket failed. Client seems to be crashed.");
			}
	  	}
    }
    
    
    /** Description of getSocket()
	 * 
	 * @return Socket 
	 */
	public Socket getSocket() {
		return socket;
	}


	public boolean isClosed() {
		return closed;
	}

}