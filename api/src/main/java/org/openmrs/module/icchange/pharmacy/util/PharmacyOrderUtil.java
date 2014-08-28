package org.openmrs.module.icchange.pharmacy.util;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.icchange.pharmacy.PharmacyItem;
import org.openmrs.module.icchange.pharmacy.PharmacyOrder;
import org.openmrs.module.icchange.pharmacy.api.ICChangePharmacyService;

public class PharmacyOrderUtil {
	
	public static boolean validatePharmacyOrder (PharmacyOrder porder) {
		
		if (porder == null)
			return false;
		
		if (porder.getPatient() == null || porder.getDrugOrder() == null)
			return false;
		
		return true;
	}

	
	public static PharmacyItem createItem (Integer itemId, Integer quantity, String unit) {
		
		if (itemId == null || quantity == null || unit == null)
			return null;
					
		PharmacyItem item = new PharmacyItem();
		
		item.setCreator(Context.getAuthenticatedUser());
		item.setDateCreated(new Date());

		item.setItemId(itemId);
		item.setQuantity(quantity);
		item.setUnit(unit);
		
		return item;
	}
	
	
	public static Set<PharmacyItem> createItemsSet (List<Integer> items, List<Integer> quantities, List<String> units) {
		if (items == null || quantities == null || units == null)
			return null;
		
		Set<PharmacyItem> itemsSet = new HashSet<PharmacyItem>();
		
		for (int i = 0; i < items.size(); i++) {		
			if (items.size() == i || quantities.size() == i || units.size() == i)
				break;
			
			PharmacyItem item = createItem(items.get(i), quantities.get(i), units.get(i));
			
			if (item != null)
				itemsSet.add(item);
		}
		return itemsSet;
	}

	public static Encounter createValidPharmacyEncounter (Encounter e, Patient p) {
		if (p == null)
			return null;
		
		if (e == null || e.getEncounterType() == null)
			return createValidPharmacyEncounter(p);

		if (e.getEncounterType().getId() != Context.getService(ICChangePharmacyService.class).getPharmacyOrderEncounterType().getId() || e.getLocation() == null)
			return createValidPharmacyEncounter(p);
		
		return null;
	}
	
	public static Encounter createValidPharmacyEncounter (Patient p) {
		if (p == null)
			return null;
		
		Encounter e = new Encounter();
		
		User u = Context.getAuthenticatedUser();
		Location l;
		Date now = new Date();
		
		e.setPatient(p);
		e.setCreator(u);
		e.setDateCreated(now);
		e.setEncounterDatetime(now);
		e.setEncounterType(Context.getService(ICChangePharmacyService.class).getPharmacyOrderEncounterType());

		try {
			l = Context.getLocationService().getLocation(Integer.parseInt(u.getUserProperty("defaultLocation")));
			e.setLocation(l);
		} catch (Exception exception) {}
		
		return e;
	}
	
	public static PharmacyOrder createNewPharmacyOrder (DrugOrder dorder) {
		return PharmacyOrderUtil.createNewPharmacyOrder(dorder, new HashSet<PharmacyItem>());
	} 

	public static PharmacyOrder createNewPharmacyOrder (DrugOrder dorder, Set<PharmacyItem> items) {
		return createNewPharmacyOrder(dorder, items, null);
	}
	
	public static PharmacyOrder createNewPharmacyOrder (DrugOrder dorder, Set<PharmacyItem> items, Encounter e) {
		PharmacyOrder porder = null;
		User u = Context.getAuthenticatedUser();
		Date now;
		
		if (dorder == null || dorder.getPatient() == null)
			return porder;
	
		porder = new PharmacyOrder();
		now = new Date();
		
		porder.setDrugOrder(dorder);
		porder.setPatient(dorder.getPatient());
		porder.setDateCreated(now);
		porder.setStartDate(now);
		porder.setCreator(u);
		porder.setOrderer(u);
		porder.setItems(items);
		porder.setAutoExpireDate(null);
		porder.setConcept(dorder.getDrug().getConcept());
		porder.setOrderType(Context.getService(ICChangePharmacyService.class).getPharmacyOrderType());
		porder.setEncounter(createValidPharmacyEncounter(e, dorder.getPatient()));
		
		return porder;
	} 

}
