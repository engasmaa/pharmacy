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
package org.openmrs.module.icchange.pharmacy.api.db;

import java.util.List;
import org.openmrs.DrugOrder;
import org.openmrs.Patient;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.icchange.pharmacy.PharmacyOrder;
import org.openmrs.module.icchange.pharmacy.api.PharmacyOrderService;

/**
 *  Database methods for {@link PharmacyOrderService}.
 */
public interface PharmacyOrderDAO {

	public PharmacyOrder getPharmacyOrder(Integer pharmacyOrderId) throws DAOException;
	
	public PharmacyOrder getPharmacyOrderByUuid(String uuid) throws DAOException;

	public List<PharmacyOrder> getPharmacyOrderByPatient(Patient patient) throws DAOException;
	
	public List<PharmacyOrder> getPharmacyOrderByDrugOrder(DrugOrder drugOrder) throws DAOException;
	
	public List<PharmacyOrder> getPharmacyOrdersByDrugOrders(List<DrugOrder> drugOrders) throws DAOException;
	
	
	
	
	
	
	public PharmacyOrder savePharmacyOrder(PharmacyOrder pharmacyOrder) throws DAOException;
	

	public List<PharmacyOrder> saveAll(List<PharmacyOrder> phamacyOrders);
}