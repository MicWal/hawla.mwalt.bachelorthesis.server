package de.landshut.haw.edu.control;

import de.landshut.haw.edu.ServerMain;

/**
 * Wait for any input on console.
 * @author Michael
 */
public class InputHandler {
	
	/**
	 * Wait for any input on console.
	 */
	public void waitForStartSignal() {
		
		System.out.println("Type anything to start: ");
		
		ServerMain.CONSOLE_IN.nextLine();
	}

	
	/**
	 * Wait for server shutdown signal.
	 */
	public void waitForCloseServerSignal() {
		
		final String keyword= "close"; 
		
		boolean notKeyword = true;
		
		while(notKeyword) {
			
			System.out.println("To close server type '" + keyword + "' into console. When still transmitting data connection will properbly closed.");

			String input = ServerMain.CONSOLE_IN.nextLine();
			
			if(input.equals(keyword)) {
				
				notKeyword = false;
				
			}
		}	
	}
}

