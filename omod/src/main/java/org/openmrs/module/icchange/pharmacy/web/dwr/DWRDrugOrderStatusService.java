package org.openmrs.module.icchange.pharmacy.web.dwr;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.openmrs.Concept;
import org.openmrs.DrugOrder;
import org.openmrs.Patient;
import org.openmrs.api.OrderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.icchange.pharmacy.DrugOrderStatus;
import org.openmrs.module.icchange.pharmacy.api.DrugOrderStatusService;
import org.openmrs.module.icchange.pharmacy.api.PharmacyOrderService;
import org.openmrs.module.icchange.pharmacy.config.PharmacyConfiguration;
import org.openmrs.module.icchange.pharmacy.web.dwr.DWRDrugOrderStatus;
import org.openmrs.util.OpenmrsUtil;

public class DWRDrugOrderStatusService {

	private OrderService orderService;
	private PharmacyOrderService pharmacyService;
	private DrugOrderStatusService drugOrderStatusService;
	
	public DWRDrugOrderStatusService () {
		this.orderService = Context.getOrderService();
		this.pharmacyService = Context.getService(PharmacyOrderService.class);
		this.drugOrderStatusService = Context.getService(DrugOrderStatusService.class);
	}
	/***
	public DWRDrugOrderStatus getDrugOrderStatusByDrugOrder (DrugOrder d) {
		
		//DWRDrugOrderStatus dh = new DWRDrugOrderStatus(d, dstatus);
		//PharmacyConfiguration config = pharmacyService.getPharmacyConfig();
		//Map<String, Set<Integer>> setsMap = config.setConceptMap;

		//dh.setDrugSetLabel("");
		//dh.setDrugOrderSetIds("");
		
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
					
		return dstatus;
	}***/
	
	public Vector<DWRDrugOrderStatus> getDrugOrderStatusByDrugOrderId (Integer id) 
	{
		if (id == null)
			return null;
		Vector<DWRDrugOrderStatus> ret = new Vector<DWRDrugOrderStatus>();
		DrugOrderStatus status = drugOrderStatusService.getDrugOrderStatusById(id);
		if (status != null)
		{
			ret.add(new DWRDrugOrderStatus(status));
			return ret;
		}
		else
		{
			return null;
		}
	}

	public Vector<DWRDrugOrderStatus> getDrugOrderStatusByPatient (Integer patientId, Date from) throws Exception{
		Vector<DWRDrugOrderStatus>  ret = new Vector<DWRDrugOrderStatus>();
		
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
				ret = getDrugOrderStatusByDrugOrderId(d.getOrderId());
		
		return ret;
	}

	
	public Vector<DWRDrugOrderStatus> getDrugOrderStatusByPatient (Integer patientId) throws Exception {
		return getDrugOrderStatusByPatient(patientId, null);
	}
}
