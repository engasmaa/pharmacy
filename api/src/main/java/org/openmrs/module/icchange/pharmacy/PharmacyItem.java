package org.openmrs.module.icchange.pharmacy;
import org.openmrs.BaseOpenmrsData;

public class PharmacyItem extends BaseOpenmrsData {
	private Integer id;
	private Integer itemId;
	private Integer quantity;
	private String unit;
	
	@Override
	public Integer getId() {
		return id;
	}
	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
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

	@Override
	public int hashCode() {
		return ("" + id + itemId + quantity).hashCode();
	}
	
	@Override
	public boolean equals (Object obj) {
		if (obj == null)
			return false;


		if (!(obj instanceof PharmacyItem))
			return false;
		
		PharmacyItem other = (PharmacyItem)obj;
		return ((id == other.id) && (itemId == other.itemId) && (quantity == other.quantity));
	}
}
