package org.openmrs.module.icchange.pharmacy.dwr;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.icchange.pharmacy.api.DrugOrderStatusService;
import org.openmrs.module.icchange.pharmacy.web.dwr.DWRPharmacyOrderService;
import org.openmrs.module.icchange.pharmacy.web.dwr.model.DWRDrugOrderHeader;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class DWRPharmacyOrderTest extends BaseModuleContextSensitiveTest {
	
	private DWRPharmacyOrderService service;
	
	@Before
	public void before() throws Exception {
		initializeInMemoryDatabase();
		executeDataSet("src/test/resources/PharmacyTestData.xml");
		service = new DWRPharmacyOrderService();
	}
	
	@Test
	public void shouldSetupContext() throws Exception {
		assertNotNull(service);
	}

	
}
