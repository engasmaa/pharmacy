/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.icchange.pharmacy.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.DrugOrder;
import org.openmrs.OrderType;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.icchange.pharmacy.PharmacyOrder;
import org.openmrs.test.BaseModuleContextSensitiveTest;

/**
 * Tests {@link ${ICChangePharmacyService}}.
 */
public class  PharmacyOrderServiceTest extends BaseModuleContextSensitiveTest {
	
	private PharmacyOrderService service;
	
	@Before
	public void before() throws Exception {
		initializeInMemoryDatabase();
		executeDataSet("src/test/resources/PharmacyTestData.xml");
		service = Context.getService(PharmacyOrderService.class);
	}
	
	
	@Test
	public void shouldSetupContext() throws Exception {
		assertNotNull(service);
	}
	
	@Test
	public void shouldEnsurePharmacyOrderTypeExist () throws Exception {
		logger.warn("Testing pharmacy order type");
		
		OrderType type = service.getPharmacyOrderType();
		assertNotNull(type);
		
		logger.warn("id= " + type.getId() + " " + type.getName());
		
		assertEquals(new Integer(18), type.getId());
		assertEquals("Pharmacy Order", type.getName());
	}
	
	@Test
	public void shouldEnsurePharmacyOrderExist () throws Exception {
		logger.warn("Looking for test Pharmacy order");

		PharmacyOrder order = service.getPharmacyOrder(300);
		
		assertNotNull(order);
		assertEquals(new Integer(300), order.getId());
		assertNotNull(order.getDrugOrder());
		assertNotNull(order.getDrugOrder().getId());	
	}
	
	@Test
	public void shouldReturnPharmacyOrderByUuid () {
		logger.warn("Looking for test Pharmacy order");
		
		PharmacyOrder order = service.getPharmacyOrderByUuid("300");
		
		assertNotNull(order);
		assertEquals(new Integer(300), order.getId());
		assertEquals("300", order.getUuid());
		assertNotNull(order.getDrugOrder());
		assertNotNull(order.getDrugOrder().getId());
		
		assertEquals(new Integer(200), order.getDrugOrder().getId());
		
	}
	
	@Test
	public void shouldReturnNullPharmacyOrderByUuid () {
		logger.warn("Looking for test Pharmacy order");
		
		PharmacyOrder order = service.getPharmacyOrderByUuid(null);
		assertNull(order);
	}

	@Test
	public void shouldReturnNullPharmacyOrderByUuid2 () {
		logger.warn("Looking for test Pharmacy order");
		
		PharmacyOrder order = service.getPharmacyOrderByUuid("");
		
		assertNull(order);
	}

	@Test
	public void shouldReturnPharmacyOrdersGivenPatient () {
		Patient p = Context.getPatientService().getPatient(2);
		assertNotNull(p);
		List<PharmacyOrder> orders = service.getPharmacyOrdersByPatient(p);
		assertNotNull(orders);
		assertEquals(new Integer(3), new Integer(orders.size()));
	}

	@Test
	public void shouldReturnNoPharmacyOrdersGivenPatient () {
		Patient p = Context.getPatientService().getPatient(7);
		assertNotNull(p);
		List<PharmacyOrder> orders = service.getPharmacyOrdersByPatient(p);
		
		assertNotNull(orders);
		assertEquals(new Integer(0), new Integer(orders.size()));
	}

	@Test
	public void shouldReturnPharmacyOrdersGivenTheDrugOrder() {
		logger.warn("Looking for test Pharmacy order");
		
		DrugOrder dorder = Context.getOrderService().getOrder(200, DrugOrder.class);
		assertNotNull(dorder);		
		List<PharmacyOrder> orders = service.getPharmacyOrdersByDrugOrder(dorder);
		assertNotNull(orders);
		assertEquals(new Integer(2), new Integer(orders.size()));
	}
	
	@Test
	public void shouldReturnNoPharmacyOrdersGivenTheDrugOrder() {
		logger.warn("Looking for test Pharmacy order");
		
		DrugOrder dorder = Context.getOrderService().getDrugOrder(298);
		assertNotNull(dorder);		
		List<PharmacyOrder> orders = service.getPharmacyOrdersByDrugOrder(dorder);
		assertNotNull(orders);
		assertEquals(new Integer(0), new Integer(orders.size()));
		
	}

