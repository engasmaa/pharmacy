package org.openmrs.module.icchange.pharmacy.web.model;

import org.openmrs.module.icchange.pharmacy.PharmacyOrder;

public class DWRPharmacyOrder extends DWROrder {

	private Integer orderId;
	
	public DWRPharmacyOrder () {}

	
	public DWRPharmacyOrder (PharmacyOrder order) {
		super(order);
		this.orderId = order.getDrugOrder().getId();
	}
	
	
	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

}
