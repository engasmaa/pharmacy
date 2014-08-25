package org.openmrs.module.icchange.pharmacy;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.openmrs.DrugOrder;

@Entity
@Table(name="pharmacy_drugorderstatus")
public class DrugOrderStatus {

	/*
	@Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	*/
	
	@OneToOne
	@JoinColumn(name = "order_id", unique = true)
	private DrugOrder drugOrder;
	
	@Basic
	@Access(AccessType.PROPERTY)
	@Column(name = "status")
	private Integer status;
	
	@Basic
	@Access(AccessType.PROPERTY)
	@Column(name = "uuid", length = 38, unique = true)
	private String uuid;
	
	public Integer getId() {
		return this.drugOrder.getId();
	}

	public String getUuid() {
		return uuid;
	}

	public DrugOrder getDrugOrder() {
		return drugOrder;
	}

	public void setDrugOrder(DrugOrder drugOrder) {
		this.drugOrder = drugOrder;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
}
