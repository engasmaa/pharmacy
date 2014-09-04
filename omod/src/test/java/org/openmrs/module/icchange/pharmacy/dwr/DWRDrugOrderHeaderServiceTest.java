package org.openmrs.module.icchange.pharmacy.dwr;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.icchange.pharmacy.web.dwr.DWRDrugOrderHeaderService;
import org.openmrs.module.icchange.pharmacy.web.dwr.model.DWRDrugOrderHeader;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;

public class DWRDrugOrderHeaderServiceTest extends BaseModuleWebContextSensitiveTest {
	
	private DWRDrugOrderHeaderService service;
	
	@Before
	public void before() throws Exception {
		initializeInMemoryDatabase();
		executeDataSet("src/test/resources/PharmacyTestData.xml");
		service = new DWRDrugOrderHeaderService();
	}
	
	@Test
	public void shouldSetupContext() throws Exception {
		assertNotNull(service);
	}

	@Test
	public void shouldResturnAValidDWRDrugOrderHeader () {
		DWRDrugOrderHeader dh = service.getDrugOrderHeaderByDrugOrderId(200);
		assertNotNull(dh);
		assertNotNull(dh.getId());
		assertNotNull(dh.getDrugName());
	}

	
}
