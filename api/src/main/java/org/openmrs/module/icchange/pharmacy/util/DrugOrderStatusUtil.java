package org.openmrs.module.icchange.pharmacy.util;

import java.util.Date;

import javax.swing.text.AbstractDocument.Content;

import org.openmrs.DrugOrder;
import org.openmrs.api.context.Context;
import org.openmrs.module.icchange.pharmacy.DrugOrderStatus;
import org.openmrs.module.icchange.pharmacy.DrugOrderStatus.Status;

public class DrugOrderStatusUtil {
	
	public static boolean isValidDrugOrderStatus (DrugOrderStatus d) {
		if (d == null)
			return false;
		
		if (d.getCreator() == null)
			return false;
		
		if (d.getDateCreated() == null)
			return false;

		if (d.getDrugOrder() == null)
			return false;
		
		if (d.getStatus() == null)
			return false;
		
		return true;
	}
	
	public static DrugOrderStatus createNewDrugOrderStatus (DrugOrder drugOrder) {
		if (drugOrder == null)
			return null;
		
		DrugOrderStatus d = new DrugOrderStatus();
		d.setCreator(Context.getAuthenticatedUser());
		d.setDateCreated(new Date());
		d.setDrugOrder(drugOrder);
		d.setStatus(Status.NO_DISPENSE);

		return d;
	}

}
