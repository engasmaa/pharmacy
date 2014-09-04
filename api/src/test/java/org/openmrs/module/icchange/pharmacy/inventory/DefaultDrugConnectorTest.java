package org.openmrs.module.icchange.pharmacy.inventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Drug;
import org.openmrs.GlobalProperty;
import org.openmrs.api.context.Context;
import org.openmrs.module.icchange.pharmacy.util.PharmacyConstants;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class DefaultDrugConnectorTest extends BaseModuleContextSensitiveTest {
	
	private InventoryConnector connector;
	private String oldMinCharSearchSize;
	
	@Before
	public void before() throws Exception {
		initializeInMemoryDatabase();
		executeDataSet("src/test/resources/PharmacyTestData.xml");
		connector = DefaultDrugConnector.getInstance();//new DefaultDrugConnector();//
		this.oldMinCharSearchSize = Context.getAdministrationService().getGlobalProperty("minSearchCharacters");
		Context.getAdministrationService().saveGlobalProperty(new GlobalProperty("minSearchCharacters", "3"));
	}

	@After
	public void after () throws Exception {
		Context.getAdministrationService().saveGlobalProperty(new GlobalProperty("minSearchCharacters", this.oldMinCharSearchSize));
	} 
	
	@Test
	public void shouldSetupContext() throws Exception {
		assertNotNull(connector);
	}

	@Test
	public void shouldReturnAllDrugItems () {
		Map<String, Integer> mapNameId = connector.listPharmacyItemsNamesIds();
		List<Drug> drugs  = Context.getConceptService().getAllDrugs();
		
		assertNotNull(drugs);
		assertNotNull(mapNameId);
		
		for (Drug d : drugs) {
			if (!mapNameId.containsKey(d.getName())){
				logger.debug("Fail on find the drug " + d.getName());
				fail("Fail to find the drug " + d.getName());
			}
		}
	}
	
	@Test
	public void shouldReturnTheDrugSpecifiedByThePharse () {
		Map<String, Integer> mapNameId = connector.listPharmacyItemsNamesIds("Aspi");
		
		assertNotNull(mapNameId);
		assertTrue(mapNameId.containsKey("Aspirin"));
		assertNotNull(mapNameId.get("Aspirin"));	
	}
	
	@Test
	public void shouldresadDefaultUnitsFromLitrealSpecification () {
		Context.getAdministrationService().saveGlobalProperty(new GlobalProperty(PharmacyConstants.unitsPropertyName, "{\"literalNames\":[\"kg\", \"mg\"]}"));
		Set<String> unitsSet = new HashSet<String>();
		unitsSet.addAll(DefaultDrugConnector.loadUnitsConfigurationAsJson());
		
		assertEquals(new Integer(2), new Integer(unitsSet.size()));
		assertTrue(unitsSet.contains("kg"));
		assertTrue(unitsSet.contains("mg"));
	}
	
	@Test
	public void shouldresadDefaultUnitsFromConceptNameSpecification () {
		Context.getAdministrationService().saveGlobalProperty(new GlobalProperty(PharmacyConstants.unitsPropertyName, "{\"conceptNames\":[\"UNKNOWN\", \"WT\"]}"));
		Set<String> unitsSet = new HashSet<String>();
		unitsSet.addAll(DefaultDrugConnector.loadUnitsConfigurationAsJson());
		
		assertEquals(new Integer(2), new Integer(unitsSet.size()));
		assertTrue(unitsSet.contains("UNKNOWN"));
		assertTrue(unitsSet.contains("WEIGHT (KG)"));
	}

	@Test
	public void shouldresadDefaultUnitsFromConceptIdsSpecification () {
		Context.getAdministrationService().saveGlobalProperty(new GlobalProperty(PharmacyConstants.unitsPropertyName, "{\"conceptIds\":[5089, 22]}"));
		Set<String> unitsSet = new HashSet<String>();
		unitsSet.addAll(DefaultDrugConnector.loadUnitsConfigurationAsJson());
		
		assertEquals(new Integer(2), new Integer(unitsSet.size()));
		assertTrue(unitsSet.contains("UNKNOWN"));
		assertTrue(unitsSet.contains("WEIGHT (KG)"));
	}

}
