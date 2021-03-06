package de.landshut.haw.edu.util;

/**
 * Contains all constant variables used in this project.
 * @author Michael
 */
public class Constants {
	
	private Constants() {}
	
	
	public final static String START_TRANSMISSION = "START_TRANSMISSION";
	
	public final static String ACTIVE_TRANSMISSION = "ACTIVE_TRANSMISSION";
	
	public final static String END_TRANSMISSION = "END_TRANSMISSION";
	
	public final static String METADATA_TRANSMISION = "METADATA_TRANSMISION";
	
	public final static String CONNECTION_FAILED = "CONNECTION_FAILED";
		
	public final static String REG_EXP ="\\d*";
	
	/*
	 * 				Miscellaneous
	 */
	
	public final static String SOCCER_SCHEMA_FILE = "resources/soccer_metadata.schema.json";
	
	public final static String CONFIG_FILE = "resources/config.properties";
	
//  resource paths for running in ecplise
//	public final static String SOCCER_SCHEMA_FILE = "soccer_metadata.schema.json";
//	
//	public final static String CONFIG_FILE = "config.properties";
	
	public final static int DEFAULT_PORT = 2360;

	public final static int REQUIRED_ARGUMENTS = 1;
	
	public final static int MIN_NUMBER_GAMETYPE = 1;

	public final static int MAX_NUMBER_GAMETYPE = 1;

	public static final String DELIMETER = " ";

	public static final long DEFAULT_ELAPSED = 5000000000L;

	public static final long DEFAULT_PRINT_COUNT = 50000L;



}
