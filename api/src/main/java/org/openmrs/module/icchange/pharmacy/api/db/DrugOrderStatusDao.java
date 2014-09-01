package org.openmrs.module.icchange.pharmacy.api.db;

import org.openmrs.DrugOrder;
import org.openmrs.module.icchange.pharmacy.DrugOrderStatus;

/**
 *  Database methods for {@link DrugOrderStatusService}.
 */
public interface DrugOrderStatusDao {

	public DrugOrderStatus saveDrugOrderStatus (DrugOrderStatus status);
	public DrugOrderStatus getDrugOrderStatusById (Integer id);
	public DrugOrderStatus getDrugOrderStatusByUuid (String uuid);
	public DrugOrderStatus getDrugOrderStatusByDrugOrder (DrugOrder order);
	
}
