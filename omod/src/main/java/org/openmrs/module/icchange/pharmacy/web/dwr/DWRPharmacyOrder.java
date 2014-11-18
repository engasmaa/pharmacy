package org.openmrs.module.icchange.pharmacy.web.dwr;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import org.openmrs.api.context.Context;

import org.openmrs.module.icchange.pharmacy.PharmacyOrder;

public class DWRPharmacyOrder {

	private Integer orderId;
	private String itemName;
	private Integer itemId;
	private Integer quantity;
	private String units;
	private String dispenseDate;
	private String notes;
	
	public DWRPharmacyOrder () {}

	
	public DWRPharmacyOrder (PharmacyOrder order) {
		//super(order);
		this.orderId = order.getDrugOrder().getOrderId();
		this.itemId = order.getItem().getId();
		this.quantity = order.getQuantity();
		this.units = order.getUnits();
		this.notes = order.getNotes(); 
		this.itemName = order.getItem().getName();

		SimpleDateFormat sdf = Context.getDateFormat();
		if (order.getDispenseDate() != null)
			dispenseDate = sdf.format(order.getDispenseDate());
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
	
	public Integer getItemId() {
		return itemId;
	}
	public void setId(Integer itemId) {
		this.itemId = itemId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getDispenseDate() {
		return dispenseDate;
	}
	public void setDispenseDate(String dispenseDate) {
		this.dispenseDate = dispenseDate;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
}
