package org.openmrs.module.icchange.pharmacy.api;

import org.openmrs.DrugOrder;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.icchange.pharmacy.DrugOrderStatus;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface DrugOrderStatusService extends OpenmrsService{

	public DrugOrderStatus getDrugOrderStatusById(Integer id)  throws APIException;
	public DrugOrderStatus getDrugOrderStatusByUuid(String uuid)  throws APIException;
	public DrugOrderStatus getDrugOrderStatusByDrugOrder(DrugOrder drugOrder)  throws APIException;
	public DrugOrderStatus saveDrugOrderStatus (DrugOrderStatus drugOrderSratus)  throws APIException;
	
}
 