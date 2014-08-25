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
import java.util.List;
import java.util.Map;

import org.openmrs.DrugOrder;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.icchange.pharmacy.PharmacyOrder;
import org.openmrs.module.icchange.pharmacy.api.ICChangePharmacyService;
import org.openmrs.module.icchange.pharmacy.api.db.PharmacyOrderDAO;

/**
 * It is a default implementation of {@link ICChangePharmacyService}.
 */
public class ICChangePharmacyServiceImpl extends BaseOpenmrsService implements ICChangePharmacyService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	//public static OrderType pharmacyOrderType;
	
	private PharmacyOrderDAO dao;
	
/*	public ICChangePharmacyServiceImpl () {
		getPharmacyOrderType();
	}
*/	
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
	public PharmacyOrder savePharmacyOrder(PharmacyOrder pharmacyOrder) {
		return dao.savePharmacyOrder(pharmacyOrder);
	}
	
	@Override
	public List<PharmacyOrder> saveAllPharmacyOrders(List<PharmacyOrder> phamacyOrders) {
		return dao.saveAll(phamacyOrders);
	}

	@Override
	public PharmacyOrder getPharmacyOrder(Integer pharmacyOrderId) {
		return dao.getPharmacyOrder(pharmacyOrderId);
	}


	@Override
	public List<PharmacyOrder> getPharmacyOrdersByPatient(Patient patient) {
		List<DrugOrder> drugOrders;

		if (patient == null)
			return null;
					
		drugOrders = Context.getOrderService().getDrugOrdersByPatient(patient);
		
		if (drugOrders == null ) 
			return null;
		
		return getPharmacyOrdersByDrugOrders(drugOrders);		
	}

	@Override
	public Map<DrugOrder, List<PharmacyOrder>> getPharmacyOrdersOrderedByDrugOrders (List<DrugOrder> drugOrders) {
		
		Map<DrugOrder, List<PharmacyOrder>> map = null;
		
		if (drugOrders == null)
			return map;
		
		map = new HashMap<DrugOrder, List<PharmacyOrder>>();
		
		List<PharmacyOrder> pharmacyOrders = this.dao.getPharmacyOrdersByDrugOrders(drugOrders);
		
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
	public List<PharmacyOrder> getPharmacyOrdersByDrugOrders(List<DrugOrder> drugOrders) {
		if(drugOrders == null)
			return null;
		
		if (drugOrders.size() == 0)
			return new ArrayList<PharmacyOrder>();
		
		return dao.getPharmacyOrdersByDrugOrders(drugOrders);	
	}	

	@Override
	public List<PharmacyOrder> getPharmacyOrdersByDrugOrder(DrugOrder drugOrder) {
		
		if (drugOrder == null)
			return null;

		List<DrugOrder> drugOrders = new ArrayList<DrugOrder>();
		drugOrders.add(drugOrder); 
		
		return getPharmacyOrdersByDrugOrders(drugOrders);
	}

/*	@Override
	public OrderType getPharmacyOrderType() {
		
		if (pharmacyOrderType == null)
			for (OrderType t: Context.getOrderService().getAllOrderTypes(false)) {
				if (t.getName().equalsIgnoreCase("Pharmacy Order"))
					pharmacyOrderType = t;
			}
		
		return pharmacyOrderType;
	}*/

}