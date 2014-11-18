package org.openmrs.module.icchange.pharmacy.web.dwr;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Boolean;
import java.util.Vector;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import org.jfree.util.Log;
import org.openmrs.Concept;
import org.openmrs.ConceptSet;
import org.openmrs.DrugOrder;
//import org.openmrs.Encounter;
//import org.openmrs.Patient;
//import org.openmrs.Visit;
import org.openmrs.api.context.Context;
//import org.openmrs.module.icchange.pharmacy.web.dwr.DWRDrugOrder;
//import org.openmrs.module.icchange.pharmacy.web.dwr.DWRDrugOrderHeader;
import org.openmrs.module.icchange.pharmacy.PharmacyOrder;
import org.openmrs.module.icchange.pharmacy.api.PharmacyOrderService;
import org.openmrs.module.icchange.pharmacy.web.dwr.DWRPharmacyOrder;
import org.openmrs.module.icchange.pharmacy.web.dwr.DWRItem;

//import org.openmrs.module.icchange.pharmacy.inventory.Item;
import org.openmrs.module.openhmis.inventory.api.model.Item;
import org.openmrs.module.openhmis.inventory.api.model.ItemStock;
import org.openmrs.module.openhmis.inventory.api.IItemDataService;
import org.openmrs.module.openhmis.inventory.api.IItemStockDataService;
//import org.openmrs.module.openhmis.inventory.api.IPharmacyConnectorService;
import org.openmrs.module.openhmis.commons.api.entity.IMetadataDataService;
import org.openmrs.util.OpenmrsUtil;

public class DWRPharmacyOrderService {

	private PharmacyOrderService service = Context.getService(PharmacyOrderService.class);

	private IItemDataService itemService = Context.getService(IItemDataService.class);
	private IItemStockDataService isds = Context.getService(IItemStockDataService.class);

	public Vector<DWRItem> listItemsByDrugId(Integer drugId) throws Exception
	{
		try
		{
			
			List<Item> items = itemService.listItemsByDrugId(drugId);
			
			Vector<DWRItem> ret = new Vector<DWRItem>();
			List<ItemStock> itemStocks;
			
			if (items != null)
			{
				for(Item i : items)
				{
					DWRItem dItem = new DWRItem(i);
					itemStocks = isds.getItemStockByItem(i, null);
					if (itemStocks != null)
					{
						for (ItemStock is : itemStocks)
						{
							dItem.setQuantity(dItem.getQuantity() + is.getQuantity());
						}
					} else
						dItem.setQuantity(0);
					ret.add(dItem);
				}
			}
			return ret;
		} catch (Exception e) {
			throw e;
		}
	}
	

	public Vector<DWRPharmacyOrder> getPharmacyOrdersByDrugOrderId(Integer drugId) throws Exception{

		try {
			DrugOrder drugOrder = Context.getOrderService().getDrugOrder(drugId);
			Vector<DWRPharmacyOrder> ret = new Vector<DWRPharmacyOrder>();
			
			if (drugOrder == null)
				return ret;
			List<PharmacyOrder> data = service.getPharmacyOrdersByDrugOrder(drugOrder);
			
			for (PharmacyOrder po: data) 
				ret.add(new DWRPharmacyOrder(po));
			
			return ret;
		} catch (Exception e) {
			throw e;
		}
	}

	public DWRPharmacyOrder savePharmacyOrder(DWRPharmacyOrder order) throws Exception {
		
		if (order == null)
			return null;
		
		PharmacyOrder porder = new PharmacyOrder();
		Date now = new Date();
		
		
		porder.setCreator(Context.getAuthenticatedUser());
		porder.setDateCreated(now);
		//porder.setDispenseDate(now);
		//porder.setAutoExpireDate(null);
		porder.setNotes(order.getNotes());
		
		if (order.getOrderId() == null)
			return null;
		
		DrugOrder d = Context.getOrderService().getDrugOrder(order.getOrderId());
		
		if (d == null)
			return null;
		
		porder.setDrugOrder(d);
		try {
			//IItemDataService itemService = Context.getService(IItemDataService.class);
			Boolean success = itemService.dispenseItem(order.getItemId(), order.getQuantity());
			if (success == true)
			{
				//PharmacyOrderService service = Context.getService(PharmacyOrderService.class);
				service.savePharmacyOrder(porder);
			}
		} catch (Exception e) {
			throw e;
		}
		DWRPharmacyOrder dwrporder = new DWRPharmacyOrder(porder);
		return dwrporder;
		//if (order.getPatientId() == null)
			//return null;
		
		//Patient p = Context.getPatientService().getPatient(order.getPatientId());
		
		//if (p == null)
			//return null;
		
		//porder.setPatient(p);

		/**
		String visitsEnabeled = Context.getAdministrationService().getGlobalProperty("visits.enabled");
		
		if (visitsEnabeled != null && visitsEnabeled.toLowerCase(null).equals("true")) {
			
			List<Visit> vlist = Context.getVisitService().getActiveVisitsByPatient(p);

			if (vlist == null || vlist.size() == 0)
				return null;
			
			if (vlist.size() > 1)
				return null;
			
			Visit v  = vlist.get(0);			
			
			Encounter e = new Encounter();
			
			e.setVisit(v);
			
			//v.get
		} else {
			
			
			
		}
		
		
		return null;	**/
		
	}
		
