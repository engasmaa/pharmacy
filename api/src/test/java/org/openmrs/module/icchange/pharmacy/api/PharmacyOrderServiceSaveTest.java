package org.openmrs.module.icchange.pharmacy.api;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.OrderType;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.Visit;
import org.openmrs.api.context.Context;
import org.openmrs.module.icchange.pharmacy.PharmacyItem;
import org.openmrs.module.icchange.pharmacy.PharmacyOrder;
import org.openmrs.module.icchange.pharmacy.util.PharmacyOrderUtil;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class PharmacyOrderServiceSaveTest extends BaseModuleContextSensitiveTest {
	
	private PharmacyOrderService service;
	private Patient testPatient;
	private DrugOrder testDrugOrder;
	
	@Before
	public void before() throws Exception {
		initializeInMemoryDatabase();
		executeDataSet("src/test/resources/PharmacyTestData.xml");
		service = Context.getService(PharmacyOrderService.class);
		testDrugOrder = Context.getOrderService().getOrder(200, DrugOrder.class);
		testPatient = testDrugOrder.getPatient();
		User u = Context.getAuthenticatedUser();
		Context.getUserService().setUserProperty(u, "defaultLocation", "3");
	}
	
	@Test
	public void shouldReturnPhamacyOrderTypeAndCreateIfNone () {
		OrderType t = service.getPharmacyOrderType();
		assertNotNull(t);
		assertEquals("Pharmacy Order", t.getName());
	}
	
	@Test
	public void shouldReturnPhamacyOrderEncounterTypeAndCreateIfNone () {
		EncounterType t = service.getPharmacyOrderEncounterType();
		assertNotNull(t);
		assertEquals("Pharmacy Encounter", t.getName());
	}

	@Test
	public void shouldSavePharmacyOrder () {
		Integer pOrdersBefore = service.getPharmacyOrdersByDrugOrder(testDrugOrder).size();
		PharmacyOrder p = service.savePharmacyOrder(PharmacyOrderUtil.createNewPharmacyOrder(testDrugOrder));
		assertNotNull(p);
		assertNotNull(p.getId());
		assertNotNull(p.getEncounter());
		assertNotNull(p.getEncounter().getLocation());
		assertEquals("Pharmacy Order", p.getOrderType().getName());
		assertEquals("Pharmacy Encounter", p.getEncounter().getEncounterType().getName());
		assertNotNull(p.getEncounter().getLocation());
		assertEquals(new Integer(3), p.getEncounter().getLocation().getId());
	}
	
	@Test
	public void shouldUpdatePharmacyOrderItems () {
		PharmacyOrder porder = service.getPharmacyOrder(300);
		
		assertNotNull(porder);
		Set<PharmacyItem> itemSet;
		int itemsBefore = 0;
		
		if (porder.getItems() != null) {
			itemSet = porder.getItems();
			itemsBefore  = itemSet.size();
		} else 
			itemSet = new HashSet<PharmacyItem>();
			
		itemSet.add(PharmacyOrderUtil.createItem(0, 0, "none"));
		itemSet.add(PharmacyOrderUtil.createItem(1, 0, "none"));
		itemSet.add(PharmacyOrderUtil.createItem(2, 0, "none"));
		itemSet.add(PharmacyOrderUtil.createItem(3, 0, "none"));
		
		porder.setItems(itemSet);
		
		PharmacyOrder p = service.savePharmacyOrder(porder);
		
		assertNotNull(p.getItems());
		assertEquals(new Integer(itemsBefore + 4), new Integer(p.getItems().size()));
	}
}
