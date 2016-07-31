package de.landshut.haw.edu.util;

import java.io.Serializable;
import java.util.ArrayList;

public class TransmissionObject implements Serializable {

	private static final long serialVersionUID = Constants.TRANSMISSION_VERSION_UID;
	
	private String transmission_status;

	private String[] content;
	
	private ArrayList<ResultLine> rs;
	
	
	public TransmissionObject(String transmission_status, String[] content) {
		
		this.transmission_status = transmission_status;
		
		this.content = content;
	}
	
	
	public TransmissionObject(String transmission_status, ArrayList<ResultLine> rs) {
		
		this.transmission_status = transmission_status;
		
		this.rs = rs;
	}
	
	
	public TransmissionObject(TransmissionObject transObj) {
		transmission_status = transObj.getTransmission_status();
		
		rs = transObj.getRs();
		
		content = transObj.content;
	}

	
	public String getTransmission_status() {
		return transmission_status;
	}
	

	public String[] getContent() {
		return content;
	}

	
	public ArrayList<ResultLine> getRs() {
		return rs;
	}


	public void unreferenceArrayList() {
		if(rs != null) {
			rs.clear();
			rs = null;
		}	
	}
}
