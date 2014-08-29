/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.icchange.pharmacy.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Order;
import org.openmrs.OrderType;
import org.openmrs.Patient;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.icchange.pharmacy.PharmacyOrder;
import org.openmrs.module.icchange.pharmacy.api.ICChangePharmacyService;
import org.openmrs.module.icchange.pharmacy.api.db.PharmacyOrderDAO;
import org.openmrs.module.icchange.pharmacy.util.PharmacyOrderUtil;

/**
 * It is a default implementation of {@link ICChangePharmacyService}.
 */
public class ICChangePharmacyServiceImpl extends BaseOpenmrsService implements ICChangePharmacyService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	private PharmacyOrderDAO dao;
	private static OrderType pharmacyOrderType = null;
	private static EncounterType pharmacyEncounterType = null;

	/**
     * @param dao the dao to set
     */
    public void setDao(PharmacyOrderDAO dao) {
	    this.dao = dao;
    }
    
    /**
     * @return the dao
     */
    public PharmacyOrderDAO getDao() {
	    return dao;
    }

	@Override
	public OrderType getPharmacyOrderType() {
		if (pharmacyOrderType == null)
			for (OrderType t: Context.getOrderService().getAllOrderTypes(false)) {
				if (t.getName().equalsIgnoreCase("Pharmacy Order"))
					pharmacyOrderType = t;
			}
		
		if (pharmacyOrderType == null) {
			OrderType t = new OrderType();
			t.setCreator(Context.getUserService().getUser(1));
			t.setDateCreated(new Date());
			t.setName("Pharmacy Order");
			t.setDescription("Pharmacy Order");
			pharmacyOrderType = Context.getOrderService().saveOrderType(t);
		}
			
		return pharmacyOrderType;
	}

	@Override
	public EncounterType getPharmacyOrderEncounterType() {
		if (pharmacyEncounterType == null)
			for (EncounterType t: Context.getEncounterService().getAllEncounterTypes(false)) {
				if (t.getName().equalsIgnoreCase("Pharmacy Encounter"))
					pharmacyEncounterType = t;
			}
		
		if (pharmacyEncounterType == null) {
			EncounterType t = new EncounterType();
			t.setCreator(Context.getUserService().getUser(1));
			t.setDateCreated(new Date());
			t.setName("Pharmacy Encounter");
			t.setDescription("Pharmacy Encounter");
			pharmacyEncounterType = Context.getEncounterService().saveEncounterType(t);
		}

		return pharmacyEncounterType;
	}

	@Override
	public PharmacyOrder getPharmacyOrder(Integer pharmacyOrderId) {
		return dao.getPharmacyOrder(pharmacyOrderId);
	}
    
	@Override
    public PharmacyOrder getPharmacyOrderByUuid (String uuid) {
    	return dao.getPharmacyOrderByUuid(uuid);
    }
		
	@Override
	public List<PharmacyOrder> getPharmacyOrdersByPatient(Patient patient) {
		return dao.getPharmacyOrderByPatient(patient);		
	}

	@Override
	public List<PharmacyOrder> getPharmacyOrdersByDrugOrder(DrugOrder drugOrder) {
		return dao.getPharmacyOrderByDrugOrder(drugOrder);
	}
	
	@Override
	public List<PharmacyOrder> getPharmacyOrdersByDrugOrders(List<DrugOrder> drugOrders) {
		if(drugOrders == null)
			return null;
		
		if (drugOrders.size() == 0)
			return new ArrayList<PharmacyOrder>();
		
		return dao.getPharmacyOrdersByDrugOrders(drugOrders);	
	}	

	@Override
	public Map<DrugOrder, List<PharmacyOrder>> getPharmacyOrdersOrderedByDrugOrders (List<DrugOrder> drugOrders) {

		Map<DrugOrder, List<PharmacyOrder>> map = null;
		
		if (drugOrders == null)
			return map;
		
		map = new HashMap<DrugOrder, List<PharmacyOrder>>();
		
		List<PharmacyOrder> pharmacyOrders = dao.getPharmacyOrdersByDrugOrders(drugOrders);
		
		for (DrugOrder dOrder: drugOrders) {
			List<PharmacyOrder> pList = new ArrayList<PharmacyOrder>();
			
			for (PharmacyOrder pOrder: pharmacyOrders) {
				if (pOrder.getDrugOrder().getId() == dOrder.getId())
					pList.add(pOrder);
			}
			
			pharmacyOrders.removeAll(pList);
			map.put(dOrder, pList);
		}	
		return map;
	}
	
	
	@Override
	public PharmacyOrder savePharmacyOrder(PharmacyOrder pharmacyOrder) throws APIException {
		
		if (pharmacyOrder == null)
			throw new APIException("Pharmacy Order cannot be null.");
		
		if (pharmacyOrder.getDrugOrder() == null)
			throw new APIException("Pharmacy Order must be associated to a drug order.");
		
		if (pharmacyOrder.getPatient() == null)
			throw new APIException("Pharmacy Order must be associated to a patient.");
		
		if (pharmacyOrder.getEncounter() == null)
			pharmacyOrder.setEncounter(PharmacyOrderUtil.createValidPharmacyEncounter(pharmacyOrder.getPatient()));

		Encounter e = pharmacyOrder.getEncounter();
		e.addOrder(pharmacyOrder);

		Context.getEncounterService().saveEncounter(e);
		PharmacyOrder p = dao.savePharmacyOrder(pharmacyOrder);
		
		return p;
	}
	
	@Override
	public PharmacyOrder addPharmacyOrderToDrugOrder(DrugOrder drugOrder, PharmacyOrder pharmacyOrder) throws APIException {
		
		if (drugOrder == null)
			throw new APIException("Pharmacy Order must be associated to a valid drug order.");

		if (drugOrder.getPatient() == null)
			throw new APIException("Pharmacy Order must be associated to a patient.");
		
		if (pharmacyOrder == null || pharmacyOrder.getDrugOrder() == null)
			pharmacyOrder = PharmacyOrderUtil.createNewPharmacyOrder(drugOrder);
		else { 
			if (drugOrder.getId() != pharmacyOrder.getDrugOrder().getId())
				throw new APIException("This Pharmacy Order is already associated to a drug order.");
		}
		
		if (pharmacyOrder == null)
			throw new APIException("Could not associate the pharmacy order to the a drug order.");
			
		if (pharmacyOrder.getEncounter() == null)
			pharmacyOrder.setEncounter(PharmacyOrderUtil.createValidPharmacyEncounter(pharmacyOrder.getPatient()));
		
		pharmacyOrder.setDrugOrder(drugOrder);	
		return savePharmacyOrder(pharmacyOrder);
	}
	
	@Override
	public List<PharmacyOrder> saveAllPharmacyOrders(List<PharmacyOrder> pharmacyOrders) {
	
		if (pharmacyOrders == null)
			throw new APIException("Pharmacy Order cannot be null.");
		
		if (pharmacyOrders.size() == 0)
			return pharmacyOrders;
		
		if (pharmacyOrders.get(0) == null)
			throw new APIException("Pharmacy Order cannot be null.");
				
		for (PharmacyOrder p : pharmacyOrders) {
			if (p.getDrugOrder() == null)
				throw new APIException("Pharmacy Order must be associated to a drug order.");
			
			if (p.getEncounter() == null)
				p.setEncounter(PharmacyOrderUtil.createValidPharmacyEncounter(p.getPatient()));
		
			p.getEncounter().addOrder(p);
			Context.getEncounterService().saveEncounter(p.getEncounter());
		}
				
		return dao.saveAll(pharmacyOrders);
	}


	@Override
	public List<PharmacyOrder> saveAllPharmacyOrdersOnSameEncounter(List<PharmacyOrder> pharmacyOrders) {
	
		if (pharmacyOrders == null)
			throw new APIException("Pharmacy Order cannot be null.");

		if (pharmacyOrders.size() == 0)
			return pharmacyOrders;

		if (pharmacyOrders.get(0) == null)
			throw new APIException("Pharmacy Order cannot be null.");
		
		Encounter e = pharmacyOrders.get(0).getEncounter();
		
		if (e == null)
			e = PharmacyOrderUtil.createValidPharmacyEncounter(pharmacyOrders.get(0).getPatient());
		
		for (PharmacyOrder p : pharmacyOrders) {
			if (p.getDrugOrder() == null)
				throw new APIException("Pharmacy Order must be associated to a drug order.");
			
			p.setEncounter(e);
			e.addOrder(p);
		}

		Context.getEncounterService().saveEncounter(e);
		return dao.saveAll(pharmacyOrders);		
	}
}