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
package org.openmrs.module.icchange.pharmacy.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.DrugOrder;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PersonAddress;
import org.openmrs.PersonName;
import org.openmrs.api.OrderService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.icchange.pharmacy.PharmacyOrder;
import org.openmrs.module.icchange.pharmacy.api.PharmacyOrderService;
import org.openmrs.module.icchange.pharmacy.web.dwr.DWRPharmacyOrderService;
import org.openmrs.module.icchange.pharmacy.web.model.DWRDrugOrderHeader;
import org.openmrs.module.web.extension.ExtensionUtil;
import org.openmrs.module.web.extension.provider.Link;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The main controller.
 */
@Controller
public class  ICChangePharmacyManageController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(value = "/module/icchange/pharmacy/manage", method = RequestMethod.GET)
	public void manage(ModelMap model) {
		model.addAttribute("user", Context.getAuthenticatedUser());
	}
	
	
	@RequestMapping(value = "/module/icchange/pharmacy/pharmacy", method = RequestMethod.GET)
	public void menu(ModelMap model) {
	}
	
	/*@RequestMapping(value = "/module/icchange/pharmacy/drugDispense", method = RequestMethod.GET)
	public void drugDispense(ModelMap model) {

	}
*/
	
	//@RequestMapping("/patientDashboard.form")

	@RequestMapping(value = "/module/icchange/pharmacy/drugDispense", method = RequestMethod.GET)
	protected void drugDispense(@RequestParam(required = true, value = "patientId") Integer patientId, ModelMap map)
	        throws Exception {
		
		// get the patient
		
		PatientService ps = Context.getPatientService();
		Patient patient = null;
		
		try {
			patient = ps.getPatient(patientId);
		}
		catch (ObjectRetrievalFailureException noPatientEx) {
			log.warn("There is no patient with id: '" + patientId + "'", noPatientEx);
		}
		
		if (patient == null)
			throw new ServletException("There is no patient with id: '" + patientId + "'");
		
		log.debug("patient: '" + patient + "'");
		map.put("patient", patient);
		
		// determine cause of death
		
		String causeOfDeathOther = "";
		
		if (Context.isAuthenticated()) {
			String propCause = Context.getAdministrationService().getGlobalProperty("concept.causeOfDeath");
			Concept conceptCause = Context.getConceptService().getConcept(propCause);
			
			if (conceptCause != null) {
				List<Obs> obssDeath = Context.getObsService().getObservationsByPersonAndConcept(patient, conceptCause);
				if (obssDeath.size() == 1) {
					Obs obsDeath = obssDeath.iterator().next();
					causeOfDeathOther = obsDeath.getValueText();
					if (causeOfDeathOther == null) {
						log.debug("cod is null, so setting to empty string");
						causeOfDeathOther = "";
					} else {
						log.debug("cod is valid: " + causeOfDeathOther);
					}
				} else {
					log.debug("obssDeath is wrong size: " + obssDeath.size());
				}
			} else {
				log.debug("No concept cause found");
			}
		}
		
		// determine patient variation
		
		String patientVariation = "";
		
		Concept reasonForExitConcept = Context.getConceptService().getConcept(
		    Context.getAdministrationService().getGlobalProperty("concept.reasonExitedCare"));
		
		if (reasonForExitConcept != null) {
			List<Obs> patientExitObs = Context.getObsService().getObservationsByPersonAndConcept(patient,
			    reasonForExitConcept);
			if (patientExitObs != null) {
				log.debug("Exit obs is size " + patientExitObs.size());
				if (patientExitObs.size() == 1) {
					Obs exitObs = patientExitObs.iterator().next();
					Concept exitReason = exitObs.getValueCoded();
					Date exitDate = exitObs.getObsDatetime();
					if (exitReason != null && exitDate != null) {
						patientVariation = "Exited";
					}
				} else if (patientExitObs.size() > 1) {
					log.error("Too many reasons for exit - not putting data into model");
				}
			}
		}
		
		map.put("patientVariation", patientVariation);
		
		// empty objects used to create blank template in the view
		
		map.put("emptyIdentifier", new PatientIdentifier());
		map.put("emptyName", new PersonName());
		map.put("emptyAddress", new PersonAddress());
		map.put("causeOfDeathOther", causeOfDeathOther);
		
		Set<Link> links = ExtensionUtil.getAllAddEncounterToVisitLinks();
		map.put("allAddEncounterToVisitLinks", links);
	
		List<DrugOrder> dlist = Context.getOrderService().getDrugOrdersByPatient(patient, OrderService.ORDER_STATUS.CURRENT_AND_FUTURE);
		
		List<String> sDrugsList = new ArrayList<String>();
		
		for (DWRDrugOrderHeader h: (new DWRPharmacyOrderService()).getDrugOrdersHeaders(patient.getId())) {
			sDrugsList.add(h.toJasonRepresentation());
		}
		
		map.put("druglist", dlist);
		map.put("drugOrderHeaders", sDrugsList);
		
		if (dlist == null)
			return;
					
		if (dlist.isEmpty())
			return;
		

		Map<Integer, List<PharmacyOrder>> orderlist = new HashMap<Integer, List<PharmacyOrder>>();
		
		for (DrugOrder drugOrder: dlist) {
			orderlist.put(drugOrder.getId(), Context.getService(PharmacyOrderService.class).getPharmacyOrdersByDrugOrder(drugOrder));
		}
		
		map.put("pharmacyordermap", orderlist);
		
	}
	
}
