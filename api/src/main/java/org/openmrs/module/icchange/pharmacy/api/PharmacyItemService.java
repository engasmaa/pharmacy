package org.openmrs.module.icchange.pharmacy.api;

import java.util.List;
import java.util.Map;

import org.openmrs.api.OpenmrsService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface PharmacyItemService extends OpenmrsService {
	public Map<String, Integer> listPharmacyItemsNamesIds(String pharse);
	public List<String> listDispenseUnitsToItem(Integer itemId);
	public Boolean dispenseItem(Integer itemId, Integer quantity, String unit);
}
