package org.openmrs.module.icchange.pharmacy.web.dwr.model;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.Visit;
import org.openmrs.api.context.Context;
import org.openmrs.module.icchange.pharmacy.DrugOrderStatus;

public class DWRDrugOrderStatus {
	
	private Integer id;
	private Integer patientId;

	private Integer ordererId;
	private String ordererName;
	private String creationDate;

	private Boolean active;
	private String status;
	private String dispenseStatus;
	private String lastDispensed;
	private String lastDispensedBy;
	
	private Double dose;
	private String units;
	private String drugName;
	
	private String drugSetLabel;
	private String drugOrderSetIds;

	private String encounterDate;
	private Integer encounterId;
	
	private String visitDate;
	private Integer visitId;
		
	public DWRDrugOrderStatus () {
		
	}
	
	public DWRDrugOrderStatus (DrugOrder order) {
		active = true;
		status = "current";
		id = order.getId();
		
		if (order.getPatient() != null)
			patientId = order.getPatient().getId();
	
		SimpleDateFormat sdf = Context.getDateFormat();
		creationDate = sdf.format(order.getDateCreated());			
		
		if (order.getOrderer() != null) {
			ordererId = order.getOrderer().getUserId();
			ordererName = order.getOrderer().getPersonName().getFullName();
		} else {
			ordererName = order.getCreator().getPersonName().getFullName();
			ordererId = order.getCreator().getId();
		}

		dose = order.getDose();
		units = order.getUnits();
			
		drugName = order.getDrug().getName();
	
		Date now = new Date();
		
		if (active && order.isVoided()) {
			active = false;
			status = "voided";
		}
		
		if (active && order.getDiscontinued()) {
			active = false;
			status = "discontinued";
		}
		
		if (active && order.getAutoExpireDate() != null &&order.getAutoExpireDate().after(now)) {
			active = false;
			status = "expired";
		}
		
		if (order.getEncounter() != null) {
			Encounter e = order.getEncounter();
			encounterDate = sdf.format(e.getDateCreated());
			encounterId = e.getEncounterId();
			
			if (e.getVisit() != null) {
				Visit v = e.getVisit();
				visitDate = sdf.format(v.getDateCreated());
				visitId = v.getId();
			}
		}
	}

	public DWRDrugOrderStatus (DrugOrder order, DrugOrderStatus status) { 
		this(order);
		this.complementDrugOrder(status);
	}

		
	public void complementDrugOrder(DrugOrderStatus status) {
		if (status == null)
			return;
		
		if (status.getStatus() != null) {
			dispenseStatus = status.getStatus().toString();
			SimpleDateFormat sdf = Context.getDateFormat();
					
			if (status.getDateChanged() == null) {
				lastDispensedBy = status.getCreator().getPersonName().getFullName();
				lastDispensed = sdf.format(status.getDateCreated());
			} else {
				lastDispensedBy = status.getChangedBy().getPersonName().getFullName();
				lastDispensed = sdf.format(status.getDateChanged());
			}
		}
	}
	
	public String toJasonRepresentation () {
		
		StringBuilder ret = new StringBuilder();		
		ret.append("{");

		for (Field f : this.getClass().getDeclaredFields()) {			
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

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	public Integer getOrdererId() {
		return ordererId;
	}
	public void setOrdererId(Integer ordererId) {
		this.ordererId = ordererId;
	}
	public String getOrdererName() {
		return ordererName;
	}
	public void setOrdererName(String ordererName) {
		this.ordererName = ordererName;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public String getDrugName() {
		return drugName;
	}
	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}
	public String getDrugSetLabel() {
		return drugSetLabel;
	}
	public void setDrugSetLabel(String drugSetLabel) {
		this.drugSetLabel = drugSetLabel;
	}
	public String getDispenseStatus() {
		return dispenseStatus;
	}
	public void setDispenseStatus(String dispenseStatus) {
		this.dispenseStatus = dispenseStatus;
	}
	public String getEncounterDate() {
		return encounterDate;
	}
	public void setEncounterDate(String encounterDate) {
		this.encounterDate = encounterDate;
	}
	public Integer getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}
	public String getVisitDate() {
		return visitDate;
	}
	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}
	public Integer getVisitId() {
		return visitId;
	}
	public void setVisitId(Integer visitId) {
		this.visitId = visitId;
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

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDrugOrderSetIds() {
		return drugOrderSetIds;
	}

	public void setDrugOrderSetIds(String drugOrderSetIds) {
		this.drugOrderSetIds = drugOrderSetIds;
	}
}
