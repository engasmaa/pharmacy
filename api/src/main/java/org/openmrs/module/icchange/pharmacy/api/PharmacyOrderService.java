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
package org.openmrs.module.icchange.pharmacy.api;

import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.OrderType;
import org.openmrs.Patient;
import org.openmrs.module.icchange.pharmacy.PharmacyOrder;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

/**
 * This service exposes module's core functionality. It is a Spring managed bean which is configured in moduleApplicationContext.xml.
 * <p>
 * It can be accessed only via Context:<br>
 * <code>
 * Context.getService(ICChangePharmacyService.class).someMethod();
 * </code>
 * 
 * @see org.openmrs.api.context.Context
 */
@Transactional
public interface PharmacyOrderService extends OpenmrsService {
     
	
	/**
	 * 
	 * @return
	 * @should return the Ordertype relative to PharmacyOrders
	 */
	public OrderType getPharmacyOrderType();

	public EncounterType getPharmacyOrderEncounterType();
	
	public PharmacyOrder getPharmacyOrder(Integer pharmacyOrderId);

	public PharmacyOrder getPharmacyOrderByUuid(String uuid);
	
	public List<PharmacyOrder> getPharmacyOrdersByPatient(Patient patient);

	public List<PharmacyOrder> getPharmacyOrdersByDrugOrder(DrugOrder drugOrder);

	public List<PharmacyOrder> getPharmacyOrdersByDrugOrders(List<DrugOrder> drugOrders);	
	
	public Map<DrugOrder, List<PharmacyOrder>> getPharmacyOrdersOrderedByDrugOrders(List<DrugOrder> drugOrders);
	
	public PharmacyOrder savePharmacyOrder(PharmacyOrder pharmacyOrder) throws APIException;
	
	public List<PharmacyOrder> saveAllPharmacyOrders(List<PharmacyOrder> phamacyOrders);
	
	public List<PharmacyOrder> saveAllPharmacyOrdersOnSameEncounter(List<PharmacyOrder> pharmacyOrders);
	
	
	public PharmacyOrder addPharmacyOrderToDrugOrder(DrugOrder drugOrder, PharmacyOrder pharmacyOrder) throws APIException;


	


}