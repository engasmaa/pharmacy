package org.openmrs.module.icchange.pharmacy.web.dwr.model;

import java.lang.reflect.Field;

import org.openmrs.module.icchange.pharmacy.PharmacyOrder;

public class DWRPharmacyOrder extends DWROrder {

	private Integer orderId;
	
	public DWRPharmacyOrder () {}

	
	public DWRPharmacyOrder (PharmacyOrder order) {
		super(order);
		this.orderId = order.getDrugOrder().getId();
	}
	
	public String toJasonRepresentation () {
		
		StringBuilder ret = new StringBuilder();		
		ret.append("{");

		for (Field f : this.getClass().getFields()) {			
			Object val = null;
			try { val = f.get(this); } catch (Exception e) { }
		
			if (val != null) {
				ret.append(f.getName());
				ret.append(":\"");		
				ret.append(val.toString());
				ret.append("\",");
			}
		}		
		
		if (ret.length() > 1)
			ret.setCharAt(ret.length() - 1, ' ');
					
		ret.append("}");
		return ret.toString();
	}
		
	@Override
	public String toString () {
		return this.toJasonRepresentation();
	}

	
	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

}
