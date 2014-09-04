package org.openmrs.module.icchange.pharmacy.web.dwr.model;

import java.util.List;

import org.openmrs.DrugOrder;


public class DWRDrugOrder extends DWROrder {

	private Integer drugId;
	private String drugName;
	private Double dose;
	private String units;
	private String frequency;
	private Boolean prn;
	private Boolean complex;
	private Integer quantity;
	private Integer drugSetId;	
	private String drugSetLabel;

	private List<DWRPharmacyOrder> dispenses;
		
	public DWRDrugOrder () {}

	public DWRDrugOrder (DrugOrder drugOrder) {
		super(drugOrder);
		
		dose = drugOrder.getDose();
		units = drugOrder.getUnits();
		frequency = drugOrder.getFrequency();
		prn = drugOrder.getPrn();
		complex = drugOrder.getComplex();
		quantity = drugOrder.getQuantity();

		if (drugOrder.getDrug() != null)
			drugId = drugOrder.getDrug().getDrugId();
		if (drugOrder.getDrug() != null)
			drugName = drugOrder.getDrug().getName();
				
	}
	
	public List<DWRPharmacyOrder> getDispenses() {
		return dispenses;
	}

	public void setDispenses(List<DWRPharmacyOrder> dispenses) {
		this.dispenses = dispenses;
	}

	public Integer getDrugId() {
		return drugId;
	}

	public void setDrugId(Integer drugId) {
		this.drugId = drugId;
	}

	public String getDrugName() {
		return drugName;
	}

	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}

	public Double getDose() {
		return dose;
	}

	public void setDose(Double dose) {
		this.dose = dose;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public Boolean getPrn() {
		return prn;
	}

	public void setPrn(Boolean prn) {
		this.prn = prn;
	}

	public Boolean getComplex() {
		return complex;
	}

	public void setComplex(Boolean complex) {
		this.complex = complex;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getDrugSetId() {
		return drugSetId;
	}

	public void setDrugSetId(Integer drugSetId) {
		this.drugSetId = drugSetId;
	}

	public String getDrugSetLabel() {
		return drugSetLabel;
	}

	public void setDrugSetLabel(String drugSetLabel) {
		this.drugSetLabel = drugSetLabel;
	}
}
