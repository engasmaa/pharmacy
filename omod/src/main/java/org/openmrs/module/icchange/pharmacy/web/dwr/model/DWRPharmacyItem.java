package org.openmrs.module.icchange.pharmacy.web.dwr.model;

import org.openmrs.module.icchange.pharmacy.PharmacyItem;

public class DWRPharmacyItem extends DWRBaseOpenmrsData {
	
	private Integer id;
	private Integer quantity;
	private String unit;
	
	public DWRPharmacyItem() {
	}
	
	public DWRPharmacyItem(PharmacyItem item) {
		super(item);
		id = item.getId();
		quantity = item.getQuantity();
		unit = item.getUnit();
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
}
