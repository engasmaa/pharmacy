package org.openmrs.module.icchange.pharmacy.web.dwr;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.util.Log;
import org.openmrs.Concept;
import org.openmrs.ConceptSet;
import org.openmrs.DrugOrder;
//import org.openmrs.Encounter;
//import org.openmrs.Patient;
//import org.openmrs.Visit;
import org.openmrs.api.context.Context;
import org.openmrs.module.icchange.pharmacy.PharmacyOrder;
import org.openmrs.module.icchange.pharmacy.api.PharmacyOrderService;
//import org.openmrs.module.icchange.pharmacy.web.dwr.DWRDrugOrder;
//import org.openmrs.module.icchange.pharmacy.web.dwr.DWRDrugOrderHeader;
import org.openmrs.module.icchange.pharmacy.web.dwr.DWRPharmacyOrder;
import org.openmrs.util.OpenmrsUtil;
import java.util.Vector;

public class DWRPharmacyOrderService {

	private PharmacyOrderService service = Context.getService(PharmacyOrderService.class);

	public DWRPharmacyOrder savePharmacyOrder (DWRPharmacyOrder order) throws Exception {
		
		if (order == null)
			return null;
		
		PharmacyOrder porder = new PharmacyOrder();
		Date now = new Date();
		
		
		porder.setCreator(Context.getAuthenticatedUser());
		porder.setDateCreated(now);
		porder.setDispenseDate(now);
		//porder.setAutoExpireDate(null);
		porder.setNotes(order.getNotes());
		
		if (order.getOrderId() == null)
			return null;
		
		DrugOrder d = Context.getOrderService().getDrugOrder(order.getOrderId());
		
		if (d == null)
			return null;
		
		porder.setDrugOrder(d);
		try {
			PharmacyOrderService service = Context.getService(PharmacyOrderService.class);
			service.savePharmacyOrder(porder);
		} catch (Exception e) {
			throw e;
		}
		DWRPharmacyOrder dwrporder = new DWRPharmacyOrder(porder);
		return dwrporder;
		//if (order.getPatientId() == null)
			//return null;
		
		//Patient p = Context.getPatientService().getPatient(order.getPatientId());
		
		//if (p == null)
			//return null;
		
		//porder.setPatient(p);
		/**
		String visitsEnabeled = Context.getAdministrationService().getGlobalProperty("visits.enabled");
		
		if (visitsEnabeled != null && visitsEnabeled.toLowerCase(null).equals("true")) {
			
			List<Visit> vlist = Context.getVisitService().getActiveVisitsByPatient(p);

			if (vlist == null || vlist.size() == 0)
				return null;
			
			if (vlist.size() > 1)
				return null;
			
			Visit v  = vlist.get(0);			
			
			Encounter e = new Encounter();
			
			e.setVisit(v);
			
			//v.get
		} else {
			
			
			
		}
		
		
		return null;	**/
		
	}
	
public Vector<DWRDrugOrder> getAllPharmacyOrdersByPatient(Integer patientId) {
		
		if (patientId == null)
			return null;
		
		Patient patient = Context.getPatientService().getPatient(patientId);
		
		if (patient == null)
			return null;
		
		List<DrugOrder> dOrders = Context.getOrderService().getDrugOrdersByPatient(patient);

		if (dOrders == null)
			return null;
		
		Map<DrugOrder, List<PharmacyOrder>> map = Context.getService(PharmacyOrderService.class).getPharmacyOrdersOrderedByDrugOrders(dOrders);
		
		List<DWRDrugOrder> dwrOrders = new ArrayList<DWRDrugOrder>();
		
		for (DrugOrder key: map.keySet()) {
			DWRDrugOrder dwrOrder = new DWRDrugOrder(key);
			
			List<DWRPharmacyOrder> pList = new ArrayList<DWRPharmacyOrder>();
			
			for (PharmacyOrder p: map.get(key)) {
				if (p != null)
					pList.add(new DWRPharmacyOrder(p));
			}
			
			dwrOrder.setDispenses(pList);
			dwrOrders.add(dwrOrder);
		}
		
		return dwrOrders;

	/***
	public List<DWRDrugOrder> getAllPharmacyOrdersByPatient(Integer patientId) {
		
		if (patientId == null)
			return null;
		
		Patient patient = Context.getPatientService().getPatient(patientId);
		
		if (patient == null)
			return null;
		
		List<DrugOrder> dOrders = Context.getOrderService().getDrugOrdersByPatient(patient);

		if (dOrders == null)
			return null;
		
		Map<DrugOrder, List<PharmacyOrder>> map = Context.getService(PharmacyOrderService.class).getPharmacyOrdersOrderedByDrugOrders(dOrders);
		
		List<DWRDrugOrder> dwrOrders = new ArrayList<DWRDrugOrder>();
		
		for (DrugOrder key: map.keySet()) {
			DWRDrugOrder dwrOrder = new DWRDrugOrder(key);
			
			List<DWRPharmacyOrder> pList = new ArrayList<DWRPharmacyOrder>();
			
			for (PharmacyOrder p: map.get(key)) {
				if (p != null)
					pList.add(new DWRPharmacyOrder(p));
			}
			
			dwrOrder.setDispenses(pList);
			dwrOrders.add(dwrOrder);
		}
		
		return dwrOrders;
	}***/
	

	
	

	public void createPharmacyOrder(Integer drugId) throws Exception{//, Integer patientId) throws Exception{
		//Patient patient = Context.getPatientService().getPatient(patientId);
		DrugOrder drugOrder =  Context.getOrderService().getDrugOrder(drugId);
		
		Log.debug("Enter dwr save pharmacy order.");
		
		//if (patient == null || drugOrder == null)
			//throw new IllegalArgumentException("Invalid patient or drug order id");
		
		
		PharmacyOrder pharmacyOrder = new PharmacyOrder();
		
		pharmacyOrder.setDrugOrder(drugOrder);
		//pharmacyOrder.setPatient(patient);
		
		try {
			PharmacyOrderService service = Context.getService(PharmacyOrderService.class);
			service.savePharmacyOrder(pharmacyOrder);
		} catch (Exception e) {
			throw e;
		}
		
		Log.debug("Exit dwr save pharmacy order");
	}
	
	
	public List<PharmacyOrder> getPharmacyOrdersByDrugOrderId(Integer drugId) throws Exception{
		
		DrugOrder drugOrder = Context.getOrderService().getDrugOrder(drugId);
		
		if (drugOrder == null)
			return new ArrayList<PharmacyOrder>();
		
		return service.getPharmacyOrdersByDrugOrder(drugOrder);
		
	}
	
}
