package org.openmrs.module.icchange.pharmacy.inventory;

import java.util.List;
import java.util.Map;

import org.openmrs.User;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface InventoryConnector {
	public void reloadConnector();
	public Map<String, Integer> listPharmacyItemsNamesIds();
	public Map<String, Integer> listPharmacyItemsNamesIds(String pharse);
	public List<String> listDispenseUnitsToItem(Integer itemId);
	public Boolean dispenseItem(Integer itemId, Integer quantity, String unit) throws Exception;
	public Boolean lockInventory(User u);
	public Boolean unLockInventory(User u);
}
