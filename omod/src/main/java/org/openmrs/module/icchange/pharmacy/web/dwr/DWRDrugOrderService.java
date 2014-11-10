package org.openmrs.module.icchange.pharmacy.web.dwr;

import java.util.ArrayList;
import java.util.List;

import org.openmrs.DrugOrder;
import org.openmrs.api.context.Context;
import org.openmrs.module.icchange.pharmacy.PharmacyOrder;
import org.openmrs.module.icchange.pharmacy.api.PharmacyOrderService;
import org.openmrs.module.icchange.pharmacy.web.dwr.DWRDrugOrder;
import org.openmrs.module.icchange.pharmacy.web.dwr.DWRPharmacyOrder;

public class DWRDrugOrderService {
	
	public DWRDrugOrder getDrugOrderById(Integer id) {

		DrugOrder d = Context.getOrderService().getDrugOrder(id);
		
		if (d == null)
			return null;
		
		DWRDrugOrder dw = new DWRDrugOrder(d);
		
		List<PharmacyOrder> pList = Context.getService(PharmacyOrderService.class).getPharmacyOrdersByDrugOrder(d);
		
		if (pList == null)
			return dw;
		
		List<DWRPharmacyOrder> dwplist = new ArrayList<DWRPharmacyOrder>();
		for (PharmacyOrder p: pList)
			dwplist.add(new DWRPharmacyOrder(p));
		
		dw.setDispenses(dwplist);
		
		return dw;
	}
	
	public DWRDrugOrder saveDWRDrugOrder (DWRDrugOrder drugOrder) {		
		return null;
	}

}
