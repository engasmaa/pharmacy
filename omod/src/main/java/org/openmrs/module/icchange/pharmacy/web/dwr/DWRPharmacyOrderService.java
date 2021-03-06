package org.openmrs.module.icchange.pharmacy.web.dwr;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.util.Log;
import org.openmrs.Concept;
import org.openmrs.ConceptSet;
import org.openmrs.DrugOrder;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.icchange.pharmacy.PharmacyOrder;
import org.openmrs.module.icchange.pharmacy.api.ICChangePharmacyService;
import org.openmrs.module.icchange.pharmacy.web.model.DWRDrugOrder;
import org.openmrs.module.icchange.pharmacy.web.model.DWRDrugOrderHeader;
import org.openmrs.module.icchange.pharmacy.web.model.DWRPharmacyOrder;
import org.openmrs.util.OpenmrsUtil;

public class DWRPharmacyOrderService {

	private ICChangePharmacyService service = Context.getService(ICChangePharmacyService.class);
	
	public List<DWRDrugOrderHeader> getDrugOrdersHeaders (Integer patientId) {

		List<DWRDrugOrderHeader> ret = new ArrayList<DWRDrugOrderHeader>();
		
		if (patientId == null)
			return ret;
		
		List<DrugOrder> orders = Context.getOrderService().getDrugOrdersByPatient(Context.getPatientService().getPatient(patientId));
		
		if (orders == null)
			return ret;
		
		String drugSetsString = Context.getAdministrationService().getGlobalProperty("dashboard.regimen.displayDrugSetIds");
		
		if (drugSetsString == null || drugSetsString.isEmpty())
			drugSetsString = "ANTIRETROVIRAL DRUGS,TUBERCULOSIS TREATMENT DRUGS";
		
		Map<String, List<Concept>> nameConceptsMap = new HashMap<String, List<Concept>>();
		
		for (String s : drugSetsString.split(",")) { 

			Concept c = OpenmrsUtil.getConceptByIdOrName(s);
			
			if (c == null || !c.isSet()) {
				nameConceptsMap.put(s, null);
			}else {
				nameConceptsMap.put(s, Context.getConceptService().getConceptsByConceptSet(c));
			}

		}

		for (DrugOrder order : orders) {
			DWRDrugOrderHeader dh = new DWRDrugOrderHeader(order);
			
			dh.setDrugSetLabel("*");
			
			for (String s: nameConceptsMap.keySet()) {
				
				List<Concept> cs = nameConceptsMap.get(s);
				
				if (cs == null)
					continue;
				
				for (Concept c: cs) {
					if (c.equals(order.getDrug().getConcept())) {
						dh.setDrugSetLabel(dh.getDrugSetLabel() + "," + s);
						break;
					}
				}
			}
			ret.add(dh);
		}

		
		for (DrugOrder o : orders) {

			String s = "*";
			
			List<ConceptSet> cs = Context.getConceptService().getSetsContainingConcept(o.getDrug().getConcept());
			
			if (cs == null)
				continue;
			
			for (ConceptSet c : cs) {
				s += c.getConcept().getName().getName();
			}
		
		}

		
		
		/*
			getSetsContainingConcept(concept)
		 * 
		 * */
		
		/*
		Map<String, Concept> nameConceptMap = new HashMap<String, Concept>();
		Map<String, Collection<ConceptSet>> nameConceptSetMap = new HashMap<String, Collection<ConceptSet>>();
				
		for (String s : drugSetsString.split(",")) {
			
			Concept c = OpenmrsUtil.getConceptByIdOrName(s);
			
			if (c == null || !c.isSet()) {
				nameConceptMap.put(s, null);
				nameConceptSetMap.put(s, null);
			}else {
				nameConceptMap.put(s, c);
				nameConceptSetMap.put(s, c.getConceptSets());
			}
			
		}
		
		
		for (DrugOrder order : orders) {
			DWRDrugOrderHeader dh = new DWRDrugOrderHeader(order);
			
			dh.setDrugSetLabel("*");
			
			for (String s: nameConceptMap.keySet()) {
				
				Concept c = nameConceptMap.get(s);
				
				if (c == null)
					break;
				
				for (ConceptSet cs: nameConceptSetMap.get(s)) {
					if (cs.getConcept().equals(order.getDrug().getConcept())) {
						dh.setDrugSetLabel(dh.getDrugSetLabel() + "," + s);
						break;
					}
				}
			}
			ret.add(dh);
		}

		*/
		
		
		/*
		
		Map<String, List<DrugOrder>> durgOrdersSetMap = OrderUtil.getDrugSetsByDrugSetIdList(orders, drugSetsString, ",");
		
		if (durgOrdersSetMap == null)
			return null;
		
		List<DWRDrugOrderHeader> ret = new ArrayList<DWRDrugOrderHeader>();
		
		for (String setName: durgOrdersSetMap.keySet()) {
			for (DrugOrder drugOrder: durgOrdersSetMap.get(setName)) {
			
				DWRDrugOrderHeader dh = new DWRDrugOrderHeader(drugOrder);
				dh.setDrugSetLabel(setName);
				ret.add(dh);
			}
		}
		*/
		return ret;
	}
	
