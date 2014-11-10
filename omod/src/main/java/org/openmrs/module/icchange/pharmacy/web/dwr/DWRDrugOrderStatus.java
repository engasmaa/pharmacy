package org.openmrs.module.icchange.pharmacy.web.dwr;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

//import org.openmrs.DrugOrder;
//import org.openmrs.Encounter;
//import org.openmrs.Visit;
import org.openmrs.api.context.Context;
import org.openmrs.module.icchange.pharmacy.DrugOrderStatus;

public class DWRDrugOrderStatus {
	
	private Integer id;
	private String status;
		
	public DWRDrugOrderStatus () {
		
	}

	public DWRDrugOrderStatus (DrugOrderStatus status) { 		
		if (status.getStatus() != null) {
			this.status = status.getStatus().toString();
		}
		if (status.getId() != null) 
		{
			id = status.getId();			
		}
		//this.complementDrugOrder(status);
	}

	/***
	public String toJasonRepresentation () {
		
		StringBuilder ret = new StringBuilder();		
		ret.append("{");

		for (Field f : this.getClass().getDeclaredFields()) {			
			Object val = null;
			try { val = f.get(this); } catch (Exception e) { }
		
			if (val != null) {
				ret.append(f.getName());
				ret.append(":\"");		
				ret.append(val.toString());
				ret.append("\",");
			}
		}		
		
		if (ret.length() > 1)
			ret.setCharAt(ret.length() - 1, ' ');
					
		ret.append("}");
		return ret.toString();
	}***/
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
