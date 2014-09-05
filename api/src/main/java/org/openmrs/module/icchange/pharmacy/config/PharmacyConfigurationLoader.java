package org.openmrs.module.icchange.pharmacy.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.OrderType;
import org.openmrs.api.context.Context;

public class PharmacyConfigurationLoader {

	protected static final Log log = LogFactory.getLog(PharmacyConfigurationLoader.class);
	private static ObjectMapper mapper = new ObjectMapper();
	
	public static PharmacyConfiguration loadGeneralConfig (PharmacyConfiguration config) {
		String jsonConfig = "";
		JsonNode root = null;
		
		config.defaultOrdersAge = null;
		
		try {
			jsonConfig = Context.getAdministrationService().getGlobalProperty(PharmacyConstants.configPropertyName);
			root = mapper.readTree(jsonConfig);
		} catch (Exception e) {
			log.warn("Could not read module configuration from global properties. Default will be used instead" , e);
		}

		try {

			JsonNode node;
			try {
				node = root.get("defaultOrdersAgeSearch");
				config.defaultOrdersAge = Integer.parseInt(node.getValueAsText());
			} catch (Exception e) {
				log.warn("Could not read default age search. Default will be used instead" , e);
				config.defaultOrdersAge = PharmacyConstants.defaultOrdersAgeSearch;
			} 

		} catch (Exception e) {
			log.warn("Could not read module configuration from global properties. Default will be used instead." , e);
		}
		
			
		return config;
	}
	
	public static List<String> loadUnitsConfiguration () {
		List<String> ret = new ArrayList<String>();
		String jsonConfig = "";
		
		try {
			jsonConfig = Context.getAdministrationService().getGlobalProperty(PharmacyConstants.unitsPropertyName);
		} catch (Exception e) {}

		try {
			JsonNode root = mapper.readTree(jsonConfig);
			Iterator<JsonNode> es;
			try {
				es = root.get("conceptNames").getElements();
				while (es.hasNext()) {
					String cName = es.next().getValueAsText();
					Concept c = Context.getConceptService().getConceptByName(cName);
					if (c != null)
						ret.add(c.getName().getName());
				}
			} catch (Exception e) {} 
			try {
				es = root.get("conceptIds").getElements();
				while (es.hasNext()) {
					Integer cId = es.next().getIntValue();
					Concept c = Context.getConceptService().getConcept(cId);
					if (c != null)
						ret.add(c.getName().getName());
				}
			} catch (Exception e) {} 
			try {
				es = root.get("literalNames").getElements();
				while (es.hasNext()) {
					String name = es.next().getValueAsText();
					if (name != null)
						ret.add(name);
				}
			} catch (Exception e) {} 
		} catch (Exception e) {
		}
		
		if (ret.isEmpty()) {
			log.debug("Could not read the json unit configuration.");
			log.debug("Module's default configuration will be loaded instead.");
			for (String s: PharmacyConstants.unitsStringList.split(","))
				ret.add(s);
		}
		return ret;
	}
	
	public static OrderType getPharmacyOrderType() {
		OrderType pharmacyOrderType = null;
	
		for (OrderType t: Context.getOrderService().getAllOrderTypes(false)) {
			if (t.getName().equalsIgnoreCase(PharmacyConstants.PHARMACYORDERTYPANAME))
				pharmacyOrderType = t;
		}
		
		if (pharmacyOrderType == null) {
			OrderType t = new OrderType();
			t.setCreator(Context.getUserService().getUser(1));
			t.setDateCreated(new Date());
			t.setName(PharmacyConstants.PHARMACYORDERTYPANAME);
			t.setDescription(PharmacyConstants.PHARMACYORDERTYPANAME);
			pharmacyOrderType = Context.getOrderService().saveOrderType(t);
		}
			
		return pharmacyOrderType;
	}

	public static EncounterType getPharmacyOrderEncounterType() {
		EncounterType pharmacyEncounterType = null;
		
		for (EncounterType t: Context.getEncounterService().getAllEncounterTypes(false)) {
			if (t.getName().equalsIgnoreCase(PharmacyConstants.PHARMACYENCOUNTERNAME))
				pharmacyEncounterType = t;
		}
		
		if (pharmacyEncounterType == null) {
			EncounterType t = new EncounterType();
			t.setCreator(Context.getUserService().getUser(1));
			t.setDateCreated(new Date());
			t.setName(PharmacyConstants.PHARMACYENCOUNTERNAME);
			t.setDescription(PharmacyConstants.PHARMACYENCOUNTERNAME);
			pharmacyEncounterType = Context.getEncounterService().saveEncounterType(t);
		}

		return pharmacyEncounterType;
	}

	public static Map<String, Set<Integer>> getDefaultConceptsMaps () {
		Map<String, Set<Integer>> ret = new HashMap<String, Set<Integer>>();
		String[] setNamesList = null;
		
		try {
			setNamesList = Context.getAdministrationService().getGlobalProperty(PharmacyConstants.setsPropertyName).split(",");
		} catch (Exception e) {
			log.debug("Could not load the drug sets. The internal stack error is\n", e);
		}
		
		if (setNamesList == null || setNamesList.length == 0)
			return ret;

		for (String setName : setNamesList) {
			Concept c = null;
			
			try {
				c = Context.getConceptService().getConcept(setName);
			} catch (Exception e) {
				log.debug("Could not find the concept related to " + setName + ". The error stack is\n", e);
			} 
			
			if (c == null) {
				log.debug("Could not find the concept related to " + setName + ".");
				continue;
			}
			ret.put(setName, new HashSet<Integer>());
			
			try {
				for (Concept ci: Context.getConceptService().getConceptsByConceptSet(c))
					ret.get(setName).add(ci.getId());
			} catch (Exception e) {
				log.debug("Could not find the concepts related to " + setName + ". The error stack is\n", e);
			}			
		}
		return ret;
	}
	
	
	public static PharmacyConfiguration getConfiguration () {	
		PharmacyConfiguration config = new PharmacyConfiguration();
		
		config.pharmacyOrderType = getPharmacyOrderType();
		config.pharmacyEncounterType = getPharmacyOrderEncounterType();
		config.setConceptMap = getDefaultConceptsMaps();
		config.units = loadUnitsConfiguration();
		loadGeneralConfig(config);
		
		return config;
	}
}
