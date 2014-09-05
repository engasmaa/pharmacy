package org.openmrs.module.icchange.pharmacy.api;

import java.util.List;
import java.util.Map;

import org.openmrs.User;
import org.openmrs.api.OpenmrsService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface PharmacyItemService extends OpenmrsService {
	public Boolean lockPharmacy(User u);
	public Boolean unLockPharmacy(User u);
	public Map<String, Integer> listPharmacyItemsNamesIds(String pharse);
	public List<String> listDispenseUnitsToItem(Integer itemId);
	public Boolean dispenseItem(Integer itemId, Integer quantity, String unit);
}