	public void createPharmacyOrderFromParts(Integer drugId, String itemName, Integer itemId, Integer quantity, String units, String dispenseDate, String notes) throws Exception{//, Integer patientId) throws Exception{
		//Patient patient = Context.getPatientService().getPatient(patientId);
		DrugOrder drugOrder =  Context.getOrderService().getDrugOrder(drugId);
		
		Log.debug("Enter dwr save pharmacy order.");
		
		//if (patient == null || drugOrder == null)
			//throw new IllegalArgumentException("Invalid patient or drug order id");
		PharmacyOrder pharmacyOrder = new PharmacyOrder();
		
		Item item = new Item();
		item.setId(itemId);
		item.setName(itemName);
		//item.setQuantity(quantity);
		//item.setUnit(units);
		
		pharmacyOrder.setItem(item);
		pharmacyOrder.setDrugOrder(drugOrder);
		//pharmacyOrder.setItemName(itemName);
		//pharmacyOrder.setItemId(itemId);
		pharmacyOrder.setQuantity(quantity);
		pharmacyOrder.setUnits(units);

		
		SimpleDateFormat sdf = Context.getDateFormat();
		Date date = new Date();
		try {
			date = sdf.parse(dispenseDate);
		}
		catch (ParseException e) {
			throw e;
		}
		pharmacyOrder.setDispenseDate(date);
		pharmacyOrder.setNotes(notes);
		
		try {
			//PharmacyOrderService service = Context.getService(PharmacyOrderService.class);
			Boolean success = itemService.dispenseItem(pharmacyOrder.getItem().getId(), pharmacyOrder.getQuantity());
			if (success == true)
			{
				service.savePharmacyOrder(pharmacyOrder);
			}
		} catch (Exception e) {
			throw e;
		}
		
		Log.debug("Exit dwr save pharmacy order");
	}
	

	public void createPharmacyOrder(Integer drugId) throws Exception{//, Integer patientId) throws Exception{
		//Patient patient = Context.getPatientService().getPatient(patientId);
		DrugOrder drugOrder =  Context.getOrderService().getDrugOrder(drugId);
		
		Log.debug("Enter dwr save pharmacy order.");
		
		//if (patient == null || drugOrder == null)
			//throw new IllegalArgumentException("Invalid patient or drug order id");
		
		
		PharmacyOrder pharmacyOrder = new PharmacyOrder();
		
		pharmacyOrder.setDrugOrder(drugOrder);
		//pharmacyOrder.setPatient(patient);
		
		try {
			PharmacyOrderService service = Context.getService(PharmacyOrderService.class);
			service.savePharmacyOrder(pharmacyOrder);
		} catch (Exception e) {
			throw e;
		}
		
		Log.debug("Exit dwr save pharmacy order");
	}	
		
	/**
		public Vector<DWRDrugOrder> getAllPharmacyOrdersByPatient(Integer patientId) 
		{
		
		if (patientId == null)
			return null;
		
		//Patient patient = Context.getPatientService().getPatient(patientId);
		
		if (patient == null)
			return null;
		
		List<DrugOrder> dOrders = Context.getOrderService().getDrugOrdersByPatient(patient);

		if (dOrders == null)
			return null;
		
		Map<DrugOrder, List<PharmacyOrder>> map = Context.getService(PharmacyOrderService.class).getPharmacyOrdersOrderedByDrugOrders(dOrders);
		
		List<DWRDrugOrder> dwrOrders = new ArrayList<DWRDrugOrder>();
		
		for (DrugOrder key: map.keySet()) 
		{
			DWRDrugOrder dwrOrder = new DWRDrugOrder(key);
			
			List<DWRPharmacyOrder> pList = new ArrayList<DWRPharmacyOrder>();
			
			for (PharmacyOrder p: map.get(key)) 
			{
				if (p != null)
					pList.add(new DWRPharmacyOrder(p));
			}
			
			dwrOrder.setDispenses(pList);
			dwrOrders.add(dwrOrder);
		}
		
		return dwrOrders;
		}
		**/
	/***
	public List<DWRDrugOrder> getAllPharmacyOrdersByPatient(Integer patientId) {
		
		if (patientId == null)
			return null;
		
		Patient patient = Context.getPatientService().getPatient(patientId);
		
		if (patient == null)
			return null;
		
		List<DrugOrder> dOrders = Context.getOrderService().getDrugOrdersByPatient(patient);

		if (dOrders == null)
			return null;
		
		Map<DrugOrder, List<PharmacyOrder>> map = Context.getService(PharmacyOrderService.class).getPharmacyOrdersOrderedByDrugOrders(dOrders);
		
		List<DWRDrugOrder> dwrOrders = new ArrayList<DWRDrugOrder>();
		
		for (DrugOrder key: map.keySet()) {
			DWRDrugOrder dwrOrder = new DWRDrugOrder(key);
			
			List<DWRPharmacyOrder> pList = new ArrayList<DWRPharmacyOrder>();
			
			for (PharmacyOrder p: map.get(key)) {
				if (p != null)
					pList.add(new DWRPharmacyOrder(p));
			}
			
			dwrOrder.setDispenses(pList);
			dwrOrders.add(dwrOrder);
		}
		
		return dwrOrders;
	}***/
	
	/**
	private Integer orderId;
	private String itemName;
	private Integer itemId;
	private Integer quantity;
	private String units;
	private String dispenseDate;
	private String notes;
	 */
	
}