	public DWRDrugOrder getDrugOrder(Integer id) {

		DrugOrder d = Context.getOrderService().getDrugOrder(id);
		
		if (d == null)
			return null;
		
		DWRDrugOrder dw = new DWRDrugOrder(d);
		
		List<PharmacyOrder> pList = Context.getService(ICChangePharmacyService.class).getPharmacyOrdersByDrugOrder(d);
		
		if (pList == null)
			return dw;
		
		List<DWRPharmacyOrder> dwpliist = new ArrayList<DWRPharmacyOrder>();
		for (PharmacyOrder p: pList)
			dwpliist.add(new DWRPharmacyOrder(p));
		
		dw.setDispenses(dwpliist);
		
		return dw;
	}
	
	public List<DWRDrugOrder> getAllPharmacyOrdersByPatient(Integer patientId) {
		
		if (patientId == null)
			return null;
		
		Patient patient = Context.getPatientService().getPatient(patientId);
		
		if (patient == null)
			return null;
		
		List<DrugOrder> dOrders = Context.getOrderService().getDrugOrdersByPatient(patient);

		if (dOrders == null)
			return null;
		
		Map<DrugOrder, List<PharmacyOrder>> map = Context.getService(ICChangePharmacyService.class).getPharmacyOrdersOrderedByDrugOrders(dOrders);
		
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
	}
	
	public DWRPharmacyOrder savePharmacyOrder (DWRPharmacyOrder order) {
		
		Log.debug("Saving pharmacy order.");
		
		//service.savePharmacyOrder(pharmacyOrder)
		
		return order;
	}
	
	
	
	public void createPharmacyOrder(Integer drugId, Integer patientId) throws Exception{
		Patient patient = Context.getPatientService().getPatient(patientId);
		DrugOrder drugOrder =  Context.getOrderService().getDrugOrder(drugId);
		
		Log.debug("Enter dwr save pharmacy order.");
		
		if (patient == null || drugOrder == null)
			throw new IllegalArgumentException("Invalid patient or drug order id");
		
		
		PharmacyOrder pharmacyOrder = new PharmacyOrder();
		
		pharmacyOrder.setDrugOrder(drugOrder);
		//pharmacyOrder.setPatient(patient);
		
		try {
			ICChangePharmacyService service = Context.getService(ICChangePharmacyService.class);
			service.savePharmacyOrder(pharmacyOrder);
		} catch (Exception e) {
			throw new Exception(e);
		}
		
		Log.debug("Exit dwr save pharmacy order");
	}
	
	
	public List<PharmacyOrder> getPharmacyOerdersByDrugOrderId(Integer drugId) throws Exception{
		
		DrugOrder drugOrder = Context.getOrderService().getDrugOrder(drugId);
		
		if (drugOrder == null)
			return new ArrayList<PharmacyOrder>();
		
		return service.getPharmacyOrdersByDrugOrder(drugOrder);
		
	}
	
}
