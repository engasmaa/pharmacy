package org.openmrs.module.icchange.pharmacy.config;

public class PharmacyConstants {
	public static final String VIEWPRIVILEGE = "View Prescriptions";
	public static final String ADDPRIVILEGE = "Add Prescriptions";
	public static final String EDITPRIVILEGE = "Edit Prescriptions";
	public static final String DELETEPRIVILEGE = "Delete Prescriptions";

	public static final String PHARMACYORDERTYPANAME = "Pharmacy Order";
	public static final String PHARMACYENCOUNTERNAME = "Pharmacy Encounter";
	
	public static final String unitsStringList = "kg,grams,mg,liters,tablets,capsules,bottles,other";
	public static final String unitsPropertyName = "icchange.pharmacy.drugs.units";
	
	public static Integer defaultOrdersAgeSearch = 6;
	public static String inventoryConnector = "org.openmrs.module.icchange.pharmacy.inventory.DefaultDrugConnector";
	
	public static final String configPropertyName = "icchange.pharmacy.config";
	public static final String defaultConfig = 
			"{\"defaultOrdersAgeSearch\":"+ defaultOrdersAgeSearch + 
			",\"inventoryConnector\":\"" + inventoryConnector + "\"}";

	
	public static final String setsPropertyName = "dashboard.regimen.displayDrugSetIds";
	
	public static final String[] pharmacyClobalPropertiesList = {unitsPropertyName, configPropertyName, setsPropertyName};
}
