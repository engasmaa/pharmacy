package org.openmrs.module.icchange.pharmacy.web.model;

import java.text.SimpleDateFormat;

import org.openmrs.Order;
import org.openmrs.api.context.Context;


public class DWROrder extends DWRBaseOpenmrsData {

	
	private Integer patientId;
	private String patientUuid;
	private String instructions;
	private Integer orderTypeId;
	private Integer conceptId;
	private String conceptName;
	private String startDate;
	private String autoExpireDate;
	private Integer encounterId;
	private Integer ordererId;
	private String ordererName;
	private Integer discontinuerId;
	private String discontinuerName;
	private String discontinuedDate;	
	private Integer discontinueReason;
	
	public DWROrder(){}
	
	public DWROrder(Order order){
		super(order);
		patientId = order.getPatient().getId();
		patientUuid = order.getPatient().getUuid();
		instructions = order.getInstructions();
		
		if (order.getOrderType() != null)
			orderTypeId = order.getOrderType().getOrderTypeId();
	
		if (order.getConcept() != null) {
			conceptId = order.getConcept().getConceptId();
			conceptName = order.getConcept().getName().getName();
		}

		SimpleDateFormat sdf = Context.getDateFormat();
		
		if (order.getStartDate() != null)
			startDate = sdf.format(order.getStartDate());
		if (order.getAutoExpireDate() != null)
			autoExpireDate = sdf.format(order.getAutoExpireDate());
		if (order.getEncounter() != null)
			encounterId = order.getEncounter().getEncounterId();
		
		if (order.getOrderer() != null) {
			ordererId = order.getOrderer().getUserId();
			ordererName = order.getOrderer().getName();
		}

		if (order.getDiscontinuedBy() != null) {
			discontinuerId = order.getDiscontinuedBy().getUserId();
			discontinuerName = order.getDiscontinuedBy().getPersonName().getFullName();
		}
		
		if (order.getDiscontinuedDate() != null)
			discontinuedDate = sdf.format(order.getDiscontinuedDate());
		if (order.getDiscontinuedReason() != null)
			discontinueReason = order.getDiscontinuedReason().getConceptId();

	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getPatientUuid() {
		return patientUuid;
	}

	public void setPatientUuid(String patientUuid) {
		this.patientUuid = patientUuid;
	}

	public Integer getOrderTypeId() {
		return orderTypeId;
	}

	public void setOrderTypeId(Integer orderTypeId) {
		this.orderTypeId = orderTypeId;
	}

	public String getConceptName() {
		return conceptName;
	}

	public void setConceptName(String conceptName) {
		this.conceptName = conceptName;
	}

	public Integer getConceptId() {
		return conceptId;
	}

	public void setConceptId(Integer conceptId) {
		this.conceptId = conceptId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getAutoExpireDate() {
		return autoExpireDate;
	}

	public void setAutoExpireDate(String autoExpireDate) {
		this.autoExpireDate = autoExpireDate;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
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

	public Integer getDiscontinuerId() {
		return discontinuerId;
	}

	public void setDiscontinuerId(Integer discontinuerId) {
		this.discontinuerId = discontinuerId;
	}

	public String getDiscontinuerName() {
		return discontinuerName;
	}

	public void setDiscontinuerName(String discontinuerName) {
		this.discontinuerName = discontinuerName;
	}

	public String getDiscontinuedDate() {
		return discontinuedDate;
	}

	public void setDiscontinuedDate(String discontinuedDate) {
		this.discontinuedDate = discontinuedDate;
	}

	public Integer getDiscontinueReason() {
		return discontinueReason;
	}

	public void setDiscontinueReason(Integer discontinueReason) {
		this.discontinueReason = discontinueReason;
	}
	
	
}
