package org.openmrs.module.icchange.pharmacy.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIAuthenticationException;
import org.openmrs.api.APIException;
import org.openmrs.api.context.ServiceContext;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.icchange.pharmacy.api.PharmacyItemService;
import org.openmrs.module.icchange.pharmacy.inventory.InventoryConnector;
import org.springframework.transaction.annotation.Transactional;

public class PharmacyItemServiceImpl extends BaseOpenmrsService implements PharmacyItemService {

	private static final Log log = LogFactory.getLog(ServiceContext.class);
	private InventoryConnector defaultDrugConnector;
	private InventoryConnector connector;
	

	public void loadConnector (String connectorClassName) throws APIException {
		throw new APIAuthenticationException("Operation not supported. Only the default connector is implemented");
	}
	
	@Override
	public void onShutdown() {
		super.onShutdown();
	};
	
	@Override
	public void onStartup() {
		super.onStartup();
		connector = defaultDrugConnector;
	};
	
	@Override
	public Map<String, Integer> listPharmacyItemsNamesIds(String pharse) {
		return connector.listPharmacyItemsNamesIds(pharse);
	}

	@Override
	public List<String> listDispenseUnitsToItem(Integer itemId) {
		return connector.listDispenseUnitsToItem(itemId);
	}

	@Override
	public Boolean dispenseItem(Integer itemId, Integer quantity, String unit) {
		try {
			connector.dispenseItem(itemId, quantity, unit);
		} catch (Exception e) {
			log.debug(e.getMessage());
			return false;
		}
		
		return true;
	}

	public InventoryConnector getDefaultDrugConnector() {
		return defaultDrugConnector;
	}

	public void setDefaultDrugConnector(InventoryConnector defaultDrugConnector) {
		this.defaultDrugConnector = defaultDrugConnector;
	}
}