	@Test
	public void shouldReturnPharmacyOrdersGivenDrugOrderList() {
		logger.warn("Looking for test Pharmacy order");
		
		DrugOrder dorder;
		List<DrugOrder> dorders = new ArrayList<DrugOrder>();
		
		dorder = Context.getOrderService().getDrugOrder(200);
		assertNotNull(dorder);
		dorders.add(dorder);

		dorder = Context.getOrderService().getDrugOrder(201);
		assertNotNull(dorder);
		dorders.add(dorder);
		
		List<PharmacyOrder> orders = service.getPharmacyOrdersByDrugOrders(dorders);
		assertNotNull(orders);
		assertEquals(new Integer(3), new Integer(orders.size()));
	}

	@Test
	public void shouldReturnPharmacyOrdersInAMapGivenDrugOrderList() {
		logger.warn("Looking for test Pharmacy order");
		
		List<DrugOrder> dorders = new ArrayList<DrugOrder>();
		
		DrugOrder dorder1 = Context.getOrderService().getDrugOrder(200);
		assertNotNull(dorder1);
		dorders.add(dorder1);

		DrugOrder dorder2 = Context.getOrderService().getDrugOrder(201);
		assertNotNull(dorder2);
		dorders.add(dorder2);
				
		Map<DrugOrder, List<PharmacyOrder>> orders = service.getPharmacyOrdersOrderedByDrugOrders(dorders);
		assertNotNull(orders);
		assertEquals(new Integer(2), new Integer(orders.size()));
		assertNotNull(orders.get(dorder1));
		assertEquals(new Integer(2), new Integer(orders.get(dorder1).size()));
		assertNotNull(orders.get(dorder2));
		assertEquals(new Integer(1), new Integer(orders.get(dorder2).size()));
	}

	@Test
	public void shouldReturnPharmacyOrdersInAMapGivenDrugOrderListWithNullOrders() {
		logger.warn("Looking for test Pharmacy order");
		
		List<DrugOrder> dorders = new ArrayList<DrugOrder>();
		
		DrugOrder dorder1 = Context.getOrderService().getDrugOrder(200);
		assertNotNull(dorder1);
		dorders.add(dorder1);

		DrugOrder dorder2 = Context.getOrderService().getDrugOrder(201);
		assertNotNull(dorder2);
		dorders.add(dorder2);
		
		DrugOrder dorder3 = Context.getOrderService().getDrugOrder(298);
		assertNotNull(dorder3);
		dorders.add(dorder3);

		Map<DrugOrder, List<PharmacyOrder>> orders = service.getPharmacyOrdersOrderedByDrugOrders(dorders);
		assertNotNull(orders);
		assertEquals(new Integer(3), new Integer(orders.size()));
		assertNotNull(orders.get(dorder1));
		assertEquals(new Integer(2), new Integer(orders.get(dorder1).size()));
		assertNotNull(orders.get(dorder2));
		assertEquals(new Integer(1), new Integer(orders.get(dorder2).size()));
		assertNotNull(orders.get(dorder3));
		assertEquals(new Integer(0), new Integer(orders.get(dorder3).size()));
	}

	@Test
	public void shouldReturnPharmacyOrdersInAMapGivenDrugOrderListWithNullObject() {
		logger.warn("Looking for test Pharmacy order");
		
		List<DrugOrder> dorders = new ArrayList<DrugOrder>();
		
		DrugOrder dorder1 = Context.getOrderService().getDrugOrder(200);
		assertNotNull(dorder1);
		dorders.add(dorder1);

		dorders.add(null);

		Map<DrugOrder, List<PharmacyOrder>> orders = service.getPharmacyOrdersOrderedByDrugOrders(dorders);
		assertNotNull(orders);
		assertEquals(new Integer(2), new Integer(orders.size()));
		assertNotNull(orders.get(dorder1));
		assertEquals(new Integer(2), new Integer(orders.get(dorder1).size()));

		assertNotNull(orders.get(null));
	}



	
	
	/*

	

	public List<PharmacyOrder> getPharmacyOrdersByDrugOrder(DrugOrder drugOrder);

	public List<PharmacyOrder> getPharmacyOrdersByDrugOrders(List<DrugOrder> drugOrders);	
	
	public Map<DrugOrder, List<PharmacyOrder>> getPharmacyOrdersOrderedByDrugOrders(List<DrugOrder> drugOrders);
	
	public PharmacyOrder savePharmacyOrder(PharmacyOrder pharmacyOrder) throws APIException;
	
	public PharmacyOrder addPharmacyOrderToDrugOrder(DrugOrder drugOrder, PharmacyOrder pharmacyOrder) throws APIException;

	 * */
	
}
