package de.landshut.haw.edu.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import de.landshut.haw.edu.ServerMain;



/**
 * Load JSON meta data from a file and parse JSON it against JSON schema.
 * @author Michael
 */
public class MetaDataProperties {
	
	private String[] metadataRaw;
	
	public MetaDataProperties() {
		System.out.println("MetaDataProperties created");

	}
	
	
	/** Description of loadProperties() <br>
	 * Load meta data from file.
	 * @param fileName Name of the file.
	 */
	public void loadMetaData(String fileName) {

			metadataRaw = readLines(fileName);
			
			int gameType = selectGameType();
			
			checkJSONAgainSchema(gameType);
			
	}
	
	
	/** Description of readLines() <br>
	 * Read data from file and return content as array.
	 * @param fileName Name of the file.
	 * @return file content as string array
	 */
	public String[] readLines(String fileName) {
		 
		ArrayList<String> string = new ArrayList<String>();
		
	 	try(FileReader fileReader = new FileReader(fileName);
        		BufferedReader bufferedReader = new BufferedReader(fileReader);) {

 	        String line = null;
 	        
 	        while ((line = bufferedReader.readLine()) != null) {
 	        	string.add(line);
 	        }
 	        
        } catch (FileNotFoundException e) {
        	System.err.println("Properties file " + fileName + " not found");
			System.exit(ErrorCodes.FILE_NOT_FOUND_ERR);
			
		} catch (IOException e) {
			System.err.println("Buffered Reader I/O error occured");
			System.exit(ErrorCodes.IO_ERR);
		}
       
        
        return string.toArray(new String[string.size()]);
    }

	
	/**
	 * Console input to decide what game will be analyzed.
	 * @return id of selected game
	 */
	private int selectGameType() {
		
		boolean correctInput = false;
		
		String value;
		
		do {
			System.out.println("Enter number to select game type: ");
			System.out.println("1: Soccer");
			System.out.println("2: AmericanFootball (not implemented)");

			value = ServerMain.CONSOLE_IN.nextLine();
			
			//check if input is allowed number
			correctInput = isAllowedNumber(value);
			
		} while(!correctInput);
		
		return Integer.parseInt(value);
	}
	
	
	/**
	 * Returns meta data in one string.
	 * @return
	 */
	public String[] getMetadata() {
		return metadataRaw;
	}
	
	
	/**
	 * Run the meta data JSON against a schema file. <br>
	 * If valid nothing happens else throws error.
	 * @throws IOException thrown if schema file doesn't exist
	 * @throws ValidationException thrown when meta data are invalid
	 */
	public void checkJSONAgainSchema(int gameType) {
    	
		String schemaFile;

		// select schema file
		schemaFile = selectSchemaFile(gameType);
		
    	try (InputStream schemaStream = getClass().getClassLoader().getResourceAsStream(schemaFile);) {
			
    		JSONObject rawSchema = new JSONObject(new JSONTokener(schemaStream));
    		
            Schema schema = SchemaLoader.load(rawSchema);
            
            StringBuilder builder = new StringBuilder();
            
            for(String s : metadataRaw) {
            	builder.append(s);
            }

            JSONObject jobj = new JSONObject(builder.toString());
           
            schema.validate(jobj); // throws a ValidationException if this object is invalid
            
    	} catch (IOException e) {
    		System.err.println("Schema file " + schemaFile + " not found");
    		System.exit(ErrorCodes.FILE_NOT_FOUND_ERR);
    		
		} catch (ValidationException e) {
			System.err.println("Meta data not error prone or invalid against selected schema.");
			System.exit(ErrorCodes.METADATA_INVALID);
		}
    }

	
	/**
	 * Get schema file name that has to be loaded.
	 * @param gameType 
	 * @return schema file name
	 */
	private String selectSchemaFile(int gameType) {
		
		String schemaFile;
		
		switch (gameType) {
        	case 1:  
        		schemaFile = Constants.SOCCER_SCHEMA_FILE;
                break;
        	default: 
        		schemaFile = Constants.SOCCER_SCHEMA_FILE;
        		break;
		}
		return schemaFile;
	}

	
	/**
	 * Test if a string is a number and if its in a specified range.
	 * @param value
	 * @return Return true if value is a number in the allowed range, else false.
	 */
	private boolean isAllowedNumber(String value) {
	     try {
	        int x = Integer.parseInt(value);
	        
	        return (x >= Constants.MIN_NUMBER_GAMETYPE && x <= Constants.MAX_NUMBER_GAMETYPE);
	        
	     } catch(NumberFormatException e) {
	        return false;
	     }
	 }
	
}

