package org.openmrs.module.icchange.pharmacy.api.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.DrugOrder;
import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.icchange.pharmacy.DrugOrderStatus;
import org.openmrs.module.icchange.pharmacy.api.DrugOrderStatusService;
import org.openmrs.module.icchange.pharmacy.api.db.DrugOrderStatusDao;
import org.springframework.transaction.annotation.Transactional;

public class DrugOrderStatusServiceImpl extends BaseOpenmrsService implements DrugOrderStatusService {

	
	protected final Log log = LogFactory.getLog(this.getClass());
	private DrugOrderStatusDao dao;
	
	@Override
	public void onStartup() {
		super.onStartup();
	}

	@Override
	public void onShutdown() {
		super.onShutdown();
	}

	public DrugOrderStatusDao getDao() {
		return dao;
	}

	public void setDao(DrugOrderStatusDao dao) {
		this.dao = dao;
	}

	@Override
	public DrugOrderStatus getDrugOrderStatusById(Integer id) throws APIException {
		if (id == null)
			throw new APIException("Drug Order Status id cannot be null.");
		
		return dao.getDrugOrderStatusById(id);
	}

	@Override
	public DrugOrderStatus getDrugOrderStatusByUuid(String uuid) throws APIException{
		if (uuid == null)
			throw new APIException("Drug Order Status uuid cannot be null.");;
		
		return dao.getDrugOrderStatusByUuid(uuid);
	}

	@Override
	public DrugOrderStatus getDrugOrderStatusByDrugOrder(DrugOrder drugOrder) throws APIException{
		if (drugOrder == null)
			throw new APIException("Drug order cannot be null.");
		
		return getDrugOrderStatusById(drugOrder.getId());
	}

	@Override
	public DrugOrderStatus saveDrugOrderStatus(DrugOrderStatus drugOrderSratus) throws APIException{
		if (drugOrderSratus == null)
			throw new APIException("Cannot save a null Drug Order Status");

		if (drugOrderSratus.getStatus() == null)
			throw new APIException("Drug Order status cannot be null");
		
		if (drugOrderSratus.getDrugOrder() == null)
			throw new APIException("The Drug Order associated to the Drug Order Status cannot be null");
			
		return dao.saveDrugOrderStatus(drugOrderSratus);
	}
}