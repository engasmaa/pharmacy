package org.openmrs.module.icchange.pharmacy;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.DrugOrder;

public class DrugOrderStatus extends BaseOpenmrsData {
	/***
	public enum Status {
		FULLY_DISPENSED, PARTIALLY_DISPENSED, NOT_DISPENSED, N_A
	};
	***/
	private Integer id;
	private DrugOrder drugOrder;
	private String status = "NOT_DISPENSED";
	private String noteToPharmacist;
	//private String noteToPhysicist;
	private String uuid;
	
	
	@Override
	public Integer getId() {
		return id;
	}
	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String getUuid() {
		return uuid;
	}
	
	@Override
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public DrugOrder getDrugOrder() {
		return drugOrder;
	}

	public void setDrugOrder(DrugOrder drugOrder) {
		this.drugOrder = drugOrder;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	/***
	public String getNoteToPhysicist() {
		return noteToPhysicist;
	}
	public void setNoteToPhysicist(String noteToPhysicist) {
		this.noteToPhysicist = noteToPhysicist;
	}***/
	public String getNoteToPharmacist() {
		return noteToPharmacist;
	}
	public void setNoteToPharmacist(String noteToPharmacist) {
		this.noteToPharmacist = noteToPharmacist;
	}	
	
}
