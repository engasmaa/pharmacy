package org.openmrs.module.icchange.pharmacy.web.dwr;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.DrugOrder;
import org.openmrs.Patient;
import org.openmrs.api.OrderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.icchange.pharmacy.DrugOrderStatus;
import org.openmrs.module.icchange.pharmacy.api.DrugOrderStatusService;
import org.openmrs.module.icchange.pharmacy.api.PharmacyOrderService;
import org.openmrs.module.icchange.pharmacy.config.PharmacyConfiguration;
import org.openmrs.module.icchange.pharmacy.web.dwr.DWRDrugOrderHeader;
import org.openmrs.util.OpenmrsUtil;

public class DWRDrugOrderHeaderService {

	private OrderService orderService;
	private PharmacyOrderService pharmacyService;
	private DrugOrderStatusService drugOrderStatusService;
	
	public DWRDrugOrderHeaderService () {
		this.orderService = Context.getOrderService();
		this.pharmacyService = Context.getService(PharmacyOrderService.class);
		this.drugOrderStatusService = Context.getService(DrugOrderStatusService.class);
	}
	
	public DWRDrugOrderHeader getDrugOrderHeaderByDrugOrder (DrugOrder d) {
		if (d == null)
			return null;
		
		DrugOrderStatus dstatus = drugOrderStatusService.getDrugOrderStatusByDrugOrder(d);
		DWRDrugOrderHeader dh = new DWRDrugOrderHeader(d, dstatus);
		PharmacyConfiguration config = pharmacyService.getPharmacyConfig();
		Map<String, Set<Integer>> setsMap = config.setConceptMap;

		dh.setDrugSetLabel("");
		dh.setDrugOrderSetIds("");
		
		for (String setName: setsMap.keySet()) {
			if (setsMap.get(setName).contains(d.getConcept().getId())) {
				dh.setDrugSetLabel(dh.getDrugSetLabel() + "," + setName);
				dh.setDrugOrderSetIds(dh.getDrugOrderSetIds() + "," + setName);	
			}
		}
		
		if (dh.getDrugSetLabel().equals("")) {
			dh.setDrugSetLabel("OTHER");
			dh.setDrugOrderSetIds("null");				
		}
						
		return dh;
	}
	
	public DWRDrugOrderHeader getDrugOrderHeaderByDrugOrderId (Integer id) {
		if (id == null)
			return null;
	
		DrugOrder d = orderService.getDrugOrder(id);		
		return getDrugOrderHeaderByDrugOrder(d);
	}

	public List<DWRDrugOrderHeader> getDrugOrderHeadersByPatient (Integer patientId, Date from) throws Exception{
		List<DWRDrugOrderHeader>  ret = new ArrayList<DWRDrugOrderHeader>();
		
		if (patientId == null)
			throw new Exception("A patient must be specified.");

		Patient patient = Context.getPatientService().getPatient(patientId);
		
		if (patient == null)
			throw new Exception("A patient must be specified.");

		Calendar cal = Calendar.getInstance();

		if (from == null) {
			cal.roll(Calendar.MONTH, -pharmacyService.getPharmacyConfig().defaultOrdersAge);
			from = cal.getTime();
		}
		
		List<DrugOrder> drugOrders = orderService.getDrugOrdersByPatient(patient);
		
		if (drugOrders == null)
			return ret;

		for (DrugOrder d: drugOrders) 
			if (d.getDateCreated().after(from))
				ret.add(getDrugOrderHeaderByDrugOrder(d));
		
		return ret;
	}

	
	public List<DWRDrugOrderHeader> getDrugOrderHeadersByPatient (Integer patientId) throws Exception {
		return getDrugOrderHeadersByPatient(patientId, null);
	}
}
