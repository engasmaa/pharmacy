package org.openmrs.module.icchange.pharmacy.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Concept;
import org.openmrs.ConceptWord;
import org.openmrs.Drug;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.ServiceContext;
import org.openmrs.module.icchange.pharmacy.config.PharmacyConstants;

public class DefaultDrugConnector implements InventoryConnector {
	
	private static final Log log = LogFactory.getLog(ServiceContext.class);
	private static SessionFactory sessionFactory;
	private static InventoryConnector instance;
	private static List<String> units;
	private static ObjectMapper mapper = new ObjectMapper();
	
	public static InventoryConnector getInstance () {
		if (instance == null) {
			instance = new DefaultDrugConnector();		
			loadUnitsConfigurationAsJson();
		}
		return instance;
	}
	
	private DefaultDrugConnector() {}

	public void reloadConnector() {
		loadUnitsConfigurationAsJson();
	}
	
	@Override
	public Map<String, Integer> listPharmacyItemsNamesIds() {
		Map<String, Integer> ret = new HashMap<String, Integer>();
		
		for (Drug d: Context.getConceptService().getAllDrugs()) {
			ret.put(d.getName(), d.getId());
		}
		return ret;
	}

	@Override
	public Map<String, Integer> listPharmacyItemsNamesIds(String phrase) {
		Map<String, Integer> ret = new HashMap<String, Integer>();
		
		if (phrase == null)
			return ret;

		Integer size = null;
		
		try {
			String minSizeStringPharse = Context.getAdministrationService().getGlobalProperty("minSearchCharacters");
			size = Integer.parseInt(minSizeStringPharse);
		} catch (Exception e) {
			size = 3;
		}
		
		if (phrase.length() < size)
			return ret;
		
		List<String> words = ConceptWord.getUniqueWords(phrase);
		List<Drug> drugs = new Vector<Drug>();
		
		if (words.size() > 0) {
			Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(Drug.class, "drug");
			
			Iterator<String> word = words.iterator();
			searchCriteria.add(Restrictions.like("name", word.next(), MatchMode.ANYWHERE).ignoreCase());
			while (word.hasNext()) {
				String w = word.next();
				log.debug(w);
				searchCriteria.add(Restrictions.like("name", w, MatchMode.ANYWHERE).ignoreCase());
			}
			searchCriteria.addOrder(Order.asc("drug.concept"));
			drugs = searchCriteria.list();
		}
		
		for (Drug d: drugs) {
			ret.put(d.getName(), d.getId());
		}
		
		return ret;
	}

	@Override
	public List<String> listDispenseUnitsToItem(Integer itemId) {
		return units;
	}

	@Override
	public Boolean dispenseItem(Integer itemId, Integer quantity, String unit) {
		
		if (itemId == null)
			return false;
		
		if (quantity == null)
			return false;
		
		if (unit == null)
			return false;
		
		return true;
	}
	
	public static List<String> loadUnitsConfigurationAsJson () {
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
		units =  ret;
		return ret;
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory session) {
		sessionFactory = session;
	}

	@Override
	public Boolean lockInventory(User u) {
		return true;
	}

	@Override
	public Boolean unLockInventory(User u) {
		return true;
	}
}
