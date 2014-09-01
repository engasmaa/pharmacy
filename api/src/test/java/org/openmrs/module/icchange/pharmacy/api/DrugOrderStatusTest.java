package org.openmrs.module.icchange.pharmacy.api;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.DrugOrder;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.icchange.pharmacy.DrugOrderStatus;
import org.openmrs.module.icchange.pharmacy.DrugOrderStatus.Status;
import org.openmrs.module.icchange.pharmacy.util.DrugOrderStatusUtil;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class DrugOrderStatusTest extends BaseModuleContextSensitiveTest {
	
	private DrugOrderStatusService service;
	
	@Before
	public void before() throws Exception {
		initializeInMemoryDatabase();
		executeDataSet("src/test/resources/PharmacyTestData.xml");
		service = Context.getService(DrugOrderStatusService.class);
	}
	
	@Test
	public void shouldSetupContext() throws Exception {
		assertNotNull(service);
	}

	@Test 
	public void shouldThrowExceptionOnBadRequests () {
		try {
			service.getDrugOrderStatusById(null);
			fail();			
		} catch (APIException e) {
			logger.debug(e.getMessage());
		} catch (Exception e) {
			fail();
		}

		try {
			service.getDrugOrderStatusByUuid(null);
			fail();			
		} catch (APIException e) {
			logger.debug(e.getMessage());
		} catch (Exception e) {
			fail();
		}
		
		try {
			assertNull(service.getDrugOrderStatusByDrugOrder(null));
			fail();			
		} catch (APIException e) {
			logger.debug(e.getMessage());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void shouldReturnNullDrugOrderStatus () throws Exception {
		assertNull(service.getDrugOrderStatusById(0));
		assertNull(service.getDrugOrderStatusByUuid(""));
	}
	
	@Test
	public void shouldReturnNotNullDrugOrderStatus () throws Exception {		
		assertNotNull(service.getDrugOrderStatusById(200));
		assertNotNull(service.getDrugOrderStatusByUuid("drugorderstatus_1"));
		assertNotNull(service.getDrugOrderStatusByDrugOrder(Context.getOrderService().getDrugOrder(200)));
	}

	@Test
	public void shouldReturnTheSameDrugOrder () {
		DrugOrderStatus s = service.getDrugOrderStatusById(200);
		DrugOrder d = Context.getOrderService().getDrugOrder(200);
		assertNotNull(s);
		assertNotNull(d);
		
		assertEquals(s.getId(), d.getId());
		assertEquals(s.getDrugOrder().getId(), d.getId());
		assertEquals(service.getDrugOrderStatusByDrugOrder(d).getId(), d.getId());
	}

	@Test
	public void shoudThrowAPIExceptionTryingSaveDrugOrderStatus () {
		DrugOrder d = Context.getOrderService().getDrugOrder(201);
		assertNotNull(d);
		
		DrugOrderStatus s = new DrugOrderStatus();
		s.setDrugOrder(d);
		s.setStatus(null);
		
		try {
			s = service.saveDrugOrderStatus(s);
		} catch (APIException e) {
			logger.debug(e.getMessage());
			return;
		}
		fail();
	}

	@Test
	public void shoudThrowAPIExceptionTryingSaveDrugOrderStatus2 () {
		DrugOrderStatus s = new DrugOrderStatus();
		
		try {
			s = service.saveDrugOrderStatus(s);
		} catch (APIException e) {
			logger.debug(e.getMessage());
			return;
		}
		fail();
	}
	
	@Test
	public void shoudSaveDrugOrderStatus () {
		DrugOrder d = Context.getOrderService().getDrugOrder(201);
		assertNotNull(d);
		
		DrugOrderStatus s = new DrugOrderStatus();
		s.setDrugOrder(d);
		s.setStatus(Status.FULL_DISPENSED);
		s = service.saveDrugOrderStatus(s);
		
		assertEquals(s.getId(), d.getId());
		assertEquals(s.getDrugOrder().getId(), d.getId());
		assertEquals(service.getDrugOrderStatusByDrugOrder(d).getId(), d.getId());		
	}
	
	@Test
	public void shouldCreateAValidDrugOrderStatus () {
		DrugOrder d = Context.getOrderService().getDrugOrder(201);
		assertNotNull(d);
		
		if (!DrugOrderStatusUtil.isValidDrugOrderStatus(DrugOrderStatusUtil.createNewDrugOrderStatus(d)))
			fail("Not a valid Drug Order Status was created.");
	}
}
