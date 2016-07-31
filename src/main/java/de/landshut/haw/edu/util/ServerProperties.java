package de.landshut.haw.edu.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Get properties from config file.
 * @author Michael
 */
public class ServerProperties {
	
	private Properties props;
	
	
	public ServerProperties() {
		System.out.println("ServerProperties created");
		
		props = new Properties();
	}
	
	
	/** Description of loadProperties()
	 * Load properties of the properties file.
	 * @throws FileNotFoundException thrown if properties file doesn't exist
	 * @throws IOException thrown when error occurs while closing InputStream
	 */
	public void loadProperties() {
				
		try (InputStream inputStream = 	getClass().getClassLoader().getResourceAsStream(Constants.CONFIG_FILE);) {
			
			props.load(inputStream);
			
		} catch (FileNotFoundException e) {
			System.err.println("Properties file " + Constants.CONFIG_FILE + " not found");
			System.exit(ErrorCodes.FILE_NOT_FOUND_ERR);
			
		} catch (IOException e) {
			System.err.println("Stream closing failed");
			System.exit(ErrorCodes.STREAM_ERR);
		}
	}
	

	/** Description of getProperty(String key)
	 * 
	 * @param key		Key from which the value is wanted
	 * @return			Value of the key-value pair or "" when key not exist
	 */
	public String getProperty(String key) {
		
		String value = "";
		
		if(props.containsKey(key)) {
			value = props.getProperty(key);
		}
		
		return value;
	}
}
