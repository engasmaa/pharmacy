package org.openmrs.module.icchange.pharmacy.web.dwr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openmrs.Concept;
import org.openmrs.DrugOrder;
import org.openmrs.api.OrderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.icchange.pharmacy.DrugOrderStatus;
import org.openmrs.module.icchange.pharmacy.api.DrugOrderStatusService;
import org.openmrs.module.icchange.pharmacy.api.PharmacyOrderService;
import org.openmrs.module.icchange.pharmacy.web.dwr.model.DWRDrugOrderHeader;
import org.openmrs.util.OpenmrsUtil;

public class DWRDrugOrderHeaderService {

	private OrderService orderService;
	private PharmacyOrderService pharmacyService;
	private DrugOrderStatusService drugOrderStatusService;
	private Map<Concept, List<Concept>> drugSetConceptsMap;
	
	public DWRDrugOrderHeaderService () {
		this.orderService = Context.getOrderService();
		this.pharmacyService = Context.getService(PharmacyOrderService.class);
		this.drugOrderStatusService = Context.getService(DrugOrderStatusService.class);
		loadDrugSets();
	}

	private void loadDrugSets () {
		String setNamesAsString = Context.getAdministrationService().getGlobalProperty("dashboard.regimen.displayDrugSetIds");
		this.drugSetConceptsMap = new HashMap<Concept, List<Concept>>();

		if (setNamesAsString == null || setNamesAsString.isEmpty())
			setNamesAsString = "ANTIRETROVIRAL DRUGS,TUBERCULOSIS TREATMENT DRUGS";
		
		for (String s : setNamesAsString.split(",")) { 
			Concept c = OpenmrsUtil.getConceptByIdOrName(s);
			if (c != null && c.isSet()) {
				this.drugSetConceptsMap.put(c, Context.getConceptService().getConceptsByConceptSet(c));
			}
		}	
	}
	
	public DWRDrugOrderHeader getDrugOrderHeaderByDrugOrderId (Integer id) {
		if (id == null)
			return null;
	
		DrugOrder d = orderService.getDrugOrder(id);
		DrugOrderStatus dstatus = drugOrderStatusService.getDrugOrderStatusById(id);
		
		if (d == null)
			return null;
		
		DWRDrugOrderHeader dh = new DWRDrugOrderHeader(d, dstatus);
		dh.setDrugSetLabel("OTHER");
		dh.setDrugOrderSetIds("null");
		
		for (Concept c : drugSetConceptsMap.keySet()) {
			List<Concept> drugsInSet= drugSetConceptsMap.get(c);
			for (Concept dconcept: drugsInSet) {
				if (d.getDrug().getConcept().getId() == dconcept.getId()) {
					dh.setDrugSetLabel(dh.getDrugSetLabel() + "," + dconcept.getName().getName());
					dh.setDrugOrderSetIds(dh.getDrugOrderSetIds() + "," + dconcept.getId());
				}
			}
		}
	
		return dh;
	}
	
}
