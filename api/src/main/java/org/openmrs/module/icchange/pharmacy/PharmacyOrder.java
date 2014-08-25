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
package org.openmrs.module.icchange.pharmacy;

import java.io.Serializable;
import java.util.Set;

import org.openmrs.BaseOpenmrsMetadata;
import org.openmrs.BaseOpenmrsObject;
import org.openmrs.DrugOrder;
import org.openmrs.Order;

/**
 * It is a model class. It should extend either {@link BaseOpenmrsObject} or {@link BaseOpenmrsMetadata}.
 */
public class PharmacyOrder extends Order implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private DrugOrder drugOrder;
	private Set<PharmacyItem> items;
	
	public PharmacyOrder() {
		super();
		//this.setOrderType(ICChangePharmacyServiceImpl.pharmacyOrderType);
	}
	
	public DrugOrder getDrugOrder() {
		return drugOrder;
	}

	public void setDrugOrder(DrugOrder drugOrder) {
		this.drugOrder = drugOrder;
	}


	public Set<PharmacyItem> getItems() {
		return items;
	}

	public void setItems(Set<PharmacyItem> items) {
		this.items = items;
	}

}