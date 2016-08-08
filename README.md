# hawla.mwalt.bachelorthesis.server

Dies ist Teil meiner Bachelorarbeit. Sie soll die Möglichkeit untersuchen ob mit der Hilfe von Sensordaten 
ein Fußballspiel in Echtzeit analysiert werde kann. Genauer kann ein System Ereignisse wie ein Tor in Echtzeit zu erkennen.

In diesem Repository steht der Server welche die Daten an den analysierenden Client schickt.

Wichitg:
  - Server muss mit der URL der Metadaten Datei als Argument gestartet werden.
  Beispiel java -jar GameServer.jar I:\BachelorarbeitCode\metadata2.json
  - Soll der Server in Eclipse gestartet werden muss in package de.landshut.haw.edu.util.Constants der Teil
    //  resource paths for running in ecplise
    //	public final static String SOCCER_SCHEMA_FILE = "soccer_metadata.schema.json";
    //	
    //	public final static String CONFIG_FILE = "config.properties";
    einkommentiert werden. Und der Teil
    	public final static String SOCCER_SCHEMA_FILE = "resources/soccer_metadata.schema.json";
	
	    public final static String CONFIG_FILE = "resources/config.properties";
	  auskommentiert werden. 


Setup:
	1. Erzeuge ein Schema/Datenbank in einer MYSQL Datenbank mit dem Namen 'bachelorthesis'.
	2. Lade die SQL Dump Datei, welche alle Spieldaten enthält, mit dem Befehl in der MYSQL Shell:
			mysql -u root -p[root_password] [database_name] < dumpfilename.sql
	3. Passe in der 'config.properties' Datei die Anmeldedaten für den MYSQL Server an.
		Die Datei befindet sich in der GamerServer.jar unter dem Pfad \src\main\resources\.
		