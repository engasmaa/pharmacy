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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.GlobalProperty;
//import org.openmrs.Patient;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
//import org.openmrs.module.icchange.pharmacy.PharmacyItem;
import org.openmrs.module.icchange.pharmacy.PharmacyOrder;
//import org.openmrs.module.icchange.pharmacy.api.PharmacyItemService;
import org.openmrs.module.icchange.pharmacy.api.PharmacyOrderService;
import org.openmrs.module.icchange.pharmacy.api.db.PharmacyOrderDAO;
import org.openmrs.module.icchange.pharmacy.config.PharmacyConfiguration;
import org.openmrs.module.icchange.pharmacy.config.PharmacyConfigurationLoader;
import org.openmrs.module.icchange.pharmacy.config.PharmacyConstants;
import org.openmrs.module.icchange.pharmacy.util.PharmacyOrderUtil;

/**
 * It is a default implementation of {@link PharmacyOrderService}.
 */
public class PharmacyOrderServiceImpl extends BaseOpenmrsService implements PharmacyOrderService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	private PharmacyOrderDAO dao;
	private static PharmacyConfiguration config = null;
	
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
	public final PharmacyConfiguration getPharmacyConfig () {
		if (config == null)
			reloadPharmacyConfig();
		
		return config;
	} 

	public void reloadPharmacyConfig () {
		config = PharmacyConfigurationLoader.getConfiguration();
	}
	
	@Override
	public void onStartup() {
		super.onStartup();
		reloadPharmacyConfig();
	};
	
	@Override
	public boolean supportsPropertyName(String propertyName) {
		for (String pname : PharmacyConstants.pharmacyClobalPropertiesList)
			if (propertyName.equalsIgnoreCase(pname))
				return true;
		
		return false;
	}

	@Override
	public void globalPropertyChanged(GlobalProperty newValue) {
		reloadPharmacyConfig();		
	}

	@Override
	public void globalPropertyDeleted(String propertyName) {
		reloadPharmacyConfig();		
	}

	@Override
	public PharmacyOrder getPharmacyOrder(Integer pharmacyOrderId) {
		return dao.getPharmacyOrder(pharmacyOrderId);
	}
    
	@Override
    public PharmacyOrder getPharmacyOrderByUuid (String uuid) {
    	return dao.getPharmacyOrderByUuid(uuid);
    }
	/***	
	@Override
	public List<PharmacyOrder> getPharmacyOrdersByPatient(Patient patient) {
		return dao.getPharmacyOrderByPatient(patient);		
	}***/

	@Override
	public List<PharmacyOrder> getPharmacyOrdersByDrugOrder(DrugOrder drugOrder) throws APIException{
		if (drugOrder == null)
			throw new APIException("Drug order cannot be null.");
		
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
		/***
		if (pharmacyOrder.getPatient() == null)
			throw new APIException("Pharmacy Order must be associated to a patient.");
		
		if (pharmacyOrder.getEncounter() == null)
			pharmacyOrder.setEncounter(PharmacyOrderUtil.createValidPharmacyEncounter(pharmacyOrder.getPatient()));

		Encounter e = pharmacyOrder.getEncounter();
		e.addOrder(pharmacyOrder);**/

		//Context.getEncounterService().saveEncounter(e);
		PharmacyOrder p = dao.savePharmacyOrder(pharmacyOrder);
		
		return p;
	}
	
	@Override
	public PharmacyOrder addPharmacyOrderToDrugOrder(DrugOrder drugOrder, PharmacyOrder pharmacyOrder) throws APIException {
		
		if (drugOrder == null)
			throw new APIException("Pharmacy Order must be associated to a valid drug order.");
		/**
		if (drugOrder.getPatient() == null)
			throw new APIException("Pharmacy Order must be associated to a patient.");**/
		
		if (pharmacyOrder == null || pharmacyOrder.getDrugOrder() == null)
			pharmacyOrder = PharmacyOrderUtil.createNewPharmacyOrder(drugOrder);
		else { 
			if (drugOrder.getId() != pharmacyOrder.getDrugOrder().getId())
				throw new APIException("This Pharmacy Order is already associated to a drug order.");
		}
		
		if (pharmacyOrder == null)
			throw new APIException("Could not associate the pharmacy order to the a drug order.");
			
		//if (pharmacyOrder.getEncounter() == null)
			//pharmacyOrder.setEncounter(PharmacyOrderUtil.createValidPharmacyEncounter(pharmacyOrder.getPatient()));
		
		pharmacyOrder.setDrugOrder(drugOrder);	
		return savePharmacyOrder(pharmacyOrder);
	}
	/***
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
	}***/
/***
	@Override
	public List<PharmacyOrder> saveAllPharmacyOrdersOnSameEncounter(List<PharmacyOrder> pharmacyOrders, Encounter e) {
	
		if (pharmacyOrders == null)
			throw new APIException("Pharmacy Order cannot be null.");

		if (pharmacyOrders.size() == 0)
			return pharmacyOrders;

		if (pharmacyOrders.get(0) == null)
			throw new APIException("Pharmacy Order cannot be null.");

		if (e == null)
			e = PharmacyOrderUtil.createValidPharmacyEncounter(pharmacyOrders.get(0).getPatient());
		else if (!e.getEncounterType().getName().equalsIgnoreCase(PharmacyConstants.PHARMACYENCOUNTERNAME))
			throw new APIException("The specified encounter must be of type Pharmacy Encounter.");
			
		for (PharmacyOrder p : pharmacyOrders) {
			if (p.getDrugOrder() == null)
				throw new APIException("Pharmacy Order must be associated to a drug order.");
			
			p.setEncounter(e);
			e.addOrder(p);
		}

		Context.getEncounterService().saveEncounter(e);
		return dao.saveAll(pharmacyOrders);		
	}
***/
	/***
	@Override
	public List<Boolean> dispenseItems(DrugOrder drugOrder, List<PharmacyItem> items) throws APIException {
		
		if (drugOrder == null)
			throw new APIException("A valid Drug order must be specified.");
		
		if (items == null)
			throw new APIException("Cannot dispense an empty llist of items");
		
		List<Boolean> ret = new ArrayList<Boolean>(items.size());
		Context.getService(PharmacyItemService.class).lockPharmacy(Context.getAuthenticatedUser());
		Set<PharmacyItem> dispensedItems = new HashSet<PharmacyItem>();
		
		for (PharmacyItem item: items) {
			Boolean status = false;
	
			try {
				status = Context.getService(PharmacyItemService.class).dispenseItem(item.getItemId(), item.getQuantity(), item.getUnit());
			} catch (Exception e) {
				log.warn("An error occuried while trying to save item "+
						item.getItemId() + " " + item.getQuantity() + " " + item.getUnit(), e);
				status = false;
			}
			
			if (status)
				dispensedItems.add(item);
			
			ret.add(status);
		}
		Context.getService(PharmacyItemService.class).unLockPharmacy(Context.getAuthenticatedUser());
		
		PharmacyOrder porder = PharmacyOrderUtil.createNewPharmacyOrder(drugOrder, dispensedItems);
		savePharmacyOrder(porder);
		return ret;
	}***/

}