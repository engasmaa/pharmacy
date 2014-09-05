package org.openmrs.module.icchange.pharmacy.config;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.OrderType;

public class PharmacyConfiguration {
	public OrderType pharmacyOrderType;
	public EncounterType pharmacyEncounterType;
	public Map<String, Set<Integer>> setConceptMap;
	public List<String> units;
	public Integer defaultOrdersAge;
}
