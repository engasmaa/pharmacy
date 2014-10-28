<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="View Patients" otherwise="/login.htm" redirect="/findPatient.htm" />
<c:set var="OPENMRS_VIEWING_PATIENT_ID" scope="request" value="${patient.patientId}" />
<openmrs:globalProperty var="enablePatientName" key="dashboard.showPatientName" defaultValue="false" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localMenuHeader.jsp"%>

<h2>
	<openmrs:message code="icchange.pharmacy.dispense" />
</h2>

<br />



<openmrs:htmlInclude file="/dwr/interface/DWROrderService.js" />
<openmrs:htmlInclude file="/dwr/engine.js" />
<openmrs:htmlInclude file="/dwr/util.js" />
<openmrs:htmlInclude file="/scripts/drugOrder.js" />

<c:if test="${patient.voided}">
	<div id="patientDashboardVoided" class="retiredMessage">
		<div>
			<openmrs:message code="Patient.voidedMessage" />
		</div>
	</div>
</c:if>

<c:if test="${patient.dead}">
	<div id="patientDashboardDeceased" class="retiredMessage">
		<div>
			<openmrs:message code="Patient.patientDeceased" />
			<c:if test="${not empty patient.deathDate}">
				&nbsp;&nbsp;&nbsp;&nbsp;
				<openmrs:message code="Person.deathDate" />: <openmrs:formatDate
					date="${patient.deathDate}" />
			</c:if>
			<c:if test="${not empty patient.causeOfDeath}">
				&nbsp;&nbsp;&nbsp;&nbsp;
				<openmrs:message code="Person.causeOfDeath" />: <openmrs:format
					concept="${patient.causeOfDeath}" />
				<c:if test="${not empty causeOfDeathOther}"> 
					  &nbsp;:&nbsp;<c:out value="${causeOfDeathOther}"></c:out>
				</c:if>
			</c:if>
		</div>
	</div>
</c:if>

<c:if test="${!patient.dead && !patient.voided}">
	<openmrs:portlet url="patientHeader" id="pharmacyPatientdHeader" patientId="${patient.patientId}" />
	<br/>
	<openmrs:globalProperty var="displayDrugSetIds" key="dashboard.regimen.displayDrugSetIds" defaultValue="ANTIRETROVIRAL DRUGS,TUBERCULOSIS TREATMENT DRUGS" />
	<openmrs:portlet url="patientRegimen" id="pharmacyRegimens" moduleId="icchange.pharmacy" patientId="${patient.patientId}" parameters="displayDrugSetIds=${displayDrugSetIds},*|displayFutureRegimens=true" />
	<br/>
</c:if>
<%@ include file="/WEB-INF/template/footer.jsp"%>
